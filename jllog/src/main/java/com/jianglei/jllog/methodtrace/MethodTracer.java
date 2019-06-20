package com.jianglei.jllog.methodtrace;

import android.os.Looper;

import com.jianglei.jllog.JlLog;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author longyi created on 19-6-14
 */
public class MethodTracer {

    private static Executor mExecutor = Executors.newSingleThreadExecutor();
    private static final String MAIN_THREAD = Looper.getMainLooper().getThread().getName();

    /**
     * 方法刚开始调用时的追踪方法
     *
     * @param className  类名
     * @param hashcode   该类的hascode
     * @param methodName 被追踪的方法名
     * @param curTime    刚开始调用时的时间，一般用System.nanoTime()去获取
     */
    public static void i(final String className, final int hashcode, final String methodName,
                         final String desc, final long curTime) {
        if (!Thread.currentThread().getName().equals(MAIN_THREAD)) {
            return;
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {

                MethodTraceInfo info = new MethodTraceInfo(className + "@" + hashcode,
                        methodName, desc, curTime, MethodTraceInfo.IN);
                JlLog.notifyMethod(info);
            }
        });


    }


    /**
     * 方法刚结束调用时的追踪方法
     *
     * @param className  类名
     * @param hashcode   该类的hascode
     * @param methodName 被追踪的方法名
     * @param curTime    方法调用结尾的时间，一般用System.currentTimeMillis获得
     */
    public static void o(final String className, final int hashcode, final String methodName,
                         final String desc, final long curTime) {

        if (!Thread.currentThread().getName().equals(MAIN_THREAD)) {
            return;
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MethodTraceInfo info = new MethodTraceInfo(className + "@" + hashcode,
                        methodName, desc, curTime, MethodTraceInfo.OUT);
                JlLog.notifyMethod(info);
            }
        });
    }
}
