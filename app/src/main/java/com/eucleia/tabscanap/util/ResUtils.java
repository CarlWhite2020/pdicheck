package com.eucleia.tabscanap.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.Utils;

public class ResUtils {

    public static String getString(@StringRes int resId){
        return Utils.getApp().getResources().getString(resId);
    }

    public static int getColor(@ColorRes int resId){
        return Utils.getApp().getResources().getColor(resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId){
        return Utils.getApp().getResources().getDrawable(resId);
    }

    public static int getDimen(@DimenRes int resId){
        return Utils.getApp().getResources().getDimensionPixelSize(resId);
    }

}
