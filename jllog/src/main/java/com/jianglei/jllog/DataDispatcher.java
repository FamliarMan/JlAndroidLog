package com.jianglei.jllog;

import android.content.Context;
import android.os.Parcelable;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.TransformData;

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
        }

        if (dataHandler == null) {
            return;
        }
        dataHandler.handle(data, context);
    }

    public void dispatchClear(int type) {
        switch (type) {
            case TransformData.TYPE_CRASH:
                DataCenter.getInstance().clearCrash();
                break;
            case TransformData.TYPE_NET:
                DataCenter.getInstance().clearNetInfo();
                break;
            case TransformData.TYPE_LIFE:
                DataCenter.getInstance().clearNetInfo();
                break;
            default:
                break;
        }
    }

}
