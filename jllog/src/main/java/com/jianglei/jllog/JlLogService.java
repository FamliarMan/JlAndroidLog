package com.jianglei.jllog;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.TransformData;

/**
 * 核心日志监控服务
 *
 * @author jianglei
 */

public class JlLogService extends Service {
//    /**
//     * 最多缓存100条
//     */
//    private LinkedList<CrashVo> crashVos = new LinkedList<>();
//    /**
//     * 最多缓存100条
//     */
//    private LinkedList<NetInfoVo> netInfoVos = new LinkedList<>();
//    private LinkedList<LifeVo> lifeVos = new LinkedList<>();
    private DataDispatcher dataDispatcher = new DataDispatcher();
    private ILogInterface.Stub mBinder = new ILogInterface.Stub() {
        @Override
        public void exit() {
            stopForeground(true);
            stopSelf();
        }

        @Override
        public void notifyData(TransformData data) throws RemoteException {
            dataDispatcher.dispatchAdd(data.getRealData(),getApplicationContext());
        }

        @Override
        public void clearData(int type) throws RemoteException {
            dataDispatcher.dispatchClear(type);
        }

    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = NotifyManager.getNotification(getApplicationContext(),0,0);
        startForeground(NotifyManager.NOTIFICATION_ID, notification);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
