/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：HeapDumpTrigger
 * @Date：2024/1/3 15:08
 * @Filename：HeapDumpTrigger
 */
package com.starlingever.leakguardian_core;


import android.os.SystemClock;

public class HeapDumpTrigger {
    private volatile Long checkTime = 0L;

    void scheduleHandlePossibleRetainedObject() {
        Long checkCurrentTime = checkTime;
        if (checkCurrentTime > 0) {
            return; // 说明已经有相应的任务调度了
        }
        // 计算新的检查调度时间，并更新checkTime变量
        checkTime = SystemClock.uptimeMillis();




    }

    void onDumpHeapListened() {

    }

    void checkPossibleRetainedObject() {

    }
}
