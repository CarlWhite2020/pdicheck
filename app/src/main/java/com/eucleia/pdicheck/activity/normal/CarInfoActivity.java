package com.eucleia.pdicheck.activity.normal;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.FileUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActCarInfoBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.model.DiagModel;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.tabscanap.constant.Language;
import com.eucleia.tabscanap.constant.PathVar;
import com.eucleia.tabscanap.util.AnalyseLogUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.SDUtils;

import java.io.File;

/**
 * 车辆信息
 */
public class CarInfoActivity extends BlueBaseActivity {

    private ActCarInfoBinding binding;
    private LayoutHeadBaseBinding headBase;
    private final SingleClickListener commitListener = view -> {
        showLoading();
        DiagModel model = EDiagUtils.get().getModel();
        model.vehCode = CharVar.WuLing;
        model.language = Language.CN.toLowerCase();
        model.vehPath = SDUtils.getLocalPath() + PathVar.rootPath + CharVar.WuLing;
        String soPath = model.vehPath + File.separator + FileVar.LIBDIAG_SO;
        if (FileUtils.isFileExists(soPath)) {
            model.vehSoPath = SDUtils.CopySoToInner(soPath, CharVar.VEHDIAG, CharVar.WuLing);
        } else {
            model.vehSoPath = "";
        }
        EDiagUtils.get().setOpenLog(DSUtils.get().getBoolean(SPKEY.COLLECT_LOG, AnalyseLogUtils.isDef()));
        EDiagUtils.get().startDiag();
    };

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActCarInfoBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }


    @Override
    protected void initView() {

        base.setBackText(ResUtils.getString(R.string.home_page));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(ResUtils.getString(R.string.car_info));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);

        binding.setListener(commitListener);
        binding.setModel(EDiagUtils.get().getModel());
    }

    @Override
    protected void OnBackClick() {
        finish();
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(AppVM.get().getVciStatus());
        headBase.setHead(base);
    }
}
