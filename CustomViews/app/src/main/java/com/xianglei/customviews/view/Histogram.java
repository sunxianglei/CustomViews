package com.xianglei.customviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xianglei.customviews.utils.Constant;
import com.xianglei.customviews.utils.DisplayUtil;

/**
 * 可定制直方图
 * Created by Administrator on 2017/8/5.
 */

public class Histogram extends View{

    private String[] names;
    private int[] colors;
    private int[] values ;
    private int textNameSize;
    private float valueWidth;
    private float valueBlankWidth;
    private float lineX;
    private float lineY;
    private float[] valueY;
    private float[] valueX;
    private int mWidth;
    private int mHeight;

    private Context mContext;
    private Paint mPaint;

    public Histogram(Context context){
        super(context);
        mContext = context;
        init();
    }

    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mPaint = new Paint();
        this.names = Constant.HISTOGRAM_NAMES;
        this.colors = Constant.HISTOGRAM_COLORS;
        this.values = Constant.HISTOGRAM_VALUES;
    }

    /**
     * 计算直方图的文字、图形高度等尺寸
     */
    private void calculateSize(){
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        lineX = mWidth - paddingLeft - paddingRight;
        lineY = mHeight - paddingTop - paddingBottom;
        valueWidth = 0.8f * (lineX * 1.0f / values.length);
        valueBlankWidth = 0.2f * (lineX * 1.0f / values.length);
        textNameSize = DisplayUtil.sp2px(mContext,15);
        valueY = new float[values.length];
        valueX = new float[values.length];
        float result = 0.0f;
        for(int i=0;i<values.length;i++){
            if(result < values[i]) result = values[i];
        }
        result = 1.2f * result;
        for(int i=0;i<values.length;i++){
            valueX[i] = valueBlankWidth + i * (valueBlankWidth + valueWidth);
            valueY[i] = lineY * (values[i]/result);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        calculateSize();// 在宽高都计算好后
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        drawNameAndRect(canvas);
    }

    /**
     * 画坐标
     */
    private void drawCoordinate(Canvas canvas){
        float[] lines = { 0, 0, 0, lineY, 0, 0, lineX, 0};
        canvas.translate((mWidth-lineX)/2, lineY + (mHeight-lineY)/2);
        canvas.scale(1, -1);
        mPaint.setColor(Color.BLACK);
        canvas.drawLines(lines,mPaint);
    }

    /**
     * 画名称及矩形
     * @param canvas
     */
    private void drawNameAndRect(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textNameSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float fontSpacing = mPaint.getFontSpacing();
        for(int i=0;i<values.length;i++){
            canvas.save();
            canvas.scale(1,-1);
            canvas.drawText(names[i], valueX[i] + valueWidth/2, fontSpacing, mPaint);
            canvas.restore();
        }
        mPaint.setStyle(Paint.Style.FILL);
        for(int i=0;i<values.length;i++){
            mPaint.setColor(colors[i]);
            canvas.drawRect(valueX[i], 0, valueX[i] + valueWidth, valueY[i], mPaint);
        }
    }
}
