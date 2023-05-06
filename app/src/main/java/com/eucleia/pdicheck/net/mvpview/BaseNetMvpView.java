package com.eucleia.pdicheck.net.mvpview;

public interface BaseNetMvpView extends BaseMvpView {

    void Empty();

    void NetSuccess();

    void NetFail(String error);

}
