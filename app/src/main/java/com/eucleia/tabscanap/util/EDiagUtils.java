package com.eucleia.tabscanap.util;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.bean.diag.BaseBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispExitBeanEvent;
import com.eucleia.pdicheck.bean.normal.DiagView;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.tabscanap.constant.IniVar;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.constant.PathVar;
import com.eucleia.tabscanap.jni.diagnostic.CDispGlobal;
import com.eucleia.tabscanap.jni.diagnostic.CDispMsgBox;
import com.eucleia.tabscanap.jni.diagnostic.CDispPdiCheck;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.jni.diagnostic.so.StdDisp;
import com.eucleia.tabscanap.bean.model.DiagModel;
import com.eucleia.tabscanap.bean.model.JNIModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

/**
 * 诊断工具类
 */
public class EDiagUtils {

    /**
     * 当前诊断的时间
     */
    private long diagTime;

    /**
     * 是否开启诊断的logcat。
     * false、true 平时可以默认关闭.
     */
    private boolean isOpenLogCat = true;

    /**
     * 是否加载Std
     */
    private volatile boolean isLoadStdDisp;

    /**
     * 是否诊断
     */
    private volatile boolean isDiagnostic;

    /**
     * 是否退出诊断
     */
    private volatile boolean isDiagnosticExiting;

    /**
     * 是否挂起
     */
    private volatile boolean isSuspending;

    /**
     * 开启日志
     */
    private volatile boolean isOpenLog;


    private volatile boolean needShowTakeDialog = false;

    private Stack<DiagView> diagPath = new Stack<>();

    /**
     * 中间层 so库
     */
    public volatile MyAsyncTask startSoThread;

    private volatile DiagModel model;
    private volatile JNIModel jniModel;


    public BaseBeanEvent getEvent(DiagView diagView) {
        if (diagView == null) return null;
        switch (diagView.getPageType()) {
            case Type_PdiCheck:
                return CDispPdiCheck.PDICHECK_MAP_EVENT.get(diagView.getObjKey());
            case Type_MsgBox:
                return CDispMsgBox.MSG_BOX_MAP_EVENT.get(diagView.getObjKey());
            case Type_GlobalMsgBox:
                return CDispGlobal.MSG_BOX_MAP_EVENT.get(diagView.getObjKey());
        }
        return null;
    }

    public String getEventPath(DiagView diagView) {
        if (diagView == null) return null;
        return diagView.getPageType().name();
    }

    public boolean isThreadEnd() {
        if (!isDiagnostic) {
            return true;
        }
        if (getJniModel().threadStatus == CDispConstant.THREAD_END) {
            return true;
        }
        return isDiagnosticExiting;
    }

    public void clearRightPage() {
        Stack<DiagView> path = (Stack<DiagView>) diagPath.clone();
        while (!path.empty()) {
            DiagView v = path.pop();
            BaseBeanEvent event = getEvent(v);
            if (event != null && event.getDispType() == CDispConstant.PageDisp.DISP_RIGHT) {
                event.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                event.lockAndSignalAll();
            }
        }
    }

    public void clearAllPage() {
        Stack<DiagView> path = (Stack<DiagView>) diagPath.clone();
        while (!path.empty()) {
            DiagView v = path.pop();
            BaseBeanEvent event = getEvent(v);
            if (event != null) {
                event.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                event.lockAndSignalAll();
            }
        }
    }

    public void okAllPage() {
        Stack<DiagView> path = (Stack<DiagView>) diagPath.clone();
        while (!path.empty()) {
            DiagView v = path.pop();
            BaseBeanEvent event = getEvent(v);
            if (event != null) {
                event.setBackFlag(CDispConstant.PageButtonType.DF_ID_OK);
                event.lockAndSignalAll();
            }
        }
    }


    private static class INSTANCE {
        private static final EDiagUtils instance = new EDiagUtils();
    }

    private EDiagUtils() {
    }

    public static EDiagUtils get() {
        return INSTANCE.instance;
    }

    public void init() {
        copyPdiDiag();

        String ssPath = Utils.getApp().getExternalCacheDir().getParentFile().getPath() + "/";
        Communication.SetStorablePath(ssPath);
        Communication.Entry();
        if (!isLoadStdDisp) {
            System.loadLibrary(CharVar.Std_Disp);
            isLoadStdDisp = true;
        }
        JNIUtils.saveCommConfig();
    }

    /**
     * 拷贝PDI
     */
    private void copyPdiDiag() {
        try {
            boolean isCompare = true;
            String localPath = SDUtils.getLocalPath() + PathVar.rootPath + CharVar.WuLing;
            InputStream open = Utils.getApp().getAssets().open(CharVar.WuLing + File.separator + FileVar.VERINFO_INI);
            Properties assetsVer = new Properties();
            assetsVer.load(open);

            String iniLocal = localPath + File.separator + FileVar.VERINFO_INI;
            if (FileUtils.isFileExists(iniLocal)) {
                Properties localVer = new Properties();
                localVer.load(new FileInputStream(iniLocal));
                isCompare = VerCompareUtils.compareByChar(String.valueOf(localVer.get(IniVar.VEH_VER)),
                        String.valueOf(assetsVer.get(IniVar.VEH_VER)));
            }
            if (isCompare) {
                FileUtils.deleteAllInDir(localPath + File.separator);
                AssetsUtils.copyAssetsFile(CharVar.WuLing, localPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void startDiag() {
        if (isDiagnostic) return;
        setModelYear();
        if (startSoThread == null || startSoThread.getStatus() != AsyncTask.Status.PENDING) {
            startSoThread = new MyAsyncTask();
            startSoThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public synchronized void stopDiag() {
        if (startSoThread != null) {
            startSoThread.cancel(true);
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ELogUtils.d(" -> startDiag");
            // Task被取消了，马上退出
            if (isCancelled()) {
                return null;
            }
            try {
                diagTime = System.currentTimeMillis();
                isDiagnostic = true;
                if (isOpenLog) {
                    Communication.AutoLogStart();
                    AnalyseLogUtils.startLog(diagTime);
                }
                // 中间层so入口
                StdDisp.entrance();

            } catch (Exception e) {
                e.printStackTrace();
                ELogUtils.d("-> Diag Exception");
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... params) {
            // 判断是否被取消
            if (isCancelled()) {
                return;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ELogUtils.d(" -> endDiag");
            needShowTakeDialog = true;
            isDiagnostic = false;
            if (isOpenLog) {
                String autoLogPath = Communication.AutoLogStop();
                AnalyseLogUtils.collect(diagTime, autoLogPath);
            }
            EventBus.getDefault().post(new CDispExitBeanEvent());
            model = null;
            jniModel = null;
            startSoThread = null;

        }

    }


    public DiagModel getModel() {
        if (model == null) {
            model = new DiagModel();
            model.brand = ResUtils.getString(R.string.wuling);
        }
        return model;
    }

    public void setModel(DiagModel model) {
        this.model = model;
    }

    public JNIModel getJniModel() {
        if (jniModel == null) {
            jniModel = new JNIModel();
        }
        return jniModel;
    }

    public void setJniModel(JNIModel jniModel) {
        this.jniModel = jniModel;
    }

    public void setModelYear() {
        String code = RoomUtils.getPdiDB().getCheckPlanDao().getFunCodeByModelYear(model.model, model.year);
        if (!TextUtils.isEmpty(code) && code.length() > 10) {
            model.carNameId = code.substring(4, 8);
            model.carYearId = code.substring(8, 10);
        }
    }


    public void addPath(int objKey, PageType type) {
        if (diagPath.size() > 0) {
            DiagView last = diagPath.peek();
            if (last.getObjKey() == objKey &&
                    last.getPageType() == type) {
                return;
            }
        }
        DiagView diagView = new DiagView();
        diagView.setCurrTime(System.currentTimeMillis());
        diagView.setObjKey(objKey);
        diagView.setPageType(type);
        diagPath.add(diagView);
    }

    public String getPath() {
        List<DiagView> path = (List<DiagView>) diagPath.clone();
        StringBuilder pathBuild = new StringBuilder();
        for (DiagView d : path) {
            pathBuild.append(getEventPath(d));
            pathBuild.append(CharVar.NEWLINE);
        }
        return pathBuild.toString();
    }

    public boolean isLoadStdDisp() {
        return isLoadStdDisp;
    }

    public void setLoadStdDisp(boolean loadStdDisp) {
        isLoadStdDisp = loadStdDisp;
    }

    public boolean isDiagnostic() {
        return isDiagnostic;
    }

    public void setDiagnostic(boolean diagnostic) {
        isDiagnostic = diagnostic;
    }

    public boolean isSuspending() {
        return isSuspending;
    }

    public void setSuspending(boolean suspending) {
        isSuspending = suspending;
    }

    public boolean isDiagnosticExiting() {
        return isDiagnosticExiting;
    }

    public void setDiagnosticExiting(boolean diagnosticExiting) {
        isDiagnosticExiting = diagnosticExiting;
    }

    public boolean isOpenLog() {
        return isOpenLog;
    }

    public void setOpenLog(boolean openLog) {
        isOpenLog = openLog;
    }

    public boolean isOpenLogCat() {
        return isOpenLogCat;
    }

    public void setOpenLogCat(boolean openLogCat) {
        isOpenLogCat = openLogCat;
    }

    public boolean isNeedShowTakeDialog() {
        return needShowTakeDialog;
    }

    public void setNeedShowTakeDialog(boolean needShowTakeDialog) {
        this.needShowTakeDialog = needShowTakeDialog;
    }
}
