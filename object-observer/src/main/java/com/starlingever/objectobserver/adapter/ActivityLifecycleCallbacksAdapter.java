/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.adapter
 * @Project：LeakGuardian
 * @name：ActivityLifecycleCallbacksAdapter
 * @Date：2024/1/5 13:57
 * @Filename：ActivityLifecycleCallbacksAdapter
 */

/**
 * Adapter模式，把实现ActivityLifecycleCallbacks转换为继承抽象Adapter的形式，降低代码冗余度
 */
package com.starlingever.objectobserver.adapter;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ActivityLifecycleCallbacksAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
