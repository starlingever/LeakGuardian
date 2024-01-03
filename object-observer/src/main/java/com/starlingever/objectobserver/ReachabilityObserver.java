/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：ReachabilityObserver
 * @Date：2024/1/3 14:59
 * @Filename：ReachabilityObserver
 */
package com.starlingever.objectobserver;


public interface ReachabilityObserver {
    void observe(Object object, String description);
}
