package com.xianglei.customviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.xianglei.customviews.utils.DisplayUtil;

/**
 * 可悬浮拖拽按钮
 * Created by sheng on 2017/8/9.
 */

public class FloatView extends ImageButton {

    private int screenWidth;
    private int screenHeight;
    private int lastX;
    private int lastY;
    private int locationY;
    private boolean isDrag;
    private boolean isFirst = true;

    private Paint mPaint;

    public FloatView(Context context) {
        super(context);
        init();
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        screenWidth = DisplayUtil.getMobileWidth(getContext());
        screenHeight = DisplayUtil.getMobileHeight(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (isFirst) {
//            int[] location = new int[2];
//            this.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
//            locationY = location[1];
//            isFirst = false;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件的问题
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                y = y < 0 ? 0 : y > screenHeight - getHeight() - locationY ? screenHeight - getHeight() - locationY : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }
}
