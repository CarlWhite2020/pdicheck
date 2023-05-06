package com.eucleia.pdicheck.activity.normal;

import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BlueBaseActivity;
import com.eucleia.pdicheck.adapter.ChooseModelSpinnerAdapter;
import com.eucleia.pdicheck.bean.normal.ModelYear;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.databinding.ActChooseCarBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadBaseBinding;
import com.eucleia.pdicheck.listener.ChooseSpinnerListener;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.model.DiagModel;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 手动选择车型
 */
public class ChooseCarActivity extends BlueBaseActivity {

    private ActChooseCarBinding binding;
    private LayoutHeadBaseBinding headBase;

    private final LinkedHashMap<String, ArrayList<Integer>> map = new LinkedHashMap<>();
    private ArrayList<String> models;
    private ArrayList<Integer> years;
    private ChooseModelSpinnerAdapter modelAdapter;
    private ChooseModelSpinnerAdapter yearAdapter;

    private final ChooseSpinnerListener chooseSpinnerListener = new ChooseSpinnerListener() {
        @Override
        public void modelSelected(AdapterView<?> parent, View view, int position, long id) {
            years = map.get(models.get(position));
            yearAdapter = new ChooseModelSpinnerAdapter(years);
            binding.spinnerYear.setAdapter(yearAdapter);
        }

        @Override
        public void yearSelected(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private final SingleClickListener commitListener = view -> {
        DiagModel model = EDiagUtils.get().getModel();
        model.year = String.valueOf(binding.spinnerYear.getSelectedItem());
        model.model = String.valueOf(binding.spinnerModel.getSelectedItem());
        model.entranceMode = CDispConstant.Enum_Entrance_Mode.ENTER_MODE_PDICHECK_MANUAL;
        startActivity(CarInfoActivity.class, true);
    };


    @Override
    protected ViewDataBinding bindAct() {
        binding = ActChooseCarBinding.inflate(getLayoutInflater());
        headBase = binding.headBase;
        return binding;
    }


    @Override
    protected void initView() {

        base.setBackText(ResUtils.getString(R.string.home_page));
        base.setVciStatus(AppVM.get().getVciStatus());
        base.setTitle(ResUtils.getString(R.string.choose_car));
        base.setBackListener(headFunListener);
        base.setRightListener(rightFunListener);
        headBase.setHead(base);

        binding.setItemListener(chooseSpinnerListener);
        binding.setListener(commitListener);

        PDIDatabase pdiDB = PDIDatabase.get();
        List<ModelYear> modelYears = pdiDB.getCheckPlanDao().loadModelYear();
        for (ModelYear my : modelYears) {
            ArrayList<Integer> integers = map.get(my.model);
            if (integers == null) {
                integers = new ArrayList<>();
            }
            integers.add(my.year);
            map.put(my.model, integers);
        }
        models = new ArrayList<>(map.keySet());
        modelAdapter = new ChooseModelSpinnerAdapter(models);
        binding.spinnerModel.setAdapter(modelAdapter);
    }

    @Override
    protected void OnBackClick() {
        finish();
    }

    @Override
    protected void vciChange(VciStatus vciStatus) {
        base.setVciStatus(vciStatus);
        headBase.setHead(base);
    }
}

