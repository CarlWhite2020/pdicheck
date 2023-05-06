package com.eucleia.pdicheck.net;


import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.net.model.PDIResponse;
import com.eucleia.tabscanap.util.ResUtils;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okio.BufferedSource;
import okio.Okio;

public abstract class BaseCallBack implements Callback {
    @Override
    public void onStart(Request request) {

    }

    @Override
    public void onSuccess(Response response) {
        if (response.isSuccessful()) {
            try {
                PDIResponse pdiResponse = (PDIResponse) response.body();
                if (pdiResponse.isSuccess()) {
                    onSuccess(pdiResponse.getResult());
                } else {
                    onError(pdiResponse.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(ResUtils.getString(R.string.network_retry));
            }
        } else {
            onError(ResUtils.getString(R.string.network_retry));
        }
    }

    @Override
    public void onCacheSuccess(Response response) {

    }

    @Override
    public void onError(Response response) {
        if (response.getException() instanceof UnknownHostException) {
            onError(ResUtils.getString(R.string.network_null));
        } else if (response.getException() instanceof SocketTimeoutException) {
            onError(ResUtils.getString(R.string.network_timeout));
        } else {
            onError(response.getException().getMessage());
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public PDIResponse convertResponse(okhttp3.Response response) throws Throwable {
        BufferedSource buffer = Okio.buffer(response.body().source());
        String s = buffer.readUtf8();
        return JSON.parseObject(s, PDIResponse.class);
    }

    public abstract void onSuccess(String result);

    public abstract void onError(String error);
}
