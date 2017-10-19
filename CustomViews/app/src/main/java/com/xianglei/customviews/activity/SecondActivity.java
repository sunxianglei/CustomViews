package com.xianglei.customviews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.xianglei.customviews.R;
import com.xianglei.customviews.fragment.RuleViewFragment;

/**
 * 二级页面
 * Created by sunxianglei on 2017/10/16.
 */

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setFragment();
    }
    private void setFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = null;
        String type = getIntent().getExtras().getString("type");
        if("ruleFragment".equals(type)){
            fragment = new RuleViewFragment(1);
        }
        if(fragment != null) {
            transaction.replace(R.id.content, fragment);
            transaction.commit();
        }
    }
}
