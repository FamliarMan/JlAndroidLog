package com.jianglei.jlandroidlog;

import android.app.Application;

import com.jianglei.jllog.JlLog;
import com.jianglei.jllog.methodtrace.MethodTracer;

/**
 * Created by jianglei on 7/24/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MethodTracer.i("MyApplication", hashCode(), "onCreate", "()V", System.nanoTime());
        JlLog.start(this, 5, true);
        MethodTracer.o("MyApplication", hashCode(), "onCreate", "()V", System.nanoTime());
    }
}
