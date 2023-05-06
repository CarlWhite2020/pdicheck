package com.eucleia.pdicheck.widget.wheelpicker;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.eucleia.pdicheck.R;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮选择器
 * <p>
 * WheelPicker
 *
 * @author AigeStudio 2015-12-12
 * @author AigeStudio 2016-06-17
 * 更新项目结构
 * <p>
 * New project structure
 * @version 1.1.0
 */
public class WheelPicker extends View implements IWheelPicker, Runnable {
    /**
     * 滚动状态标识值
     *
     * @see OnWheelChangeListener#onWheelScrollStateChanged(int)
     */
    public static final int SCROLL_STATE_IDLE = 0, SCROLL_STATE_DRAGGING = 1,
            SCROLL_STATE_SCROLLING = 2;

    /**
     * 数据项对齐方式标识值
     *
     * @see #setItemAlign(int)
     */
    public static final int ALIGN_CENTER = 0, ALIGN_LEFT = 1, ALIGN_RIGHT = 2;

    protected static final String TAG = WheelPicker.class.getSimpleName();

    protected final Handler mHandler = new Handler();

    protected Paint mPaint;
    protected Scroller mScroller;
    protected VelocityTracker mTracker;

    /**
     * 相关监听器
     *
     * @see OnWheelChangeListener,OnItemSelectedListener
     */
    protected OnItemSelectedListener mOnItemSelectedListener;
    protected OnWheelChangeListener mOnWheelChangeListener;

    protected Rect mRectDrawn;
    protected Rect mRectIndicatorHead, mRectIndicatorFoot;
    protected Rect mRectCurrentItem;

    protected Camera mCamera;
    protected Matrix mMatrixRotate, mMatrixDepth;

    /**
     * 数据源
     */
    protected List mData = new ArrayList();

    /**
     * 最宽的文本
     *
     * @see #setMaximumWidthText(String)
     */
    protected String mMaxWidthText;

    /**
     * 滚轮选择器中可见的数据项数量和滚轮选择器将会绘制的数据项数量
     *
     * @see #setVisibleItemCount(int)
     */
    protected int mVisibleItemCount = 7, mDrawnItemCount;

    /**
     * 滚轮选择器将会绘制的Item数量的一半
     */
    protected int mHalfDrawnItemCount;

    /**
     * 单个文本最大宽高
     */
    protected int mTextMaxWidth, mTextMaxHeight;

    /**
     * 数据项文本颜色以及被选中的数据项文本颜色
     *
     * @see #setItemTextColor(int)
     * @see #setSelectedItemTextColor(int)
     */
    protected int mItemTextColor = ResUtils.getColor(R.color.text_hint),
            mSelectedItemTextColor = ResUtils.getColor(R.color.text_main);

    /**
     * 数据项文本尺寸
     *
     * @see #setItemTextSize(int)
     */
    protected int mItemTextSize;

    /**
     * 指示器尺寸
     *
     * @see #setIndicatorSize(int)
     */
    protected int mIndicatorSize;

    /**
     * 指示器颜色
     *
     * @see #setIndicatorColor(int)
     */
    protected int mIndicatorColor = 0xFFEE3333;

    /**
     * 幕布颜色
     *
     * @see #setCurtainColor(int)
     */
    protected int mCurtainColor = ResUtils.getColor(R.color.main90);

    /**
     * 数据项之间间距
     *
     * @see #setItemSpace(int)
     */
    protected int mItemSpace;

    /**
     * 数据项对齐方式
     *
     * @see #setItemAlign(int)
     */
    protected int mItemAlign = ALIGN_CENTER;

    /**
     * 滚轮选择器单个数据项高度以及单个数据项一半的高度
     */
    protected int mItemHeight, mHalfItemHeight;

    /**
     * 滚轮选择器内容区域高度的一半
     */
    protected int mHalfWheelHeight;

    /**
     * 当前被选中的数据项所显示的数据在数据源中的位置
     *
     * @see #setSelectedItemPosition(int)
     */
    protected int mSelectedItemPosition = 0;

    /**
     * 当前被选中的数据项所显示的数据在数据源中的位置
     *
     * @see #getCurrentItemPosition()
     */
    protected int mCurrentItemPosition;

    /**
     * 滚轮滑动时可以滑动到的最小/最大的Y坐标
     */
    protected int mMinFlingY, mMaxFlingY;

    /**
     * 滚轮滑动时的最小/最大速度
     */
    protected int mMinimumVelocity = 50, mMaximumVelocity = 8000;

    /**
     * 滚轮选择器中心坐标
     */
    protected int mWheelCenterX, mWheelCenterY;

    /**
     * 滚轮选择器绘制中心坐标
     */
    protected int mDrawnCenterX, mDrawnCenterY;

    /**
     * 滚轮选择器视图区域在Y轴方向上的偏移值
     */
    protected int mScrollOffsetY;

    /**
     * 滚轮选择器中最宽或最高的文本在数据源中的位置
     */
    protected int mTextMaxWidthPosition = -1;

    /**
     * 用户手指上一次触摸事件发生时事件Y坐标
     */
    protected int mLastPointY;

    /**
     * 手指触摸屏幕时事件点的Y坐标
     */
    protected int mDownPointY;

    /**
     * 点击与触摸的切换阀值
     */
    protected int mTouchSlop = 8;

    /**
     * 滚轮选择器的每一个数据项文本是否拥有相同的宽度
     *
     * @see #setSameWidth(boolean)
     */
    protected boolean hasSameWidth = false;

    /**
     * 是否显示指示器
     *
     * @see #setIndicator(boolean)
     */
    protected boolean hasIndicator = false;

    /**
     * 是否显示幕布
     *
     * @see #setCurtain(boolean)
     */
    protected boolean hasCurtain = false;

    /**
     * 是否显示空气感效果
     *
     * @see #setAtmospheric(boolean)
     */
    protected boolean hasAtmospheric = false;

    /**
     * 数据是否循环展示
     *
     * @see #setCyclic(boolean)
     */
    protected boolean isCyclic = false;

    /**
     * 滚轮是否为卷曲效果
     *
     * @see #setCurved(boolean)
     */
    protected boolean isCurved = false;

    /**
     * 是否为点击模式
     */
    protected boolean isClick;

    /**
     * 是否为强制结束滑动
     */
    protected boolean isForceFinishScroll;


    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        mItemTextSize = ResUtils.getDimen(R.dimen.sp_12);
        mItemSpace = ResUtils.getDimen(R.dimen.dp_12);
        mIndicatorSize = ResUtils.getDimen(R.dimen.dp_2);

        // 可见数据项改变后更新与之相关的参数
        updateVisibleItemCount();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setTextSize(mItemTextSize);

        // 更新文本对齐方式
        // Update alignment of text
        updateItemTextAlign();

        // 计算文本尺寸
        // Correct sizes of text
        computeTextSize();

        mScroller = new Scroller(getContext());

        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mMinimumVelocity = conf.getScaledMinimumFlingVelocity();
        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        mTouchSlop = conf.getScaledTouchSlop();
        mRectDrawn = new Rect();

        mRectIndicatorHead = new Rect();
        mRectIndicatorFoot = new Rect();

        mRectCurrentItem = new Rect();

        mCamera = new Camera();

        mMatrixRotate = new Matrix();
        mMatrixDepth = new Matrix();
    }

    private void updateVisibleItemCount() {
        if (mVisibleItemCount < 2)
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");

        // 确保滚轮选择器可见数据项数量为奇数
        // Be sure count of visible item is odd number
        if (mVisibleItemCount % 2 == 0)
            mVisibleItemCount += 1;
        mDrawnItemCount = mVisibleItemCount + 2;
        mHalfDrawnItemCount = mDrawnItemCount / 2;
    }

    private void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (hasSameWidth) {
            mTextMaxWidth = (int) mPaint.measureText(String.valueOf(mData.get(0)));
        } else if (isPosInRang(mTextMaxWidthPosition)) {
            mTextMaxWidth = (int) mPaint.measureText
                    (String.valueOf(mData.get(mTextMaxWidthPosition)));
        } else if (!TextUtils.isEmpty(mMaxWidthText)) {
            mTextMaxWidth = (int) mPaint.measureText(mMaxWidthText);
        } else {
            for (Object obj : mData) {
                String text = String.valueOf(obj);
                int width = (int) mPaint.measureText(text);
                mTextMaxWidth = Math.max(mTextMaxWidth, width);
            }
        }
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void updateItemTextAlign() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                mPaint.setTextAlign(Paint.Align.LEFT);
                break;
            case ALIGN_RIGHT:
                mPaint.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                mPaint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算原始内容尺寸
        // Correct sizes of original content
        int resultWidth = mTextMaxWidth;
        int resultHeight = mTextMaxHeight * mVisibleItemCount + mItemSpace * (mVisibleItemCount - 1);

        // 如果开启弯曲效果则需要重新计算弯曲后的尺寸
        // Correct view sizes again if curved is enable
        if (isCurved) {
            resultHeight = (int) (2 * resultHeight / Math.PI);
        }

        // 考虑内边距对尺寸的影响
        // Consideration padding influence the view sizes
        resultWidth += getPaddingLeft() + getPaddingRight();
        resultHeight += getPaddingTop() + getPaddingBottom();

        // 考虑父容器对尺寸的影响
        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth);
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight);

        setMeasuredDimension(resultWidth, resultHeight);
    }

    private int measureSize(int mode, int sizeExpect, int sizeActual) {
        int realSize;
        if (mode == MeasureSpec.EXACTLY) {
            realSize = sizeExpect;
        } else {
            realSize = sizeActual;
            if (mode == MeasureSpec.AT_MOST)
                realSize = Math.min(realSize, sizeExpect);
        }
        return realSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        // 设置内容区域
        // Set content region
        mRectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        // 获取内容区域中心坐标
        // Get the center coordinates of content region
        mWheelCenterX = mRectDrawn.centerX();
        mWheelCenterY = mRectDrawn.centerY();

        // 计算数据项绘制中心
        // Correct item drawn center
        computeDrawnCenter();

        mHalfWheelHeight = mRectDrawn.height() / 2;

        mItemHeight = mRectDrawn.height() / mVisibleItemCount;
        mHalfItemHeight = mItemHeight / 2;

        // 初始化滑动最大坐标
        // Initialize fling max Y-coordinates
        computeFlingLimitY();

        // 计算指示器绘制区域
        // Correct region of indicator
        computeIndicatorRect();

        // 计算当前选中的数据项区域
        // Correct region of current select item
        computeCurrentItemRect();
    }

    private void computeDrawnCenter() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                mDrawnCenterX = mRectDrawn.left;
                break;
            case ALIGN_RIGHT:
                mDrawnCenterX = mRectDrawn.right;
                break;
            default:
                mDrawnCenterX = mWheelCenterX;
                break;
        }
        mDrawnCenterY = (int) (mWheelCenterY - ((mPaint.ascent() + mPaint.descent()) / 2));
    }

    private void computeFlingLimitY() {
        int currentItemOffset = mSelectedItemPosition * mItemHeight;
        mMinFlingY = isCyclic ? Integer.MIN_VALUE :
                -mItemHeight * (mData.size() - 1) + currentItemOffset;
        mMaxFlingY = isCyclic ? Integer.MAX_VALUE : currentItemOffset;
    }

    private void computeIndicatorRect() {
        if (!hasIndicator) return;
        int halfIndicatorSize = mIndicatorSize / 2;
        int indicatorHeadCenterY = mWheelCenterY + mHalfItemHeight;
        int indicatorFootCenterY = mWheelCenterY - mHalfItemHeight;
        mRectIndicatorHead.set(mRectDrawn.left, indicatorHeadCenterY - halfIndicatorSize,
                mRectDrawn.right, indicatorHeadCenterY + halfIndicatorSize);
        mRectIndicatorFoot.set(mRectDrawn.left, indicatorFootCenterY - halfIndicatorSize,
                mRectDrawn.right, indicatorFootCenterY + halfIndicatorSize);
    }

    protected void computeCurrentItemRect() {
        if (!hasCurtain && mSelectedItemTextColor == -1) return;
        mRectCurrentItem.set(mRectDrawn.left, mWheelCenterY - mHalfItemHeight - mItemSpace / 2, mRectDrawn.right,
                mWheelCenterY + mHalfItemHeight + mItemSpace / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mOnWheelChangeListener)
            mOnWheelChangeListener.onWheelScrolled(mScrollOffsetY);
        int drawnDataStartPos = -mScrollOffsetY / mItemHeight - mHalfDrawnItemCount;


        // 是否需要绘制幕布
        // Need to draw curtain or not
        if (hasCurtain) {
            mPaint.setColor(mCurtainColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mRectCurrentItem, mPaint);
        }

        for (int drawnDataPos = drawnDataStartPos + mSelectedItemPosition,
             drawnOffsetPos = -mHalfDrawnItemCount;
             drawnDataPos < drawnDataStartPos + mSelectedItemPosition + mDrawnItemCount;
             drawnDataPos++, drawnOffsetPos++) {
            String data = "";
            if (isCyclic) {
                int actualPos = drawnDataPos % mData.size();
                actualPos = actualPos < 0 ? (actualPos + mData.size()) : actualPos;
                data = String.valueOf(mData.get(actualPos));
            } else {
                if (isPosInRang(drawnDataPos))
                    data = String.valueOf(mData.get(drawnDataPos));
            }

            mPaint.setColor(mItemTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            int mDrawnItemCenterY = mDrawnCenterY + (drawnOffsetPos * mItemHeight) +
                    mScrollOffsetY % mItemHeight;

            int distanceToCenter = 0;
            if (isCurved) {
                // 计算数据项绘制中心距离滚轮中心的距离比率
                // Correct ratio of item's drawn center to wheel center
                float ratio = (mDrawnCenterY - Math.abs(mDrawnCenterY - mDrawnItemCenterY) -
                        mRectDrawn.top) * 1.0F / (mDrawnCenterY - mRectDrawn.top);

                // 计算单位
                // Correct unit
                int unit = 0;
                if (mDrawnItemCenterY > mDrawnCenterY)
                    unit = 1;
                else if (mDrawnItemCenterY < mDrawnCenterY)
                    unit = -1;

                float degree = (-(1 - ratio) * 90 * unit);
                if (degree < -90) degree = -90;
                if (degree > 90) degree = 90;
                distanceToCenter = computeSpace((int) degree);

                int transX = mWheelCenterX;
                switch (mItemAlign) {
                    case ALIGN_LEFT:
                        transX = mRectDrawn.left;
                        break;
                    case ALIGN_RIGHT:
                        transX = mRectDrawn.right;
                        break;
                }
                int transY = mWheelCenterY - distanceToCenter;

                mCamera.save();
                mCamera.rotateX(degree);
                mCamera.getMatrix(mMatrixRotate);
                mCamera.restore();
                mMatrixRotate.preTranslate(-transX, -transY);
                mMatrixRotate.postTranslate(transX, transY);

                mCamera.save();
                mCamera.translate(0, 0, computeDepth((int) degree));
                mCamera.getMatrix(mMatrixDepth);
                mCamera.restore();
                mMatrixDepth.preTranslate(-transX, -transY);
                mMatrixDepth.postTranslate(transX, transY);

                mMatrixRotate.postConcat(mMatrixDepth);
            }
            if (hasAtmospheric) {
                int alpha = (int) ((mDrawnCenterY - Math.abs(mDrawnCenterY - mDrawnItemCenterY)) *
                        1.0F / mDrawnCenterY * 255);
                alpha = alpha < 0 ? 0 : alpha;
                mPaint.setAlpha(alpha);
            }
            // 根据卷曲与否计算数据项绘制Y方向中心坐标
            // Correct item's drawn centerY base on curved state
            int drawnCenterY = isCurved ? mDrawnCenterY - distanceToCenter : mDrawnItemCenterY;

            // 判断是否需要为当前数据项绘制不同颜色
            // Judges need to draw different color for current item or not
            if (mSelectedItemTextColor != -1) {
                canvas.save();
                if (isCurved) canvas.concat(mMatrixRotate);
                canvas.clipRect(mRectCurrentItem, Region.Op.DIFFERENCE);
                canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
                canvas.restore();

                mPaint.setColor(mSelectedItemTextColor);
                canvas.save();
                if (isCurved) canvas.concat(mMatrixRotate);
                canvas.clipRect(mRectCurrentItem);
                canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(mRectDrawn);
                if (isCurved) canvas.concat(mMatrixRotate);
                canvas.drawText(data, mDrawnCenterX, drawnCenterY, mPaint);
                canvas.restore();
            }
        }


        // 是否需要绘制指示器
        // Need to draw indicator or not
        if (hasIndicator) {
            mPaint.setColor(mIndicatorColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mRectIndicatorHead, mPaint);
            canvas.drawRect(mRectIndicatorFoot, mPaint);
        }
    }

    private boolean isPosInRang(int position) {
        return position >= 0 && position < mData.size();
    }

    private int computeSpace(int degree) {
        return (int) (Math.sin(Math.toRadians(degree)) * mHalfWheelHeight);
    }

    private int computeDepth(int degree) {
        return (int) (mHalfWheelHeight - Math.cos(Math.toRadians(degree)) * mHalfWheelHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null != getParent())
                    getParent().requestDisallowInterceptTouchEvent(true);
                if (null == mTracker)
                    mTracker = VelocityTracker.obtain();
                else
                    mTracker.clear();
                mTracker.addMovement(event);
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    isForceFinishScroll = true;
                }
                mDownPointY = mLastPointY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownPointY - event.getY()) < mTouchSlop) {
                    isClick = true;
                    break;
                }
                isClick = false;
                mTracker.addMovement(event);
                if (null != mOnWheelChangeListener)
                    mOnWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_DRAGGING);

                // 滚动内容
                // Scroll WheelPicker's content
                float move = event.getY() - mLastPointY;
                if (Math.abs(move) < 1) break;
                mScrollOffsetY += move;
                mLastPointY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (null != getParent())
                    getParent().requestDisallowInterceptTouchEvent(false);
                if (isClick) break;
                mTracker.addMovement(event);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT)
                    mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                else
                    mTracker.computeCurrentVelocity(1000);

                // 根据速度判断是该滚动还是滑动
                // Judges the WheelPicker is scroll or fling base on current velocity
                isForceFinishScroll = false;
                int velocity = (int) mTracker.getYVelocity();
                if (Math.abs(velocity) > mMinimumVelocity) {
                    mScroller.fling(0, mScrollOffsetY, 0, velocity, 0, 0, mMinFlingY, mMaxFlingY);
                    mScroller.setFinalY(mScroller.getFinalY() +
                            computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
                } else {
                    mScroller.startScroll(0, mScrollOffsetY, 0,
                            computeDistanceToEndPoint(mScrollOffsetY % mItemHeight));
                }
                // 校正坐标
                // Correct coordinates
                if (!isCyclic)
                    if (mScroller.getFinalY() > mMaxFlingY)
                        mScroller.setFinalY(mMaxFlingY);
                    else if (mScroller.getFinalY() < mMinFlingY)
                        mScroller.setFinalY(mMinFlingY);
                mHandler.post(this);
                if (null != mTracker) {
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (null != getParent())
                    getParent().requestDisallowInterceptTouchEvent(false);
                if (null != mTracker) {
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
        }
        return true;
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mHalfItemHeight)
            if (mScrollOffsetY < 0)
                return -mItemHeight - remainder;
            else
                return mItemHeight - remainder;
        else
            return -remainder;
    }

    @Override
    public void run() {
        if (null == mData || mData.size() == 0) return;
        if (mScroller.isFinished() && !isForceFinishScroll) {
            if (mItemHeight == 0) return;
            int position = (-mScrollOffsetY / mItemHeight + mSelectedItemPosition) % mData.size();
            position = position < 0 ? position + mData.size() : position;
            mCurrentItemPosition = position;
            if (null != mOnItemSelectedListener)
                mOnItemSelectedListener.onItemSelected(this, mData.get(position), position);
            if (null != mOnWheelChangeListener) {
                mOnWheelChangeListener.onWheelSelected(position);
                mOnWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_IDLE);
            }
        }
        if (mScroller.computeScrollOffset()) {
            if (null != mOnWheelChangeListener)
                mOnWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_SCROLLING);
            mScrollOffsetY = mScroller.getCurrY();
            postInvalidate();
            mHandler.postDelayed(this, 16);
        }
    }


    @Override
    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    @Override
    public void setVisibleItemCount(int count) {
        mVisibleItemCount = count;
        updateVisibleItemCount();
        requestLayout();
    }

    @Override
    public boolean isCyclic() {
        return isCyclic;
    }

    @Override
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        computeFlingLimitY();
        invalidate();
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    @Override
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    @Override
    public void setSelectedItemPosition(int position) {
        position = Math.min(position, mData.size() - 1);
        position = Math.max(position, 0);
        mSelectedItemPosition = position;
        mCurrentItemPosition = position;
        mScrollOffsetY = 0;
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    @Override
    public int getCurrentItemPosition() {
        return mCurrentItemPosition;
    }

    @Override
    public List getData() {
        return mData;
    }

    @Override
    public void setData(List data) {
        if (null == data)
            throw new NullPointerException("WheelPicker's data can not be null!");
        mData = data;

        // 重置位置
        if (mSelectedItemPosition > data.size() - 1 || mCurrentItemPosition > data.size() - 1) {
            mSelectedItemPosition = mCurrentItemPosition = data.size() - 1;
        } else {
            mSelectedItemPosition = mCurrentItemPosition;
        }
        mScrollOffsetY = 0;
        computeTextSize();
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    public void setSameWidth(boolean hasSameWidth) {
        this.hasSameWidth = hasSameWidth;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override
    public boolean hasSameWidth() {
        return hasSameWidth;
    }

    @Override
    public void setOnWheelChangeListener(OnWheelChangeListener listener) {
        mOnWheelChangeListener = listener;
    }

    @Override
    public String getMaximumWidthText() {
        return mMaxWidthText;
    }

    @Override
    public void setMaximumWidthText(String text) {
        if (null == text)
            throw new NullPointerException("Maximum width text can not be null!");
        mMaxWidthText = text;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override
    public int getMaximumWidthTextPosition() {
        return mTextMaxWidthPosition;
    }

    @Override
    public void setMaximumWidthTextPosition(int position) {
        if (!isPosInRang(position))
            throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " +
                    mData.size() + "), but current is " + position);
        mTextMaxWidthPosition = position;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override
    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    @Override
    public void setSelectedItemTextColor(int color) {
        mSelectedItemTextColor = color;
        computeCurrentItemRect();
        invalidate();
    }

    @Override
    public int getItemTextColor() {
        return mItemTextColor;
    }

    @Override
    public void setItemTextColor(int color) {
        mItemTextColor = color;
        invalidate();
    }

    @Override
    public int getItemTextSize() {
        return mItemTextSize;
    }

    @Override
    public void setItemTextSize(int size) {
        setItemTextSize(size, false);
    }

    public void setItemTextSize(int size, boolean invalidate) {
        mItemTextSize = size;
        mPaint.setTextSize(mItemTextSize);
        computeTextSize();
        if (invalidate) {
            requestLayout();
            invalidate();
        }
    }

    @Override
    public int getItemSpace() {
        return mItemSpace;
    }

    @Override
    public void setItemSpace(int space) {
        mItemSpace = space;
        requestLayout();
        invalidate();
    }

    @Override
    public void setIndicator(boolean hasIndicator) {
        this.hasIndicator = hasIndicator;
        computeIndicatorRect();
        invalidate();
    }

    @Override
    public boolean hasIndicator() {
        return hasIndicator;
    }

    @Override
    public int getIndicatorSize() {
        return mIndicatorSize;
    }

    @Override
    public void setIndicatorSize(int size) {
        mIndicatorSize = size;
        computeIndicatorRect();
        invalidate();
    }

    @Override
    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    @Override
    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        invalidate();
    }

    @Override
    public void setCurtain(boolean hasCurtain) {
        this.hasCurtain = hasCurtain;
        computeCurrentItemRect();
        invalidate();
    }

    @Override
    public boolean hasCurtain() {
        return hasCurtain;
    }

    @Override
    public int getCurtainColor() {
        return mCurtainColor;
    }

    @Override
    public void setCurtainColor(int color) {
        mCurtainColor = color;
        invalidate();
    }

    @Override
    public void setAtmospheric(boolean hasAtmospheric) {
        this.hasAtmospheric = hasAtmospheric;
        invalidate();
    }

    @Override
    public boolean hasAtmospheric() {
        return hasAtmospheric;
    }

    @Override
    public boolean isCurved() {
        return isCurved;
    }

    @Override
    public void setCurved(boolean isCurved) {
        this.isCurved = isCurved;
        requestLayout();
        invalidate();
    }

    @Override
    public int getItemAlign() {
        return mItemAlign;
    }

    @Override
    public void setItemAlign(int align) {
        mItemAlign = align;
        updateItemTextAlign();
        computeDrawnCenter();
        invalidate();
    }

    @Override
    public Typeface getTypeface() {
        if (null != mPaint)
            return mPaint.getTypeface();
        return null;
    }

    @Override
    public void setTypeface(Typeface tf) {
        if (null != mPaint)
            mPaint.setTypeface(tf);
        computeTextSize();
        requestLayout();
        invalidate();
    }

    /**
     * 滚轮选择器Item项被选中时监听接口
     *
     * @author AigeStudio 2016-06-17
     * 新项目结构
     * @version 1.1.0
     */
    public interface OnItemSelectedListener {
        /**
         * 当滚轮选择器数据项被选中时回调该方法
         * 滚动选择器滚动停止后会回调该方法并将当前选中的数据和数据在数据列表中对应的位置返回
         *
         * @param picker   滚轮选择器
         * @param data     当前选中的数据
         * @param position 当前选中的数据在数据列表中的位置
         */
        void onItemSelected(WheelPicker picker, Object data, int position);
    }

    /**
     * 滚轮选择器滚动时监听接口
     *
     * @since 2016-06-17
     */
    public interface OnWheelChangeListener {
        /**
         * 当滚轮选择器滚动时回调该方法
         * 滚轮选择器滚动时会将当前滚动位置与滚轮初始位置之间的偏移距离返回，该偏移距离有正负之分，正值表示
         * 滚轮正在往上滚动，负值则表示滚轮正在往下滚动
         *
         * @param offset 当前滚轮滚动距离上一次滚轮滚动停止后偏移的距离
         */
        void onWheelScrolled(int offset);

        /**
         * 当滚轮选择器停止后回调该方法
         * 滚轮选择器停止后会回调该方法并将当前选中的数据项在数据列表中的位置返回
         *
         * @param position 当前选中的数据项在数据列表中的位置
         */
        void onWheelSelected(int position);

        /**
         * 当滚轮选择器滚动状态改变时回调该方法
         * 滚动选择器的状态总是会在静止、拖动和滑动三者之间切换，当状态改变时回调该方法
         * <p>
         * Invoke when WheelPicker's scroll state changed
         * The state of WheelPicker always between idle, dragging, and scrolling, this method will
         * be called when they switch
         */
        void onWheelScrollStateChanged(int state);
    }
}