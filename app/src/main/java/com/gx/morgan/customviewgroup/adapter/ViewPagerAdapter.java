package com.gx.morgan.customviewgroup.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/5/1 13:48
 */
public class ViewPagerAdapter  extends PagerAdapter {

    private List<View> views;
    private List<String> titles;
    public  ViewPagerAdapter( List<View> views,List<String> titles){
        this.views=views;
        this.titles=titles;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = views.get(position);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
