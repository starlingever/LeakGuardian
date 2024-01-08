/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：InternalLeakGuardian
 * @Date：2024/1/3 15:10
 * @Filename：InternalLeakGuardian
 */
package com.starlingever.leakguardian_core;


import android.app.Application;

import com.starlingever.objectobserver.GlobalObserver;
import com.starlingever.objectobserver.ObjectObserver;
import com.starlingever.objectobserver.OnObjectRetainedListener;

public class InternalLeakGuardian implements OnObjectRetainedListener {
    private HeapDumpTrigger heapDumpTrigger;

    private Application application;



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

}
