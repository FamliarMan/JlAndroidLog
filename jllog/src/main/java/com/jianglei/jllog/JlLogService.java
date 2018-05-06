package com.jianglei.jllog;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.http.SslCertificate;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.IUICallback;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 核心日志监控服务
 * Created by jianglei on 5/5/18.
 */

public class JlLogService extends Service {
    /**
     * 最多缓存100条
     */
    private List<CrashVo> crashVos = new LinkedList<>();
    /**
     * 最多缓存100条
     */
    private List<NetInfoVo> netInfoVos = new LinkedList<>();
    private ILogInterface.Stub mBinder = new ILogInterface.Stub() {
        @Override
        public void notifyNetInfo(NetInfoVo netInfoVo) throws RemoteException {
            if (netInfoVos.size() == JlLog.getMaxNetRecord()) {
                netInfoVos.remove(0);
            }
            updateUi(netInfoVo);
            netInfoVos.add(netInfoVo);
        }

        @Override
        public void notifyCrash(CrashVo crashVo) throws RemoteException {
            if (crashVos.size() == JlLog.getMaxCrashRecord()) {
                crashVos.remove(0);
            }
            updateUi(crashVo);
            crashVos.add(crashVo);
        }

        @Override
        public List<NetInfoVo> getNetInfoVos() throws RemoteException {
            return netInfoVos;
        }

        @Override
        public List<CrashVo> getCrashVos() throws RemoteException {
            return crashVos;
        }

    };

    private void updateUi(NetInfoVo netInfoVo) {
        final Intent intent = new Intent();
        intent.setAction("updateUI");
        intent.putExtra("netInfoVo", netInfoVo);
        sendBroadcast(intent);
    }

    private void updateUi(CrashVo crashVo) {
        final Intent intent = new Intent();
        intent.setAction("updateUI");
        intent.putExtra("crashVo", crashVo);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Application application = getApplication();
        Intent clickIntent = new Intent(this, LogShowActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        clickIntent.setAction(Long.toString(System.currentTimeMillis()));
        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(application, "jlLog")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("日志帮助")
                .setContentText("触摸查看详细日志")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(101, notification);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
//        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
