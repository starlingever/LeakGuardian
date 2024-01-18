/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：LeakGuardianHeapDumpListener
 * @Date：2024/1/9 17:03
 * @Filename：LeakGuardianHeapDumpListener
 */
package com.starlingever.objectobserver.analyzer;


import android.content.Context;
import android.util.Log;

import com.starlingever.objectobserver.HeapDump;
import com.starlingever.objectobserver.utils.GlobalData;

public final class LeakGuardianHeapDumpListener implements HeapDump.Listener {
    private final Context context;

    public LeakGuardianHeapDumpListener(Context context) {
        this.context = context;
    }


    @Override
    public void analyze(HeapDump heapDump) {
        Log.d(GlobalData.ANAL, "正在进行引用链分析...");
        Log.d(GlobalData.ANAL, "当前线程为" + Thread.currentThread());
        HeapAnalyzerService.runAnalysis(context, heapDump);
    }
}
