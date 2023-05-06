package com.eucleia.pdicheck.net.presenter;


import com.eucleia.pdicheck.net.mvpview.BaseMvpView;

import java.util.ArrayList;

public class BasePresenterImpl<E extends BaseMvpView> implements BasePresenter<E> {


    private ArrayList<BaseMvpView> mMvpView = new ArrayList<>();

    @Override
    public void attachView(BaseMvpView view) {
        if (!mMvpView.contains(view)) {
            mMvpView.add(view);
        }
    }

    @Override
    public void detachView(BaseMvpView view) {
        mMvpView.remove(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<E> getMvpViews() {
        return (ArrayList<E>) mMvpView.clone();
    }


}
