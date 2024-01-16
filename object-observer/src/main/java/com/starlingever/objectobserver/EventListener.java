/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver
 * @Project：LeakGuardian
 * @name：EventListener
 * @Date：2024/1/10 15:49
 * @Filename：EventListener
 */
package com.starlingever.objectobserver;


import java.io.File;
import java.io.Serializable;

public interface EventListener {
    void onEvent(Event event);

    class Event implements Serializable {
        public final String uniqueId;

        public Event(String uniqueId) {
            this.uniqueId = uniqueId;
        }
    }

    class HeapDump extends Event {
        public final File file;
        public HeapDump(String uniqueId, File file) {
            super(uniqueId);
            this.file = file;
        }
    }

    class HeapDumpFailed extends Event {
        public HeapDumpFailed(String uniqueId) {
            super(uniqueId);
        }
    }

    class HeapAnalysisProgress extends Event {
        final double progressPercent;

        public HeapAnalysisProgress(String uniqueId, double progressPercent) {
            super(uniqueId);
            this.progressPercent = progressPercent;
        }
    }
}
