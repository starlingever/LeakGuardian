/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.utils
 * @Project：LeakGuardian
 * @name：Notifications
 * @Date：2024/1/16 14:37
 * @Filename：Notifications
 */
package com.starlingever.objectobserver.utils;


import android.app.NotificationManager;

public class Notifications {

    private boolean notificationPermissionRequested = false;

    public static boolean canShowNotification = true;

    private NotificationManager notificationManager;

    private static Notifications notifications;

}
