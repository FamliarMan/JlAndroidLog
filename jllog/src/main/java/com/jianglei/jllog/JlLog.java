package com.jianglei.jllog;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;
import com.jianglei.jllog.aidl.TransformData;
import com.jianglei.jllog.life.LifeTracer;
import com.jianglei.jllog.uiblock.UiBlockVo;
import com.jianglei.jllog.uiblock.UiTracer;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制中心
 *
 * @author jianglei
 */

public class JlLog {
    /**
     * 最多记录网络信息条数
     */
    public static int MAX_NET_RECORD = 100;

    /**
     * 最多记录崩溃次数
     */
    public static int MAX_CRASH_RECORD = 100;
    public static int MAX_LIFE_RECORD = 1000;
    /**
     * 最多记录的ui block信息
     */
    public static int MAX_UI_RECORT = 100;

    private static boolean isDebug;


    private static ILogInterface logInterface;

    /**
     * 刚开始服务有可能还没启动起来，会导致有些生命周期信息丢失，这里用来存储这些丢失的信息
     */
    private static List<LifeVo> mPendingLifes = new ArrayList<>();
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
        return MAX_NET_RECORD;
    }


    public static int getMaxCrashRecord() {
        return MAX_CRASH_RECORD;
    }


    /**
     * 开始日志监控
     *
     * @param application application
     * @param uiBlockTime ui阻塞超过的该时间会被记录
     * @param isDebug     当前是否是调试环境
     */
    public static void start(Application application, int uiBlockTime, boolean isDebug) {
        JlLog.isDebug = isDebug;
        if (!isDebug) {
            return;
        }
        JlCrashHandler.getInstance().init(application);
        Intent intent = new Intent(application, JlLogService.class);
        application.startService(intent);
        application.bindService(intent, serviceConnection, 0);
        UiTracer.start(uiBlockTime);
        LifeTracer.start(application);
    }

    public static void start(Application application, boolean isDebug) {
        JlLog.start(application, 5, isDebug);
    }

    public static void notifyCrash(CrashVo crashVo) {
        if (!isDebug) {
            return;
        }
        if (logInterface != null) {
            try {
                TransformData transformData = new TransformData(crashVo);
                logInterface.notifyData(transformData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyNetInfo(NetInfoVo netInfoVo) {
        if (!isDebug) {
            return;
        }
        if (logInterface != null) {
            try {
                TransformData transformData = new TransformData(netInfoVo);
                logInterface.notifyData(transformData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyLife(LifeVo lifeVo) {
        if (!isDebug) {
            return;
        }
        if (logInterface != null) {
            try {
                if (mPendingLifes.size() != 0) {
                    for (LifeVo vo : mPendingLifes) {
                        TransformData transformData = new TransformData(vo);
                        logInterface.notifyData(transformData);
                    }
                    mPendingLifes.clear();
                }
                TransformData transformData = new TransformData(lifeVo);
                logInterface.notifyData(transformData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            //有可能服务还没启动起来，所以先暂存
            if (mPendingLifes.size() == MAX_NET_RECORD) {
                return;
            }
            mPendingLifes.add(lifeVo);
        }
    }

    public static void notifyUiBlock(UiBlockVo uiBlockVo) {
        if (!isDebug) {
            return;
        }
        if (logInterface != null) {
            try {
                TransformData transformData = new TransformData(uiBlockVo);
                logInterface.notifyData(transformData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isIsDebug() {
        return isDebug;
    }
}
