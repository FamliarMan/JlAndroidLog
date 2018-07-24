package com.jianglei.jllog;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Button;

import com.jianglei.jllog.aidl.CrashVo;
import com.jianglei.jllog.aidl.ILogInterface;
import com.jianglei.jllog.aidl.LifeVo;
import com.jianglei.jllog.aidl.NetInfoVo;

import java.util.LinkedList;
import java.util.List;

/**
 * 核心日志监控服务
 *
 * @author jianglei
 */

public class JlLogService extends Service {
    private static int NOTIFICATION_ID = 101;
    /**
     * 最多缓存100条
     */
    private LinkedList<CrashVo> crashVos = new LinkedList<>();
    /**
     * 最多缓存100条
     */
    private LinkedList<NetInfoVo> netInfoVos = new LinkedList<>();
    private LinkedList<LifeVo> lifeVos = new LinkedList<>();
    private ILogInterface.Stub mBinder = new ILogInterface.Stub() {
        @Override
        public void notifyNetInfo(NetInfoVo netInfoVo) throws RemoteException {
            if (netInfoVos.size() == JlLog.getMaxNetRecord()) {
                netInfoVos.remove(0);
            }
            netInfoVos.add(netInfoVo);
            updateUi(netInfoVo);
            updateNotification();

        }

        @Override
        public void notifyCrash(CrashVo crashVo) throws RemoteException {
            if (crashVos.size() == JlLog.getMaxCrashRecord()) {
                crashVos.remove(0);
            }
            crashVos.add(crashVo);
            updateUi(crashVo);
            updateNotification();
        }

        @Override
        public List<NetInfoVo> getNetInfoVos() throws RemoteException {
            return netInfoVos;
        }

        @Override
        public List<CrashVo> getCrashVos() throws RemoteException {
            return crashVos;
        }

        @Override
        public void notifyLife(LifeVo lifeVo) throws RemoteException {
            Log.d("longyi","notify life: "+lifeVo.toString());
            if (lifeVos.size() == JlLog.getMaxCrashRecord()) {
                lifeVos.remove(0);
            }
            lifeVos.add(lifeVo);
            //生命周期不会在后台更新，所以此处无需发送广播实时更新页面
        }

        @Override
        public void clearLife() throws RemoteException {
            lifeVos.clear();
        }

        @Override
        public List<LifeVo> getLifeVos() throws RemoteException {
            return lifeVos;
        }

        @Override
        public void clearNetInfo() {
            netInfoVos.clear();
            updateNotification();
        }

        @Override
        public void clearCrash() {
            crashVos.clear();
            updateNotification();
        }

        @Override
        public void exit() {
            stopForeground(true);
            stopSelf();
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
        Notification notification = getNotification();
        startForeground(NOTIFICATION_ID, notification);
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

    public void updateNotification() {
        Notification notification = getNotification();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    public Notification getNotification() {
        Application application = getApplication();
        Intent clickIntent = new Intent(this, LogShowActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntent.setAction(Long.toString(System.currentTimeMillis()));
        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "log";
            String channelName = getString(R.string.jl_log_system);
            int importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(channelId, channelName, importance);
        }
        return new NotificationCompat.Builder(application, "log")
                .setSmallIcon(getAppIconRes())
                .setContentTitle(getString(R.string.jl_log_system))
                .setContentText(getString(R.string.jl_log_title, netInfoVos.size(), crashVos.size()))
                .setContentIntent(pendingIntent)
                .build();
    }

    private int getAppIconRes() {

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

}
