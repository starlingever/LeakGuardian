/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：ActivityObserver
 * @Date：2024/1/3 15:06
 * @Filename：ActivityObserver
 */
package com.starlingever.objectobserver;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.starlingever.objectobserver.adapter.ActivityLifecycleCallbacksAdapter;
import com.starlingever.objectobserver.utils.GlobalData;

public final class ActivityObserver implements InsatllableObserver {
    private static volatile ActivityObserver activityObserver = null;
    private final Application application;
    private final ReachabilityObserver reachabilityObserver;

    private ActivityObserver(Application application, ReachabilityObserver reachabilityObserver) {
        this.application = application;
        this.reachabilityObserver = reachabilityObserver;
    }

    // 双重校验锁的方式获取ActivityObserver的单例
    public static ActivityObserver getInstance(Application application, ReachabilityObserver reachabilityObserver) {
        if (activityObserver == null) {
            synchronized (ActivityObserver.class) {
                if (activityObserver == null) {
                    activityObserver = new ActivityObserver(application, reachabilityObserver);
                }
            }
        }
        return activityObserver;
    }

    @Override
    public void install() {
        Log.d(GlobalData.INIT, this.getClass().getName() + " 监视器已经安装");
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                super.onActivityDestroyed(activity);
                reachabilityObserver.observe(activity, "对activity进行销毁监控");
            }
        });
    }

    @Override
    public void uninstall() {
        // TODO 解除绑定的逻辑,正常来说不太需要
    }
}
