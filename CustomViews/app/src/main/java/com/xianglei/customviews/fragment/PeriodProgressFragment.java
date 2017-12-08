package com.xianglei.customviews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xianglei.customviews.R;
import com.xianglei.customviews.view.PeriodProgress;

/**
 * Created by sheng on 2017/12/8.
 */

public class PeriodProgressFragment extends Fragment {

    private String[] name = {"申购", "配号", "中签缴款", "完成"};
    private String[] time = {"10-16", "10-17", "10-20", "10-21"};
    private Boolean[] isFinish = {true, true ,false, false};

    private PeriodProgress mPeriodProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_period, container, false);
        mPeriodProgress = (PeriodProgress) view.findViewById(R.id.progress);
        mPeriodProgress.setData(name, time, isFinish);
        return view;
    }
}
