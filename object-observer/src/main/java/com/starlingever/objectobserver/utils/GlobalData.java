/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.utils
 * @Project：LeakGuardian
 * @name：GlobalData
 * @Date：2024/1/5 14:24
 * @Filename：GlobalData
 */
package com.starlingever.objectobserver.utils;


public class GlobalData {

    public static String INIT = "泄漏守卫者_正在初始化";

    public static String OBS = "泄漏守卫者_正在观测";

    public static String DUMP = "泄漏守卫者_正在DumpHeap";

    public static String ANAL = "泄漏守卫者_正在分析";

    public static String UI = "泄漏守卫者_正在可视化";


    private GlobalData() {
    }
}
