package com.gx.morgan.viewgrouplib;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gx.morgan.viewgrouplib.base.CustomViewGroup;
import com.gx.morgan.viewgrouplib.utils.CommonUtil;
import com.gx.morgan.viewgrouplib.utils.ViewUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * description：多状态转换器,loading/fail/empty/content
 * <br>author：caowugao
 * <br>time：2018/5/5 15:44
 */
public class StateSwitchLayout extends CustomViewGroup {

    private SparseArray<View> views;
    private SparseBooleanArray isInitializes;
    private SparseIntArray viewIds;
    private OnInitializeListener onInitializeListener;
    private OnStateViewClickListener onStateViewClickListener;
    private OnSwitchListener onSwitchListener;
    private boolean switchAnimate = true;
    private int currentState = -1;
    private int oldState = -1;
    private LayoutInflater inflater;
    private OnCustomViewClickListener customViewClickListener;
    private static final int CLICK_TAG = R.id.state_switch_click_id;

    private int showIndex = 0;

    public static class State {
        private State() {
        }

        public static final int CONTENT = 1000;
        public static final int ERROR = 1002;
        public static final int LOADING = 1001;
        public static final int EMPTY = 1003;

        public static String parseStateDesc(@StateValue int state) {
            if (state == CONTENT) {
                return "content";
            } else if (state == ERROR) {
                return "error";
            } else if (state == LOADING) {
                return "loading";
            } else if (state == EMPTY) {
                return "empty";
            }
            return "unkown state";
        }
    }

    private static class OnCustomViewClickListener implements View.OnClickListener {

        private WeakReference<StateSwitchLayout> switchLayoutWeakReference;

        private OnCustomViewClickListener(StateSwitchLayout layout) {
            switchLayoutWeakReference = new WeakReference<>(layout);
        }

        @Override
        public void onClick(View v) {
            StateSwitchLayout stateSwitchLayout = switchLayoutWeakReference.get();
            if (null == stateSwitchLayout) {
                return;
            }
            stateSwitchLayout.handleClick(v);
        }
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({State.CONTENT, State.ERROR, State.LOADING, State.EMPTY})
    public @interface StateValue {
    }

    public interface OnInitializeListener {
        void onInitialize(int state, View stateView);
    }

    public interface OnStateViewClickListener {
        void onStateViewClick(int state, View stateView);
    }

    public interface OnSwitchListener {
        void onSwitch(int oldState, int newState, View oldStateView, View newStateView);
    }

    public static class Builder {
        private Context context;
        private AttributeSet attrs;
        private int defStyleAttr;
        private int defStyleRes;
        private SparseArray<View> views;
        private SparseIntArray viewIds;
        private OnInitializeListener onInitializeListener;
        private OnStateViewClickListener onStateViewClickListener;
        private OnSwitchListener onSwitchListener;
        private boolean switchAnimate;

        public Builder(@NonNull Context context) {
            CommonUtil.checkNull(context, "context");
            this.context = context;
            views = new SparseArray<>();
            viewIds = new SparseIntArray();
        }

        public Builder(@NonNull Context context, AttributeSet attrs) {
            this(context);
            this.attrs = attrs;
        }

        public Builder(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
            this(context, attrs);
            this.defStyleAttr = defStyleAttr;
        }

        public Builder(@NonNull Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            this(context, attrs, defStyleAttr);
            this.defStyleRes = defStyleRes;
        }

        public Builder setContentView(@NonNull View contentView) {
            CommonUtil.checkNull(contentView, "contentView");
            views.put(State.CONTENT, contentView);
            return this;
        }

        public Builder setErrorView(@NonNull View errorView) {
            CommonUtil.checkNull(errorView, "errorView");
            views.put(State.ERROR, errorView);
            return this;
        }

        public Builder setLoadingView(@NonNull View loadingView) {
            CommonUtil.checkNull(loadingView, "loadingView");
            views.put(State.LOADING, loadingView);
            return this;
        }

        public Builder setEmptyView(@NonNull View emptyView) {
            CommonUtil.checkNull(emptyView, "emptyView");
            views.put(State.EMPTY, emptyView);
            return this;
        }

        public Builder setContentLayoutRes(@LayoutRes int contentLayoutRes) {
            viewIds.put(State.CONTENT, contentLayoutRes);
            return this;
        }

        public Builder setErrorLayoutRes(@LayoutRes int errorLayoutRes) {
            viewIds.put(State.ERROR, errorLayoutRes);
            return this;
        }

        public Builder setLoadingLayoutRes(@LayoutRes int loadingLayoutRes) {
            viewIds.put(State.LOADING, loadingLayoutRes);
            return this;
        }

        public Builder setEmptyLayoutRes(@LayoutRes int emptyLayoutRes) {
            viewIds.put(State.EMPTY, emptyLayoutRes);
            return this;
        }

        public Builder setOnInitializeListener(OnInitializeListener listener) {
            onInitializeListener = listener;
            return this;
        }

        public Builder setOnStateViewClickListener(OnStateViewClickListener listener) {
            this.onStateViewClickListener = listener;
            return this;
        }

        public Builder setOnSwitchListener(OnSwitchListener listener) {
            this.onSwitchListener = listener;
            return this;
        }

        public Builder setSwitchAnimate(boolean switchAnimate) {
            this.switchAnimate = switchAnimate;
            return this;
        }

        public StateSwitchLayout build() {
            return new StateSwitchLayout(this);
        }
    }

    public StateSwitchLayout(Builder builder) {
        this(builder.context, builder.attrs, builder.defStyleAttr, builder.defStyleRes);
        views = builder.views;
        viewIds = builder.viewIds;
        onInitializeListener = builder.onInitializeListener;
        onStateViewClickListener = builder.onStateViewClickListener;
        onSwitchListener = builder.onSwitchListener;
        switchAnimate = builder.switchAnimate;
    }

    public StateSwitchLayout(Context context) {
        super(context);
        initMine(context, null);
    }

    public StateSwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMine(context, attrs);
    }

    public StateSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMine(context, attrs);
    }

    public StateSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMine(context, attrs);
    }

    private void initMine(Context context, AttributeSet attrs) {
        inflater = LayoutInflater.from(context);
        isInitializes = new SparseBooleanArray();
        customViewClickListener = new OnCustomViewClickListener(this);
    }

    public void setContentView(@NonNull View contentView) {
        CommonUtil.checkNull(contentView, "contentView");
        putStateView(State.CONTENT, contentView);
    }

    public void setErrorView(@NonNull View errorView) {
        CommonUtil.checkNull(errorView, "errorView");
        putStateView(State.ERROR, errorView);
    }

    public void setLoadingView(@NonNull View loadingView) {
        CommonUtil.checkNull(loadingView, "loadingView");
        putStateView(State.LOADING, loadingView);
    }

    public void setEmptyView(@NonNull View emptyView) {
        CommonUtil.checkNull(emptyView, "emptyView");
        putStateView(State.EMPTY, emptyView);
    }

    private void putStateView(int key, View view) {
        if (null != views) {
            View oldStateView = views.get(key);
            if (oldStateView != view) {
                if (null != oldStateView) {
                    removeView(oldStateView);
                }
                views.put(key, view);
                isInitializes.put(key, false);
            }
        } else {
            views = new SparseArray<>();
            views.put(key, view);
            isInitializes.put(key, false);
        }
    }


    public void setOnInitializeListener(OnInitializeListener listener) {
        onInitializeListener = listener;
    }

    public void setOnStateViewClickListener(OnStateViewClickListener listener) {
        onStateViewClickListener = listener;
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        onSwitchListener = listener;
    }

    public void setSwitchAnimate(boolean switchAnimate) {
        this.switchAnimate = switchAnimate;
    }

    public boolean isSwitchAnimate() {
        return switchAnimate;
    }

    public void showContentView() {
        show(State.CONTENT);
    }

    public void showErrorView() {
        show(State.ERROR);
    }

    public void showLoadingView() {
        show(State.LOADING);
    }

    public void showEmptyView() {
        show(State.EMPTY);
    }

    public int getShowingState() {
        return currentState;
    }

    private void show(@StateValue int state) {
        if (null == views && null == viewIds) {
            throw new RuntimeException("You must set at least one kind of state view before show");
        }
        if (currentState == state) {
            return;
        }

        if (null != views) {
            View view = views.get(state);
            if (null == view) {
                if (null != viewIds) {
                    int layoutRes = viewIds.get(state);
                    view = inflater.inflate(layoutRes, null);
                    if (null != view) {
                        views.put(state, view);
                    }
                }
            }
            if (null == view) {
                throw new RuntimeException("You must set the " + State.parseStateDesc(state) + " View before show");
            }

            realShow(state, view);
            return;
        }

        //执行到这里，viewIds一定不为空
        int layoutRes = viewIds.get(state);
        View view = inflater.inflate(layoutRes, null);
        if (null == view) {
            throw new RuntimeException("You must set the " + State.parseStateDesc(state) + " View before show");
        }
        views.put(state, view);

        realShow(state, view);
    }

    private void realShow(@StateValue int willShowState, @NonNull View willShowView) {
        boolean isInitialize = isInitializes.get(willShowState);
        if (!isInitialize) {
            addView(willShowView);
            if (null != onInitializeListener) {
                onInitializeListener.onInitialize(willShowState, willShowView);
            }
            isInitializes.put(willShowState, true);
            willShowView.setTag(CLICK_TAG, willShowState);
            willShowView.setOnClickListener(customViewClickListener);
        }

        oldState = currentState;
        View oldStateView = views.get(oldState);
        ViewUtil.setVisible(oldStateView, View.GONE);

        currentState = willShowState;
        ViewUtil.setVisible(willShowView, View.VISIBLE);

        if (null != onSwitchListener) {
            onSwitchListener.onSwitch(oldState, currentState, oldStateView, willShowView);
        }
        requestLayout();
    }

    private void handleClick(View v) {
        if (null == onStateViewClickListener) {
            return;
        }

        Object stateObj = v.getTag(CLICK_TAG);
        if (null == stateObj) {
            return;
        }
        int state = (int) stateObj;
        View view = views.get(state);
        onStateViewClickListener.onStateViewClick(state, view);
    }


    @Override
    protected void onDetachedFromWindow() {
        if (null != views) {
            views.clear();
        }
        if (null != viewIds) {
            viewIds.clear();
        }
        if (null != isInitializes) {
            isInitializes.clear();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected int[] onMeasureChildrenSize(SparseArray<ChildInfo> childInfos, int exactlyWidthSize, int
            exactlyHeightSize, ViewGroup parent) {
        int width = 0;
        int height = 0;
        for (int i = 0, childSize = childInfos.size(); i < childSize; i++) {
            ChildInfo childInfo = childInfos.get(i);
            if (View.GONE == childInfo.visible) {
                continue;
            }
            width = childInfo.measuredWidth;
            height = childInfo.measuredHeight;
            if (0 != width && 0 != height) {
                width = getChildHorizontalSpaceInParent(childInfo);
                height = getChildVerticalSpaceInParent(childInfo);
                showIndex = i;
                break;
            }
        }
        return new int[]{width, height};
    }

    @Override
    protected void onChildLayout(SparseArray<ChildInfo> childInfos, boolean changed, int l, int t, int r, int b) {
        if(showIndex<0){
           throw new RuntimeException("ShowIndex cannot be less than 0.showIndex is "+showIndex);
        }
        if(showIndex>=childInfos.size()){
            throw new RuntimeException("ShowIndex must less than childInfos’size.ChildInfos’size is "+childInfos.size()+",however showIndex is "+showIndex);
        }

        int contentWidth = getWidth()-getPaddingLeft()-getPaddingRight();
        int contentHeight = getHeight()-getPaddingTop()-getPaddingBottom();

        ChildInfo childInfo = childInfos.get(showIndex);
        CustomLayoutParams layoutParams = childInfo.layoutParams;
        int childHorizontalSpaceInParent = getChildHorizontalSpaceInParent(childInfo);
        int childVerticalSpaceInParent = getChildVerticalSpaceInParent(childInfo);

        int childLeft = (contentWidth -childHorizontalSpaceInParent)/2 +layoutParams.rightMargin;
        int childTop = (contentHeight-childVerticalSpaceInParent)/2+layoutParams.topMargin;
        int childRight = childLeft+childInfo.measuredWidth;
        int childBottom = childTop+childInfo.measuredHeight;
        childInfo.view.layout(childLeft, childTop, childRight, childBottom);

    }
}
