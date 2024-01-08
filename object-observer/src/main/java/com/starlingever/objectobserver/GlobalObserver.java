/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：GlobalObserver
 * @Date：2024/1/4 15:48
 * @Filename：GlobalObserver
 */
package com.starlingever.objectobserver;

import static androidx.core.util.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class GlobalObserver {
    // Todo 未初始化
    private static final ObjectObserver objectObserver = new ObjectObserver(new Executor() {
        @Override
        public void execute(Runnable command) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.postDelayed(command, 5000);
        }
    }, true);

    @SuppressLint("RestrictedApi")
    public static void manuallyInstall(Application application, Long retainedDelayMillis) {
        // Todo 需要在这个地方创建InternalLeakGuardian的单实例，并进行必要的初始化工作
        checkNotNull(objectObserver);
        List<InsatllableObserver> observersForInstall = appDefaultObservers(application, objectObserver);
        for (InsatllableObserver insatllableObserver : observersForInstall) {
            insatllableObserver.install();
        }
    }

    private static List<InsatllableObserver> appDefaultObservers(Application application, ReachabilityObserver reachabilityObserver) {
        List<InsatllableObserver> observersForInstall = new ArrayList<>();
        observersForInstall.add(ActivityObserver.getInstance(application, reachabilityObserver));
        return observersForInstall;
    }


}
