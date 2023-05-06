package com.eucleia.pdicheck.fragment.normal;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.FragMineAboutBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.net.NetAPI;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 关于
 */
public class MineAboutFragment extends BaseFragment {

    private FragMineAboutBinding binding;
    private LayoutHeadNormalBinding headNormal;


    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineAboutBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_about;
    }

    @Override
    protected void initView() {
        super.initView();
        initUI();
    }


    private void initUI() {
        headNormal.setHead(ResUtils.getString(R.string.about));
        binding.setListener(view -> {
            Intent urlIntent = new Intent(Intent.ACTION_VIEW);
            urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            urlIntent.setData(Uri.parse(NetAPI.GUIDE_URL));
            Utils.getApp().startActivity(urlIntent);
        });
        setValue();
    }


    @Override
    protected void reLoadFrag() {
        super.reLoadFrag();
        setValue();
    }

    private void setValue() {
        //产品名称
        String appName = ResUtils.getString(R.string.model_name);
        if (BuildConfig.DEBUG) {
            appName += CharVar.DEBUG;
        }
        binding.productName.setText(appName);

        // App版本
        binding.appVersion.setText(CharVar.VERSION + AppUtils.getAppVersionName());

        //序列号
        String sncode = DSUtils.get().getString(SPKEY.SN_CODE);
        if (TextUtils.isEmpty(sncode)) {
            if (BTConstant.isVciConnect) {
                sncode = ResUtils.getString(R.string.serial_rule);
            } else {
                sncode = CharVar.NODATA;
            }
        }
        binding.snCode.setText(sncode);

        // 密码
        String chcekCode = DSUtils.get().getString(SPKEY.CHECK_CODE);
        if (TextUtils.isEmpty(chcekCode)) {
            if (BTConstant.isVciConnect) {
                chcekCode = getString(R.string.serial_rule);
            } else {
                chcekCode = CharVar.NODATA;
            }
        }
        binding.password.setText(chcekCode);

        // 固件版本
        String vciVersion = DSUtils.get().getString(SPKEY.VCI_VERSION);
        if (TextUtils.isEmpty(vciVersion)) {
            binding.firmwareVersion.setText(CharVar.NODATA);
        } else {
            binding.firmwareVersion.setText(vciVersion);
        }

        // 通讯程序库版本
        String comVersion = DSUtils.get().getString(SPKEY.COMMUNICATION_VERSION);
        if (TextUtils.isEmpty(comVersion)) {
            binding.communicationVersion.setText(CharVar.NODATA);
        } else {
            binding.communicationVersion.setText(comVersion);
        }

        // 显示程序版本
        String dynamicVer = DSUtils.get().getString(SPKEY.DYNAMIC_VERSION);
        if (TextUtils.isEmpty(dynamicVer)) {
            binding.dynamicVersion.setText(CharVar.NODATA);
        } else {
            binding.dynamicVersion.setText(dynamicVer);
        }
    }
}
