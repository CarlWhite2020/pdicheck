package com.eucleia.pdicheck.dialog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.adapter.BlueDialogAdapter;
import com.eucleia.pdicheck.databinding.DialogBlueBinding;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.tabscanap.dialog.BaseNormalDialog;
import com.eucleia.tabscanap.listener.BluetoothActiveListener;
import com.eucleia.tabscanap.bean.model.BlueModel;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.ResUtils;

public class BlueDialog extends BaseNormalDialog implements BluetoothActiveListener {

    private DialogBlueBinding binding;

    private BlueDialogAdapter adapter;
    private Context context;

    protected void setAttributes() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = gravity;
        if (Constant.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = ResUtils.getDimen(R.dimen.dp_260);
            layoutParams.height = ResUtils.getDimen(R.dimen.dp_300);
        } else {
            layoutParams.width = ScreenUtils.getScreenWidth() / 5 * 4;
            layoutParams.height = ScreenUtils.getScreenHeight() / 5 * 3;
        }
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }


    public BlueDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_blue, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        binding.close.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void listAdapter(BlueModel blueModel) {
        if (adapter == null || blueModel == null) {
            adapter = new BlueDialogAdapter();
            binding.btList.setLayoutManager(new LinearLayoutManager(context));
            binding.btList.setAdapter(adapter);
        } else {
            adapter.update(blueModel);
        }
    }

    @Override
    public void show() {
        super.show();
        BTUtils.get().addBluetoothActiveListener(this);
        listAdapter(null);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        BTConstant.isShowList = false;
        BTUtils.get().removeBluetoothActiveListener(this);
    }

    @Override
    public void btActive(String action, BlueModel blueModel) {
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            listAdapter(blueModel);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            binding.searchHint.setVisibility(View.VISIBLE);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            binding.searchHint.setVisibility(View.GONE);
        }
    }
}
