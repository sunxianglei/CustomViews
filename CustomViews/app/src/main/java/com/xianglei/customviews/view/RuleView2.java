package com.xianglei.customviews.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.xianglei.customviews.R;
import com.xianglei.customviews.utils.DisplayUtil;
import com.xianglei.customviews.utils.MathUtil;


/**
 * 滑动卷尺（仿薄荷健康滑动卷尺）
 * Created by sunxianglei on 2017/10/17.
 */

public class RuleView2 extends View {


    private int mHeight;                    //View的高度
    private float minUnitWidth;             //最小刻度的宽度
    private float mValue = 10f;             //现在的值
    private float mMoveDistance;            //屏幕上移动的距离
    private float mLastX;
    private int mMinVelocity;               //手势滑动最小速度

//    private float minUnitValue = 0.1f;      //最小刻度的值，默认1毫米
//    private float maxValue = 200;           //最大值，默认200cm
//    private float showedScaleValue = 3.2f;  //页面能显示的刻度范围

    private Paint mMiddleLinePaint;
    private Paint mScaleTextPaint;
    private Paint mScaleLinePaint;
    private Scroller mScroller;
    private VelocityTracker mVeloctyTracker;

    private Attrs mAttrs;
    private OnValueChangeListener mValueChangeListener;

    public RuleView2(Context context) {
        this(context, null);
    }

    public RuleView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RuleView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttr(attrs);
        init();
    }

    private void init(){

        mMiddleLinePaint = new Paint();
        mMiddleLinePaint.setAntiAlias(true);
        mMiddleLinePaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 4));
        mMiddleLinePaint.setColor(mAttrs.middleLineColor);

        mScaleTextPaint = new Paint();
        mScaleTextPaint.setAntiAlias(true);
        mScaleTextPaint.setColor(mAttrs.scaleTextColor);
        mScaleTextPaint.setTextSize(mAttrs.scaleTextSize);

        mScaleLinePaint = new Paint();
        mScaleLinePaint.setAntiAlias(true);
        mScaleLinePaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2));
        mScaleLinePaint.setColor(mAttrs.scaleLineColor);

        mScroller = new Scroller(getContext());
        mVeloctyTracker = VelocityTracker.obtain();
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        setBackgroundDrawable(createBackground());
    }

    /**
     * 渐变背景色
     * @return
     */
    private GradientDrawable createBackground(){
        int colors[] = {0xfff4f1f1, 0xffffffff};// 分别为开始颜色，结束颜色
        GradientDrawable bgDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        return bgDrawable;
    }

    private void parseAttr(AttributeSet attrs){
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RuleView);
        mAttrs = new Attrs();
        mAttrs.middleLineColor = ta.getColor(R.styleable.RuleView_rule_middle_line_color, Color.GREEN);
        mAttrs.scaleLineColor = ta.getColor(R.styleable.RuleView_rule_scale_line_color, Color.parseColor("#dad8d8"));
        mAttrs.scaleTextColor = ta.getColor(R.styleable.RuleView_rule_scale_text_color, Color.BLACK);
        mAttrs.scaleTextSize = ta.getDimensionPixelSize(R.styleable.RuleView_rule_scale_text_size,
                DisplayUtil.sp2px(getContext(), 25));
        mAttrs.minUnitValue = ta.getFloat(R.styleable.RuleView_rule_min_unit_value, 0.1f);
        mAttrs.maxShowValue = ta.getFloat(R.styleable.RuleView_rule_max_show_value, 3.2f);
        mAttrs.maxValue = ta.getFloat(R.styleable.RuleView_rule_max_value, 200f);
        mAttrs.defaultValue = ta.getFloat(R.styleable.RuleView_rule_default_value, 50f);

        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minUnitWidth = w / (mAttrs.maxShowValue / mAttrs.minUnitValue);
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRuler(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float currentX = event.getX();
        mVeloctyTracker.addMovement(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = currentX;
                mMoveDistance = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveDistance += mLastX - currentX;
                SmoothByMove();
                mLastX = currentX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                SmoothMoveEnd();
                SmoothByVelocity();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 作用：计算需要滑动距离以及现在的中间值
     * 思路：将累积的滑动距离除最小单位的长度从而得到新的值和距离，只要这样做才能做到滑动的连贯性以及不会缺失值
     */
    private void SmoothByMove(){
        int moveScale = Math.round(mMoveDistance / minUnitWidth);
        if(Math.abs(moveScale) > 0){
            mValue = MathUtil.addFloatNum(mValue, moveScale * mAttrs.minUnitValue);
            mMoveDistance -= moveScale * minUnitWidth;
            if(mValue < 0 || mValue > mAttrs.maxValue){
                mValue = mValue < 0 ? 0 : mAttrs.maxValue;
                mMoveDistance = 0;
                mScroller.forceFinished(true);
            }
        }
        if(mValueChangeListener != null) mValueChangeListener.onValueChange(MathUtil.keepDecimals(mValue, 2));
        postInvalidate();
    }

    /**
     * 手指抬起的时候要指到最近的刻度上
     */
    private void SmoothMoveEnd(){
        int moveScale = Math.round(mMoveDistance / minUnitWidth);           //四舍五入
        mValue = MathUtil.addFloatNum(mValue, moveScale * mAttrs.minUnitValue);
        if(mValue < 0 || mValue > mAttrs.maxValue){
            mValue = mValue < 0 ? 0 : mAttrs.maxValue;
        }
        mMoveDistance = 0;
        mLastX = 0;
        if(mValueChangeListener != null) mValueChangeListener.onValueChange(MathUtil.keepDecimals(mValue, 2));
        postInvalidate();
    }

    /**
     * 根据速率惯性滑动
     */
    private void SmoothByVelocity(){
        mVeloctyTracker.computeCurrentVelocity(1000);
        float velocityX = mVeloctyTracker.getXVelocity();
        if(Math.abs(velocityX) > mMinVelocity){
            mScroller.fling(0, 0, (int)velocityX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            if(mScroller.getCurrX() == mScroller.getFinalX()){
                SmoothMoveEnd();
            }else{
                mMoveDistance += mLastX - mScroller.getCurrX();
                SmoothByMove();
                mLastX = mScroller.getCurrX();
            }
        }
    }

    /**
     * 画刻度尺
     * @param canvas
     */
    private void drawRuler(Canvas canvas){
        int originalPointX = getLeft();
        int originalPointY = getTop();
//        canvas.translate(originalPointX, originalPointY); // 移动坐标原点，使计算更加简便
        int centerX = (getRight() + getLeft())/2;
        int shortLine = mHeight / 4;
        int longLine = mHeight / 2;
        Rect rect = new Rect();
        String text = null;
        float value = mValue;
        int length = (int)(mAttrs.maxShowValue / mAttrs.minUnitValue / 2);    // 计算页面能显示的刻度数量的一半来分别往左右两边画
        //向左画刻度
        for(int i = 0;i < length;i++){
            if(value == Math.round(value)){
                canvas.drawLine(centerX - minUnitWidth * i - mMoveDistance, 0, centerX - minUnitWidth * i - mMoveDistance, longLine, mScaleLinePaint);
                text = String.valueOf((int)value);
                mScaleTextPaint.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text, centerX - minUnitWidth * i - mMoveDistance - (rect.right - rect.left)/2,
                        longLine + mScaleTextPaint.getFontSpacing(), mScaleTextPaint);
            }else {
                canvas.drawLine(centerX - minUnitWidth * i - mMoveDistance, 0, centerX - minUnitWidth * i - mMoveDistance, shortLine, mScaleLinePaint);
            }
            value = MathUtil.subFloatNum(value, mAttrs.minUnitValue);
        }
        value = mValue;
        //向右画刻度
        for(int i = 0;i < length;i++){
            if(value == Math.round(value)){
                canvas.drawLine(centerX + minUnitWidth * i - mMoveDistance, 0, centerX + minUnitWidth * i - mMoveDistance, longLine, mScaleLinePaint);
                text = String.valueOf((int)value);
                mScaleTextPaint.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text, centerX + minUnitWidth * i - mMoveDistance - (rect.right - rect.left)/2,
                        longLine + mScaleTextPaint.getFontSpacing(), mScaleTextPaint);
            }else {
                canvas.drawLine(centerX + minUnitWidth * i - mMoveDistance, 0, centerX + minUnitWidth * i - mMoveDistance, shortLine, mScaleLinePaint);
            }
            value = MathUtil.addFloatNum(value, mAttrs.minUnitValue);
        }

        canvas.drawLine(centerX, 0, centerX, longLine + DisplayUtil.dip2px(getContext(), 10), mMiddleLinePaint);   // 画中间指示线
    }

    public float getValue(){
        return mValue;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener){
        this.mValueChangeListener = listener;
    }

    // 内部接口、类
    // ==========================

    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }

    /**
     * 自定义属性类
     */
    public class Attrs{
        int scaleLineColor;     //刻度线颜色
        int middleLineColor;    //中间线颜色
        int scaleTextColor;     //数字颜色
        int scaleTextSize;      //数字大小
        float minUnitValue;     //最小刻度单位（cm为准）
        float maxValue;         //刻度最大值
        float maxShowValue;     //UI上可展示的刻度值范围
        float defaultValue;     //默认刻度值
    }
}
