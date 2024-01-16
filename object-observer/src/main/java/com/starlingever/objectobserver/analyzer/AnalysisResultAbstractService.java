/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：AnalysisResultAbstractService
 * @Date：2024/1/10 13:24
 * @Filename：AnalysisResultAbstractService
 */
package com.starlingever.objectobserver.analyzer;


import android.content.Intent;

public class AnalysisResultAbstractService extends ForegroundService{



    public AnalysisResultAbstractService(String name) {
        super(name, "Analyzing Heap dump");
    }

    @Override
    protected void onHandleIntentInForeground(Intent intent) {

    }
}
