package com.xianglei.customviews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xianglei.customviews.R;
import com.xianglei.customviews.activity.SecondActivity;
import com.xianglei.customviews.view.RuleView;
import com.xianglei.customviews.view.RuleView2;

/**
 * Created by sunxianglei on 2017/10/16.
 */

public class RuleViewFragment extends Fragment {

    private int level = 0;  //在哪个activity里

    private Button mRuleBtn;
    private TextView mValueTv;
    private RuleView mRuleView;
    private RuleView2 mRuleView2;

    public RuleViewFragment(){}

    public RuleViewFragment(int level){
        this.level = level;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_rule, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mRuleBtn = (Button)view.findViewById(R.id.btn_rule);
        mValueTv = (TextView)view.findViewById(R.id.tv_value);
        mRuleView = (RuleView) view.findViewById(R.id.view_rule);
        mRuleView2 = (RuleView2) view.findViewById(R.id.view_rule2);
        if(0 == level){
            mRuleBtn.setVisibility(View.VISIBLE);
            mValueTv.setVisibility(View.GONE);
            mRuleView.setVisibility(View.GONE);
            mRuleView2.setVisibility(View.GONE);
        }else if(1 == level){
            mRuleBtn.setVisibility(View.GONE);
            mValueTv.setVisibility(View.VISIBLE);
            mRuleView.setVisibility(View.VISIBLE);
            mRuleView2.setVisibility(View.VISIBLE);
        }

        mRuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra("type", "ruleFragment");
                startActivity(intent);
            }
        });
        mRuleView2.setOnValueChangeListener(new RuleView2.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mValueTv.setText(String.valueOf(value));
            }
        });

        mValueTv.setText(String.valueOf(mRuleView2.getValue()));

    }
}
