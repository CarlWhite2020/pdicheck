package com.eucleia.pdicheck.net.presenter;


import com.eucleia.pdicheck.net.mvpview.BaseNetMvpView;

import java.util.ArrayList;

/**
 * 网络回调基类
 */
public class BaseNetPresenter extends BasePresenterImpl<BaseNetMvpView> {

    public void notifyEmpty() {
        ArrayList<BaseNetMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).Empty();
        }
    }

    public void notifyNetFail(String error) {
        ArrayList<BaseNetMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).NetFail(error);
        }
    }

    public void notifySuccess() {
        ArrayList<BaseNetMvpView> mvpViews = getMvpViews();
        int size = mvpViews.size();
        for (int i = 0; i < size; i++) {
            mvpViews.get(i).NetSuccess();
        }
    }


}
