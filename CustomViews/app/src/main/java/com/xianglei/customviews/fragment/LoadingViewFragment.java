package com.xianglei.customviews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.xianglei.customviews.R;
import com.xianglei.customviews.view.LoadingView;

/**
 * Created by sunxianglei on 2017/10/13.
 */

public class LoadingViewFragment extends Fragment {

    private LoadingView mLoadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_loading, container, false);
        mLoadingView = (LoadingView)view.findViewById(R.id.view_loading);
        startAnimation();
        return view;
    }

    private void startAnimation(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLoadingView,"progress", 0, 100);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(2000);
        animator.start();
    }
}
