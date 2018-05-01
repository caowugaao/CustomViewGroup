package com.gx.morgan.customviewgroup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.gx.morgan.customviewgroup.adapter.ViewPagerAdapter;
import com.gx.morgan.customviewgroup.view.SlidingTab;
import com.gx.morgan.viewgrouplib.CustomViewGroup;
import com.gx.morgan.viewgrouplib.FlowLayout;
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
        LayoutInflater inflater = getLayoutInflater();

        FlowLayout flowLayout= (FlowLayout) inflater.inflate(R.layout.layout_flowlayout,null);
        initFlowLayoutData(flowLayout);


        TextView test = getTextView("TEST");

        List<View> views=new ArrayList<>(2);
        views.add(flowLayout);
        views.add(test);
        List<String> titles=new ArrayList<>(2);
        titles.add(FlowLayout.class.getSimpleName());
        titles.add("test");

        ViewPagerAdapter adapter=new ViewPagerAdapter(views,titles);
        viewPager.setAdapter(adapter);

        SlidingTab tabs=findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setShouldExpand(true);
        tabs.setTextSize((int) ViewUtil.dp2px(this,16));
        tabs.setTextColorResource(android.R.color.black);
        tabs.setSelectedTextColorResource(R.color.colorPrimary);

    }

    private void initFlowLayoutData(FlowLayout flowLayout) {
        for (int i = 0,size=7; i <size ; i++) {
            TextView textView = getTextView(String.valueOf(i + 1));
            if(0==(i+1)%2){
                textView.setVisibility(View.GONE);
            }
            flowLayout.addView(textView);
        }
    }

    private TextView getTextView(String text) {
        TextView textView=new TextView(this);
        int layoutSize = (int) ViewUtil.dp2px(this, 100);
        int margin= (int) ViewUtil.dp2px(this,10);
        FlowLayout.CustomLayoutParams layoutParams=new CustomViewGroup.CustomLayoutParams(layoutSize,layoutSize);
        layoutParams.setMargins(margin,margin,margin,margin);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.color.colorPrimary);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
