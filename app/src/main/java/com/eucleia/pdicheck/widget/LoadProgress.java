package com.eucleia.pdicheck.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.eucleia.pdicheck.R;


/**
 * 车辆进度
 */
public class LoadProgress extends AppCompatTextView {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ScheduledExecutorService executorService;
    private Bitmap mBitmapBG;
    private Bitmap mBitmapSRC;

    private volatile int progress = 0;
    private int lastPro = 0;

    private int w;
    private int h;

    private Rect mRectBG;
    private Rect mRectSrc = new Rect();

    private String text = "0%";
    ;
    private Paint textPaint;
    private final static long initialDelay = 0;
    private final static long period = 10;


    public LoadProgress(Context context) {
        super(context);
        init();
    }


    public LoadProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = (int) (Resources.getSystem().getDisplayMetrics().densityDpi * 1.5);
        options.inScaled = true;

        mBitmapBG = BitmapFactory.decodeResource(getResources(), R.drawable.loading_base_bg, options);
        mBitmapSRC = BitmapFactory.decodeResource(getResources(), R.drawable.loading_base_src, options);
        w = mBitmapBG.getWidth();
        h = mBitmapBG.getHeight();
        mRectBG = new Rect(0, 0, w, h);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            if (progress > lastPro) {
                lastPro++;
                int drawW = w * lastPro / 100;
                mRectSrc.set(0, 0, drawW, h);
                text = lastPro + "%";
                invalidate();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCar(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(w, (int) (h + getTextSize() + 2));
    }

    /**
     * 画车
     *
     * @param canvas
     */
    private void drawCar(Canvas canvas) {
        canvas.drawBitmap(mBitmapBG, mRectBG, mRectBG, paint);
        canvas.drawBitmap(mBitmapSRC, mRectSrc, mRectSrc, paint);
        int left = 0;
        int textWidth = getTextWidth();
        if (mRectSrc.right > textWidth) {
            left = mRectSrc.right - textWidth;
        }
        canvas.drawText(text, left, mRectBG.bottom + getTextSize(), textPaint);
    }

    private int getTextWidth() {
        if (TextUtils.isEmpty(text)) return 0;
        String tempText = text;
        int width = 0;
        float[] widths = new float[tempText.length()];
        textPaint.getTextWidths(tempText, widths);
        for (int i = 0; i < tempText.length(); i++) {
            width += (int) Math.ceil(widths[i]);
        }
        return width;
    }

    /**
     * 设置进度条
     *
     * @param progress
     */
    public void setProgress(final int progress) {
        if (lastPro > this.progress) {
            lastPro = this.progress;
        } else {
            lastPro = this.progress;
        }
        this.progress = progress;
        if (progress == 0) {
            mRectSrc.set(0, 0, 0, h);
            text = lastPro + "%";
        }
        invalidate();
    }

    public void release() {
        if (mBitmapBG != null) {
            mBitmapBG.recycle();
        }
        if (mBitmapSRC != null) {
            mBitmapSRC.recycle();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
