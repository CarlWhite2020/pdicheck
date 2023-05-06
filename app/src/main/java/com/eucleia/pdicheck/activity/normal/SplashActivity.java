package com.eucleia.pdicheck.activity.normal;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.ActSplashBinding;
import com.eucleia.tabscanap.util.DSUtils;


/**
 * 启动页
 */
public class SplashActivity extends BlueBaseActivity {

    @Override
    protected ViewDataBinding bindAct() {
       return ActSplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        openBluetooth();
    }

    @Override
    protected void openBlueSuccess() {
        if (DSUtils.get().getBoolean(SPKEY.IS_LOGIN)) {
            startActivity(MainActivity.class, true);
        } else {
            startActivity(LoginActivity.class, true);
        }
    }

    @Override
    protected void openBlueFail() {
        super.openBlueFail();
        finish();
    }
}
