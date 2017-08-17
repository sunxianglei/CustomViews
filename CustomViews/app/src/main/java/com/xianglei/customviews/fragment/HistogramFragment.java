package com.xianglei.customviews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xianglei.customviews.R;
import com.xianglei.customviews.view.Histogram;

/**
 * 展示直方图的页面
 * Created by sunxianglei on 2017/8/16.
 */

public class HistogramFragment extends Fragment{

    public static final String[] HISTOGRAM_NAMES = {"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
    public static final int[] HISTOGRAM_COLORS = {Color.BLUE, Color.GREEN, Color.RED,
            Color.BLACK, Color.GREEN, Color.RED, Color.BLUE};
    public static final int[] HISTOGRAM_VALUES = {10 ,100, 100, 1000, 3000, 2300, 800};

    private Histogram mHistogram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_histogram, container, false);
        mHistogram = (Histogram) view.findViewById(R.id.histogram);
        mHistogram.setData(HISTOGRAM_NAMES, HISTOGRAM_COLORS, HISTOGRAM_VALUES);
        return view;
    }

}
