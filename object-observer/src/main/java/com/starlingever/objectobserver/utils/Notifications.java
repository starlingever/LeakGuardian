/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.utils
 * @Project：LeakGuardian
 * @name：Notifications
 * @Date：2024/1/16 14:37
 * @Filename：Notifications
 */
package com.starlingever.objectobserver.utils;


import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.starlingever.objectobserver.R;

public class Notifications {

    public static boolean canShowNotification = true;

    private static NotificationManager notificationManager;

    public static NotificationManager getNotificationManager(Application application) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public static void showNotification(Application application, int objectCount, String contentText) {
        if (!canShowNotification) {
            return;
        }
        notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification", "通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        String title = objectCount + "个泄漏对象被检测到!";
        @SuppressLint({"NewApi", "LocalSuppress"}) Notification.Builder builder = new Notification.Builder(application, "notification");
        builder.setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

}
