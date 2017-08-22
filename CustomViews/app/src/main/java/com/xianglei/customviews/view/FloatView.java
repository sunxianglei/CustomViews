package com.xianglei.customviews.view;

import android.content.Context;

import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.xianglei.customviews.utils.DisplayUtil;

/**
 * 可悬浮拖拽按钮
 * 该按钮支持在父控件内拖动，如需全屏拖动只需把父控件设成最大，当然无法拖到标题那边
 * 整屏拖动暂不支持，后续可能会修改
 * Created by sunxianglei on 2017/8/9.
 */

public class FloatView extends FloatingActionButton {

    private int lastX;
    private int lastY;
    private boolean isDrag;
    private int parentBottom;
    private int screenWidth;

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

    private void init(){
        screenWidth = DisplayUtil.getMobileWidth(getContext());
        setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //以父控件的底部作为边缘，因为根据系统设置的标题等原因Android的坐标轴不是在屏幕的左上角，而是在标题左下方，所以以屏幕高度为范围会超出边界
        parentBottom = ((ViewGroup)this.getParent()).getBottom();
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
                float x = this.getX() + dx;
                float y = this.getY() + dy;
                //检测是否到达父控件边缘
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                y = y < 0 ? 0 : y > parentBottom - getHeight() ? parentBottom - getHeight() : y;
                Log.v("FloatView_sun", "screenWidth = " + screenWidth + "parentBottom = " + parentBottom +
                        "x = " + x + "y = " + y + "getX() = " + getX() + "dx = " + dx);
                this.setX(x);
                this.setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if(isDrag){
                    int centerX = screenWidth / 2;
                    if (centerX > getX()) { // 吸附到左边
                        ObjectAnimator animator = ObjectAnimator.ofFloat(FloatView.this, "translationX", getX(), 0);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setDuration(500);
                        animator.start();
                    } else { // 吸附到右边
                        ObjectAnimator animator = ObjectAnimator.ofFloat(FloatView.this, "translationX", getX(), screenWidth - getWidth());
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setDuration(500);
                        animator.start();
                    }
                }
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }
}
