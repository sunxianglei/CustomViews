package com.xianglei.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;
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
    private static final float Y_HEIGHT_RATIO = 1.2f;               // Y轴高度相对最高矩形的倍数

    private String[] names;
    private int[] colors;
    private int[] values;
    private AttrsModel attrsModel;
    private CoordinateModel coordinateModel;
    private List<HistogramModel> histogramModelList;
    private boolean isHideScale;//是否隐藏Y轴刻度
    private boolean isHideValue;//是否隐藏矩形上方的值

    private Context mContext;
    private Paint mTextPaint;
    private Paint mRectPaint;
    private Paint mLinePaint;

    public Histogram(Context context) {
        super(context, null);
    }

    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mTextPaint = new Paint();
        mRectPaint = new Paint();
        mLinePaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);

        this.names = Constant.HISTOGRAM_NAMES;
        this.colors = Constant.HISTOGRAM_COLORS;
        this.values = Constant.HISTOGRAM_VALUES;

        coordinateModel = new CoordinateModel();
        attrsModel = new AttrsModel();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.Histogram);
        attrsModel.textSize = ta.getDimension(R.styleable.Histogram_histogram_textSize, DisplayUtil.sp2px(mContext, HISTOGRAM_TEXTSIZE));
        attrsModel.textColor = ta.getColor(R.styleable.Histogram_histogram_textColor, Color.BLACK);
        attrsModel.scaleTextSize = ta.getDimension(R.styleable.Histogram_histogram_scaleTextSize, DisplayUtil.sp2px(mContext, HISTOGRAM_TEXTSIZE));
        attrsModel.scaleTextColor = ta.getColor(R.styleable.Histogram_histogram_scaleTextColor, Color.BLACK);

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
     * 隐藏Y轴坐标的刻度
     * @param isHideScale
     */
    public void hideScale(boolean isHideScale){
        this.isHideScale = isHideScale;
    }

    /**
     * 隐藏矩形上方的值
     * @param isHideValue
     */
    public void hideValue(boolean isHideValue){
        this.isHideValue = isHideValue;
    }

    /**
     * 清除数据
     */
    private void clearData(){
        histogramModelList.clear();
    }

    /**
     * 计算坐标、柱状图数据，并存放到model中
     */
    private void countData() {
        if ((names.length != values.length && values.length != colors.length) || (names.length == 0))
            return;
        float valueWidth = RECT_WIDTH_SCALE * (coordinateModel.lengthX * 1.0f / names.length); // 每个矩形的宽度
        float valueBlankWidth = RECT_BLANK_WIDTH_SCALE * (coordinateModel.lengthX * 1.0f / (names.length + 1)); // 每个矩形之间的距离
        float maxValue = 0;
        for (int i = 0; i < values.length; i++) {
            if (maxValue < values[i]) maxValue = values[i];
        }
        coordinateModel.maxValue = Y_HEIGHT_RATIO * maxValue; // Y轴的最大值
        for (int i = 0; i < names.length; i++) {
            HistogramModel model = new HistogramModel();
            model.left = valueBlankWidth + i * (valueBlankWidth + valueWidth);
            model.top = -coordinateModel.lengthY * (values[i] / coordinateModel.maxValue);
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
     * 根据计算出的平均值拿出第一位数并乘以对应的以10为底的指数
     * @return
     */
    private int countUnitValue(){
        int eachValue = (int) (coordinateModel.maxValue/histogramModelList.size());
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
        drawCoordinate(canvas);
        if (histogramModelList == null) {
            histogramModelList = new ArrayList<>();
            countData();
            startAnimation();
        }
        if (!isHideScale){
            drawScale(canvas);
        }
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
        coordinateModel.lengthX = width - left - right;
        coordinateModel.lengthY = height - top - bottom;
        float[] coordinate = {0, 0, coordinateModel.lengthX, 0, 0, 0, 0, -coordinateModel.lengthY};
        canvas.translate(left, height - bottom);
        mLinePaint.setColor(Color.BLACK);
        canvas.drawLines(coordinate, mLinePaint);
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
        int length = (int)(coordinateModel.maxValue / unitValue);
        float scale = 0;
        Rect mBounds = new Rect();
        mLinePaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(attrsModel.scaleTextColor);
        mTextPaint.setTextSize(attrsModel.scaleTextSize);
        for(int i=0;i < length;i++) {
            scale += unitValue * 1.0f / coordinateModel.maxValue * coordinateModel.lengthY;
            canvas.drawLine(0, -scale, DisplayUtil.dip2px(mContext, SCALE_LENGHT), -scale, mLinePaint);
            String value = String.valueOf(unitValue * (i + 1));
            mTextPaint.getTextBounds(value, 0, value.length(), mBounds);
            canvas.drawText(value, -DisplayUtil.dip2px(mContext, SCALE_LENGHT),
                    -scale + mBounds.height()/2, mTextPaint);
        }
    }

    /**
     * 画直方图
     * @param canvas
     */
    private void drawHistogram(Canvas canvas) {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(attrsModel.textColor);
        mTextPaint.setTextSize(attrsModel.textSize);
        float fontSpacing = mTextPaint.getFontSpacing();
        for (int i = 0; i < histogramModelList.size(); i++) {
            HistogramModel model = histogramModelList.get(i);
            canvas.drawText(model.name, model.centerX, fontSpacing, mTextPaint);                       // 画名称
            if (!isHideValue) {
                canvas.drawText(String.valueOf(model.nowValue), model.centerX, model.nowHeight - 10, mTextPaint);   // 画值
            }
            mRectPaint.setColor(model.color);
            canvas.drawRect(model.left, model.nowHeight, model.right, model.bottom, mRectPaint);              // 画矩形
        }
    }

    /**
     * 开始动画
     */
    private void startAnimation(){
        for (int i = 0; i < histogramModelList.size(); i++) {
            final HistogramModel model = histogramModelList.get(i);
            ValueAnimator animator = ValueAnimator.ofFloat(0, model.top);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    model.nowHeight = (float) animation.getAnimatedValue();
                    model.nowValue = (int)(model.nowHeight / model.top * model.value);
                    postInvalidate();
                }
            });
            animator.start();
        }
    }

    /**
     * 最好停止动画，避免发生内存泄露
     */
    public void stopAnimation() {
        this.clearAnimation();
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
        public float nowHeight; //动画的实时高度
        public int nowValue;  //实时的值
    }

    /**
     * 属性模型
     */
    class AttrsModel {
        public float textSize;
        public int textColor;
        public float scaleTextSize; //刻度值文字大小
        public int scaleTextColor;  //刻度值文字颜色
    }

    /**
     * 坐标数据模型
     */
    class CoordinateModel{
        public float lengthX;      //X轴的长度
        public float lengthY;      //Y轴的长度
        public float maxValue;     //对应Y轴最高点的值
    }
}
