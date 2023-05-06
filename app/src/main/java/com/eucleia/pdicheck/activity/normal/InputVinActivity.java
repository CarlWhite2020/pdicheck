package com.eucleia.pdicheck.activity.normal;

import android.text.TextUtils;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.bean.normal.ModelYear;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActInputVinBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.pdicheck.widget.UpperTransformationMethod;
import com.eucleia.tabscanap.bean.model.DiagModel;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.ResUtils;
import com.eucleia.tabscanap.util.VINUtils;

import java.util.List;
import java.util.Locale;


/**
 * 输入VIN码
 */
public class InputVinActivity extends BlueBaseActivity {

    private ActInputVinBinding binding;
    private LayoutHeadBaseBinding headBase;

    //确认验证
    private final SingleClickListener commitClickListener = view -> {
        CharSequence vin = binding.inputVin.getText();
        if (TextUtils.isEmpty(vin)) {
            EToastUtils.get().showShort(R.string.not_vin_hint1,false);
            return;
        }
        if (vin.length() < 17) {
            EToastUtils.get().showShort(R.string.not_vin_hint2,false);
            return;
        }
        if (!VINUtils.calcVin(vin.toString())) {
            EToastUtils.get().showShort(R.string.unuse_vin,false);
            return;
        }
        EDiagUtils.get().getModel().vin = vin.toString().toUpperCase(Locale.ROOT);
        List<ModelYear> modelYears = PDIDatabase.get().getCheckPlanDao().loadModelYear();
        if (modelYears != null && modelYears.size() > 0) {
            ModelYear modelYear = modelYears.get(0);
            EDiagUtils.get().getModel().year = String.valueOf(modelYear.year);
            EDiagUtils.get().getModel().model = modelYear.model;
        }
        DiagModel model = EDiagUtils.get().getModel();
        model.entranceMode = CDispConstant.Enum_Entrance_Mode.ENTER_MODE_PDICHECK_ENTERVIN;
        startActivity(CarInfoActivity.class, true);
    };

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActInputVinBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }


    @Override
    protected void initView() {
        base.setBackText(ResUtils.getString(R.string.home_page));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(ResUtils.getString(R.string.input_vin));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);

        if (BuildConfig.DEBUG) {
            binding.inputVin.setText("LZWADAGA0NB188881");
        }
        binding.inputVin.setTransformationMethod(new UpperTransformationMethod());
        binding.setListener(commitClickListener);
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
    }

    @Override
    protected void OnBackClick() {
        finish();
    }
}
