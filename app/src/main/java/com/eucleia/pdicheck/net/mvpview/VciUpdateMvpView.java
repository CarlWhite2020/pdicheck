package com.eucleia.pdicheck.net.mvpview;

public interface VciUpdateMvpView extends BaseMvpView {

    void notifySuccess();

    void notifyError();

    void notifyProgress(int progress, String content);

    void notifyStart();
}
