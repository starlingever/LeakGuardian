/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian
 * @Project：LeakGuardian
 * @name：TestSingleInstance
 * @Date：2024/1/9 13:25
 * @Filename：TestSingleInstance
 */
package com.starlingever.leakguardian;


import android.content.Context;

public class TestSingleInstance {
    private Context context;
    private static volatile TestSingleInstance testSingleInstance;

    private TestSingleInstance(Context context) {
        this.context = context;
    }

    public static TestSingleInstance getSingleInstace(Context context) {
        if (testSingleInstance == null) {
            synchronized (TestSingleInstance.class) {
                if (testSingleInstance == null) {
                    testSingleInstance = new TestSingleInstance(context);
                }
            }
        }
        return testSingleInstance;
    }
}
