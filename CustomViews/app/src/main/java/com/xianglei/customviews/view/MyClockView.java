package com.xianglei.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.xianglei.customviews.R;
import com.xianglei.customviews.utils.DisplayUtil;

import java.util.Calendar;

/**
 * 自定义机械表
 * Created by sunxianglei on 2017/2/3.
 */

public class MyClockView extends View {

    // 常量区块
    private static final int PADDING_INSIDE = 10;   //表盘内边距
    private static final int ROTATE_ANGLE = 6;  //相连刻度的角度差
    private static final float SCALE_LONG_WIDTH = 2.0f;    //长刻度的宽度
    private static final float SCALE_SHORT_WIDTH = 1.0f;   //短刻度的宽度
    // 成员变量区块
    private float mRadius;    //钟表半径
    private Paint mTextPaint;
    private Paint mCiclePaint;
    private Paint mLinePaint;
    private Context mContext;
    private AttrsModel attrsModel;

    public MyClockView(Context context) {
        super(context);
        init();
    }

    public MyClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        obtainStyledAttr(attrs);
    }

    private void init() {
        mContext = getContext();
        attrsModel = new AttrsModel();
        mTextPaint = new Paint();
        mCiclePaint = new Paint();
        mLinePaint = new Paint();

        mTextPaint.setAntiAlias(true);
        mCiclePaint.setAntiAlias(true);
        mLinePaint.setAntiAlias(true);
    }

    /**
     * 获取属性值
     *
     * @param attrs
     */
    private void obtainStyledAttr(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MyClockView);
        attrsModel.mCenterCircleRadius = array.getDimension(R.styleable.MyClockView_clock_center_circle_radius, DisplayUtil.dip2px(mContext, 20));
        attrsModel.mTextSize = array.getDimension(R.styleable.MyClockView_clock_text_size, DisplayUtil.sp2px(mContext, 13));
        attrsModel.mHourPointerWidth = array.getDimension(R.styleable.MyClockView_clock_hour_pointer_width, DisplayUtil.dip2px(mContext, 5));
        attrsModel.mMinutePointerWidth = array.getDimension(R.styleable.MyClockView_clock_minute_pointer_width, DisplayUtil.dip2px(mContext, 3));
        attrsModel.mSecondPointerWidth = array.getDimension(R.styleable.MyClockView_clock_second_pointer_width, DisplayUtil.dip2px(mContext, 2));
        attrsModel.mPointerCornerRadius = array.getDimension(R.styleable.MyClockView_clock_pointer_corner_radius, DisplayUtil.dip2px(mContext, 10));
        attrsModel.mHourPointerOutLength = array.getDimension(R.styleable.MyClockView_clock_hour_pointer_out_length, DisplayUtil.dip2px(mContext, 0));
        attrsModel.mMinutePointerOutLength = array.getDimension(R.styleable.MyClockView_clock_minute_pointer_out_length, DisplayUtil.dip2px(mContext, 0));
        attrsModel.mSecondPointerOutLength = array.getDimension(R.styleable.MyClockView_clock_second_pointer_out_length, DisplayUtil.dip2px(mContext, 20));
        attrsModel.mScaleShortLength = array.getDimension(R.styleable.MyClockView_clock_scale_length, DisplayUtil.dip2px(mContext, 14));
        attrsModel.mScaleLongColor = array.getColor(R.styleable.MyClockView_clock_scale_long_color, Color.argb(225, 0, 0, 0));
        attrsModel.mScaleShortColor = array.getColor(R.styleable.MyClockView_clock_scale_short_color, Color.argb(125, 0, 0, 0));
        attrsModel.mHourPointerColor = array.getColor(R.styleable.MyClockView_clock_hour_pointer_color, Color.BLACK);
        attrsModel.mMinutePointerColor = array.getColor(R.styleable.MyClockView_clock_minute_pointer_color, Color.BLACK);
        attrsModel.mSecondPointerColor = array.getColor(R.styleable.MyClockView_clock_second_pointer_color, Color.RED);
        array.recycle();    //记得回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 1000;    //最大值，表盘是只能是正方形才好看，所以长宽相等

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = Math.min(width, widthSize);
            width = Math.min(width, heightSize);
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        paintCircle(canvas);
        paintScaleAndTime(canvas);
        paintPointer(canvas);
        postInvalidateDelayed(1000);
    }

    /**
     * 绘制外圆表盘
     *
     * @param canvas
     */
    private void paintCircle(Canvas canvas) {
        mCiclePaint.setColor(Color.WHITE);
        mCiclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mRadius, mCiclePaint);
    }

    /**
     * 绘制刻度和时间
     *
     * @param canvas
     */
    private void paintScaleAndTime(Canvas canvas) {
        float lineWidth = 0;
        mTextPaint.setStrokeWidth(SCALE_LONG_WIDTH);
        mTextPaint.setTextSize(attrsModel.mTextSize);
        mTextPaint.setColor(Color.BLACK);
        //虽然坐标是第四象限，但是是从12点开始绘制的
        for (int i = 1; i <= 60; i++) {
            canvas.rotate(ROTATE_ANGLE);
            if (i % 5 == 0) {
                lineWidth = attrsModel.mScaleShortLength * 1.5f;
                //绘制时间
                String time = String.valueOf(i / 5);
                Rect textBound = new Rect();
                mTextPaint.getTextBounds(time, 0, time.length(), textBound);   //数字矩形区域，为了保证数字不偏移
                //这里操作的目的是让数字正确摆放
                //先移动画布到数字的中心点，然后旋转画布（否则数字会随着画布的坐标而倾斜），最后绘制（因为数字是从左上角开始绘制的，所以要取中心点）
                canvas.save();  //保存上次画布的状态
                canvas.translate(0, -mRadius + lineWidth + PADDING_INSIDE * 2 + (textBound.bottom - textBound.top) / 2);
                canvas.rotate(-ROTATE_ANGLE * i);
                canvas.drawText(time, (textBound.left - textBound.right) / 2, (textBound.bottom - textBound.top) / 2, mTextPaint);
                canvas.restore();   //返回上次画布
                //准备绘制刻度
                mLinePaint.setStrokeWidth(SCALE_LONG_WIDTH);
                mLinePaint.setColor(attrsModel.mScaleLongColor);
            } else {
                lineWidth = attrsModel.mScaleShortLength;
                //准备绘制刻度
                mLinePaint.setStrokeWidth(SCALE_SHORT_WIDTH);
                mLinePaint.setColor(attrsModel.mScaleShortColor);
            }
            canvas.drawLine(0, -mRadius + PADDING_INSIDE, 0, -mRadius + PADDING_INSIDE + lineWidth, mLinePaint);    //绘制刻度

        }
    }

    /**
     * 绘制指针和中心圆
     *
     * @param canvas
     */
    private void paintPointer(Canvas canvas) {
        //绘制中心圆
        mCiclePaint.setColor(Color.RED);
        mCiclePaint.setStyle(Paint.Style.FILL);
        mCiclePaint.setAntiAlias(true);
        canvas.drawCircle(0, 0, attrsModel.mCenterCircleRadius, mCiclePaint);
        //得到当前时间
        Calendar cal = Calendar.getInstance();
        float hour = cal.get(Calendar.HOUR_OF_DAY);
        float minute = cal.get(Calendar.MINUTE);
        float second = cal.get(Calendar.SECOND);
        float angleSecond = second * 360 / 60;
        float angleMinute = (minute + second / 60) * 360 / 60;
        float angleHour = ((int) (hour) % 12 + minute / 60) * 360 / 12;
        //绘制时针
        canvas.save();
        mLinePaint.setColor(attrsModel.mHourPointerColor);
        mLinePaint.setStrokeWidth(attrsModel.mHourPointerWidth);
        canvas.rotate((int) angleHour);
        RectF rectHour = new RectF(-attrsModel.mHourPointerWidth / 2, -mRadius * 3 / 5,
                attrsModel.mHourPointerWidth / 2, attrsModel.mHourPointerOutLength);   //时针占3/5的长度
        canvas.drawRoundRect(rectHour, attrsModel.mPointerCornerRadius, attrsModel.mPointerCornerRadius, mLinePaint);
        canvas.restore();
        //绘制分针
        canvas.save();
        mLinePaint.setColor(attrsModel.mMinutePointerColor);
        mLinePaint.setStrokeWidth(attrsModel.mMinutePointerWidth);
        canvas.rotate((int) angleMinute);
        RectF rectMinute = new RectF(-attrsModel.mMinutePointerWidth / 2, -mRadius * 3.5f / 5,
                attrsModel.mMinutePointerWidth / 2, attrsModel.mMinutePointerOutLength);   //分针占3.5/5的长度
        canvas.drawRoundRect(rectMinute, attrsModel.mPointerCornerRadius, attrsModel.mPointerCornerRadius, mLinePaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        mLinePaint.setColor(attrsModel.mSecondPointerColor);
        mLinePaint.setStrokeWidth(attrsModel.mSecondPointerWidth);
        canvas.rotate((int) angleSecond);
        RectF rectSecond = new RectF(-attrsModel.mSecondPointerWidth / 2, -mRadius + PADDING_INSIDE,
                attrsModel.mSecondPointerWidth / 2, attrsModel.mSecondPointerOutLength);   //秒针的长度
        canvas.drawRoundRect(rectSecond, attrsModel.mPointerCornerRadius, attrsModel.mPointerCornerRadius, mLinePaint);
        canvas.restore();
    }

    /**
     * 自定义属性对象
     */
    class AttrsModel {
        private float mCenterCircleRadius;  //中心圆半径
        private float mTextSize;    //时间数字字体大小
        private float mHourPointerWidth;    //时针宽度
        private float mMinutePointerWidth;  //分针宽度
        private float mSecondPointerWidth;  //秒针宽度
        private float mPointerCornerRadius; //指针圆角半径
        private float mHourPointerOutLength;    //时针超出中心点宽度
        private float mMinutePointerOutLength;    //分针超出中心点宽度
        private float mSecondPointerOutLength;    //秒针超出中心点宽度
        private float mScaleShortLength;   //短刻度长度(长刻度是短刻度的1.5倍)
        private int mScaleLongColor;    //长刻度颜色
        private int mScaleShortColor;   //短刻度颜色
        private int mHourPointerColor; //时针颜色
        private int mMinutePointerColor;    //分针颜色
        private int mSecondPointerColor;    //秒针颜色
    }
}
