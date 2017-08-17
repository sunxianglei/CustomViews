package com.xianglei.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xianglei.customviews.R;
import com.xianglei.customviews.utils.Constant;
import com.xianglei.customviews.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 可定制直方图
 * Created by sunxianglei on 2017/8/5.
 */

public class Histogram extends View {

    private static final int HISTOGRAM_TEXTSIZE = 12;               // 默认文字大小
    private static final int SCALE_LENGHT = 3;                      // Y轴刻度长度
    private static final float RECT_WIDTH_SCALE = 0.8f;             // 矩形宽度占比
    private static final float RECT_BLANK_WIDTH_SCALE = 0.2f;       // 矩形之间距离占比
    private static final float Y_HEIGHT_SCALE = 1.2f;               // Y轴高度相对最高矩形的倍数

    private String[] names;
    private int[] colors;
    private int[] values;
    private float lengthX;      //X轴的长度
    private float lengthY;      //Y轴的长度
    private float maxValue;     //对应Y轴最高点的值
    private AttrsModel attrsModel;
    private List<HistogramModel> histogramModelList;

    private Context mContext;
    private Paint mPaint;

    public Histogram(Context context) {
        super(context, null);
    }

    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.names = Constant.HISTOGRAM_NAMES;
        this.colors = Constant.HISTOGRAM_COLORS;
        this.values = Constant.HISTOGRAM_VALUES;

        histogramModelList = new ArrayList<>();
        attrsModel = new AttrsModel();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.Histogram);
        attrsModel.textSize = ta.getDimension(R.styleable.Histogram_histogram_textSize, DisplayUtil.dip2px(mContext, HISTOGRAM_TEXTSIZE));
        attrsModel.textColor = ta.getColor(R.styleable.Histogram_histogram_textColor, Color.BLACK);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(attrsModel.textSize);
    }

    /**
     * 自定义设置数据
     *
     * @param names
     * @param colors
     * @param values
     */
    public void setData(String[] names, int[] colors, int[] values) {
        if (names != null) this.names = names;
        if (colors != null) this.colors = colors;
        if (values != null) this.values = values;
    }

    /**
     * 计算各种数据，并存放到model中
     */
    private void countData() {
        if ((names.length != values.length && values.length != colors.length) || (names.length == 0))
            return;
        float valueWidth = RECT_WIDTH_SCALE * (lengthX * 1.0f / names.length); // 每个矩形的宽度
        float valueBlankWidth = RECT_BLANK_WIDTH_SCALE * (lengthX * 1.0f / (names.length + 1)); // 每个矩形之间的距离
        maxValue = 0;
        for (int i = 0; i < values.length; i++) {
            if (maxValue < values[i]) maxValue = values[i];
        }
        maxValue = Y_HEIGHT_SCALE * maxValue; // Y轴的高度
        for (int i = 0; i < names.length; i++) {
            HistogramModel model = new HistogramModel();
            model.left = valueBlankWidth + i * (valueBlankWidth + valueWidth);
            model.top = -lengthY * (values[i] / maxValue);
            model.right = model.left + valueWidth;
            model.bottom = 0;
            model.centerX = model.left + valueWidth / 2;
            model.color = colors[i];
            model.name = names[i];
            model.value = values[i];
            histogramModelList.add(model);
        }
    }

    /**
     * 计算单位刻度
     * @return
     */
    private int countUnitValue(){
        int eachValue = (int) (maxValue/histogramModelList.size());
        StringBuffer str = new StringBuffer();
        while(eachValue != 0){
            int num = eachValue % 10;
            str.append(num);
            eachValue /= 10;
        }
        str.reverse();
        int result = (int) ((str.charAt(0) - '0') * Math.pow(10, str.length() - 1));
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        histogramModelList.clear();
        drawCoordinate(canvas);
        countData();
        drawScale(canvas);
        drawHistogram(canvas);
    }

    /**
     * 画坐标, 做完这一步才能确定接下去的坐标
     * 注意：这个是表面的坐标，android的X轴坐标向右是正方向，Y轴坐标向下是正方向
     */
    private void drawCoordinate(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        lengthX = width - left - right;
        lengthY = height - top - bottom;
        float[] coordinate = {0, 0, lengthX, 0, 0, 0, 0, -lengthY};
        canvas.translate(left, height - bottom);
        mPaint.setColor(Color.BLACK);
        canvas.drawLines(coordinate, mPaint);
    }

    /**
     * 画Y轴的刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas){
        int size = histogramModelList.size();
        if(size == 0)
            return;
        int unitValue = countUnitValue();
        int length = (int)(maxValue / unitValue);
        float scale = 0;
        for(int i=0;i < length;i++) {
            scale += unitValue * 1.0f / maxValue * lengthY;
            canvas.drawLine(0, -scale, DisplayUtil.dip2px(mContext, SCALE_LENGHT), -scale, mPaint);
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(unitValue * (i + 1)), -DisplayUtil.dip2px(mContext, SCALE_LENGHT), -scale + mPaint.getFontSpacing()/3, mPaint);
        }
    }

    /**
     * 画直方图
     * @param canvas
     */
    private void drawHistogram(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        float fontSpacing = mPaint.getFontSpacing();
        for (int i = 0; i < histogramModelList.size(); i++) {
            HistogramModel model = histogramModelList.get(i);
            mPaint.setColor(attrsModel.textColor);
            canvas.drawText(model.name, model.centerX, fontSpacing, mPaint);                       // 画名称
            canvas.drawText(String.valueOf(model.value), model.centerX, model.top - 10, mPaint);   // 画值
            mPaint.setColor(model.color);
            canvas.drawRect(model.left, model.top, model.right, model.bottom, mPaint);              // 画矩形
        }
    }

    /**
     * 直方图数据模型
     */
    class HistogramModel {
        public String name;
        public int value;
        public int color;
        public float top;       //矩形左上角Y坐标
        public float left;      //矩形左上角X坐标
        public float bottom;    //矩形右下角Y坐标
        public float right;     //矩形右下角X坐标
        public float centerX;   //矩形中心X坐标
    }

    /**
     * 属性模型
     */
    class AttrsModel {
        public float textSize;
        public int textColor;
    }
}
