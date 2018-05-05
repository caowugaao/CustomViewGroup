package com.gx.morgan.viewgrouplib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.gx.morgan.viewgrouplib.base.CustomViewGroup;

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


        totalViews.clear();
        rowViews.clear();
        lineHeights.clear();

        int maxContentWidth = exactlyWidthSize - getPaddingLeft() - getPaddingRight();
        int maxContentHeight = exactlyHeightSize - getPaddingTop() - getPaddingBottom();

        Log.e(TAG, "onMeasureChildrenSize: maxContentWidth="+maxContentWidth+",maxContentHeight="+maxContentHeight );

        initTotalViews(childInfos,maxContentWidth);

       int totalWidth= getTotalWidthForTotalViews(totalViews);
        int totalHeight=getTotalHeightForLineHeights(lineHeights);

        Log.e(TAG, "onMeasureChildrenSize: totalWidth="+totalWidth+",totalHeight="+totalHeight);

        return new int[]{totalWidth, totalHeight};
    }

    private int getTotalHeightForLineHeights(List<Integer> lineHeights) {
        if(null==lineHeights||lineHeights.isEmpty()){
            return 0;
        }
        int result=0;
        for(Integer height:lineHeights){
            result+=height.intValue();
        }
        return result;
    }

    private int getTotalWidthForTotalViews(List<List<ChildInfo>> totalViews) {
        if(null==totalViews||totalViews.isEmpty()){
            return 0;
        }
        int max= getTotalWidthForRows(totalViews.get(0));
        for (int i = 1,size=totalViews.size(); i <size ; i++) {
            List<ChildInfo> rowViews = totalViews.get(i);
            int totalWidth = getTotalWidthForRows(rowViews);
            if(max<totalWidth){
                max=totalWidth;
            }
        }
        return max;
    }

    private int getTotalHeightForTotalViews(List<List<ChildInfo>> totalViews){
        if(null==totalViews||totalViews.isEmpty()){
            return 0;
        }

        int result=0;
        for(List<ChildInfo>rowViews:totalViews){
            result+=getMaxHeight(rowViews);
        }
        return result;
    }

    private int getTotalWidthForRows(List<ChildInfo> rowViews) {
        if(null==rowViews||rowViews.isEmpty()){
            return 0;
        }
       int result=0;
        CustomLayoutParams layoutParams;
        for(ChildInfo childInfo:rowViews){
            layoutParams=childInfo.layoutParams;
            result=result+childInfo.measuredWidth+layoutParams.leftMargin+layoutParams.rightMargin;
        }
        return result;
    }


    private int getMaxHeight(List<ChildInfo> rowViews) {
        if(null==rowViews||rowViews.isEmpty()){
            return 0;
        }
        ChildInfo firstChildInfo = rowViews.get(0);
        int max=getChildVerticalSpaceInParent(firstChildInfo);
        for (int i = 1,size=rowViews.size(); i <size ; i++) {
            ChildInfo childInfo = rowViews.get(i);
            int height=getChildVerticalSpaceInParent(childInfo);
            if(max<height){
                max=height;
            }
        }
      return max;
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
        if(totalViews.isEmpty()){
            int width = getWidth();
            int contentWidth = width - getPaddingLeft() - getPaddingRight();
            initTotalViews(childInfos,contentWidth);
        }

        CustomLayoutParams layoutParams;
        int childWidthWithMargin = 0;
        int  lineWidth=0;
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
                childWidthWithMargin = getChildHorizontalSpaceInParent(childInfo);

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

    private void initTotalViews(SparseArray<ChildInfo> childInfos,int maxContentWidth) {

        totalViews.clear();
        rowViews.clear();
        lineHeights.clear();


        int lineWidth = 0;

        int childWidthWithMargin;
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            ChildInfo childInfo = childInfos.get(i);
            if (null == childInfo || View.GONE == childInfo.visible) {
                if(childCount-1==i){
                    totalViews.add(rowViews);
                    int maxHeightFromRowViews = getMaxHeight(rowViews);
                    lineHeights.add(maxHeightFromRowViews);
                }
                continue;
            }
            childWidthWithMargin = getChildHorizontalSpaceInParent(childInfo);

            if (lineWidth + childWidthWithMargin > maxContentWidth) {//大于最大内容宽度

                totalViews.add(rowViews);
                int maxHeight = getMaxHeight(rowViews);
                lineHeights.add(maxHeight);

                rowViews = new ArrayList<>();
                rowViews.add(childInfo);
                lineWidth = childWidthWithMargin;


            } else {

                rowViews.add(childInfo);
                lineWidth += childWidthWithMargin;

            }

            if(childCount-1==i){
                totalViews.add(rowViews);
                int maxHeight = getMaxHeight(rowViews);
                lineHeights.add(maxHeight);
            }

        }
    }
}
