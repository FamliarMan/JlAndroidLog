package com.jianglei.jllog;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.NetInfoVo;

/**
 * 控制中心
 * Created by jianglei on 5/5/18.
 */

public class JlLog {
    /**
     * 最多记录网络信息条数
     */
    private static int maxNetRecord = 100;

    /**
     * 最多记录崩溃次数
     */
    private static int maxCrashRecord = 100;


    private static ILogInterface logInterface;

    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logInterface = ILogInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logInterface = null;
        }
    };

    public static int getMaxNetRecord() {
        return maxNetRecord;
    }


    public static int getMaxCrashRecord() {
        return maxCrashRecord;
    }


    public static void start(Application application) {
        JlCrashHandler.getInstance().init(application);
        Intent intent = new Intent(application, JlLogService.class);
        application.startService(intent);
        application.bindService(intent, serviceConnection, 0);
    }

    public static void notifyCrash(CrashVo crashVo) {
        if (logInterface != null) {
            try {
                logInterface.notifyCrash(crashVo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyNetInfo(NetInfoVo netInfoVo) {
        if (logInterface != null) {
            try {
                logInterface.notifyNetInfo(netInfoVo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
