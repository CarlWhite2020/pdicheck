package com.eucleia.pdicheck.fragment.normal;

import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.databinding.FragMineVciBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.bean.normal.VciVersion;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.presenter.VciUpdatePresenter;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.VerCompareUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 固件升级
 */
public class MineVciFragment extends BaseFragment {

    private FragMineVciBinding binding;
    private LayoutHeadNormalBinding headNormal;

    private SingleClickListener singleClickListener = view -> VciUpdatePresenter.get().start();


    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineVciBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_vci;
    }

    @Override
    protected void initLayout() {
        headNormal.setHead("固件升级");
        binding.setListener(singleClickListener);
        setUpdateInfo();
    }

    @Override
    protected void reLoadFrag() {
        setUpdateInfo();
    }

    private void setUpdateInfo() {
        VciVersion vciInfo = getVciInfo();
        binding.setVer(vciInfo);
        if (BTConstant.isVciConnect) {
            binding.update.setVisibility(View.VISIBLE);
            String vciVersion = DSUtils.get().getString(SPKEY.VCI_VERSION);
            if (VerCompareUtils.compareByChar(vciVersion, vciInfo.getVersion())) {
                binding.update.setText(ResUtils.getString(R.string.vciUpdate));
                binding.update.setEnabled(true);
            } else {
                binding.update.setText(ResUtils.getString(R.string.version_new));
                binding.update.setEnabled(false);
            }
        } else {
            binding.update.setVisibility(View.GONE);
        }
    }

    private VciVersion getVciInfo() {
        VciVersion version = new VciVersion();
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream is = assetManager.open("firmware/vci.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            String s = stringBuilder.toString();
            version = JSON.parseObject(s, VciVersion.class);

            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return version;
        }
    }
}
