package com.jianglei.jllog;

import android.content.Context;
import android.os.Parcelable;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.TransformData;
import com.jianglei.jllog.life.LifeDataHandler;
import com.jianglei.jllog.methodtrace.MethodHandler;
import com.jianglei.jllog.methodtrace.MethodTraceInfo;
import com.jianglei.jllog.methodtrace.MethodTracer;
import com.jianglei.jllog.uiblock.UITracerHandler;
import com.jianglei.jllog.uiblock.UiBlockVo;
import com.jianglei.jllog.utils.LogUtils;

/**
 * @author jianglei on 11/23/18.
 */

public class DataDispatcher {
    public void dispatchAdd(Parcelable data, Context context) {
        IDataHandler dataHandler = null;
        if (data instanceof CrashVo) {
            dataHandler = CrashDataHandler.getInstance();
        } else if (data instanceof LifeVo) {
            dataHandler = LifeDataHandler.getInstance();
        } else if (data instanceof NetInfoVo) {
            dataHandler = NetDataHandler.getInstance();
        } else if (data instanceof UiBlockVo) {
            dataHandler = UITracerHandler.getInstance();
        } else if (data instanceof MethodTraceInfo) {
            dataHandler = MethodHandler.getInstance();
        }

        if (dataHandler == null) {
            return;
        }
        dataHandler.handle(data, context);
    }

    public void dispatchClear(int type, Context context) {
        IDataHandler dataHandler = null;
        switch (type) {
            case TransformData.TYPE_CRASH:
                dataHandler = CrashDataHandler.getInstance();
                break;
            case TransformData.TYPE_NET:
                dataHandler = NetDataHandler.getInstance();
                break;
            case TransformData.TYPE_LIFE:
                dataHandler = LifeDataHandler.getInstance();
                break;
            case TransformData.TYPE_UI:
                dataHandler = UITracerHandler.getInstance();
            default:
                break;
        }
        if (dataHandler == null) {
            return;
        }
        dataHandler.clear(context);
    }

}
