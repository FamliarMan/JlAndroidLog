package com.jianglei.jllog.life;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jianglei.jllog.JlLog;
import com.jianglei.jllog.aidl.LifeVo;

/**
 * @author jianglei on 12/23/18.
 */

public class LifeTracer {
    public static void start(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_CREATE, activity.getClass().getName()));
            }

            @Override
            public void onActivityStarted(Activity activity) {
                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_START, activity.getClass().getName()));

            }

            @Override
            public void onActivityResumed(Activity activity) {
                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_RESUME, activity.getClass().getName()));
            }

            @Override
            public void onActivityPaused(Activity activity) {
                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_PAUSE, activity.getClass().getName()));
            }

            @Override
            public void onActivityStopped(Activity activity) {
                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_STOP, activity.getClass().getName()));
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                JlLog.notifyLife(new LifeVo(System.currentTimeMillis(),
                        LifeVo.TYPE_ON_DESTROY, activity.getClass().getName()));
            }
        });
    }
}
