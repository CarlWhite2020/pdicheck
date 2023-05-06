package com.eucleia.pdicheck.fragment.normal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.ChangeMine;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.FragMineDeviceBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ResUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 我的设备
 */
public class MineDeviceFragment extends BaseFragment {
    private FragMineDeviceBinding binding;
    private LayoutHeadNormalBinding headNormal;

    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineDeviceBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_device;
    }

    @Override
    protected void initView() {
        headNormal.setHead(ResUtils.getString(R.string.my_device));
        binding.myVci.setText(DSUtils.get().getString(SPKEY.SN_CODE));
        binding.snCode.setText(DSUtils.get().getString(SPKEY.SN_CODE));
        binding.password.setText(DSUtils.get().getString(SPKEY.CHECK_CODE));
        binding.firmwareVersion.setText(DSUtils.get().getString(SPKEY.VCI_VERSION));
        binding.disconnect.setOnClickListener(v -> {
            BTConstant.btAddress = null;
            BTConstant.btName = null;
            DSUtils.get().putString(SPKEY.BtName, BTConstant.btName);
            DSUtils.get().putString(SPKEY.BtMac, BTConstant.btAddress);
            BTUtils.get().disConnect();
        });
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        super.vciChange(vciStatus);
        if (vciStatus.getVciStatus() == 0) {
            EventBus.getDefault().post(new ChangeMine(-1));
        }
    }
}
