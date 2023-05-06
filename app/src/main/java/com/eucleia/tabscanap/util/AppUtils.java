package com.eucleia.tabscanap.util;

import android.app.ActivityManager;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

import java.util.List;

/**
 * App工具类.
 */
public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 返回当前进程的进程名称.
     *
     * @param context Application or Context
     * @return 当前进程名
     */
    public static String getCurProcessName(final Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 判断应用程序是否处于前台.
     *
     * @return true 在前台，false 不在前台
     */
    public static boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager)
                Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName =
                com.blankj.utilcode.util.AppUtils.getAppPackageName();

        List<ActivityManager.RunningAppProcessInfo>
                appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager
                    .RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


}
