package com.eucleia.tabscanap.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.ResUtils;

public class BaseDialog extends Dialog {

private boolean isDiagnostic;
    private TextView titleTv, contentTv, mCancel, mDefine, mSingle, contentEd;
    private LinearLayoutCompat mBotRl1, mBotRl2, edLay;
    private View view;

    private Context mContext;
    private OnLeftClickListener mOnLeftClickListener;
    private OnRightClickListener mOnRightClickListener;
    private OnSingleClickListener mOnSingleClickListener;
    private OnRightEditClickListener onRightEditClickListener;


    public BaseDialog(Context context) {
        super(context, R.style.DialogStyle);
        setOwnerActivity((Activity) context);
        mContext = context;
        initView();
        createDialog();
    }

    private void initView() {
        view = View.inflate(mContext, R.layout.dialog_base, null);
        titleTv = view.findViewById(R.id.dialog_title);
        contentTv = view.findViewById(R.id.dialog_content);
        contentEd = view.findViewById(R.id.dialog_input);
        mCancel = view.findViewById(R.id.btn_cancel);
        mDefine = view.findViewById(R.id.btn_ok);
        mBotRl1 = view.findViewById(R.id.btn_layout2);
        mBotRl2 = view.findViewById(R.id.btn_layout1);
        mSingle = view.findViewById(R.id.btn_single);
        edLay = view.findViewById(R.id.input_lay);
    }


    /**
     * 构造对话框
     */
    private void createDialog() {
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        getWindow().setWindowAnimations(0);

        mBotRl1.setVisibility(View.GONE);
        mBotRl2.setVisibility(View.GONE);

        mCancel.setOnClickListener(v -> {
            if (mOnLeftClickListener != null) {
                mOnLeftClickListener.onLeftClick();
            }
            dismiss();
        });

        mDefine.setOnClickListener(v -> {
            if (mOnRightClickListener != null) {
                mOnRightClickListener.onRightClick();
            }
            if (onRightEditClickListener != null && edLay.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(contentEd.getText())) {
                    setEdtAnimation();
                    ELogUtils.v(R.string.input_null);
                    return;
                }
                onRightEditClickListener.onRightEditClick(contentEd.getText().toString());
            }
            dismiss();
        });

        mSingle.setOnClickListener(v -> {
            if (mOnSingleClickListener != null) {
                mOnSingleClickListener.onSingleClick();
            }
            dismiss();
        });

    }

    public BaseDialog setTitles(CharSequence msg) {
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(msg);
//        contentTv.setBackgroundColor(ResUtils.getColor(R.color.text_main));
        return this;
    }

    public BaseDialog hideTitle() {
        titleTv.setVisibility(View.GONE);
//        contentTv.setBackgroundResource(R.drawable.bg_dialog_a1_base_top_content);
        return this;
    }

    public void resetMsg() {
        if (titleTv.getVisibility() == View.GONE) {
//            contentTv.setBackgroundResource(R.drawable.bg_dialog_a1_base_content);
        } else {
//            contentTv.setBackgroundResource(R.drawable.bg_dialog_a1_base_bottom_content);
        }
        mBotRl1.setVisibility(View.GONE);
        mBotRl2.setVisibility(View.GONE);
    }

    public BaseDialog setMsg(CharSequence msg) {
        contentTv.setVisibility(View.VISIBLE);
        edLay.setVisibility(View.GONE);
        contentTv.setText(msg);
        contentTv.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
        return this;
    }

    public void setTipDrawable(int drawableRes) {
        contentTv.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0);
    }

    public void hideTipDrawable() {
        contentTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void setMsgGravity(int gravity) {
        contentTv.setGravity(gravity);
    }

    public void requesetMsg() {
        contentTv.requestLayout();
        contentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        contentTv.setScrollY(0);
    }

    public BaseDialog setEdit(CharSequence msg, OnRightEditClickListener listener) {
        edLay.setVisibility(View.VISIBLE);
        contentTv.setVisibility(View.GONE);
        contentEd.setText("");
        contentEd.setHint(msg);
        mBotRl1.setVisibility(View.VISIBLE);
        mDefine.setTextColor(ResUtils.getColor(R.color.main));
        mCancel.setTextColor(ResUtils.getColor(R.color.text_hint));
        onRightEditClickListener = listener;
        return this;
    }

    public BaseDialog setLeftButton(CharSequence msg, OnLeftClickListener listener) {
        return setLeftButton(msg, ResUtils.getColor(R.color.text_hint), listener);
    }

    public BaseDialog setLeftButton(CharSequence msg, int colorInt, OnLeftClickListener listener) {
        mBotRl1.setVisibility(View.VISIBLE);
        mBotRl2.setVisibility(View.GONE);
        mCancel.setText(msg);
        mCancel.setTextColor(colorInt);
        setOnLeftClickListener(listener);
        return this;
    }

    public BaseDialog setRightButton(CharSequence msg, OnRightClickListener listener) {
        return setRightButton(msg, ResUtils.getColor(R.color.main), listener);
    }

    public BaseDialog setRightButton(CharSequence msg, int colorInt, OnRightClickListener listener) {
        mBotRl1.setVisibility(View.VISIBLE);
        mBotRl2.setVisibility(View.GONE);
        mDefine.setText(msg);
        mDefine.setTextColor(colorInt);
        setOnRightClickListener(listener);
        return this;
    }

    public BaseDialog setSingleButton(CharSequence msg, OnSingleClickListener listener) {
        return setSingleButton(msg, ResUtils.getColor(R.color.main), listener);
    }

    public BaseDialog setSingleButton(CharSequence msg, int colorInt, OnSingleClickListener listener) {
        mSingle.setText(msg);
        mSingle.setTextColor(colorInt);
        mBotRl1.setVisibility(View.GONE);
        mBotRl2.setVisibility(View.VISIBLE);
        setOnSingleClickListener(listener);
        return this;
    }

    public void setEdtAnimation() {
        //没有输入文字的时候执行颤抖动画
        TranslateAnimation animation = new TranslateAnimation(-10, 10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(10);
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);
        contentEd.startAnimation(animation);
    }


    @Override
    public void show() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        super.show();
    }

    public interface OnLeftClickListener {
        void onLeftClick();
    }

    private void setOnLeftClickListener(OnLeftClickListener listen) {
        mOnLeftClickListener = listen;
    }

    public interface OnRightClickListener {
        void onRightClick();
    }

    public interface OnRightEditClickListener {
        void onRightEditClick(String edit);
    }

    private void setOnRightClickListener(OnRightClickListener listen) {
        mOnRightClickListener = listen;
    }

    public interface OnSingleClickListener {
        void onSingleClick();
    }

    private void setOnSingleClickListener(OnSingleClickListener listen) {
        mOnSingleClickListener = listen;
    }

}
