package com.jianglei.jllog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.jianglei.jllog.aidl.LifeVo;

/**
 * @author jianglei
 */

public abstract  class JlBaseActivity extends AppCompatActivity {
    private LinearLayout mainLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.jl_activity_base);
        mainLayout = (LinearLayout) findViewById(R.id.parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.jl_toolbar_color));

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.jl_white));
        setSupportActionBar(toolbar);

    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mainLayout, false);
        mainLayout.addView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_START,getClass().getName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_RESUME,getClass().getName()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_RESTART,getClass().getName()));

    }

    @Override
    protected void onStop() {
        super.onStop();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_STOP,getClass().getName()));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_PAUSE,getClass().getName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),LifeVo.TYPE_ON_DESTROY,getClass().getName()));

    }
}
