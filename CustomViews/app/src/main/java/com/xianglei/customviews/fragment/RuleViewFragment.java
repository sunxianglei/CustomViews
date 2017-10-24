package com.xianglei.customviews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xianglei.customviews.R;
import com.xianglei.customviews.view.RuleView;

/**
 * Created by sunxianglei on 2017/10/16.
 */

public class RuleViewFragment extends Fragment {

    private TextView mValueTv;
    private RuleView mRuleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_rule, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mValueTv = (TextView)view.findViewById(R.id.tv_value);
        mRuleView = (RuleView) view.findViewById(R.id.view_rule);
        mRuleView.setOnValueChangeListener(new RuleView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mValueTv.setText(String.valueOf(value));
            }
        });

        mValueTv.setText(String.valueOf(mRuleView.getValue()));

    }
}
