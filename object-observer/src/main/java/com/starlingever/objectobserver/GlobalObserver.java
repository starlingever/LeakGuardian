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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class GlobalObserver {
    private static final Long retainedDelayMillis = 5000L;
    // Todo 未初始化
    private static final ObjectObserver objectObserver = new ObjectObserver(new Executor() {
        @Override
        public void execute(Runnable command) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.postDelayed(command, retainedDelayMillis);
        }
    }, true);

    @SuppressLint("RestrictedApi")
    /*
     * 目前初始化采用的方案是在Application中显式的调用GlobalObserver.manuallyInstall()方法
     * */
    public static void manuallyInstall(Application application) {
        Toast.makeText(application, "正在初始化LeakGuardian", Toast.LENGTH_SHORT).show();
        InternalLeakGuardian.getInstance().init(application, objectObserver);
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
