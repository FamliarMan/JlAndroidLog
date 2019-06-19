package com.jianglei.jllog;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.TransformData;
import com.jianglei.jllog.life.LifeCyclerFragment;
import com.jianglei.jllog.methodtrace.MethodFragment;
import com.jianglei.jllog.methodtrace.MethodTracer;
import com.jianglei.jllog.uiblock.UiBlockListFragment;
import com.jianglei.jllog.uiblock.UiBlockVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianglei
 */
public class LogShowActivity extends JlBaseActivity implements ILogShowActivity {

    private TabLayout tabLayout;
    private NoScrollViewPager viewPager;

    private ILogInterface logInterface;
    private boolean isUnbind;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logInterface = ILogInterface.Stub.asInterface(iBinder);
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logInterface = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, JlLogService.class);
        bindService(intent, serviceConnection, 0);
        setContentView(R.layout.activity_log_show);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
    }

    private void init() {
        List<Fragment> fragments = new ArrayList<>();
        NetInfoFragment netInfoFragment = NetInfoFragment.newInstance();
        CrashListFragment crashListFragment = CrashListFragment.newInstance();
        fragments.add(netInfoFragment);
        fragments.add(MethodFragment.getInstance());
        fragments.add(crashListFragment);
        fragments.add(UiBlockListFragment.newInstance());
        fragments.add(LifeCyclerFragment.newInstance());
        List<String> tab = new ArrayList<>();
        tab.add(getString(R.string.jl_http_request));
        tab.add(getString(R.string.jl_method));
        tab.add(getString(R.string.jl_crash_info));
        tab.add(getString(R.string.jl_ui_block));
        tab.add(getString(R.string.jl_life));
        viewPager.setAdapter(new LogFragmentAdapter(getSupportFragmentManager(), tab, fragments));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void clearNet() {
        if (logInterface != null) {
            try {
                logInterface.clearData(TransformData.TYPE_NET);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clearCrash() {
        if (logInterface == null) {
            return;
        }
        try {
            logInterface.clearData(TransformData.TYPE_CRASH);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<NetInfoVo> getNetInfo() {
        return DataCenter.getInstance().getNetInfoVos();
    }

    @Override
    public List<CrashVo> getCrashVo() {
        return DataCenter.getInstance().getCrashVos();
    }

    @Override
    public void clearLife() {
        if (logInterface == null) {
            return;
        }
        try {
            logInterface.clearData(TransformData.TYPE_LIFE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<LifeVo> getLifeVos() {
        return DataCenter.getInstance().getLifeVos();
    }

    @Override
    public void clearUi() {
        if (logInterface == null) {
            return;
        }
        try {
            logInterface.clearData(TransformData.TYPE_UI);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<UiBlockVo> getUiTraces() {
        return DataCenter.getInstance().getUiBlockVos();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jl_log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.jl_menu_exit) {
            if (logInterface != null) {
                try {
                    unBindService();
                    logInterface.exit();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void unBindService() {
        if (isUnbind) {
            return;
        }
        unbindService(serviceConnection);
        isUnbind = true;
    }
}
