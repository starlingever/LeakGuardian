/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：HeapDirectoryProvider
 * @Date：2024/1/8 13:52
 * @Filename：HeapDirectoryProvider
 */
package com.starlingever.leakguardian_core;


import androidx.annotation.NonNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public interface HeapDirectoryProvider {
    @NonNull
    List<File> listFiles(@NonNull FilenameFilter filter);

    File newHeapDumpFile();

    void clearHeapDirectory();

}
