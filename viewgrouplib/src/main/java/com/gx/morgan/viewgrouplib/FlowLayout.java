package com.gx.morgan.viewgrouplib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * description：浮动布局
 * <br>author：caowugao
 * <br>time：2018/4/30 16:30
 */
public class FlowLayout extends CustomViewGroup {

    private static final String TAG = "FlowLayout";
    private List<List<ChildInfo>> totalViews;
    private List<ChildInfo> rowViews;
    private List<Integer> lineHeights;

    public FlowLayout(Context context) {
        super(context);
        init(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected int[] onMeasureChildrenSize(SparseArray<ChildInfo> childInfos, int exactlyWidthSize, int
            exactlyHeightSize, ViewGroup parent) {


        int maxWidth = exactlyWidthSize - getPaddingLeft() - getPaddingRight();
        int maxHeight = exactlyHeightSize - getPaddingTop() - getPaddingBottom();

        Log.e(TAG, "onMeasureChildrenSize: maxWidth="+maxWidth+",maxHeight="+maxHeight );

        int lineWidth = 0;
        int lineHeight = 0;

        int totalWidth = 0;
        int totalHeight = 0;

        int childWidthWithMargin = 0;
        int childHeightWithMargin = 0;

        CustomLayoutParams layoutParams;

        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            ChildInfo childInfo = childInfos.get(i);
            if (null == childInfo || View.GONE == childInfo.visible) {
                if (childCount - 1 == i) {//加上最后一个
                    totalWidth = Math.max(lineWidth, totalWidth);
                    totalHeight = Math.max(lineHeight, totalHeight);
                }
                continue;
            }
            layoutParams = childInfo.layoutParams;
            childWidthWithMargin = childInfo.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            childHeightWithMargin = childInfo.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;


            if (lineHeight + childHeightWithMargin > maxHeight) {//超过最大高度
                totalHeight = Math.max(lineHeight, totalHeight);
//                Log.e(TAG, "onMeasureChildrenSize: 超过最大高度 i="+i );
            } else {
                if (lineWidth + childWidthWithMargin > maxWidth) {//超过最大宽度
                    totalWidth = Math.max(lineWidth, totalWidth);
//                    Log.e(TAG, "onMeasureChildrenSize: 超过最大宽度 i="+i );
                    lineWidth=childWidthWithMargin;
                    lineHeight += childHeightWithMargin;
                } else {
                    lineWidth += childWidthWithMargin;
//                    Log.e(TAG, "onMeasureChildrenSize:  i="+i );
                    if(0==lineHeight){
                        lineHeight=childHeightWithMargin;
                    }
                }

            }

            //加上最后一个
            if (childCount - 1 == i) {
                totalWidth = Math.max(lineWidth, totalWidth);
                totalHeight = Math.max(lineHeight, totalHeight);
            }

        }

        Log.e(TAG, "onMeasureChildrenSize: totalWidth="+totalWidth+",totalHeight="+totalHeight +",lineWidth="+lineWidth+",lineHeight="+lineHeight);

        return new int[]{totalWidth, totalHeight};
    }

    private void init(Context context, AttributeSet attrs) {
        totalViews = new ArrayList<>();
        rowViews = new ArrayList<>();
        lineHeights = new ArrayList<>();
    }

    private static <T>void clearList(List<T> list){
        if(null!=list){
            list.clear();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        clearList(totalViews);
        clearList(rowViews);
        clearList(lineHeights);

        super.onDetachedFromWindow();

    }

    @Override
    protected void onChildLayout(SparseArray<ChildInfo> childInfos, boolean changed, int l, int t, int r, int b) {


        Log.e(TAG, "onChildLayout: childInfos‘size="+childInfos.size() );
        CustomLayoutParams layoutParams;
        int width = getWidth();
        int height = getHeight();

        int contentWidth = width - getPaddingLeft() - getPaddingRight();

        int childWidthWithMargin = 0;
        int childHeightWithMargin = 0;
        int lineWidth = 0;

        totalViews.clear();
        rowViews.clear();
        lineHeights.clear();


        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            ChildInfo childInfo = childInfos.get(i);
            if (null == childInfo || View.GONE == childInfo.visible) {
                if(childCount-1==i){
                    totalViews.add(rowViews);
                    lineHeights.add(childHeightWithMargin);
                }
                continue;
            }
            layoutParams = childInfo.layoutParams;
            childWidthWithMargin = childInfo.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            childHeightWithMargin=childInfo.measuredHeight+layoutParams.topMargin+layoutParams.bottomMargin;


            if (lineWidth + childWidthWithMargin > contentWidth) {//大于最大内容宽度

                totalViews.add(rowViews);

                rowViews = new ArrayList<>();
                rowViews.add(childInfo);
                lineWidth = childWidthWithMargin;

                lineHeights.add(childHeightWithMargin);

            } else {

                rowViews.add(childInfo);
                lineWidth += childWidthWithMargin;

            }

            if(childCount-1==i){
                totalViews.add(rowViews);
                lineHeights.add(childHeightWithMargin);
            }

        }


        lineWidth=0;
        int lineHeight = 0;

        int childLeft=0;
        int childTop=0;
        int childRight=0;
        int childButtom=0;


        int rowsNum = totalViews.size();
        Log.e(TAG, "onChildLayout: totalViews.size="+totalViews.size()+",lineHeights.size="+lineHeights.size());
        for (int i = 0 ; i < rowsNum; i++) {
            List<ChildInfo> rows = totalViews.get(i);
            Integer childLineHeight = lineHeights.get(i);

            for (int j = 0, colNum = rows.size(); j < colNum; j++) {
                ChildInfo childInfo = rows.get(j);

                layoutParams = childInfo.layoutParams;
                childWidthWithMargin = childInfo.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                 childLeft=lineWidth+layoutParams.leftMargin;
                 childTop=lineHeight+layoutParams.topMargin;
                 childRight=childLeft+childInfo.measuredWidth;
                 childButtom=childTop+childInfo.measuredHeight;
                 childInfo.view.layout(childLeft,childTop,childRight,childButtom);

                lineWidth+=childWidthWithMargin;
            }
            lineWidth=0;
            lineHeight+=childLineHeight.intValue();
        }

    }
}
