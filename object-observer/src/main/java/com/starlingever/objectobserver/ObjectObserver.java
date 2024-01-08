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

import kotlin.jvm.Synchronized;


public class ObjectObserver implements ReachabilityObserver {

    private final Executor checkRetainedExecutor;

    private boolean isEnabled;

    private GcTrigger gcTrigger = GcTrigger.DEFAULT;

    private int retainedObjectNum;

    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    private final Set<String> observedObjectKeys = new CopyOnWriteArraySet<>();

    private final Set<OnObjectRetainedListener> onObjectRetainedListeners = new CopyOnWriteArraySet<>();


    @SuppressLint("RestrictedApi")
    public ObjectObserver(Executor checkRetainedExecutor, boolean isEnabled) {
        this.checkRetainedExecutor = checkNotNull(checkRetainedExecutor, "线程延时执行器");
        this.isEnabled = isEnabled;
    }

    public int getRetainedObjectNum() {
        removeWeaklyReachableReferences();
        return observedObjectKeys.size();
    }

    public void observe(Object observedObject) {
        observe(observedObject, "");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void observe(Object observedObject, String description) {
        checkNotNull(observedObject, "待观测对象");
        checkNotNull(description, "对象名称");
        // final long observeStartNanoTime = System.nanoTime();
        String key = UUID.randomUUID().toString();
        observedObjectKeys.add(key);
        final KeyedWeakReference keyedWeakReference = new KeyedWeakReference(observedObject, queue, key, description);
        checkRetainedExecutor.execute(() -> moveToRetained(keyedWeakReference));
    }

    private void moveToRetained(KeyedWeakReference reference) {
        // 调用heapDump
        // Todo 垃圾回收的执行地
        removeWeaklyReachableReferences();
        // 显式的调用gc以增加准确率，也可以不调用
        // gcTrigger.runGc();
        if (!exist(reference)) {
            for (OnObjectRetainedListener onObjectRetainedListener : onObjectRetainedListeners) {
                onObjectRetainedListener.onObjectRetained();
            }
        }
    }

    private boolean exist(KeyedWeakReference reference) {
        return observedObjectKeys.contains(reference.key);
    }

    private void removeWeaklyReachableReferences() {
        KeyedWeakReference ref;
        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
            observedObjectKeys.remove(ref.key);
        }
    }

    synchronized void addOnObjectRetainedListener(OnObjectRetainedListener listener) {
        onObjectRetainedListeners.add(listener);
    }

    synchronized void removeOnObjectRetainedListener(OnObjectRetainedListener listener) {
        onObjectRetainedListeners.remove(listener);
    }
}
