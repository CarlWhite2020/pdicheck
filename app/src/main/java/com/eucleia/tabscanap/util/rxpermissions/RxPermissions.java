package com.eucleia.tabscanap.util.rxpermissions;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class RxPermissions {

    static final String TAG = "RxPermissions";

    RxPermissionsFragment mRxPermissionsFragment;

    public RxPermissions(@NonNull FragmentActivity activity) {
        mRxPermissionsFragment = getRxPermissionsFragment(activity);
    }

    public void release(){

    }

    private RxPermissionsFragment getRxPermissionsFragment(FragmentActivity activity) {
        RxPermissionsFragment rxPermissionsFragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new RxPermissionsFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(rxPermissionsFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxPermissionsFragment;
    }

    private RxPermissionsFragment findRxPermissionsFragment(FragmentActivity activity) {
        return (RxPermissionsFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void setLogging(boolean logging) {
        mRxPermissionsFragment.setLogging(logging);
    }

    /**
     * Map emitted items from the source observable into {@code true} if permissions in parameters
     * are granted, or {@code false} if not.
     * <p>
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    @SuppressWarnings("WeakerAccess")
    public ObservableTransformer<Object, Boolean> ensure(final String... permissions) {
        return new ObservableTransformer<Object, Boolean>() {
            @Override
            public Observable<Boolean> apply(Observable<Object> o) {
                return request(o, permissions)
                        // Transform Observable<Permission> to Observable<Boolean>
                        .buffer(permissions.length)
                        .flatMap(new Function<List<Permission>, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> apply(List<Permission> permissions) {
                                if (permissions.isEmpty()) {
                                    // Occurs during orientation change, when the subject receives onComplete.
                                    // In that case we don't want to propagate that empty list to the
                                    // subscriber, only the onComplete.
                                    return Observable.empty();
                                }
                                // Return true if all permissions are granted.
                                for (Permission p : permissions) {
                                    if (!p.granted) {
                                        return Observable.just(false);
                                    }
                                }
                                return Observable.just(true);
                            }
                        });
            }
        };
    }

    /**
     * Map emitted items from the source observable into {@link Permission} objects for each
     * permission in parameters.
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    @SuppressWarnings("WeakerAccess")
    public ObservableTransformer<Object, Permission> ensureEach(final String... permissions) {
        return new ObservableTransformer<Object, Permission>() {
            @Override
            public Observable<Permission> apply(Observable<Object> o) {
                return request(o, permissions);
            }
        };
    }

    /**
     * Request permissions immediately, <b>must be invoked during initialization phase
     * of your application</b>.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Boolean> request(final String... permissions) {
        return Observable.just(TAG).compose(ensure(permissions));
    }

    /**
     * Request permissions immediately, <b>must be invoked during initialization phase
     * of your application</b>.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Observable<Permission> requestEach(final String... permissions) {
        return Observable.just(TAG).compose(ensureEach(permissions));
    }

    private Observable<Permission> request(final Observable<?> trigger, final String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission");
        }
        return oneOf(trigger, pending(permissions))
                .flatMap(new Function<Object, Observable<Permission>>() {
                    @Override
                    public Observable<Permission> apply(Object o) {
                        return requestImplementation(permissions);
                    }
                });
    }

    private Observable<?> pending(final String... permissions) {
        for (String p : permissions) {
            if (!mRxPermissionsFragment.containsByPermission(p)) {
                return Observable.empty();
            }
        }
        return Observable.just(TAG);
    }

    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        if (trigger == null) {
            return Observable.just(TAG);
        }
        return Observable.merge(trigger, pending);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private Observable<Permission> requestImplementation(final String... permissions) {
        List<Observable<Permission>> list = new ArrayList<>(permissions.length);
        List<String> unrequestedPermissions = new ArrayList<>();

        // In case of multiple permissions, we create an Observable for each of them.
        // At the end, the observables are combined to have a unique response.
        for (String permission : permissions) {
            mRxPermissionsFragment.log("Requesting permission " + permission);
            if (isGranted(permission)) {
                // Already granted, or not Android M
                // Return a granted Permission object.
                list.add(Observable.just(new Permission(permission, true, false)));
                continue;
            }

            if (isRevoked(permission)) {
                // Revoked by a policy, return a denied Permission object.
                list.add(Observable.just(new Permission(permission, false, false)));
                continue;
            }

            PublishSubject<Permission> subject = mRxPermissionsFragment.getSubjectByPermission(permission);
            // Create a new subject if not exists
            if (subject == null) {
                unrequestedPermissions.add(permission);
                subject = PublishSubject.create();
                mRxPermissionsFragment.setSubjectForPermission(permission, subject);
            }

            list.add(subject);
        }

        if (!unrequestedPermissions.isEmpty()) {
            String[] unrequestedPermissionsArray = unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray);
        }
        return Observable.concat(list);
    }

    /**
     * Invokes FragmentActivity.shouldShowRequestPermissionRationale and wraps
     * the returned value in an observable.
     * In case of multiple permissions, only emits true if
     * FragmentActivity.shouldShowRequestPermissionRationale returned true for
     * all revoked permissions.
     * You shouldn't call this method if all permissions have been granted.
     * For SDK < 23, the observable will always emit false.
     */
    @SuppressWarnings("WeakerAccess")
    public Observable<Boolean> shouldShowRequestPermissionRationale(final FragmentActivity activity, final String... permissions) {
        if (!isMarshmallow()) {
            return Observable.just(false);
        }
        return Observable.just(shouldShowRequestPermissionRationaleImplementation(activity, permissions));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationaleImplementation(final FragmentActivity activity, final String... permissions) {
        for (String p : permissions) {
            if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissionsFromFragment(String[] permissions) {
        mRxPermissionsFragment.log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        mRxPermissionsFragment.requestPermissions(permissions);
    }

    /**
     * Returns true if the permission is already granted.
     * Always true if SDK < 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isGranted(String permission) {
        return !isMarshmallow() || mRxPermissionsFragment.isGranted(permission);
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     * Always false if SDK < 23.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isRevoked(String permission) {
        return isMarshmallow() && mRxPermissionsFragment.isRevoked(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults) {
        mRxPermissionsFragment.onRequestPermissionsResult(permissions, grantResults, new boolean[permissions.length]);
    }

}
