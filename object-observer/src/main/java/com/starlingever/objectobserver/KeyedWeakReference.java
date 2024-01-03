/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：KeyedWeakReference
 * @Date：2024/1/3 16:14
 * @Filename：KeyedWeakReference
 */
package com.starlingever.objectobserver;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


public class KeyedWeakReference extends WeakReference<Object> {
    public final String key;
    public final String name;

    /*弱引用队列的构造器，未加判空处理 checkNotNull*/
    public KeyedWeakReference(Object referent, ReferenceQueue<Object> referenceQueue, String key, String name) {
        super(referent, referenceQueue);
        this.key = key;
        this.name = name;
    }

}