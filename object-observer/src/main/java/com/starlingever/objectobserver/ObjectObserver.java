/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：ObjectObserver
 * @Date：2024/1/3 13:49
 * @Filename：ObjectObserver
 */
package com.starlingever.objectobserver;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;


public class ObjectObserver implements ReachabilityObserver {

    private Executor checkRetainedExecutor;

    private boolean isEnabled = true;

    private ReferenceQueue<Object> queue;

    private final Set<String> observedObjectKeys;

    @SuppressLint("RestrictedApi")
    public ObjectObserver(Executor checkRetainedExecutor, boolean isEnabled) {
        this.checkRetainedExecutor = checkNotNull(checkRetainedExecutor, " 执行器");
        this.isEnabled = isEnabled;
        queue = new ReferenceQueue<>();
        observedObjectKeys = new CopyOnWriteArraySet<>();
    }

    public void observe(Object observedObject) {
        observe(observedObject, "");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void observe(Object observedObject, String description) {
        checkNotNull(observedObject, "待观测对象");
        checkNotNull(description, "对象名称");
        final long observeStartNanoTime = System.nanoTime();
        String key = UUID.randomUUID().toString();
        observedObjectKeys.add(key);
        final KeyedWeakReference keyedWeakReference = new KeyedWeakReference(observedObject, queue, key, description);
        checkRetainedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                moveToRetained(key);
            }
        });

    }

    private void moveToRetained(String key) {
        // 调用heapDump
    }
}
