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
import android.util.Log;
import android.widget.Toast;

import com.starlingever.objectobserver.utils.GlobalData;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import leakcanary.KeyedWeakReference;


public class ObjectObserver implements ReachabilityObserver {

    private final Executor checkRetainedExecutor;

    private boolean isEnabled;

    private GcTrigger gcTrigger = GcTrigger.DEFAULT;


    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    private final Set<String> observedObjectKeys = new CopyOnWriteArraySet<>();

    private final Map<String, KeyedWeakReference> observedObjects = new HashMap<String, KeyedWeakReference>();

    private final Set<OnObjectRetainedListener> onObjectRetainedListeners = new CopyOnWriteArraySet<>();


    @SuppressLint("RestrictedApi")
    public ObjectObserver(Executor checkRetainedExecutor, boolean isEnabled) {
        this.checkRetainedExecutor = checkNotNull(checkRetainedExecutor, "线程延时执行器");
        this.isEnabled = isEnabled;
    }

//    public int getRetainedObjectNum() {
//        removeWeaklyReachableReferences();
//        return observedObjectKeys.size();
//    }

    public int getRetainedObjectNumMap() {
        removeMapWeaklyReachable();
        return observedObjects.size();
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
        // observedObjectKeys.add(key);
        long observedTime = NANOSECONDS.toMillis(System.nanoTime());
        Log.d(GlobalData.OBS, "正在监控对象，时间为" + observedTime);
        // final KeyedWeakReference keyedWeakReference = new KeyedWeakReference(observedObject, queue, key, description);
        KeyedWeakReference keyedWeakReferenceHelper = new KeyedWeakReference(observedObject, key, description, observedTime, queue);
        observedObjects.put(key, keyedWeakReferenceHelper);
        checkRetainedExecutor.execute(() -> moveToRetained(key));
    }

    /*
     * 针对于Set类型的泄漏监控
     * */
//    private void moveToRetained(KeyedWeakReference reference) {
//        removeWeaklyReachableReferences();
//        // 显式的调用gc以增加准确率，也可以不调用
//        // gcTrigger.runGc();
//        if (exist(reference)) {
//            Log.d(GlobalData.OBS, "监控到泄漏对象存在!");
//            for (OnObjectRetainedListener onObjectRetainedListener : onObjectRetainedListeners) {
//                onObjectRetainedListener.onObjectRetained();
//            }
//        }
//    }

    /*
     * 针对于Map类型的泄漏监控
     * */
    private void moveToRetained(String key) {
        // 显式的调用gc以增加准确率，也可以不调用
        gcTrigger.runGc();
        removeMapWeaklyReachable();
        if (observedObjects.get(key) != null) {
            long observedTime = NANOSECONDS.toMillis(System.nanoTime());
            Log.d(GlobalData.OBS, "监控到泄漏对象存在!时间为" + observedTime);
            observedObjects.get(key).setRetainedUptimeMillis(observedTime);
            for (OnObjectRetainedListener onObjectRetainedListener : onObjectRetainedListeners) {
                onObjectRetainedListener.onObjectRetained();
            }
        }
    }

    private boolean exist(KeyedWeakReference reference) {
        return observedObjects.containsKey(reference.getKey());
    }
//
//    private void removeWeaklyReachableReferences() {
//        KeyedWeakReference ref;
//        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
//            observedObjectKeys.remove(ref.key);
//        }
//    }

    private void removeMapWeaklyReachable() {
        KeyedWeakReference ref;
        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
            observedObjects.remove(ref.getKey());
        }
    }

    public synchronized void addOnObjectRetainedListener(OnObjectRetainedListener listener) {
        onObjectRetainedListeners.add(listener);
    }

    public synchronized void removeOnObjectRetainedListener(OnObjectRetainedListener listener) {
        onObjectRetainedListeners.remove(listener);
    }

    public void clearObservedBefore() {
        int size = observedObjectKeys.size();
        Log.d(GlobalData.DUMP, "key集合中仍存在的对象个数为" + size);
        // ToDo
    }
}
