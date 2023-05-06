package com.eucleia.pdicheck.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.DialogAppUpdateBinding;
import com.eucleia.pdicheck.listener.DownloadListener;
import com.eucleia.pdicheck.net.model.AppVersion;
import com.eucleia.tabscanap.util.ResUtils;


public class AppUpdateDialog extends Dialog {

    private DialogAppUpdateBinding binding;
    private AppVersion version;
    private DownloadListener listener;

    public AppUpdateDialog(@NonNull Context context, AppVersion version, DownloadListener listener) {
        super(context, R.style.DialogStyle);
        this.version = version;
        this.listener = listener;
        createDialog();
    }


    /**
     * 构造对话框
     */
    private void createDialog() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_app_update, null, false);
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(ResUtils.getColor(R.color.transparent)));
        binding.upVersion.setText(ResUtils.getString(R.string.new_version) + version.getAppVersion());
        binding.upMsg.setText(version.getNote());
        binding.upBtn.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.download(version.getDownFileName(),version.getFileName());
            }
        });
        binding.close.setOnClickListener(v -> dismiss());

        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backClick();
    }

    public void backClick() {

    }
}
