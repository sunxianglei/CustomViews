package com.xianglei.customviews.view;

import android.content.Context;
import android.graphics.Canvas;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * 可悬浮拖拽按钮
 * 该按钮支持在父控件内拖动，如需全屏拖动只需把父控件设成最大，当然无法拖到标题那边
 * 整屏拖动暂不支持，后续可能会修改
 * Created by sunxianglei on 2017/8/9.
 */

public class FloatView extends ImageButton {

    private int lastX;
    private int lastY;
    private boolean isDrag;
    private int parentTop;
    private int parentLeft;
    private int parentBottom;
    private int parentRight;

    public FloatView(Context context) {
        super(context);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        parentTop = ((ViewGroup)this.getParent()).getTop();
        parentLeft = ((ViewGroup)this.getParent()).getLeft();
        parentBottom = ((ViewGroup)this.getParent()).getBottom();
        parentRight = ((ViewGroup)this.getParent()).getRight();
        Log.v("FloatView_sun", "parentTop = " + parentTop + "parentLeft = " + parentLeft +
                "parentBottom = " + parentBottom + "parentRight = " + parentRight);
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
                float x = ((ViewGroup)this.getParent()).getX() + dx;
                float y = ((ViewGroup)this.getParent()).getY() + dy;
                //检测是否到达父控件边缘
                x = x < parentLeft ? parentLeft : x > parentRight - getWidth() ? parentRight - getWidth() : x;
                y = y < parentTop ? parentTop : y > parentBottom - getHeight() ? parentBottom - getHeight() : y;
                ((ViewGroup)this.getParent()).setX(x);
                ((ViewGroup)this.getParent()).setY(y);
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
