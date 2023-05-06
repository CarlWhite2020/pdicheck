package com.eucleia.pdicheck.net.mvpview;


import com.eucleia.pdicheck.net.model.AppVersion;

/**
 * 更新应用
 */
public interface UpdateAppMvpView extends BaseMvpView {

    void updateApkSucess(AppVersion appVersion);//刷新APK成功

    void updateNotData(String errroMsg);

    void updateError(String errroMsg);


}
