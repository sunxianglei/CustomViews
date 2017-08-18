package com.xianglei.customviews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xianglei.customviews.R;
import com.xianglei.customviews.view.PieChart;

/**
 * 展示饼图的页面
 * Created by sunxianglei on 2017/8/16.
 */

public class PieChartFragment extends Fragment{

    public static final String[] PIE_NAMES = {"Lollopop", "KitKat", "Jelly", "SandWich", "Ginger",
            "FroYo", "Marsh"};
    public static final int[] PIE_COLORS = {Color.parseColor("#0a77f4"), Color.parseColor("#000000"), Color.parseColor("#c91313"),
            Color.parseColor("#206b05"), Color.parseColor("#0a77f4"), Color.parseColor("#ffd000"), Color.parseColor("#206b05")};
    public static final int[] PIE_VALUES = {30 ,25, 20, 40, 15, 35, 10};

    private PieChart mPieChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pie_chart, container, false);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieChart.setData(PIE_NAMES, PIE_COLORS, PIE_VALUES);
        mPieChart.setMode(PieChart.INSIDE_MODE);
        return view;
    }

}
