package com.jianglei.jllog.methodtrace;

import android.content.Context;
import android.os.Parcelable;

import com.jianglei.jllog.IDataHandler;


public class MethodHandler implements IDataHandler {
    private static MethodHandler methodHandler = new MethodHandler();
    public static MethodHandler getInstance(){
        return methodHandler;
    }


    @Override
    public void handle(Parcelable parcelable, Context context) {
        if (!(parcelable instanceof MethodTraceInfo)) {
            return;
        }
        MethodTraceInfo info = (MethodTraceInfo) parcelable;
        MethodStack.getInstance().addMethodTrace(info);
    }

    @Override
    public void clear(Context context) {
        //方法信息不会被清除，所以这里不用实现任何东西

    }


}
