/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：HeapDumper
 * @Date：2024/1/8 10:24
 * @Filename：HeapDumper
 */
package com.starlingever.objectobserver;


import java.io.File;

public interface HeapDumper {

    File dumpHeap();
}
