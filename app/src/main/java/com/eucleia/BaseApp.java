package com.eucleia;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.ChangeOrientation;
import com.eucleia.pdicheck.net.ApiInterceptor;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.model.BtFilter;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.util.AnalyseLogUtils;
import com.eucleia.tabscanap.util.AppUtils;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.CrashHandler;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.VciProductUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Application
 */
public class BaseApp extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (Constant.PackageName.equals(AppUtils.getCurProcessName(this)) && shouldInit()) {
            Constant.orientation = getResources().getConfiguration().orientation;
            initTools();
            initViewModel();
            AnalyseLogUtils.clear();
            EDiagUtils.get().init();
        }

    }

    private void initViewModel() {
        AppVM.get();
    }


    private void initTools() {
        //初始化util工具类
        Utils.init(this);
        HttpHeaders.setUserAgent("okHttp-okGo/pdiCheck");
        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(
                    new ApiInterceptor()
                ).build();
        OkGo.getInstance().init(this).setRetryCount(1).setOkHttpClient(client);


        CrashHandler.get().init();

        BtFilter bt1 = new BtFilter(VciProductUtils.PRODUCT_T3, VciProductUtils.CUSTOM_SGM);
        BtFilter bt2 = new BtFilter("A", null);
        BTUtils.get().setFilter(bt1
                ,bt2
        );

        BTConstant.btName = DSUtils.get().getString(SPKEY.BtName);
        BTConstant.btAddress = DSUtils.get().getString(SPKEY.BtMac);
    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Constant.orientation != newConfig.orientation) {
            Constant.orientation = newConfig.orientation;
            EventBus.getDefault().post(new ChangeOrientation());
        }
    }


}
