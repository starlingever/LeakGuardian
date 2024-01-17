/**
 *@Author：LingSida
 *@Package：com.starlingever.objectobserver.adapter
 *@Project：LeakGuardian
 *@name：HeapAnalysisGetHelper
 *@Date：2024/1/17  11:26
 *@Filename：HeapAnalysisGetHelper
 */
package com.starlingever.objectobserver.adapter

import shark.HeapAnalysis


object HeapAnalysisGetHelper {

    fun getApplicationLeaksSize(
        heapAnalysis: HeapAnalysis
    ): String {
        return heapAnalysis.toString();
    }
}