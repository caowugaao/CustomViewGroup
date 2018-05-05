package com.gx.morgan.customviewgroup.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gx.morgan.customviewgroup.R;
import com.gx.morgan.viewgrouplib.FlowLayout;
import com.gx.morgan.viewgrouplib.StateSwitchLayout;
import com.gx.morgan.viewgrouplib.base.CustomViewGroup;
import com.gx.morgan.viewgrouplib.utils.ViewUtil;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/5/1 14:38
 */
public class StateSwitchLayoutFragment extends Fragment {

    private static final String TAG = "StateSwitchLayoutFra";
    private StateSwitchLayout stateSwitchLayout;

    private StateSwitchLayout.OnInitializeListener onInitializeListener = new StateSwitchLayout.OnInitializeListener() {
        @Override
        public void onInitialize(int state, View stateView) {
            String stateDesc = StateSwitchLayout.State.parseStateDesc(state);
            String stateText = getStateText(stateView);
            Log.e(TAG, "onInitialize: state=" + stateDesc + ",stateText=" + stateText);
        }
    };

    private StateSwitchLayout.OnStateViewClickListener onStateViewClickListener = new StateSwitchLayout
            .OnStateViewClickListener() {
        @Override
        public void onStateViewClick(int state, View stateView) {
            String stateDesc = StateSwitchLayout.State.parseStateDesc(state);
            String stateText = getStateText(stateView);
            Log.e(TAG, "onStateViewClick: state=" + stateDesc + ",stateText=" + stateText);
        }
    };
    private StateSwitchLayout.OnSwitchListener onSwitchListener = new StateSwitchLayout.OnSwitchListener() {
        @Override
        public void onSwitch(int oldState, int newState, View oldStateView, View newStateView) {

            String oldStateDesc = StateSwitchLayout.State.parseStateDesc(oldState);
            String newStateDesc = StateSwitchLayout.State.parseStateDesc(newState);
            String oldStateText = getStateText(oldStateView);
            String newStateText = getStateText(newStateView);

            Log.e(TAG, "onSwitch: oldState=" + oldStateDesc + ",newState=" + newStateDesc + ",oldStateText=" +
                    oldStateText + ",newStateText=" + newStateText);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_stateswitchlayout, null);
        return getRootView();
    }

    private View getRootView() {

        Context context = getContext();

        ConstraintLayout root = new ConstraintLayout(context);

        View contentView = getContentView();
        View errorView = getErrorView();
        View loadingView = getLoadingView();
        View emptyView = getEmptyView();
        stateSwitchLayout = new StateSwitchLayout
                .Builder(context)
                .setContentView(contentView)
                .setErrorView(errorView)
                .setLoadingView(loadingView)
                .setEmptyView(emptyView)
                .setOnInitializeListener(onInitializeListener)
                .setOnStateViewClickListener(onStateViewClickListener)
                .setOnSwitchListener(onSwitchListener)
                .build();

        int stateSwitchLayoutId = R.id.stateSwitchLayout;
        this.stateSwitchLayout.setId(stateSwitchLayoutId);

        ConstraintLayout.LayoutParams paramsStateSwitchLayout = new ConstraintLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int parentId = ConstraintLayout.LayoutParams.PARENT_ID;
        paramsStateSwitchLayout.bottomToBottom = parentId;
        paramsStateSwitchLayout.topToTop = parentId;
        paramsStateSwitchLayout.leftToLeft = parentId;
        paramsStateSwitchLayout.rightToRight = parentId;
        this.stateSwitchLayout.setLayoutParams(paramsStateSwitchLayout);

        root.addView(this.stateSwitchLayout);

        Button btnChange = new Button(context);
        btnChange.setId(R.id.btn_change);
        ConstraintLayout.LayoutParams paramsChange = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsChange.topToBottom = stateSwitchLayoutId;
        paramsChange.leftToLeft = stateSwitchLayoutId;
        paramsChange.rightToRight = stateSwitchLayoutId;
        int margin = (int) ViewUtil.dp2px(context, 20);
        paramsChange.setMargins(margin, margin, margin, margin);
        btnChange.setLayoutParams(paramsChange);

        btnChange.setText("改变状态");
        btnChange.setTextColor(Color.BLACK);
        btnChange.setTextSize(20);

        root.addView(btnChange);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        stateSwitchLayout = view.findViewById(R.id.stateSwitchLayout);
//        initStateSwitchView();
        stateSwitchLayout.showContentView();
        Button btnChange = view.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChangeState();

            }
        });
    }

    private void doChangeState() {
        int state = stateSwitchLayout.getShowingState();
        switch (state) {
            case -1:
                stateSwitchLayout.showContentView();
                break;
            case StateSwitchLayout.State.CONTENT:
                stateSwitchLayout.showErrorView();
                break;
            case StateSwitchLayout.State.ERROR:
                stateSwitchLayout.showLoadingView();
                break;
            case StateSwitchLayout.State.LOADING:
                stateSwitchLayout.showEmptyView();
                break;
            case StateSwitchLayout.State.EMPTY:
                stateSwitchLayout.showContentView();
                break;
        }
    }

    private void initStateSwitchView() {

        View contentView = getContentView();
        View errorView = getErrorView();
        View loadingView = getLoadingView();
        View emptyView = getEmptyView();

        stateSwitchLayout.setContentView(contentView);
        stateSwitchLayout.setErrorView(errorView);
        stateSwitchLayout.setLoadingView(loadingView);
        stateSwitchLayout.setEmptyView(emptyView);

        stateSwitchLayout.showContentView();

        stateSwitchLayout.setOnInitializeListener(onInitializeListener);
        stateSwitchLayout.setOnStateViewClickListener(onStateViewClickListener);
        stateSwitchLayout.setOnSwitchListener(onSwitchListener);
    }

    private String getStateText(View stateView) {
        if (null != stateView && stateView instanceof TextView) {
            TextView textView = (TextView) stateView;
            return textView.getText().toString();
        }
        return "NULL";
    }

    private View getContentView() {
        FragmentActivity activity = getActivity();
        int layoutSize = (int) ViewUtil.dp2px(activity, 300);
        return getTextView("content view", layoutSize, R.color.colorPrimary, Color.WHITE);
    }

    private View getErrorView() {
        FragmentActivity activity = getActivity();
        int layoutSize = (int) ViewUtil.dp2px(activity, 150);
        return getTextView("error view", layoutSize, android.R.color.holo_green_dark, Color.WHITE);
    }

    private View getLoadingView() {
        FragmentActivity activity = getActivity();
        int layoutSize = (int) ViewUtil.dp2px(activity, 200);
        return getTextView("loading view", layoutSize, android.R.color.holo_orange_dark, Color.WHITE);
    }

    private View getEmptyView() {
        FragmentActivity activity = getActivity();
        int layoutSize = (int) ViewUtil.dp2px(activity, 100);
        return getTextView("empty view", layoutSize, android.R.color.holo_purple, Color.WHITE);
    }

//    private TextView getTextView(String text) {
//        FragmentActivity activity = getActivity();
//        int layoutSize = (int) ViewUtil.dp2px(activity, 200);
//        return getTextView(text, layoutSize, R.color.colorPrimary, Color.WHITE);
//    }

    private TextView getTextView(String text, int layoutSize, @ColorRes int backgroundResource, @ColorInt int
            textColor) {
        FragmentActivity activity = getActivity();
        TextView textView = new TextView(activity);
        int margin = (int) ViewUtil.dp2px(activity, 10);
        FlowLayout.CustomLayoutParams layoutParams = new CustomViewGroup.CustomLayoutParams(layoutSize, layoutSize);
        layoutParams.setMargins(margin, margin, margin, margin);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(backgroundResource);
        textView.setTextColor(textColor);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}
