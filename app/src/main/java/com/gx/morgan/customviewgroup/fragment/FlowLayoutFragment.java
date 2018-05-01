package com.gx.morgan.customviewgroup.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FlowLayout flowLayout=view.findViewById(R.id.flowLayout);
        initFlowLayoutData(flowLayout);
    }

    private TextView getTextView(String text) {
        FragmentActivity activity = getActivity();
        int layoutSize = (int) ViewUtil.dp2px(activity, 50);
        return getTextView(text,layoutSize,R.color.colorPrimary);
    }
    private TextView getTextView(String text, int layoutSize, @ColorRes int colorRes) {
        FragmentActivity activity = getActivity();
        TextView textView=new TextView(activity);
        int margin= (int) ViewUtil.dp2px(activity,10);
        FlowLayout.CustomLayoutParams layoutParams=new CustomViewGroup.CustomLayoutParams(layoutSize,layoutSize);
        layoutParams.setMargins(margin,margin,margin,margin);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(colorRes);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }



    private void initFlowLayoutData(FlowLayout flowLayout) {

        FragmentActivity activity = getActivity();

        for (int i = 0,size=9; i <size ; i++) {
            TextView textView=null;
            if(0==i){
                textView= getTextView(String.valueOf(i + 1));
                textView.setVisibility(View.GONE);
            }
            else {
                if(((i+1)%3==0)){
                    int layoutSize = (int) ViewUtil.dp2px(activity, 80);
                    textView=getTextView(String.valueOf(i + 1),layoutSize,android.R.color.holo_green_dark);
                }
                else if((i+1)%2==0){
                    int layoutSize = (int) ViewUtil.dp2px(activity, 100);
                    textView=getTextView(String.valueOf(i + 1),layoutSize,android.R.color.holo_orange_dark);
                }
                else{
                    int layoutSize = (int) ViewUtil.dp2px(activity, 120);
                    textView=getTextView(String.valueOf(i + 1),layoutSize,android.R.color.holo_blue_dark);
                }
            }

            flowLayout.addView(textView);
        }
    }
}
