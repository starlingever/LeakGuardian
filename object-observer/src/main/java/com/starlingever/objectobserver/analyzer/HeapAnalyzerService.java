/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：HeapAnalyzerService
 * @Date：2024/1/9 17:30
 * @Filename：HeapAnalyzerService
 */
package com.starlingever.objectobserver.analyzer;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.starlingever.objectobserver.HeapDump;
import com.starlingever.objectobserver.utils.GlobalData;

import java.io.IOException;
import java.text.ParseException;


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
                    mainAnalyzer.runAnalysis(heapDump);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
