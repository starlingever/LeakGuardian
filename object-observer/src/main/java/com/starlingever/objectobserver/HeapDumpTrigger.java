/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：HeapDumpTrigger
 * @Date：2024/1/3 15:08
 * @Filename：HeapDumpTrigger
 */
package com.starlingever.objectobserver;


import android.app.Application;
import android.content.res.AssetManager;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.starlingever.objectobserver.utils.GlobalData;

import java.io.File;
import java.util.UUID;

public final class HeapDumpTrigger implements HeapDumper {

    private final int threshold = 1;

    private volatile Long checkTime = 0L;

    private Application application;

    private Handler backgroundHandler;

    private GcTrigger gcTrigger;

    private ObjectObserver objectObserver;

    private HeapDirectoryProvider heapDirectoryProvider;

    private final HeapDump.Listener heapDumpListener;


    public HeapDumpTrigger(Application application, Handler handler, GcTrigger gcTrigger, ObjectObserver objectObserver, HeapDirectoryProvider heapDirectoryProvider, HeapDump.Listener heapDumpListener) {
        this.application = application;
        this.backgroundHandler = handler;
        this.gcTrigger = gcTrigger;
        this.objectObserver = objectObserver;
        this.heapDirectoryProvider = heapDirectoryProvider;
        this.heapDumpListener = heapDumpListener;
    }

    public void scheduleHandlePossibleRetainedObject(Long delayMillis) {
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
//        int retainedObjectNum = objectObserver.getRetainedObjectNum();
        int retainedObjectNum = objectObserver.getRetainedObjectNumMap();
        Log.d(GlobalData.DUMP, "有" + retainedObjectNum + "个泄漏对象");
        if (retainedObjectNum >= threshold) {
            Log.d(GlobalData.DUMP, "可以进行heapDump");
            File heapDumpFile = dumpHeap();
            // 清除已经被检测的对象key
            objectObserver.clearObservedBefore();
            assert heapDumpFile != null;
            long length = heapDumpFile.length();
            if (length == 0L) {
                throw new RuntimeException("Heap堆的大小为0！");
            }
            Log.d(GlobalData.DUMP, "转存堆快照大小为" + length + "字节");
            // Todo 另起一个独立进程进行heapDumpFile的分析，以后台服务的形式
            String currentEventUniqueId = UUID.randomUUID().toString();
            HeapDump heapDump = new HeapDump(heapDumpFile, currentEventUniqueId);
            heapDumpListener.analyze(heapDump);
        }


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
