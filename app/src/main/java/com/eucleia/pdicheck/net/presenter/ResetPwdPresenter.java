package com.eucleia.pdicheck.net.presenter;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.User;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.pdicheck.net.BaseCallBack;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.MDUtlis;
import com.eucleia.tabscanap.util.ResUtils;
import com.lzy.okgo.OkGo;

import java.util.HashMap;
import java.util.Map;

/**
 * 重设密码工具
 */
public class ResetPwdPresenter extends BaseNetPresenter {

    private ResetPwdPresenter() {
    }

    private static class INSTANCE {
        private static final ResetPwdPresenter presenter = new ResetPwdPresenter();
    }

    public static ResetPwdPresenter get() {
        return INSTANCE.presenter;
    }

    public void resetPwd(String oldPwd, String newPwd) {
        User currUser = AppDatabase.get().getUserDao().getCurrUser();
        if (currUser == null) {
            EToastUtils.get().showShort(R.string.relogin,false);
            notifyEmpty();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userCode", currUser.userCode);
        map.put("oldPassWord", MDUtlis.getMd5(oldPwd));
        map.put("newPassWord", MDUtlis.getMd5(newPwd));
        OkGo.post(NetAPI.getAPI(NetAPI.PW_CHANGE))
                .upJson(JSON.toJSONString(map))
                .execute(new BaseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        notifySuccess();
                    }

                    @Override
                    public void onError(String error) {
                        notifyNetFail(error);
                    }
                });
    }

}
