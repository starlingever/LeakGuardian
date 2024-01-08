/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：GcTrigger
 * @Date：2024/1/8 10:08
 * @Filename：GcTrigger
 */
package com.starlingever.objectobserver;


public interface GcTrigger {
    void runGc();

    GcTrigger DEFAULT = new GcTrigger() {
        @Override
        public void runGc() {
            Runtime.getRuntime().gc();
            enqueueReferences();
            System.runFinalization();
        }

        private void enqueueReferences() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new AssertionError();
            }
        }
    };
};



