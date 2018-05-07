package com.jianglei.jllog;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.IUICallback;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.List;

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

    private static Application application;

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
        JlLog.application = application;
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

    public static List<NetInfoVo> getNetInfoVos() {
        try {
            if (logInterface != null) {
                return logInterface.getNetInfoVos();
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<CrashVo> getCrashVos() {
        try {
            if (logInterface != null) {
                return logInterface.getCrashVos();
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearNetInfo() {
        try {
            if (logInterface != null) {
                logInterface.clearNetInfo();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void clearCrash() {
        try {
            if (logInterface != null) {
                logInterface.clearCrash();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        application.unbindService(serviceConnection);
        Intent intent = new Intent(application, JlLogService.class);
        application.stopService(intent);
    }

}
