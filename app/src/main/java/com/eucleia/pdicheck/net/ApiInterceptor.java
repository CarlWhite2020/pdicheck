package com.eucleia.pdicheck.net;


import android.util.Log;

import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.tabscanap.constant.Constant;
import com.eucleia.tabscanap.util.DSUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 云端服务器请求拦截器
 */
public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();//管道中的请求对象
        Response originalResponse = chain.proceed(originalRequest);//待返回的请求对象

        //修改请求头示例
        //Request updateRequest = originalRequest.newBuilder()
        //        .header("Authorization", "").build();
        String name = DSUtils.get().getString(SPKEY.UserName);
        Log.d(Constant.APP_TAG, "intercept: 用户名:" + name);
        return originalResponse;
    }
}
