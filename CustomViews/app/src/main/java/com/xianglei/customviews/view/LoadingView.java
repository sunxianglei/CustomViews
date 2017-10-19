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

/**
 * 自定义加载框
 * Created by sunxianglei on 2017/10/13.
 */

public class LoadingView extends View {

    private int radius;
    private float progress = 0;
    private boolean hideText;

    private Paint mTextPaint;
    private Paint mLoadingPaint;
    private RectF rectF;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mTextPaint.setColor(ta.getColor(R.styleable.LoadingView_loading_text_color, Color.WHITE));
        mLoadingPaint.setColor(ta.getColor(R.styleable.LoadingView_loading_circle_color, Color.parseColor("#E91E63")));
        ta.recycle();    //记得回收
    }

    private void init(){
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mLoadingPaint = new Paint();
        mLoadingPaint.setAntiAlias(true);
        mLoadingPaint.setStyle(Paint.Style.STROKE);
        mLoadingPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 500; //最大大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode != MeasureSpec.UNSPECIFIED){
            size = Math.min(size, widthSize);
        }
        if(heightMode != MeasureSpec.UNSPECIFIED){
            size = Math.min(size, heightSize);
        }
        radius = size * 3 / 8;
        mTextPaint.setTextSize(size/5);
        mLoadingPaint.setStrokeWidth(size/10);
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = (getRight() + getLeft())/2;
        int centerY = (getBottom() + getTop())/2;
        rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, 135, progress * 3.6f, false, mLoadingPaint);
        if(!hideText) {
            Rect bounds = new Rect();
            String text = (int) progress + "%";
            mTextPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, centerX - (bounds.right - bounds.left) / 2, centerY + (bounds.bottom - bounds.top) / 2, mTextPaint);
        }
    }

    /**
     * 隐藏中间的文字
     * @param hideText
     */
    public void setHideText(boolean hideText){
        this.hideText = hideText;
    }

    public float getProgress(){
        return progress;
    }

    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }
}
