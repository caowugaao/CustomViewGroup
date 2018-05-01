package com.gx.morgan.customviewgroup.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gx.morgan.customviewgroup.R;
import com.gx.morgan.viewgrouplib.CustomViewGroup;
import com.gx.morgan.viewgrouplib.FlowLayout;
import com.gx.morgan.viewgrouplib.utils.ViewUtil;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/5/1 14:33
 */
public class FlowLayoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flowlayout, null);
        FlowLayout flowLayout=view.findViewById(R.id.flowLayout);
        initFlowLayoutData(flowLayout);
        return view;
    }


    private TextView getTextView(String text) {
        FragmentActivity activity = getActivity();
        TextView textView=new TextView(activity);
        int layoutSize = (int) ViewUtil.dp2px(activity, 100);
        int margin= (int) ViewUtil.dp2px(activity,10);
        FlowLayout.CustomLayoutParams layoutParams=new CustomViewGroup.CustomLayoutParams(layoutSize,layoutSize);
        layoutParams.setMargins(margin,margin,margin,margin);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.color.colorPrimary);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
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
}
