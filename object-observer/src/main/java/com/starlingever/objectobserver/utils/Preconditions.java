/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.utils
 * @Project：LeakGuardian
 * @name：Preconditions
 * @Date：2024/1/3 16:30
 * @Filename：Preconditions
 */
package com.starlingever.objectobserver.utils;

final public class Preconditions {
    static <T> T checkNotNull(T instance, String name) {
        if (instance == null) {
            throw new NullPointerException(name + " 必须不能为空");
        }
        return instance;
    }
}
