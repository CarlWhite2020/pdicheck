package com.eucleia.pdicheck.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.normal.CarCheckActivity;
import com.eucleia.pdicheck.activity.normal.CarInfoActivity;
import com.eucleia.pdicheck.activity.normal.ChooseCarActivity;
import com.eucleia.pdicheck.activity.normal.InputVinActivity;
import com.eucleia.pdicheck.activity.normal.LoginActivity;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.DiagView;
import com.eucleia.pdicheck.bean.normal.HeadBase;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.mvpview.BaseNetMvpView;
import com.eucleia.pdicheck.net.presenter.ResetPwdPresenter;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.diag.BaseBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispExitBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispMsgBoxBeanEvent;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.constant.RequestCode;
import com.eucleia.tabscanap.dialog.BaseDialog;
import com.eucleia.tabscanap.dialog.CustomDialog;
import com.eucleia.tabscanap.dialog.ProgressDialog;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.util.AppUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CustomDialog loadingDialog;
    protected BaseDialog baseDialog;
    protected ProgressDialog progressDialog;

    protected boolean isFront;
    protected HeadBase base = new HeadBase();
    protected SingleClickListener headFunListener = view -> OnBackClick();


    private final BaseNetMvpView resPwdMvpView = new BaseNetMvpView() {
        @Override
        public void Empty() {
            DSUtils.get().putBoolean(SPKEY.IS_LOGIN, false);
            AppDatabase.get().getUserDao().resetUser();
            Intent loginIntent = new Intent(Utils.getApp(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityUtils.startActivity(loginIntent);
        }

        @Override
        public void NetSuccess() {
            DSUtils.get().putBoolean(SPKEY.IS_LOGIN, false);
            AppDatabase.get().getUserDao().resetUser();
            Intent loginIntent = new Intent(Utils.getApp(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityUtils.startActivity(loginIntent);
        }

        @Override
        public void NetFail(String error) {
            EToastUtils.get().showShort(error, false);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(bindAct().getRoot());
        if (isOpenBus()) {
            EventBus.getDefault().register(this);
        }
        setImmersionBar();
        setModel();
        initView();
        ResetPwdPresenter.get().attachView(resPwdMvpView);
    }


    protected boolean isOpenBus() {
        return isDiagnosticAct();
    }

    protected boolean isDiagnosticAct() {
        return false;
    }

    protected void setModel() {
        AppVM.get().getVciStatusLiveData().observe(this, vciStatus -> {
            vciChange(vciStatus);
        });
        AppVM.get().getDiagViewLiveData().observe(this, diagView -> {
            onDiagViewChange(diagView);
        });
    }

    /**
     * 下位机状态更新
     *
     * @param vciStatus
     */
    protected void vciChange(VciStatus vciStatus) {

    }

    protected void onDiagViewChange(DiagView diagView) {
        if (EDiagUtils.get().isThreadEnd()) return;
        BaseBeanEvent event = EDiagUtils.get().getEvent(diagView);
        if (event == null) return;
        switch (diagView.getPageType()) {
            case Type_Null:
                dismissDiagnostic();
                return;

            case Type_PdiCheck:
                dismissDiagnostic();
                if (event.isShowModel()) {
                    event.setShowModel(false);
                    startActivity(CarCheckActivity.class);
                    ActivityUtils.finishActivity(CarInfoActivity.class);
                    ActivityUtils.finishActivity(InputVinActivity.class);
                    ActivityUtils.finishActivity(ChooseCarActivity.class);
                }
                break;
            case Type_MsgBox:
            case Type_GlobalMsgBox:
                CDispMsgBoxBeanEvent msgEvent = (CDispMsgBoxBeanEvent) event;
                if (msgEvent.getbState()) {
                    dismissProgress();
                    dismissBaseDialog();
                    createLoading();
                    loadingDialog.setMsg(msgEvent.getStrContext());
                    loadingDialog.show();
                } else if (msgEvent.isbHaveHourglass()) {
                    dismissLoading();
                    dismissBaseDialog();
                    int progress = msgEvent.getiPercent() * 100 / msgEvent.getiAllPercent();
                    createProgress();
                    progressDialog.setContent(msgEvent.getStrContext());
                    progressDialog.setProgress(progress);
                    progressDialog.show();
                } else {
                    dismissLoading();
                    dismissProgress();
                    createBaseDialog();
                    List<Integer> btnBackFlag = new ArrayList<>();
                    List<String> btnText = new ArrayList<>();

                    if (msgEvent.isHasOkCancelBtn()) {
                        btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_CANCEL);
                        btnText.add(ResUtils.getString(R.string.cancel));

                        btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_OK);
                        btnText.add(ResUtils.getString(R.string.define));

                    } else if (msgEvent.isHasYesNoBtn()) {

                        btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_NO);
                        btnText.add(ResUtils.getString(R.string.no));

                        btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_YES);
                        btnText.add(ResUtils.getString(R.string.yes));

                    } else {
                        //左按钮
                        if (msgEvent.isHasCancelBtn()) {
                            btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_CANCEL);
                            btnText.add(ResUtils.getString(R.string.cancel));

                        } else if (msgEvent.isHasNoBtn()) {
                            btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_NO);
                            btnText.add(ResUtils.getString(R.string.no));
                        }

                        //右按钮
                        if (msgEvent.isHasOkBtn()) {
                            btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_OK);
                            btnText.add(ResUtils.getString(R.string.define));
                        } else if (msgEvent.isHasYesBtn()) {
                            btnBackFlag.add(CDispConstant.PageButtonType.DF_ID_YES);
                            btnText.add(ResUtils.getString(R.string.yes));
                        }
                    }

                    if (btnBackFlag.size() == 1) {
                        baseDialog.setSingleButton(btnText.get(0), () -> {
                            msgEvent.setBackFlag(btnBackFlag.get(0));
                            msgEvent.lockAndSignalAll();
                        });
                    } else if (btnBackFlag.size() == 2) {
                        baseDialog.setLeftButton(btnText.get(0), () -> {
                            msgEvent.setBackFlag(btnBackFlag.get(0));
                            msgEvent.lockAndSignalAll();
                        });
                        baseDialog.setRightButton(btnText.get(1), () -> {
                            msgEvent.setBackFlag(btnBackFlag.get(1));
                            msgEvent.lockAndSignalAll();
                        });
                    }
                    if (TextUtils.isEmpty(msgEvent.getStrTitle())) {
                        baseDialog.hideTitle();
                    }
                    baseDialog.setMsg(msgEvent.getStrContext());
                    baseDialog.show();
                }
                return;
        }
    }

    protected abstract ViewDataBinding bindAct();

    protected void initView() {

    }

    protected void setImmersionBar() {
        ImmersionBar.with(this).init();
    }


    protected Context getContext() {
        return this;
    }


    /**
     * 创建带按钮的弹窗
     */
    public BaseDialog createBaseDialog() {
        if (baseDialog == null) {
            baseDialog = new BaseDialog(getContext());
        }
        return baseDialog;
    }

    /**
     * 显示带按钮的弹窗
     */
    public void showBaseDialog() {
        createBaseDialog();
        baseDialog.show();
    }

    /**
     * 显示带按钮的弹窗
     */
    public void dismissBaseDialog() {
        if (baseDialog != null) {
            baseDialog.dismiss();
        }
    }

    /**
     * 创建加载圈的弹窗
     */
    private void createLoading() {
        if (loadingDialog == null) {
            loadingDialog = new CustomDialog(getContext());
        }
    }

    /**
     * 显示普通加载弹窗
     */
    public void showLoading() {
        createLoading();
        loadingDialog.setMsg(ResUtils.getString(R.string.loading));
        loadingDialog.show();
    }

    /**
     * 显示普通加载弹窗
     *
     * @param msg 显示文本
     */
    public void showLoading(String msg) {
        createLoading();
        loadingDialog.setMsg(msg);
        loadingDialog.show();
    }

    /**
     * 隐藏普通加载弹窗
     */
    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 创建带进度的弹窗
     */
    private void createProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
    }

    /**
     * 显示带进度的弹窗
     */
    public void showProgress(int progress) {
        createProgress();
        progressDialog.setProgress(progress);
        progressDialog.show();
    }

    /**
     * 显示带进度的弹窗
     */
    public void showProgress(String text) {
        createProgress();
        progressDialog.setContent(text);
        progressDialog.show();
    }

    /**
     * 显示带进度的弹窗
     */
    public void showProgress(int progress, String text) {
        createProgress();
        progressDialog.setContent(text);
        progressDialog.setProgress(progress);
        progressDialog.show();
    }

    /**
     * 隐藏带进度的弹窗
     */
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void dismissDiagnostic() {
        dismissProgress();
        dismissLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
        if (Constant.isActive) {
            //app 从后台唤醒，进入前台
            Communication.SetApkActive(1);
            ELogUtils.v("app 从后台唤醒，进入前台");
            Constant.isActive = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground()) {
            //app 进入后台
            Communication.SetApkActive(0);
            ELogUtils.v("app 进入后台");
            Constant.isActive = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOpenBus()) {
            EventBus.getDefault().unregister(this);
        }
        ResetPwdPresenter.get().detachView(resPwdMvpView);
        if (progressDialog != null) {
            progressDialog.release();
        }
        dismissDiagnostic();
        dismissBaseDialog();
    }

    protected void OnBackClick() {

    }


    //跳转到新的activity
    public void startActivity(Class clazz) {
        startActivity(clazz, false);
    }


    //跳转到新的activity
    public void startActivity(Class clazz, boolean isFinish) {
        ActivityUtils.startActivity(clazz);
        if (isFinish) {
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0], top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }

    protected PageType getPageType() {
        return PageType.Type_Null;
    }

    @Subscribe
    public void exitDiagnostic(CDispExitBeanEvent event) {
        finishDiagnostic();
    }

    /**
     * 结束诊断.
     */
    protected void finishDiagnostic() {
        if (isDiagnosticAct()) {
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RequestCode.READ_WRITE == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    EToastUtils.get().showShort(R.string.permission_write_read, false);
                    break;
                }
            }
        }
    }

}
