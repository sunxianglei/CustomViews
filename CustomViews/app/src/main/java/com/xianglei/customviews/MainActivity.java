package com.xianglei.customviews;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xianglei.customviews.fragment.FloatViewFragment;
import com.xianglei.customviews.fragment.HistogramFragment;
import com.xianglei.customviews.fragment.PieChartFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPageModel();
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return pageModel.fragment;
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    private void initPageModel(){
        pageModels.add(new PageModel( R.string.histogram, new HistogramFragment()));
        pageModels.add(new PageModel( R.string.pie_chart, new PieChartFragment()));
        pageModels.add(new PageModel( R.string.float_view, new FloatViewFragment()));
    }

    private class PageModel {
        @StringRes
        int titleRes;
        Fragment fragment;

        PageModel(@StringRes int titleRes, Fragment fragment) {
            this.titleRes = titleRes;
            this.fragment = fragment;
        }
    }
}
