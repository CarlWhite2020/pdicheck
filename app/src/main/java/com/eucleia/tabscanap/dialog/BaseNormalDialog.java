package com.eucleia.tabscanap.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.blankj.utilcode.util.ScreenUtils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.tabscanap.util.ResUtils;

public class BaseNormalDialog extends AppCompatDialog {

    protected int gravity = Gravity.CENTER;

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        setAttributes();
    }

    public BaseNormalDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        setOwnerActivity((Activity) context);
    }



    @Override
    protected void onStart() {
        super.onStart();
        // for landscape mode
        setAttributes();
    }

    protected void setAttributes() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = gravity;
        if (Constant.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = ResUtils.getDimen(R.dimen.dp_400);
        } else {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        layoutParams.height = ScreenUtils.getScreenHeight() / 5 * 4;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setAttributes();
    }
}
