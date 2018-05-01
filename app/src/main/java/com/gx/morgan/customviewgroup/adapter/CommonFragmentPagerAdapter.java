package com.gx.morgan.customviewgroup.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * description：
 * <br>author：caowugao
 * <br>time：2018/5/1 14:28
 */
public class CommonFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    private CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        this(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null==titles||titles.isEmpty()?super.getPageTitle(position):titles.get(position);
    }
}
