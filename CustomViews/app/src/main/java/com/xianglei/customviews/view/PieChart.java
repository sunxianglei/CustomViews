package com.xianglei.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xianglei.customviews.R;
import com.xianglei.customviews.utils.Constant;
import com.xianglei.customviews.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 可定制饼图
 * Created by sunxianglei on 2017/8/5.
 */

public class PieChart extends View{

    public static final int OUTSIDE_MODE = 0;               //文字显示在饼图外部
    public static final int INSIDE_MODE = 1;                //文字显示在饼图内部
    private static final int DEFAULT_TEXTSIZE = 10;
    private static final Float DEFAULT_LINE_RATIO = 0.3f;

    private String[] names;
    private int[] colors;
    private int[] values;
    private int mRadius;
    private float mTextLineLength;
    private AttrsModel attrsModel;
    private List<ArcModel> arcModelList;
    private int showMode = OUTSIDE_MODE;

    private Context mContext;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mArcPaint;

    public PieChart(Context context) {
        super(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mTextPaint = new Paint();
        mLinePaint = new Paint();
        mArcPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mLinePaint.setAntiAlias(true);
        mArcPaint.setAntiAlias(true);

        this.names = Constant.PIE_NAMES;
        this.colors = Constant.PIE_COLORS;
        this.values = Constant.PIE_VALUES;

        arcModelList = new ArrayList<>();
        attrsModel = new AttrsModel();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.PieChart);
        attrsModel.textSize = ta.getDimension(R.styleable.PieChart_pie_textSize, DisplayUtil.dip2px(mContext, DEFAULT_TEXTSIZE));
        attrsModel.textColor = ta.getColor(R.styleable.PieChart_pie_textColor, Color.BLACK);
        attrsModel.lineRatio = ta.getFloat(R.styleable.PieChart_pie_lineRatio, DEFAULT_LINE_RATIO);
        attrsModel.lineColor = ta.getColor(R.styleable.PieChart_pie_lineColor, Color.BLACK);
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

    /**
     * 选择饼图显示的模式
     * @param showMode
     */
    public void setMode(int showMode){
        this.showMode = showMode;
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
        mTextLineLength = mRadius * attrsModel.lineRatio;
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
            arcModel.ratio = String.format("%.2f", values[i] * 1.0f / total * 100) + "%";
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
        mTextPaint.setTextSize(attrsModel.textSize);
        mTextPaint.setColor(attrsModel.textColor);
        mLinePaint.setColor(attrsModel.lineColor);
        RectF rectF = new RectF(-mRadius,-mRadius,mRadius,mRadius);
        canvas.translate(getWidth()/2,getHeight()/2);
        for(int i=0;i<arcModelList.size();i++){
            ArcModel arcModel = arcModelList.get(i);
            mArcPaint.setColor(arcModel.color);
            canvas.drawArc(rectF, arcModel.startAngle, arcModel.sweepAngle, true, mArcPaint);
        }
        //分开画是为了避免文字被饼图遮住
        for(int i=0;i<arcModelList.size();i++) {
            ArcModel arcModel = arcModelList.get(i);
            if(showMode == OUTSIDE_MODE) {
                drawOutsideText(canvas, arcModel);
            }else if(showMode == INSIDE_MODE){
                drawInsideText(canvas, arcModel);
            }
        }
    }

    /**
     * 画文字显示在外部的模式
     * @param canvas
     */
    private void drawOutsideText(Canvas canvas, ArcModel arcModel){
        canvas.save();
        canvas.rotate(arcModel.centerAngle);
        canvas.translate(mRadius, 0);
        canvas.drawLine(0, 0, mTextLineLength / 3, 0, mLinePaint);
        canvas.save();
        canvas.translate(mTextLineLength / 3, 0);
        if (arcModel.centerAngle > 90 && arcModel.centerAngle < 270) {//第二三象限的直线往左边
            canvas.rotate(-arcModel.centerAngle - 180);
            canvas.drawLine(0, 0, 2 * mTextLineLength / 3, 0, mLinePaint);
            //在左边画文字的时候会出现颠倒的情况，所以要把坐标翻一下
            canvas.save();
            canvas.translate(2 * mTextLineLength / 3, 0);
            canvas.scale(-1, -1);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(arcModel.textName, 0, 0, mTextPaint);
            canvas.drawText(arcModel.ratio, 0, mTextPaint.getFontSpacing(), mTextPaint);
            canvas.restore();
        } else {//第一四象限的直线往左边
            canvas.rotate(-arcModel.centerAngle);
            canvas.drawLine(0, 0, 2 * mTextLineLength / 3, 0, mLinePaint);
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(arcModel.textName, 2 * mTextLineLength / 3, 0, mTextPaint);
            canvas.drawText(arcModel.ratio, 2 * mTextLineLength / 3, mTextPaint.getFontSpacing(), mTextPaint);
        }
        canvas.restore();
        canvas.restore();
    }

    /**
     * 画文字显示在内部的模式
     * @param canvas
     * @param arcModel
     */
    private void drawInsideText(Canvas canvas, ArcModel arcModel){
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.save();
        canvas.rotate(arcModel.centerAngle);
        canvas.translate(mRadius/1.5f, 0);
        if (arcModel.centerAngle > 90 && arcModel.centerAngle < 270) {//第二三象限的直线往左边
            canvas.rotate(-arcModel.centerAngle - 180);
            //在左边画文字的时候会出现颠倒的情况，所以要把坐标翻一下
            canvas.save();
            canvas.scale(-1, -1);
            canvas.drawText(arcModel.textName, 0, 0, mTextPaint);
            canvas.drawText(arcModel.ratio, 0, mTextPaint.getFontSpacing(), mTextPaint);
            canvas.restore();
        } else {//第一四象限的直线往左边
            canvas.rotate(-arcModel.centerAngle);
            canvas.drawText(arcModel.textName, 0, 0, mTextPaint);
            canvas.drawText(arcModel.ratio, 0, mTextPaint.getFontSpacing(), mTextPaint);
        }
        canvas.restore();
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
        public String ratio;     //占比
    }

    /**
     * 属性模型
     */
    class AttrsModel {
        public float textSize;
        public int textColor;
        public float lineRatio; //刻度值文字大小
        public int lineColor;   //刻度值文字颜色
    }
}
