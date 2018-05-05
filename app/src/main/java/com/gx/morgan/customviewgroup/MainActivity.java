package com.gx.morgan.customviewgroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.gx.morgan.customviewgroup.adapter.CommonFragmentPagerAdapter;
import com.gx.morgan.customviewgroup.fragment.FlowLayoutFragment;
import com.gx.morgan.customviewgroup.fragment.StateSwitchLayoutFragment;
import com.gx.morgan.customviewgroup.view.SlidingTab;
import com.gx.morgan.viewgrouplib.FlowLayout;
import com.gx.morgan.viewgrouplib.StateSwitchLayout;
import com.gx.morgan.viewgrouplib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initViews();

    }

    private void initViews() {

        ViewPager viewPager=findViewById(R.id.viewPager);
        List<Fragment> fragments=new ArrayList<>(2);
        fragments.add(new FlowLayoutFragment());
        fragments.add(new StateSwitchLayoutFragment());

        List<String> titles=new ArrayList<>(2);
        titles.add(FlowLayout.class.getSimpleName());
        titles.add(StateSwitchLayout.class.getSimpleName());

        CommonFragmentPagerAdapter adapter=new CommonFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);

        SlidingTab tabs=findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setShouldExpand(true);
        tabs.setTextSize((int) ViewUtil.dp2px(this,16));
        tabs.setTextColorResource(android.R.color.black);
        tabs.setSelectedTextColorResource(R.color.colorPrimary);

    }



}
