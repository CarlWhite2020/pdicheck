package com.eucleia.pdicheck.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;


public class CheckPlanScrollView extends NestedScrollView {

    private boolean isScrollBottom;
    private int height;

    public CheckPlanScrollView(@NonNull Context context) {
        super(context);
    }

    public CheckPlanScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            height = getChildAt(0).getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isScrollBottom) {
            isScrollBottom = false;
            scrollTo(0, height);
        }
    }


    public boolean isScrollBottom() {
        return isScrollBottom;
    }

    public void setScrollBottom(boolean scrollBottom) {
        isScrollBottom = scrollBottom;
    }
}
