/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：HeapAnalyzerService
 * @Date：2024/1/9 17:30
 * @Filename：HeapAnalyzerService
 */
package com.starlingever.objectobserver.analyzer;


import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.starlingever.objectobserver.HeapDump;
import com.starlingever.objectobserver.R;
import com.starlingever.objectobserver.activities.AnalysisActivity;
import com.starlingever.objectobserver.utils.GlobalData;
import com.starlingever.objectobserver.utils.Notifications;

import java.io.IOException;
import java.text.ParseException;

import shark.HeapAnalysis;


public class HeapAnalyzerService extends ForegroundService {
    private static final String HEAPDUMP_EXTRA = "heapdump_extra";
    private static final String LEAK_GUARDIAN_THREAD_NAME_2 = "ANALYSIS THREAD";

    public HeapAnalyzerService() {
        super(HeapAnalyzerService.class.getSimpleName(), "Analyzing Heap dump");
    }

    public static void runAnalysis(Context context, HeapDump heapDump) {
        Intent intent = new Intent(context, HeapAnalyzerService.class);
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        ContextCompat.startForegroundService(context, intent);
        Log.d(GlobalData.ANAL, "走到了HeapAnalyzerService的静态方法");
        HandlerThread handlerThread = new HandlerThread(LEAK_GUARDIAN_THREAD_NAME_2);
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(GlobalData.ANAL, "马上准备在后台线程进行堆分析！当前线程为" + Thread.currentThread());
                MainAnalyzer mainAnalyzer = MainAnalyzer.getInstance();
                try {
                    HeapAnalysis heapAnalysis = mainAnalyzer.runAnalysis(heapDump);
                    String content = "点击查看分析报告!";
                    showAnalysisDoneNotification(context, content, heapAnalysis);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void showAnalysisDoneNotification(Context context, String content, HeapAnalysis heapAnalysis) {
        if (!Notifications.canShowNotification) {
            return;
        }
        NotificationManager notificationManager = Notifications.getNotificationManager((Application) context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification", "通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        double sec = (heapAnalysis.getAnalysisDurationMillis()) / 1000.0;
        String title = "引用链分析完成,本次用时" + sec + "秒";
        Intent intent = new Intent(context, AnalysisActivity.class);
        intent.putExtra("analysis", heapAnalysis);
        PendingIntent pd = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        @SuppressLint({"NewApi", "LocalSuppress"}) Notification.Builder builder = new Notification.Builder(context, "notification");
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setContentIntent(pd)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onHandleIntentInForeground(Intent intent) {
        Log.d(GlobalData.ANAL, "走到了前台Service的HandleIntentInForeground回调!");
        // IntentService的机制默认在后台线程执行任务
        if (intent == null) {
            Log.d(GlobalData.ANAL, "发现null intent");
            return;
        }
        // Todo 在这里进行引用链分析
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        // HeapAnalyzer heapAnalyzer = new HeapAnalyzer(null);
        MainAnalyzer mainAnalyzer = MainAnalyzer.getInstance();
        assert heapDump != null;
        Log.d(GlobalData.ANAL, "马上准备在后台线程进行堆分析！当前线程为" + Thread.currentThread());
//        mainAnalyzer.runAnalysis(heapDump);


    }
}
