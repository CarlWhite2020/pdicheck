package com.eucleia.tabscanap.util;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Rx回调基类.
 *
 * @param <T>
 */
public class BaseObserver<T> implements Observer<T> {

    /**
     * 错误回调.
     *
     * @param e 错误信息
     */
    @Override
    public void onError(final Throwable e) {
        e.printStackTrace();
    }

    /**
     * 完成.
     */
    @Override
    public void onComplete() {
        ELogUtils.d();
    }

    @Override
    public void onSubscribe(final Disposable s) {
    }

    /**
     * 下一步操作.
     *
     * @param t 传递的对象
     */
    @Override
    public void onNext(final T t) {
        ELogUtils.d(t.getClass());
    }
}
