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
import com.jianglei.jllog.methodtrace.MethodTraceInfo;
import com.jianglei.jllog.uiblock.UiBlockVo;
import com.jianglei.jllog.uiblock.UiTracer;
import com.jianglei.jllog.utils.LogUtils;
import com.jianglei.jllog.utils.MethodUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制中心
 *
 * @author jianglei
 */

public class JlLog {
    private static final int STATUS_NOT_READY = 0;
    private static final int STATUS_CONNECTED = 1;
    private static final int STATUS_CLOSED = 2;

    /**
     * 最多记录网络信息条数
     */
    public static int MAX_NET_RECORD = 10;

    /**
     * 最多记录崩溃次数
     */
    public static int MAX_CRASH_RECORD = 100;
    public static int MAX_LIFE_RECORD = 1000;
    /**
     * 最多记录的ui block信息
     */
    public static int MAX_UI_RECORT = 100;

    /**
     * 此处必须默认为true，因为方法耗时追踪可能在初始化之前就开始，
     * 如果想保留初始化之前的方法信息，这里必须默认为true
     */
    private static boolean isDebug = true;


    private static ILogInterface logInterface;

    /**
     * 刚开始服务有可能还没启动起来，会导致有些生命周期信息丢失，这里用来存储这些丢失的信息
     */
    private static List<LifeVo> mPendingLifes = new ArrayList<>();

    private static Application application;

    private static List<MethodTraceInfo> mPendingMethods = new ArrayList<>();
    private static int mServiceStatus = STATUS_NOT_READY;
    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logInterface = ILogInterface.Stub.asInterface(iBinder);

            try {
                if (mPendingLifes.size() != 0) {
                    for (LifeVo vo : mPendingLifes) {
                        TransformData transformData = new TransformData(vo);
                        logInterface.notifyData(transformData);
                    }
                    mPendingLifes.clear();
                }

                synchronized (JlLog.class) {
                    if (mPendingMethods.size() != 0) {
                        for (MethodTraceInfo vo : mPendingMethods) {
                            vo.setProcessName(MethodUtils.getProcessName(application));
                            TransformData transformData = new TransformData(vo);
                            logInterface.notifyData(transformData);
                        }
                        mPendingMethods.clear();
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mServiceStatus = STATUS_CONNECTED;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logInterface = null;
            mServiceStatus = STATUS_CLOSED;
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
        JlLog.application = application;
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
            TransformData transformData = new TransformData(lifeVo);
            try {
                logInterface.notifyData(transformData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            //有可能服务还没启动起来，所以先暂存
            if (mServiceStatus == STATUS_NOT_READY) {
                mPendingLifes.add(lifeVo);
            }
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

    public static void notifyMethod(MethodTraceInfo info) {
        if (!isDebug) {
            return;
        }
        if (mServiceStatus == STATUS_NOT_READY) {
            //有可能服务还没启动起来
            synchronized (JlLog.class) {
                mPendingMethods.add(info);
            }
            return;
        }
        if (logInterface == null) {
            return;
        }
        String processName = MethodUtils.getProcessName(application);
//        if(processName.contains(":Log")){
//            //日志进程本身不参与进来
//        }
        info.setProcessName(MethodUtils.getProcessName(application));
        TransformData transformData = new TransformData(info);
        try {
            logInterface.notifyData(transformData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIsDebug() {
        return isDebug;
    }
}
