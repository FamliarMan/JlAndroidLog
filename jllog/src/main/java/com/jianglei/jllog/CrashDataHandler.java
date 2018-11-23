package com.jianglei.jllog;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.jianglei.jllog.aidl.CrashVo;

/**
 * @author jianglei on 11/23/18.
 */

public class CrashDataHandler implements IDataHandler {
    private static CrashDataHandler instance;
    @Override
    public void handle(Parcelable parcelable, Context context) {
        if(!(parcelable instanceof CrashVo)){
            return;
        }

        CrashVo crashVo = (CrashVo) parcelable;
        DataCenter.getInstance().addCrashVo(crashVo);

        final Intent intent = new Intent();
        intent.setAction("updateUI");
        intent.putExtra("crashVo", crashVo);
        context.sendBroadcast(intent);
        NotifyManager.updateNotification(context);
    }

    public static CrashDataHandler getInstance() {
        if(instance == null){
            instance = new CrashDataHandler();
        }
        return instance;
    }
}
