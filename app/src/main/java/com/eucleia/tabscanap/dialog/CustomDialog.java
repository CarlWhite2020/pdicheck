package com.eucleia.tabscanap.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.DialogAppUpdateBinding;
import com.eucleia.pdicheck.databinding.LoadDialogBinding;


public class CustomDialog extends Dialog {

    private LoadDialogBinding binding;
    private String msg;

    public CustomDialog(Context context) {
        super(context, R.style.DialogStyle);
        init();
    }


    private void init() {

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.load_dialog, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        updateLoding();
        super.show();
    }

    private void updateLoding() {
        binding.tvLoadDialog.setText(msg);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
