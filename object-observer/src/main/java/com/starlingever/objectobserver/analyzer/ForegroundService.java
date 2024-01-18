/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：ForgroundService
 * @Date：2024/1/9 17:04
 * @Filename：ForgroundService
 */
package com.starlingever.objectobserver.analyzer;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


public abstract class ForegroundService extends IntentService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ForegroundService(String name, String analyzingHeapDump) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        onHandleIntentInForeground(intent);
    }

    protected abstract void onHandleIntentInForeground(Intent intent);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
