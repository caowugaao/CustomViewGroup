package com.gx.morgan.viewgrouplib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/4/30 16:31
 */
public abstract class CustomViewGroup extends ViewGroup {

    private static final String TAG = "CustomViewGroup";

    protected SparseArray<ChildInfo> childInfos;

    public CustomViewGroup(Context context) {
        super(context);
        init();
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        childInfos = new SparseArray<>();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != childInfos) {
            childInfos.clear();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildrenSize(widthMeasureSpec, heightMeasureSpec);

        int[] totalSizeArr = onMeasureChildrenSize(childInfos, widthSize, heightSize, this);


        if (2 != totalSizeArr.length) {
            throw new IllegalArgumentException("Method onMeasureChildrenSize result'length must be 2");
        }

        Log.e(TAG, "onMeasure: measure width=" + totalSizeArr[0] + ",height=" + totalSizeArr[1]);

        int totalWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : totalSizeArr[0];
        int totalHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : totalSizeArr[1];

        setMeasuredDimension(totalWidth, totalHeight);
    }

    protected abstract int[] onMeasureChildrenSize(SparseArray<ChildInfo> childInfos, int exactlyWidthSize, int
            exactlyHeightSize, ViewGroup parent);

    private void measureChildrenSize(int widthMeasureSpec, int heightMeasureSpec) {

        childInfos.clear();

        int childWidth;
        int childHeight;
        int childVisibility;
        CustomLayoutParams childLayoutParams;
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View child = getChildAt(i);
//            if (child.getVisibility() == View.GONE) {
//                onChildVisibilityGone(child, i, this);
//                continue;
//            }
            childVisibility = child.getVisibility();
            if (View.GONE != childVisibility) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            LayoutParams layoutParams = child.getLayoutParams();
            if (!(layoutParams instanceof CustomLayoutParams)) {
                new IllegalArgumentException("child's layoutParams must be CustomLayoutParams or Subclass of " +
                        "CustomLayoutParams. layoutParams=" + layoutParams);
            }
            childLayoutParams = (CustomLayoutParams) layoutParams;
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            childWidth=0==childWidth?childLayoutParams.width:childWidth;
            childHeight=0==childHeight?childLayoutParams.height:childHeight;

            ChildInfo childInfo = new ChildInfo(childLayoutParams, childWidth, childHeight, i, childVisibility, child);
            childInfos.put(i, childInfo);

            if (childVisibility == View.GONE) {
                onChildVisibilityGone(child, i, this);
                continue;
            }

            onChildMeasureWidth(this, i, childWidth, childLayoutParams);
            onChildMeasureHeight(this, i, childHeight, childLayoutParams);
        }
    }


    protected void onChildMeasureHeight(ViewGroup parent, int index, int childHeight, CustomLayoutParams
            childLayoutParams) {

    }

    protected void onChildMeasureWidth(ViewGroup parent, int index, int childWidth, CustomLayoutParams
            childLayoutParams) {

    }

    protected void onChildVisibilityGone(View child, int index, ViewGroup parent) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        onChildLayout(childInfos, changed, l, t, r, b);
    }


    /**
     * 调用该方法来确定子View在ViewGroup中的位置
     *
     * @param childInfos 一定非空
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    protected abstract void onChildLayout(SparseArray<ChildInfo> childInfos, boolean changed, int l, int t, int r,
                                          int b);


    public static class ChildInfo {
        public CustomLayoutParams layoutParams;
        public int measuredWidth;
        public int measuredHeight;
        public int index;
        public int visible;
        public View view;

        public ChildInfo() {
        }

        public ChildInfo(CustomLayoutParams layoutParams, int measuredWidth, int measuredHeight, int index, int
                visible, View view) {
            this.layoutParams = layoutParams;
            this.measuredWidth = measuredWidth;
            this.measuredHeight = measuredHeight;
            this.index = index;
            this.visible = visible;
            this.view = view;
        }
    }

    public static class CustomLayoutParams extends ViewGroup.MarginLayoutParams {

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CustomLayoutParams(int width, int height) {
            super(width, height);
        }

        public CustomLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(), attrs);
    }
}
