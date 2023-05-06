package com.eucleia.tabscanap.util;

import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ToastUtils;
import com.eucleia.pdicheck.R;

public class EToastUtils {

    private final ToastUtils toast;

    public static EToastUtils get() {
        return InClass.E;
    }

    private EToastUtils() {
        toast = ToastUtils.make();
        toast.setBgResource(R.drawable.block_white8);
        toast.setTextSize(ResUtils.getDimen(R.dimen.sp_10));
        toast.setTextColor(ResUtils.getColor(R.color.text_main));
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    private static class InClass {
        private static final EToastUtils E = new EToastUtils();
    }


    /**
     * @param text The text.
     */
    public void showShort(@Nullable final CharSequence text) {
        removeDrawable();
        toast.show(text);
    }

    /**
     * @param resId The resource id for text.
     */
    public void showShort(@StringRes final int resId) {
        removeDrawable();
        toast.show(resId);
    }

    /**
     * @param text The text.
     */
    public void showShort(@Nullable final CharSequence text, boolean isSuccess) {
        setLeftDrawable(isSuccess);
        toast.show(text);
    }

    /**
     * @param resId The resource id for text.
     */
    public void showShort(@StringRes final int resId, boolean isSuccess) {
        setLeftDrawable(isSuccess);
        toast.show(ResUtils.getString(resId), Toast.LENGTH_SHORT);
    }

    public void setLeftDrawable(boolean isSuccess) {
        if (isSuccess) {
            toast.setLeftIcon(R.drawable.ic_success);
        } else {
            toast.setLeftIcon(R.drawable.ic_fail);
        }
    }

    public void removeDrawable() {
        toast.setLeftIcon(null);
    }
}
