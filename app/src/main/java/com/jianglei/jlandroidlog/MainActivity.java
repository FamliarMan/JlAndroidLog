package com.jianglei.jlandroidlog;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jianglei.jllog.JlLog;
import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.NetInfoVo;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    int k = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JlLog.start(getApplication());
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetInfoVo netInfoVo = new NetInfoVo();
                netInfoVo.setUrl("www.baidu.com");
                JlLog.notifyNetInfo(netInfoVo);
            }
        });
        handler = new Handler(Looper.myLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                CrashVo crashVo = new CrashVo(System.currentTimeMillis());
                crashVo.setCrashInfo("haha,崩溃了");
//                NetInfoVo netInfoVo = new NetInfoVo();
//                netInfoVo.setUrl("www.baidu.com  "+k);
//                JlLog.notifyNetInfo(netInfoVo);
                JlLog.notifyCrash(crashVo);
                k++;
                handler.postDelayed(runnable,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
