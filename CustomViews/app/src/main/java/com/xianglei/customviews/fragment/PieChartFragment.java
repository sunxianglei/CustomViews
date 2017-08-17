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

    public static final String[] PIE_NAMES = {"Lollopop", "KitKat", "Jelly Bean", "Ice Cream SandWich", "GingerBread",
            "FroYo", "Marshmallow"};
    public static final int[] PIE_COLORS = {Color.RED, Color.BLUE, Color.GREEN,
            Color.GRAY, Color.BLACK, Color.YELLOW, Color.CYAN};
    public static final int[] PIE_VALUES = {30 ,25, 20, 5, 3, 2, 15};

    private PieChart mPieChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pie_chart, container, false);
        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieChart.setData(PIE_NAMES, PIE_COLORS, null);
        return view;
    }

}
