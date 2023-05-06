package com.eucleia.pdicheck.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ScreenUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.pdicheck.databinding.DialogReportClearBinding;
import com.eucleia.tabscanap.dialog.BaseNormalDialog;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 报告清除弹窗
 */
public class ReportClearDialog extends BaseNormalDialog {

    private DialogReportClearBinding binding;
    private ReportClearListener listener;

    public ReportClearDialog(@NonNull Context context,ReportClearListener listener) {
        super(context);
        this.listener = listener;
        createDialog();
    }

    protected void setAttributes() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = gravity;
        if (Constant.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = ResUtils.getDimen(R.dimen.dp_260);
            layoutParams.height = ResUtils.getDimen(R.dimen.dp_300);
        } else {
            layoutParams.width = ResUtils.getDimen(R.dimen.dp_200);
            layoutParams.height = ScreenUtils.getScreenHeight() / 5 * 3;
        }
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }



    /**
     * 构造对话框
     */
    private void createDialog() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_report_clear, null, false);
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(ResUtils.getColor(R.color.transparent)));
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        binding.cancelButton.setOnClickListener(v -> dismiss());

        binding.clearAll.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.clearType(0);
            }
        });
        binding.keepMonth.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.clearType(1);
            }
        });
        binding.keepWeek.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.clearType(2);
            }
        });
        binding.keepDay.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.clearType(3);
            }
        });
    }

    public interface ReportClearListener{
        //type 0:all 1:month 2:week 3:day
        void clearType(int type);
    }
}
