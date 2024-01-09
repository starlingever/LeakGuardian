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

public class HeapDump {


    public interface Listener {
        void analyze(HeapDump heapDump);

        Listener NONE = new Listener() {
            @Override
            public void analyze(HeapDump heapDump) {

            }
        };
    }

    public final File heapDumpFile;

    public final String referKey;

    public final String referName;

    public HeapDump(File heapDumpFile, String referKey, String referName) {
        this.heapDumpFile = heapDumpFile;
        this.referKey = referKey;
        this.referName = referName;
    }
}
