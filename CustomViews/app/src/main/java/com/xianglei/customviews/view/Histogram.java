package com.xianglei.customviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xianglei.customviews.utils.Constant;
import com.xianglei.customviews.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 可定制直方图
 * Created by Administrator on 2017/8/5.
 */

public class Histogram extends View{

    private String[] names;
    private int[] colors;
    private int[] values ;
    private int textNameSize;
    private float lengthX;      //X轴的长度
    private float lengthY;      //Y轴的长度
    private List<HistogramModel> histogramModelList;

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
        mPaint.setAntiAlias(true);
        histogramModelList = new ArrayList<>();
        this.names = Constant.HISTOGRAM_NAMES;
        this.colors = Constant.HISTOGRAM_COLORS;
        this.values = Constant.HISTOGRAM_VALUES;
    }

    /**
     * 计算各种数据，并存放到model中
     */
    private void getModel(){
        textNameSize = DisplayUtil.sp2px(mContext,13);
        float valueWidth = 0.8f * (lengthX * 1.0f / values.length);             // 每个矩形的宽度
        float valueBlankWidth = 0.2f * (lengthX * 1.0f / (values.length + 1));  // 每个矩形之间的距离
        float maxValue = 0.0f;
        for(int i=0;i<values.length;i++){
            if(maxValue < values[i]) maxValue = values[i];
        }
        maxValue = 1.2f * maxValue;                                             // 矩形高度的指标
        for(int i=0;i<values.length;i++){
            HistogramModel model = new HistogramModel();
            model.left = valueBlankWidth + i * (valueBlankWidth + valueWidth);
            model.top = -lengthY * (values[i]/maxValue);
            model.right = model.left + valueWidth;
            model.bottom = 0;
            model.centerX = model.left + valueWidth/2;
            model.color = colors[i];
            model.name = names[i];
            model.value = values[i];
            histogramModelList.add(model);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        getModel();
        drawHistogram(canvas);
    }

    /**
     * 画坐标, 做完这一步才能确定接下去的坐标
     * 注意：这个是表面的坐标，android的X轴坐标向右是正方向，Y轴坐标向下是正方向
     */
    private void drawCoordinate(Canvas canvas){
        int width = getWidth();
        int height = getHeight();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        lengthX = width - left - right;
        lengthY = height - top - bottom;
        float[] coordinate = { 0 , 0, lengthX, 0, 0, 0, 0, -lengthY};
        canvas.translate(left, height - bottom);
        mPaint.setColor(Color.BLACK);
        canvas.drawLines(coordinate,mPaint);
    }

    /**
     * 画直方图
     * @param canvas
     */
    private void drawHistogram(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textNameSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float fontSpacing = mPaint.getFontSpacing();
        for(int i=0;i<values.length;i++){
            HistogramModel model = histogramModelList.get(i);
            mPaint.setColor(Color.BLACK);
            canvas.drawText(model.name,  model.centerX, fontSpacing, mPaint);                       // 画名称
            canvas.drawText(String.valueOf(model.value),  model.centerX, model.top - 10, mPaint);   // 画值
            mPaint.setColor(model.color);
            canvas.drawRect(model.left, model.top, model.right, model.bottom, mPaint);              // 画矩形
        }
    }

    /**
     * 直方图模型
     */
    class HistogramModel{
        public String name;
        public int value;
        public int color;
        public float top;       //矩形左上角Y坐标
        public float left;      //矩形左上角X坐标
        public float bottom;    //矩形右下角Y坐标
        public float right;     //矩形右下角X坐标
        public float centerX;   //矩形中心X坐标
    }
}
