package com.eucleia.tabscanap.util;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.RoleCode;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.AnalyzeLog;
import com.eucleia.pdicheck.room.entity.User;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.tabscanap.constant.PathVar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 日志采集类.
 */
public final class AnalyseLogUtils {

    private AnalyseLogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 开始日志收集.
     *
     * @param startTime 诊断开始时间
     */
    public static void startLog(final long startTime) {

        AnalyzeLog log = new AnalyzeLog();
        log.id = null;
        log.startTime = startTime;
        log.user = JSON.toJSONString(AppDatabase.get().getUserDao()
                .getCurrUser());
        log.jniModel = JSON.toJSONString(EDiagUtils.get().getJniModel());
        log.diagModel = JSON.toJSONString(EDiagUtils.get().getModel());
        log.snCode = DSUtils.get().getString(SPKEY.SN_CODE);
        log.checkCode = DSUtils.get().getString(SPKEY.CHECK_CODE);
        log.vciVersion = DSUtils.get().getString(SPKEY.VCI_VERSION);
        log.dynamicVersion = DSUtils.get().getString(SPKEY.DYNAMIC_VERSION);
        log.commVersion = DSUtils.get().getString(SPKEY.COMMUNICATION_VERSION);
        log.appVersion = AppUtils.getAppVersionName();
        log.appName = AppUtils.getAppName();
        AppDatabase.get().getLogDao().addLog(log);
    }

    /**
     * 结束日志采集.
     *
     * @param startTime 诊断开始时间
     * @param logPath   诊断日志存储地址
     */
    public static void collect(final long startTime, final String logPath) {
        Observable.create(subscriber -> {
            AnalyzeLog currLog = AppDatabase.get()
                    .getLogDao().loadCurr(startTime);
            currLog.endTime = System.currentTimeMillis();
            currLog.jniModel = JSON.toJSONString(
                    EDiagUtils.get().getJniModel());
            currLog.logPath = logPath;
            currLog.diagStep = EDiagUtils.get().getPath();

            String commonName = SimpleDateFormat.getDateInstance()
                    .format(startTime)
                    + SimpleDateFormat.getTimeInstance()
                    .format(startTime);
            String zipTempRoot = SDUtils.getLocalPath()
                    + PathVar.autoCollectTemp;
            String logsRoot = SDUtils.getLocalPath() + PathVar.collectLog;
            String crashLog = SDUtils.getLocalPath() + PathVar.autoCrashLog;

            //初始化临时文件夹
            String zipTemp = zipTempRoot + commonName + File.separator;
            FileUtils.createOrExistsDir(zipTemp);
            FileUtils.createOrExistsDir(logsRoot);

            //若存在Crash文件，则拷贝APK奔溃日志到临时目录
            if (FileUtils.isFileExists(crashLog)) {
                FileUtils.move(crashLog, zipTemp);
            }

            // 移动JNI日志文件到临时目录
            if (logPath != null) {
                String[] strListPath = logPath.split("\\+");
                for (String tempPath : strListPath) {
                    File file = new File(tempPath);
                    String destPath = zipTemp + file.getName();
                    FileUtils.createOrExistsFile(destPath);
                    FileUtils.move(tempPath, destPath);
                }
            }

            //日志附加信息
            String infoStr = JSON.toJSONString(currLog);
            File infoFile = new File(zipTemp,
                    currLog.snCode + FileVar.TxtSuffix);
            FileUtils.createOrExistsFile(infoFile);
            FileIOUtils.writeFileFromString(infoFile, infoStr);

            String diagLog = SDUtils.getLocalPath()
                    + PathVar.rootPath
                    + CharVar.WuLing
                    + File.separator
                    + FileVar.LOG_NAME;
            FileUtils.copy(diagLog,
                    zipTemp + FileVar.LOG_NAME);

            // 将临时目录内的文件生成压缩包并存放到 待上传压缩包目录
            File zipFile = new File(logsRoot, currLog.snCode
                    + CharVar.MINUS
                    + SimpleDateFormat.getTimeInstance().format(startTime)
                    + FileVar.LogSuffix);
            FileUtils.createOrExistsFile(zipFile);


            //压缩单个文件夹
            ZipUtils.zipFiles(FileUtils.listFilesInDir(zipTemp), zipFile);
            FileUtils.deleteAllInDir(zipTemp);
            currLog.fileName = zipFile.getName();
            currLog.zipPath = zipFile.getPath();
            AppDatabase.get().getLogDao().updateLog(currLog);

        }).subscribeOn(Schedulers.io()).subscribe(new BaseObserver<Object>() {
            @Override
            public void onError(final Throwable e) {
                e.printStackTrace();
            }
        });


    }

    /**
     * 删除数据库中的本地日志.
     */
    public static void clear() {
        Observable.create(subscriber -> {
            List<AnalyzeLog> analyzeLogs = AppDatabase.get()
                    .getLogDao().loadAll();
            for (AnalyzeLog log : analyzeLogs) {
                if (!FileUtils.isFileExists(log.zipPath)) {
                    AppDatabase.get().getLogDao().deleteLog(log.id);
                }
            }
            AppDatabase.get().getLogDao().clearLog();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 返回当前用户默认的日志开关.
     *
     * @return true 打开 / false 关闭
     */
    public static boolean isDef() {
        User currUser = AppDatabase.get().getUserDao().getCurrUser();
        return RoleCode.DE.name().equals(currUser.roleCode)
                || BuildConfig.DEBUG;
    }
}
