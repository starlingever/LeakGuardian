/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：HeapAnalyzerService
 * @Date：2024/1/9 17:30
 * @Filename：HeapAnalyzerService
 */
package com.starlingever.objectobserver.analyzer;

import android.content.Context;
import android.content.Intent;

import com.starlingever.objectobserver.HeapDump;


public class HeapAnalyzerService extends ForegroundService {

    public HeapAnalyzerService(String name) {
        super(name);
    }

    public static void runAnalysis(Context context, HeapDump heapDump) {
        Intent intent = new Intent(context, HeapAnalyzerService.class);
    }

    @Override
    protected void onHandleIntentInForeground(Intent intent) {
        if (intent == null) {
            return;
        }
        HeapAnalyzer heapAnalyzer = new HeapAnalyzer();
        // Todo 在这里进行引用链分析

    }
}
