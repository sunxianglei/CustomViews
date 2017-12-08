package com.xianglei.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xianglei.customviews.R;
import com.xianglei.customviews.utils.DisplayUtil;

/**
 * 阶段性进度条
 * Created by sunxianglei on 2017/11/16.
 */

public class PeriodProgress extends View {

    private static final int DEFAULT_TEXTSIZE = 13;          //默认文字大小
    private static final int DEFAULT_RADIUS = 6;             //默认半径
    private static final int DEFAULT_PANDDINGLEFT = 30;      //默认左侧第一个圆和屏幕左侧的间距
    private static final int DEFAULT_LINEWIDTH = 2;          //默认线段宽度

    private String[] name;
    private String[] time;
    private Boolean[] isFinish;
    private int mWidth;

    private int mFirstTextSize;
    private int mSecondTextSize;
    private int mFirstTextColor;
    private int mFirstTextFinishColor;
    private int mSecondTextColor;
    private int mSecondTextFinishColor;
    private int mCircleRadius;
    private int mCircleColor;
    private int mCircleFinishColor;
    private int mLineWidth;
    private int mLineColor;
    private int mLineFinishColor;

    private Paint mLinePaint;
    private Paint mCirclePaint;
    private Paint mFirstTextPaint;
    private Paint mSecondTextPaint;
    private Rect mRect;

    public PeriodProgress(Context context) {
        this(context,null);
    }

    public PeriodProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mFirstTextPaint = new Paint();
        mFirstTextPaint.setAntiAlias(true);
        mSecondTextPaint = new Paint();
        mSecondTextPaint.setAntiAlias(true);
        mRect = new Rect();

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PeriodProgress);
        mFirstTextSize = ta.getDimensionPixelSize(R.styleable.PeriodProgress_text_size_first,
                DisplayUtil.sp2px(getContext(),DEFAULT_TEXTSIZE));
        mSecondTextSize = ta.getDimensionPixelSize(R.styleable.PeriodProgress_text_size_second,
                DisplayUtil.sp2px(getContext(),DEFAULT_TEXTSIZE));
        mFirstTextColor = ta.getColor(R.styleable.PeriodProgress_text_color_first, Color.DKGRAY);
        mFirstTextFinishColor = ta.getColor(R.styleable.PeriodProgress_text_finish_color_first, Color.DKGRAY);
        mSecondTextColor = ta.getColor(R.styleable.PeriodProgress_text_color_second, Color.GRAY);
        mSecondTextFinishColor = ta.getColor(R.styleable.PeriodProgress_text_finish_color_second, Color.DKGRAY);
        mCircleRadius = ta.getDimensionPixelSize(R.styleable.PeriodProgress_circle_radius,
                DisplayUtil.dip2px(getContext(),DEFAULT_RADIUS));
        mCircleColor = ta.getColor(R.styleable.PeriodProgress_circle_color, Color.LTGRAY);
        mCircleFinishColor = ta.getColor(R.styleable.PeriodProgress_circle_finish_color, Color.GRAY);
        mLineWidth = ta.getDimensionPixelSize(R.styleable.PeriodProgress_line_width,
                DisplayUtil.dip2px(getContext(), DEFAULT_LINEWIDTH));
        mLineColor = ta.getColor(R.styleable.PeriodProgress_line_color, Color.LTGRAY);
        mLineFinishColor = ta.getColor(R.styleable.PeriodProgress_line_finish_color, Color.GRAY);

        mCirclePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    /**
     * @param name  第一行文字
     * @param time  第二行文字
     * @param isFinish  第三行状态
     */
    public void setData(String[] name, String[] time, Boolean[] isFinish){
        this.name = name;
        this.time = time;
        this.isFinish = isFinish;
        if(name != null) {
            mFirstTextPaint.setTextSize(mFirstTextSize);
        }
        if(time != null){
            mSecondTextPaint.setTextSize(mSecondTextSize);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultHeight = (int) (mFirstTextPaint.getFontSpacing() + mSecondTextPaint.getFontSpacing() +
                2 * mCircleRadius + DisplayUtil.dip2px(getContext(), 10));    //wrap_content时的默认高度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = defaultHeight;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFinish == null){   //状态一定不能为空
            return;
        }
        // 根据设置的间距来确定原坐标
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        mWidth = getWidth() - paddingLeft - paddingRight - DisplayUtil.dip2px(getContext(), 2 * DEFAULT_PANDDINGLEFT);
        canvas.translate(paddingLeft + DisplayUtil.dip2px(getContext(), DEFAULT_PANDDINGLEFT), 0);

        int length = isFinish.length;
        for(int i=0;i<length;i++){
            if(isFinish[i]){
                mFirstTextPaint.setColor(mFirstTextFinishColor);
                mSecondTextPaint.setColor(mSecondTextFinishColor);
                mCirclePaint.setColor(mCircleFinishColor);
                mLinePaint.setColor(mLineFinishColor);
            }else{
                mFirstTextPaint.setColor(mFirstTextColor);
                mSecondTextPaint.setColor(mSecondTextColor);
                mCirclePaint.setColor(mCircleColor);
                mLinePaint.setColor(mLineColor);
            }
            int spaceWidth = mWidth * i / (length - 1);
            if(name != null && name.length == length) { // 画第一行文字
                mFirstTextPaint.getTextBounds(name[i], 0, name[i].length(), mRect);
                canvas.drawText(name[i], spaceWidth - (mRect.right - mRect.left) / 2,
                        mFirstTextPaint.getFontSpacing(), mFirstTextPaint);
            }
            if(time != null && time.length == length) { // 画第二行文字
                mSecondTextPaint.getTextBounds(time[i], 0, time[i].length(), mRect);
                canvas.drawText(time[i], spaceWidth - (mRect.right - mRect.left) / 2,
                        mFirstTextPaint.getFontSpacing() + mSecondTextPaint.getFontSpacing() , mSecondTextPaint);
            }
            // 画圆以及连接线
            canvas.drawCircle(spaceWidth, mFirstTextPaint.getFontSpacing() + mSecondTextPaint.getFontSpacing() + 2 * mCircleRadius,
                    mCircleRadius, mCirclePaint);
            if(i != length - 1){
                canvas.drawLine(spaceWidth + 2 * mCircleRadius,mFirstTextPaint.getFontSpacing() + mSecondTextPaint.getFontSpacing( )+ 2 * mCircleRadius,
                        mWidth * (i + 1) / (length - 1) - 2 * mCircleRadius, mFirstTextPaint.getFontSpacing() + mSecondTextPaint.getFontSpacing() + 2 * mCircleRadius, mLinePaint);
            }
        }
    }

}
