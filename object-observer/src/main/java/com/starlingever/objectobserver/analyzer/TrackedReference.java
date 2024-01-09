/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：TrackedReference
 * @Date：2024/1/9 17:38
 * @Filename：TrackedReference
 */
package com.starlingever.objectobserver.analyzer;


public class TrackedReference {

    public final String key;
    public final String Name;
    public final String className;

    public TrackedReference(String key, String name, String className) {
        this.key = key;
        Name = name;
        this.className = className;
    }
}
