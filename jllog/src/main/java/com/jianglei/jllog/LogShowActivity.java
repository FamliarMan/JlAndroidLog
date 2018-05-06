package com.jianglei.jllog;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.IUICallback;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.ArrayList;
import java.util.List;

public class LogShowActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<NetInfoVo> netInfoVos;
    private List<CrashVo> crashVos;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_show);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jl_log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.jl_menu_exit) {
            JlLog.exit();
            finish();
        }
        return super.onOptionsItemSelected(item);
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
