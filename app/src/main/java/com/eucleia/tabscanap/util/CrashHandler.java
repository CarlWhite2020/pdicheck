package com.eucleia.tabscanap.util;

import android.os.Build;
import android.os.Looper;

import com.alibaba.fastjson.util.IOUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.PathVar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private Map<String, String> mInfoMap = new HashMap<String, String>();

    private static class InnerClass {
        private static final CrashHandler HANDLER = new CrashHandler();
    }

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    public static CrashHandler get() {
        return InnerClass.HANDLER;
    }

    /**
     * 初始化
     */
    public void init() {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                ELogUtils.e(TAG + "error : " + e.getMessage());
            }
        }
        //退出程序
        Looper.getMainLooper().quit();
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        //收集设备参数信息
        collectDeviceInfo();
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo() {
        mInfoMap.put("versionName", AppUtils.getAppVersionName());
        mInfoMap.put("versionCode", String.valueOf(AppUtils.getAppVersionCode()));
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfoMap.put(field.getName(), field.get(null).toString());
                ELogUtils.d(TAG + field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = DateFormat.getDateTimeInstance().format(new Date());
            String fileName = "CRASH" + CharVar.MINUS + time + ".log";
            String path = SDUtils.getLocalPath() + PathVar.CrashPath;
            FileIOUtils.writeFileFromString(path + fileName, sb.toString());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(printWriter);
        }
        return null;
    }
}