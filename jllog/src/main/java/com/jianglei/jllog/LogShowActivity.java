package com.jianglei.jllog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.ArrayList;
import java.util.List;

public class LogShowActivity extends JlBaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<NetInfoVo> netInfoVos;
    private List<CrashVo> crashVos;
    private List<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_show);
        tabLayout =(TabLayout) findViewById(R.id.tab);
        viewPager =(ViewPager)findViewById(R.id.viewpager);
        init();
    }

    private void init() {
        netInfoVos = JlLog.getNetInfoVos();
        crashVos = JlLog.getCrashVos();
        fragments = new ArrayList<>();
        NetInfoFragment netInfoFragment = NetInfoFragment.newInstance(netInfoVos);
        CrashFragment crashFragment = CrashFragment.newInstance(crashVos);
        fragments.add(netInfoFragment);
        fragments.add(crashFragment);
        List<String> tab = new ArrayList<>();
        tab.add(getString(R.string.jl_http_request));
        tab.add(getString(R.string.jl_crash_info));
        viewPager.setAdapter(new LogFragmentAdapter(getSupportFragmentManager(), tab, fragments));
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
