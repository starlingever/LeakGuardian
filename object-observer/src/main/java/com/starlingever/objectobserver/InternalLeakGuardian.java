/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：InternalLeakGuardian
 * @Date：2024/1/3 15:10
 * @Filename：InternalLeakGuardian
 */
package com.starlingever.objectobserver;


import android.app.Application;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.starlingever.objectobserver.analyzer.LeakGuardianHeapDumpListener;


/*
 * 核心类，进行LeakGuardian的初始化工作，包括HeapDumpTrigger的初始化
 * 需要全局单实例
 * 在GlobalObserver中使用该类单例对象，并进行初始化工作
 */

public class InternalLeakGuardian implements OnObjectRetainedListener {
    private HeapDumpTrigger heapDumpTrigger;

    public Application application;

    private static final String LEAK_GUARDIAN_THREAD_NAME = "LeakGuardianThread";

    private HandlerThread handlerThread;

    private Handler backgroundHandler;

    private HeapDirectoryProvider heapDirectoryProvider;

    private InternalLeakGuardian() {
    }

    private static class SingletonHolder {
        private static final InternalLeakGuardian instance = new InternalLeakGuardian();
    }

    public static InternalLeakGuardian getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void onObjectRetained() {
        scheduleRetainedObjectCheck();
    }

    private void scheduleRetainedObjectCheck() {
        if (heapDumpTrigger != null) {
            heapDumpTrigger.scheduleHandlePossibleRetainedObject(0L);
        } else {
            throw new IllegalStateException("heapDumpTrigger is null");
        }
    }

    /*
     * init方法要主动调用，还是可以放入getInstance方法中？
     * */
    public void init(Application application, ObjectObserver objectObserver) {
        this.application = application;
        // 在监听器列表中将自己实例注册监听
        objectObserver.addOnObjectRetainedListener(this);
        // 设置垃圾回收触发器
        GcTrigger gcTrigger = GcTrigger.DEFAULT;
        // 设置BackgroundHandler
        handlerThread = new HandlerThread(LEAK_GUARDIAN_THREAD_NAME);
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
        // 设置堆存储路径对象
        heapDirectoryProvider = new LeakGuardianDirectoryProvider(application, 1);
        // 设置堆内存转储触发器
        HeapDump.Listener heapDumpListener = new LeakGuardianHeapDumpListener(application);
        heapDumpTrigger = new HeapDumpTrigger(application, backgroundHandler, gcTrigger, objectObserver, heapDirectoryProvider, heapDumpListener);
    }

}
