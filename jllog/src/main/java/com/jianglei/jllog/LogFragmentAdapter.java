package com.jianglei.jllog;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by jianglei on 5/5/18.
 */

public class LogFragmentAdapter extends FragmentPagerAdapter {
    private List<String> tab;
    private List<Fragment> fragments;

    public LogFragmentAdapter(FragmentManager fm, List<String> tab, List<Fragment> fragments) {
        super(fm);
        this.tab = tab;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
