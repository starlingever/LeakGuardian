/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian
 * @Project：LeakGuardian
 * @name：LeakGuardianApplication
 * @Date：2024/1/9 13:26
 * @Filename：LeakGuardianApplication
 */
package com.starlingever.leakguardian;


import android.app.Application;

import com.starlingever.objectobserver.GlobalObserver;

public class LeakGuardianApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalObserver.manuallyInstall(this);
    }
}
