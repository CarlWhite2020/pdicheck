package com.eucleia.tabscanap.util.immersionbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.util.ELogUtils;


/**
 * The type Bar config.
 *
 * @author geyifeng
 * @date 2017 /5/11
 */
class BarConfig {

    private final int mStatusBarHeight;
    private final int mActionBarHeight;
    private final boolean mHasNavigationBar;
    private final int mNavigationBarHeight;
    private final int mNavigationBarWidth;
    private final boolean mInPortrait;
    private final float mSmallestWidthDp;

    /**
     * Instantiates a new Bar config.
     *
     * @param activity the activity
     */
    BarConfig(Activity activity) {
        Resources res=activity.getResources();
        mInPortrait=(res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        mSmallestWidthDp=getSmallestWidthDp(activity);
        mStatusBarHeight=getInternalDimensionSize(activity, Constants.IMMERSION_STATUS_BAR_HEIGHT);
        mActionBarHeight=getActionBarHeight(activity);
        mNavigationBarHeight=getNavigationBarHeight(activity);
        mNavigationBarWidth=getNavigationBarWidth(activity);
        mHasNavigationBar=(mNavigationBarHeight > 0);
    }

    @TargetApi(14)
    private int getActionBarHeight(Activity activity) {
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            View actionBar=activity.getWindow().findViewById(R.id.action_bar_container);
            if (actionBar != null) {
                result=actionBar.getMeasuredHeight();
            }
            if (result == 0) {
                TypedValue tv=new TypedValue();
                activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result= TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarHeight(Context context) {
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if (mInPortrait) {
                    key=Constants.IMMERSION_NAVIGATION_BAR_HEIGHT;
                } else {
                    key=Constants.IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarWidth(Context context) {
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                return getInternalDimensionSize(context, Constants.IMMERSION_NAVIGATION_BAR_WIDTH);
            }
        }
        return result;
    }

    @TargetApi(14)
    private boolean hasNavBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //判断小米手机是否开启了全面屏，开启了，直接返回false
            if (Settings.Global.getInt(activity.getContentResolver(), Constants.IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                return false;
            }
            //判断华为手机是否隐藏了导航栏，隐藏了，直接返回false
            if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (Settings.System.getInt(activity.getContentResolver(), Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                        return false;
                    }
                } else {
                    if (Settings.Global.getInt(activity.getContentResolver(), Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                        return false;
                    }
                }
            }
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager=activity.getWindowManager();
        Display d=windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics=new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight=realDisplayMetrics.heightPixels;
        int realWidth=realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics=new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight=displayMetrics.heightPixels;
        int displayWidth=displayMetrics.widthPixels;

        return (realWidth-displayWidth) > 0 || (realHeight-displayHeight) > 0;
    }

    private int getInternalDimensionSize(Context context, String key) {
        int result=0;
        try {
            int resourceId= Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                int sizeOne=context.getResources().getDimensionPixelSize(resourceId);
                int sizeTwo= Resources.getSystem().getDimensionPixelSize(resourceId);

                if (sizeTwo >= sizeOne) {
                    return sizeTwo;
                } else {
                    float densityOne=context.getResources().getDisplayMetrics().density;
                    float densityTwo= Resources.getSystem().getDisplayMetrics().density;
                    float f=sizeOne * densityTwo / densityOne;
                    return (int) ((f >= 0) ? (f+0.5f) : (f-0.5f));
                }
            }
        } catch (Resources.NotFoundException ignored) {
            ELogUtils.v("getInternalDimensionSize: "+ignored.getMessage());
            return 0;
        }
        return result;
    }

    @SuppressLint("NewApi")
    private float getSmallestWidthDp(Activity activity) {
        DisplayMetrics metrics=new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        float widthDp=metrics.widthPixels / metrics.density;
        float heightDp=metrics.heightPixels / metrics.density;
        return Math.min(widthDp, heightDp);
    }

    /**
     * 判断导航是否在底部
     * @return
     */
    private boolean isNavBottom() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            return Gravity.BOTTOM == getNavPosO(Utils.getApp());
        } else {
            return Gravity.BOTTOM == getNavPosP(Utils.getApp());
        }
    }


    /**
     * 8.0及以下系统获取导航栏显示位置
     * 返回导航栏位置，1是显示在底部，2是显示在右侧,0是不显示
     *
     * @param cn
     * @return
     */
    private static int getNavPosO(Context cn) {
        WindowManager wmg=(WindowManager) cn.getSystemService(Context.WINDOW_SERVICE);
        final Display display=wmg.getDefaultDisplay();
        DisplayMetrics metricsReal=new DisplayMetrics();
        display.getRealMetrics(metricsReal);

        DisplayMetrics metricsCurrent=new DisplayMetrics();
        wmg.getDefaultDisplay().getMetrics(metricsCurrent);

        int currH=metricsCurrent.heightPixels;
        int currW=metricsCurrent.widthPixels;

        int realW=metricsReal.widthPixels;
        int realH=metricsReal.heightPixels;

        if (currH != realH && currW == realW) {
            return Gravity.BOTTOM;
        } else if (currW != realW && currH == realH) {
            return Gravity.RIGHT;
        } else {
            return 0;
        }

    }

    /**
     * 9.0获取导航栏显示位置，1左边，2右边，4底部
     *
     * @param context
     * @return
     */
    private static int getNavPosP(Context context) {
        WindowManager wmg=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int pos=0;
        Display display=wmg.getDefaultDisplay();
        int rotate=display.getRotation();
        int height=display.getHeight();
        int width=display.getWidth();
        if (width > height) {
            if (rotate == Surface.ROTATION_270) {
                //left
                pos= Gravity.START;
            } else {
                //right
                pos= Gravity.END;
            }
        } else {
            //bottom
            pos= Gravity.BOTTOM;
        }
        return pos;
    }


    /**
     * Should a navigation bar appear at the bottom of the screen in the current
     * device configuration? A navigation bar may appear on the right side of
     * the screen in certain configurations.
     *
     * @return True if navigation should appear at the bottom of the screen, False otherwise.
     */
    boolean isNavigationAtBottom() {
        return isNavBottom();
//        return (mSmallestWidthDp >= 600 || mInPortrait);
    }


    /**
     * Get the height of the system status bar.
     *
     * @return The height of the status bar (in pixels).
     */
    int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    /**
     * Get the height of the action bar.
     *
     * @return The height of the action bar (in pixels).
     */
    int getActionBarHeight() {
        return mActionBarHeight;
    }

    /**
     * Does this device have a system navigation bar?
     *
     * @return True if this device uses soft key navigation, False otherwise.
     */
    boolean hasNavigationBar() {
        return mHasNavigationBar;
    }

    /**
     * Get the height of the system navigation bar.
     *
     * @return The height of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    int getNavigationBarHeight() {
        return mNavigationBarHeight;
    }

    /**
     * Get the width of the system navigation bar when it is placed vertically on the screen.
     *
     * @return The width of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    int getNavigationBarWidth() {
        return mNavigationBarWidth;
    }
}