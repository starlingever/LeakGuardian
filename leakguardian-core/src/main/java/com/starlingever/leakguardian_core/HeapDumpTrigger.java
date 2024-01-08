/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：HeapDumpTrigger
 * @Date：2024/1/3 15:08
 * @Filename：HeapDumpTrigger
 */
package com.starlingever.leakguardian_core;


import android.app.Application;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.starlingever.objectobserver.GcTrigger;
import com.starlingever.objectobserver.ObjectObserver;
import com.starlingever.objectobserver.utils.GlobalData;

import java.io.File;

public final class HeapDumpTrigger implements HeapDumper {

    private final int threshold = 1;

    private volatile Long checkTime = 0L;

    private Application application;

    private Handler backgroundHandler;

    private GcTrigger gcTrigger;

    private ObjectObserver objectObserver;

    private HeapDirectoryProvider heapDirectoryProvider;

    public HeapDumpTrigger(Application application, Handler handler, GcTrigger gcTrigger, ObjectObserver objectObserver, HeapDirectoryProvider heapDirectoryProvider) {
        this.application = application;
        this.backgroundHandler = handler;
        this.gcTrigger = gcTrigger;
        this.objectObserver = objectObserver;
        this.heapDirectoryProvider = heapDirectoryProvider;
    }

    void scheduleHandlePossibleRetainedObject(Long delayMillis) {
        Long checkCurrentTime = checkTime;
        if (checkCurrentTime > 0) {
            return; // 说明已经有相应的任务调度了
        }
        // 计算新的检查调度时间，并更新checkTime变量
        checkTime = SystemClock.uptimeMillis();
        backgroundHandler.postDelayed(() -> {
            checkTime = 0L;
            checkRetainedObjects();
        }, delayMillis);

    }

    private void checkRetainedObjects() {
        int retainedObjectNum = objectObserver.getRetainedObjectNum();
        Log.d(GlobalData.DUMP, "有" + retainedObjectNum + "个泄漏对象");
        if (retainedObjectNum >= threshold) {
            Log.d(GlobalData.DUMP, "可以进行heapDump");
            File heapDumpFile = dumpHeap();
        }
        // 另起一个独立进程进行heapDumpFile的分析

    }

    void onDumpHeapListened() {

    }

    void checkPossibleRetainedObject() {

    }

    @Override
    public File dumpHeap() {
        File file = heapDirectoryProvider.newHeapDumpFile();
        try {
            Debug.dumpHprofData(file.getAbsolutePath());
            Log.d("未发现异常", "正在下载堆快照");
            return file;
        } catch (Exception e) {
            Log.d("出现异常", "无法下载堆快照");
            return null;
        }
    }
}
