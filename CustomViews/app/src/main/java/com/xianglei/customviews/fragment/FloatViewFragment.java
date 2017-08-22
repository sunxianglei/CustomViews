package com.xianglei.customviews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xianglei.customviews.R;
import com.xianglei.customviews.view.FloatView;

/**
 * 展示悬浮按钮的页面
 * Created by sunxianglei on 2017/8/16.
 */

public class FloatViewFragment extends Fragment{

    private FloatView mFloatView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_float, container, false);
        mFloatView = (FloatView)view.findViewById(R.id.view_float);
        mFloatView.setClickable(true);
        return view;
    }

}
