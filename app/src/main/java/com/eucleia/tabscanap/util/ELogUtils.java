package com.eucleia.tabscanap.util;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.tabscanap.constant.PathVar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志打印类
 */
public final class ELogUtils {

    private ELogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private final static String customTagPrefix = "APP_LOG -> "; // 自定义Tag的前缀，可以是作者名
    private static final boolean isSaveLog = false; // 是否把保存日志到SD卡中
    private static final boolean isDebug = BuildConfig.DEBUG;

    private static final String PATH_LOG_INFO = SDUtils.getLocalPath() + PathVar.LogPath;


    // 容许打印日志的类型，默认是true，设置为false则不打印
    private static boolean allowD = true;
    private static boolean allowE = true;
    private static boolean allowI = true;
    private static boolean allowV = true;
    private static boolean allowW = true;
    private static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {
        String result;
        if (caller.getLineNumber() >= 0) {
            result = " .(" + caller.getFileName() + ":" + caller.getLineNumber() + ")";
        } else {
            result = " .(" + caller.getFileName() + ")";
        }

        return result;
    }

    public static void e() {
        e("");
    }

    public static void e(boolean isOpen) {
        if (isOpen) {
            e();
        }
    }

    public static void e(boolean isOpen, Object msg) {
        if (isOpen) {
            e(msg);
        }
    }


    public static void v() {
        v("");
    }

    public static void d() {
        d("");
    }


    public static void d(Object content) {
        d(JSON.toJSONString(content));
    }

    public static void d(String TAG, String content) {
        if (!allowV || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.d(TAG + " -> " + caller.getMethodName(), content + link);
    }


    public static void d(String content) {
        if (!allowD || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);
        Log.d(customTagPrefix + caller.getMethodName(), content + link);
    }

    public static void d(String content, Throwable tr) {
        if (!allowD || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.d(customTagPrefix + caller.getMethodName(), content + link, tr);
    }

    public static void e(Object content) {
        e(JSON.toJSONString(content));
    }


    public static void e(String content) {
        if (!allowE || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.e(customTagPrefix + caller.getMethodName(), content + link);
        if (isSaveLog) {
            point(PATH_LOG_INFO, customTagPrefix + caller.getMethodName(), content + link);
        }
    }

    public static void e(String link, String content) {
        if (!allowE || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();

        Log.e(customTagPrefix + caller.getMethodName(), content + link);
        if (isSaveLog) {
            point(PATH_LOG_INFO, customTagPrefix + caller.getMethodName(), content + link);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.e(customTagPrefix + caller.getMethodName(), content + link, tr);
        if (isSaveLog) {
            point(PATH_LOG_INFO, customTagPrefix + caller.getMethodName(), tr.getMessage());
        }
    }

    public static void i(String content) {
        if (!allowI || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.i(customTagPrefix + caller.getMethodName(), content + link);

    }

    public static void i(String content, Throwable tr) {
        if (!allowI || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.i(customTagPrefix + caller.getMethodName(), content + link, tr);

    }

    public static void v(Object obj) {
        v(JSON.toJSONString(obj));
    }

    public static void v(String content) {
        if (!allowV || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.v(customTagPrefix + caller.getMethodName(), content + link);
    }

    public static void v(String TAG, String content) {
        if (!allowV || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.v(TAG + " -> " + caller.getMethodName(), content + link);
    }

    public static void v(String content, Throwable tr) {
        if (!allowV || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.v(customTagPrefix + caller.getMethodName(), content + link, tr);
    }

    public static void w(String content) {
        if (!allowW || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.w(customTagPrefix + caller.getMethodName(), content + link);
    }

    public static void w(String content, Throwable tr) {
        if (!allowW || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.w(customTagPrefix + caller.getMethodName(), content + link, tr);
    }

    public static void w(Throwable tr) {
        if (!allowW || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(customTagPrefix + caller.getMethodName(), tr);
    }

    public static void wtf(String content) {
        if (!allowWtf || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.wtf(customTagPrefix + caller.getMethodName(), content + link);
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String link = generateTag(caller);

        Log.wtf(customTagPrefix + caller.getMethodName(), content + link, tr);
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf || !isDebug)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.wtf(customTagPrefix + caller.getMethodName(), tr);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 4) {
            for (int i = 4; i < stackTrace.length; i++) {
                if (!stackTrace[i].getClassName().equals(ELogUtils.class.getName())) {
                    return stackTrace[i];
                }
            }
        }
        return stackTrace[stackTrace.length - 1];
    }

    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean isSDAva() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || SDUtils.getLocalFile().exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAllowD() {
        return allowD;
    }

    public static void setAllowD(boolean allowD) {
        ELogUtils.allowD = allowD;
    }

    public static boolean isAllowE() {
        return allowE;
    }

    public static void setAllowE(boolean allowE) {
        ELogUtils.allowE = allowE;
    }

    public static boolean isAllowI() {
        return allowI;
    }

    public static void setAllowI(boolean allowI) {
        ELogUtils.allowI = allowI;
    }

    public static boolean isAllowV() {
        return allowV;
    }

    public static void setAllowV(boolean allowV) {
        ELogUtils.allowV = allowV;
    }

    public static boolean isAllowW() {
        return allowW;
    }

    public static void setAllowW(boolean allowW) {
        ELogUtils.allowW = allowW;
    }

    public static boolean isAllowWtf() {
        return allowWtf;
    }

    public static void setAllowWtf(boolean allowWtf) {
        ELogUtils.allowWtf = allowWtf;
    }
}
