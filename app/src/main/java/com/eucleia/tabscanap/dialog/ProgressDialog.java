package com.eucleia.tabscanap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.widget.LoadProgress;
import com.eucleia.tabscanap.util.ResUtils;


public class ProgressDialog extends Dialog {

    private TextView dialogTv;
    private LoadProgress progressBar;

    private boolean isShowProgress;
    private String content;
    private int progress;


    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        createDialog();
    }



    /**
     * 构造对话框
     */
    private void createDialog() {
        View view = View.inflate(getContext(), R.layout.dialog_progress, null);
        progressBar = view.findViewById(R.id.load_base);
        dialogTv = view.findViewById(R.id.dialog_tv);
        setContentView(view);//800D111C
        setCancelable(false);
        getWindow().setWindowAnimations(0);
        getWindow().setBackgroundDrawable(new ColorDrawable(ResUtils.getColor(R.color.transparent)));
        updateDialog();
    }

    public void updateDialog() {
        // 设置进度条总进度、当前进度
        progressBar.setProgress(progress);
        if (TextUtils.isEmpty(content)) {
            dialogTv.setVisibility(View.GONE);
        } else {
            dialogTv.setVisibility(View.VISIBLE);
            dialogTv.setText(content);
        }

    }


    public boolean isShowProgress() {
        return isShowProgress;
    }

    public void setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        updateDialog();
    }

    public void release() {
        if (progressBar!=null){
            progressBar.release();
        }
    }
}
