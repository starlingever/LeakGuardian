/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：InsatllableObserver
 * @Date：2024/1/3 15:01
 * @Filename：InsatllableObserver
 */
package com.starlingever.objectobserver;


public interface InsatllableObserver {
    void install();

    void uninstall();
}
