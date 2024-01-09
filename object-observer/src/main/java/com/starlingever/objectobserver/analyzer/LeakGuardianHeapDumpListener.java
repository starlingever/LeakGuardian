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

import com.starlingever.objectobserver.HeapDump;

public final class LeakGuardianHeapDumpListener implements HeapDump.Listener {
    private final Context context;

    public LeakGuardianHeapDumpListener(Context context) {
        this.context = context;
    }


    @Override
    public void analyze(HeapDump heapDump) {
        // Todo 具体的分析逻辑
        HeapAnalyzerService.runAnalysis(context, heapDump);
    }
}
