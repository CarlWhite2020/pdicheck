package com.eucleia.pdicheck.net.presenter;

import java.util.ArrayList;

/**
 * 网络请求回调基类
 */
public interface BasePresenter<V> {

    void attachView(V view);

    void detachView(V view);

    ArrayList<V> getMvpViews();
}
