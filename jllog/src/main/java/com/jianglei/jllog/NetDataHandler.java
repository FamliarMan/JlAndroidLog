package com.jianglei.jllog;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.jianglei.jllog.aidl.NetInfoVo;

/**
 * @author jianglei on 11/23/18.
 */

public class NetDataHandler implements IDataHandler {
    private static NetDataHandler instance;
    @Override
    public void handle(Parcelable parcelable, Context context) {
        if(!(parcelable instanceof NetInfoVo)){
            return;
        }
        NetInfoVo netInfoVo = (NetInfoVo) parcelable;
        DataCenter.getInstance().addNetInfoVo(netInfoVo);
        Intent intent = new Intent();
        intent.setAction("updateUI");
        intent.putExtra("netInfoVo", netInfoVo);
        context.sendBroadcast(intent);
        NotifyManager.updateNotification(context);
    }

    @Override
    public void clear(Context context) {
        DataCenter.getInstance().clearNetInfo();
        NotifyManager.updateNotification(context);
    }

    public static NetDataHandler getInstance() {
        if(instance == null){
            instance = new NetDataHandler();
        }
        return instance;
    }
}
