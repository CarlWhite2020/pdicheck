package com.eucleia.pdicheck.net.presenter;

import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.net.model.AppVersion;
import com.eucleia.pdicheck.net.mvpview.UpdateAppMvpView;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.pdicheck.net.BaseCallBack;
import com.eucleia.tabscanap.constant.CharVar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;

/**
 * 更新应用
 */
public class UpdateAppPresenter extends BasePresenterImpl<UpdateAppMvpView> {


    private UpdateAppPresenter() {
    }

    public static class INSTANCE {
        private static final UpdateAppPresenter updateAppPresenter = new UpdateAppPresenter();
    }

    public static UpdateAppPresenter get() {
        return INSTANCE.updateAppPresenter;
    }


    public void updateApk() {
        if (Constant.isAppBundle) {
            updateNotData("");
            return;
        }
        HttpParams params = new HttpParams();
        params.put("appId", CharVar.MODEL);
        OkGo.get(NetAPI.getAPI(NetAPI.APP_UPDATE)).params(params).execute(new BaseCallBack() {

            @Override
            public void onSuccess(String result) {
                AppVersion appVersion = JSON.parseObject(result, AppVersion.class);
                updateApkSucess(appVersion);
            }

            @Override
            public void onError(String error) {
                updateError(error);
            }
        });
    }


    public void updateApkSucess(AppVersion appVersion) {
        ArrayList<UpdateAppMvpView> updateMvpViews = getMvpViews();
        int size = updateMvpViews.size();
        for (int i = 0; i < size; i++) {
            updateMvpViews.get(i).updateApkSucess(appVersion);
        }
    }

    public void updateNotData(String errroMsg) {
        ArrayList<UpdateAppMvpView> updateMvpViews = getMvpViews();
        int size = updateMvpViews.size();
        for (int i = 0; i < size; i++) {
            updateMvpViews.get(i).updateNotData(errroMsg);
        }
    }

    public void updateError(String errroMsg) {
        ArrayList<UpdateAppMvpView> updateMvpViews = getMvpViews();
        int size = updateMvpViews.size();
        for (int i = 0; i < size; i++) {
            updateMvpViews.get(i).updateError(errroMsg);
        }
    }

}
