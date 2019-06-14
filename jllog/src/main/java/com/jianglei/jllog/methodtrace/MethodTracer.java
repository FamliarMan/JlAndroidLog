package com.jianglei.jllog.methodtrace;

import android.os.Looper;

/**
 * @author longyi created on 19-6-14
 */
public class MethodTracer {

    private static final String MAIN_THREAD = Looper.getMainLooper().getThread().getName();
    /**
     * 方法刚开始调用时的追踪方法
     * @param className 类名
     * @param hashcode 该类的hascode
     * @param methodName 被追踪的方法名
     * @param curTime 刚开始调用时的时间，一般用System.currentTimeMillis获得
     */
    public static void i(String className,int hashcode,String methodName, long curTime){
        if(!Thread.currentThread().getName().equals(MAIN_THREAD)){
            return;
        }


    }


    /**
     * 方法刚结束调用时的追踪方法
     * @param className 类名
     * @param hashcode 该类的hascode
     * @param methodName 被追踪的方法名
     * @param curTime 方法调用结尾的时间，一般用System.currentTimeMillis获得
     */
    public static void o(String className,int hashcode,String methodName, long curTime){

    }
}
