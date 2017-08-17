package com.xianglei.customviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xianglei.customviews.utils.Constant;
import com.xianglei.customviews.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 可定制饼图
 * Created by sunxianglei on 2017/8/5.
 */

public class PieChart extends View{

    private String[] names;
    private int[] colors;
    private int[] values;
    private int mRadius;
    private float mTextLineLength;

    private Context mContext;
    private Paint mPaint;
    private List<ArcModel> arcModelList;

    public PieChart(Context context) {
        super(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mPaint = new Paint();
        arcModelList = new ArrayList<>();
        this.names = Constant.PIE_NAMES;
        this.colors = Constant.PIE_COLORS;
        this.values = Constant.PIE_VALUES;
    }

    /**
     * 自定义设置数据
     * @param names
     * @param colors
     * @param values
     */
    public void setData(String[] names, int[] colors, int[] values){
        if(names != null) this.names = names;
        if(colors != null) this.colors = colors;
        if(values != null) this.values = values;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        countData(w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPeiChart(canvas);
    }

    /**
     * 计算需要的数据
     * @param width
     * @param height
     */
    private void countData(int width, int height){
        if((names.length != values.length && values.length != colors.length) || (names.length == 0))
            return ;
        mRadius = Math.min(width, height) / 3;
        mTextLineLength = mRadius / 3;
        float total = 0, max = 0;
        for(int i=0;i<values.length;i++){
            total += values[i];
            if(values[i] > max) {
                max = values[i];
            }
        }
        float startAngle = 0;
        for(int i=0;i<values.length;i++){
            ArcModel arcModel = new ArcModel();
            arcModel.startAngle = startAngle;
            arcModel.sweepAngle = values[i] * 1.0f / total * 360;
            arcModel.centerAngle = arcModel.startAngle + arcModel.sweepAngle / 2;
            arcModel.color = colors[i];
            arcModel.textName = names[i];
            arcModelList.add(arcModel);
            startAngle += arcModel.sweepAngle;
        }
    }

    /**
     * 画饼图
     * @param canvas
     */
    private void drawPeiChart(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DisplayUtil.sp2px(mContext, 10));
        RectF rectF = new RectF(-mRadius,-mRadius,mRadius,mRadius);
        canvas.translate(getWidth()/2,getHeight()/2);
        for(int i=0;i<arcModelList.size();i++){
            ArcModel arcModel = arcModelList.get(i);
            mPaint.setColor(arcModel.color);
            canvas.drawArc(rectF, arcModel.startAngle, arcModel.sweepAngle, true, mPaint);
            mPaint.setColor(Color.WHITE);
            // 这里画布画的是指向文字的线
            canvas.save();
            canvas.rotate(arcModel.centerAngle);
            canvas.translate(mRadius, 0);
            canvas.drawLine(0, 0, mTextLineLength / 3, 0, mPaint);
            canvas.save();
            canvas.translate(mTextLineLength / 3, 0);
            if(arcModel.centerAngle > 90 && arcModel.centerAngle < 270) {//第二三象限的直线往左边
                canvas.rotate(-arcModel.centerAngle - 180);
                canvas.drawLine(0, 0, 2 * mTextLineLength / 3, 0, mPaint);
                mPaint.setTextAlign(Paint.Align.RIGHT);
                //在左边画文字的时候会出现颠倒的情况，所以要把坐标翻一下
                canvas.save();
                canvas.translate(2 * mTextLineLength / 3, 0);
                canvas.scale(-1,-1);
                canvas.drawText(arcModel.textName, 0, 0, mPaint);
                canvas.restore();
            } else {//第一四象限的直线往左边
                canvas.rotate(-arcModel.centerAngle);
                canvas.drawLine(0, 0, 2 * mTextLineLength / 3, 0, mPaint);
                mPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(arcModel.textName, 2 * mTextLineLength / 3, 0, mPaint);
            }
            canvas.restore();
            canvas.restore();
        }
    }

    /**
     * 扇形的模型
     */
    class ArcModel{
        public float startAngle; //开始角度
        public float sweepAngle; //扫过角度
        public float centerAngle;//中心位置角度
        public String textName;  //名称
        public int color;        //扇形颜色
    }
}
