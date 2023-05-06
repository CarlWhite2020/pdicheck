package com.eucleia.pdicheck.net.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.User;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.pdicheck.net.BaseCallBack;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.Constant;
import com.eucleia.tabscanap.util.AnalyseLogUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.MDUtlis;
import com.lzy.okgo.OkGo;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter extends BaseNetPresenter {


    private static class Instance {
        private final static LoginPresenter presenter = new LoginPresenter();
    }

    private LoginPresenter() {
    }

    public static LoginPresenter get() {
        return Instance.presenter;
    }

    public void login(String userName, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("passWord", MDUtlis.getMd5(pwd));
        params.put("appId", CharVar.MODEL);
//        OkGo.post(NetAPI.getAPI(NetAPI.LOGIN))
//                .upJson(JSON.toJSONString(params))
//                .execute(new BaseCallBack() {
//                    @Override
//                    public void onSuccess(String result) {
//
//                        Log.d(TAG, "onSuccess: 登录成功");
//                        User user = JSON.parseObject(result, User.class);
//                        user.isLogin = true;
//                        AppDatabase.get().getUserDao().insertUser(user);
//                        DSUtils.get().putBoolean(SPKEY.IS_LOGIN, true);
//                        DSUtils.get().putBoolean(SPKEY.COLLECT_LOG, AnalyseLogUtils.isDef());
//                        ELogUtils.d(user);
//                        notifySuccess();
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        notifyNetFail(error);
//                    }
//                });

        String api="http://192.168.125.89:5000/api/account/login";
        OkGo.post(NetAPI.getAPI(api))
                .upJson(JSON.toJSONString(params))
                .execute(new BaseCallBack() {
                    @Override
                    public void onSuccess(String result) {

                        Log.d(Constant.APP_TAG, "onSuccess: 登录成功");
                        User user = JSON.parseObject(result, User.class);
                        user.isLogin = true;
                        AppDatabase.get().getUserDao().insertUser(user);
                        DSUtils.get().putBoolean(SPKEY.IS_LOGIN, true);
                        DSUtils.get().putBoolean(SPKEY.COLLECT_LOG, AnalyseLogUtils.isDef());
                        DSUtils.get().putString(SPKEY.UserName,user.userName);
                        ELogUtils.d(user);
                        notifySuccess();
                    }

                    @Override
                    public void onError(String error) {
                        notifyNetFail(error);
                    }


                });
    }

}
