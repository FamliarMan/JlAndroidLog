package com.jianglei.jllog;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author jianglei on 11/23/18.
 */

public class NotifyManager {
    public static int NOTIFICATION_ID = 101;

    public static void updateNotification(Context context) {
        int crashSize = DataCenter.getInstance().getCrashVos().size();
        int netSize = DataCenter.getInstance().getNetInfoVos().size();
        Notification notification = getNotification(context, netSize, crashSize);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    public static Notification getNotification(Context context, int netInfoSize, int crashInfoSize) {
        Intent clickIntent = new Intent(context, LogShowActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntent.setAction(Long.toString(System.currentTimeMillis()));
        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "log";
            String channelName = context.getString(R.string.jl_log_system);
            int importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(context, channelId, channelName, importance);
        }
        return new NotificationCompat.Builder(context, "log")
                .setSmallIcon(getAppIconRes(context))
                .setContentTitle(context.getString(R.string.jl_log_system))
                .setContentText(context.getString(R.string.jl_log_title, netInfoSize, crashInfoSize))
                .setContentIntent(pendingIntent)
                .build();
    }

    private static int getAppIconRes(Context context) {

        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return R.mipmap.jl_knife ;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
