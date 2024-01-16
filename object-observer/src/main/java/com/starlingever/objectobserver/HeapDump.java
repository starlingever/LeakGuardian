/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：HeapDump
 * @Date：2024/1/9 16:47
 * @Filename：HeapDump
 */
package com.starlingever.objectobserver;


import java.io.File;
import java.io.Serializable;

public class HeapDump implements Serializable {


    public interface Listener {
        void analyze(HeapDump heapDump);

        Listener NONE = new Listener() {
            @Override
            public void analyze(HeapDump heapDump) {

            }
        };
    }

    public final File heapDumpFile;

    private final String uniqueId;

    public HeapDump(File heapDumpFile, String uniqueId) {
        this.heapDumpFile = heapDumpFile;
        this.uniqueId = uniqueId;
    }
}
