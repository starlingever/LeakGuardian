/**
 *@Author：LingSida
 *@Package：com.starlingever.objectobserver.adapter
 *@Project：LeakGuardian
 *@name：KeyedWeakedReferenceFinderHelper
 *@Date：2024/1/15  10:42
 *@Filename：KeyedWeakedReferenceFinderHelper
 */
package com.starlingever.objectobserver.adapter


import android.util.Log
import shark.HeapGraph
import shark.LeakingObjectFinder
import shark.ObjectInspectors.KEYED_WEAK_REFERENCE

object KeyedWeakReferenceFinderHelper : LeakingObjectFinder {
    override fun findLeakingObjectIds(graph: HeapGraph): Set<Long> =
        findKeyedWeakReferences(graph)
            .filter { it.hasReferent || it.isRetained }
            .map { it.referent.value }
            .toSet()

//    fun heapDumpUptimeMillis(graph: HeapGraph): Long? {
//        val keyedWeakReferenceClass =
//            graph.findClassByName("objectobserver.GlobalObserver")
//        Log.d("AAA", keyedWeakReferenceClass.toString())
//        val heapDumpUptimeMillis = if (keyedWeakReferenceClass == null) {
//            Log.d("AAA", "heapDumpUptimeMillis is null")
//            null
//        } else {
//            Log.d(
//                "AAA",
//                keyedWeakReferenceClass["heapDumpUptimeMillis"]?.value?.asLong.toString()
//            )
//            keyedWeakReferenceClass["heapDumpUptimeMillis"]?.value?.asLong
//        }
//        if (heapDumpUptimeMillis == null) {
//            println("d")
//        }
//        return graph.context.getOrPut("heapDumpUptimeMillis") {
//            heapDumpUptimeMillis
//        }
//    }

    internal fun findKeyedWeakReferences(graph: HeapGraph): List<KeyedWeakReferenceMirrorHelper> {
        val keyedWeakReferenceClass =
            graph.findClassByName("com.starlingever.objectobserver.adapter.KeyedWeakReference")
        Log.d("AAAkeyedWeakReferenceClass", keyedWeakReferenceClass.toString())
        val keyedWeakReferenceClassId = keyedWeakReferenceClass?.objectId ?: 0
        Log.d("AAAkeyedWeakReferenceClassId", keyedWeakReferenceClassId.toString())
        val legacyKeyedWeakReferenceClassId =
            graph.findClassByName("com.starlingever.objectobserver.adapter.KeyedWeakReference")?.objectId
                ?: 0
        Log.d("AAAlegacyKeyedWeakReferenceClassId", legacyKeyedWeakReferenceClassId.toString())
//        val heapDumpUptimeMillis = heapDumpUptimeMillis(graph)

        val addedToContext: List<KeyedWeakReferenceMirrorHelper> = graph.instances
            .filter { instance ->
                instance.instanceClassId == keyedWeakReferenceClassId || instance.instanceClassId == legacyKeyedWeakReferenceClassId
            }
            .map {
                KeyedWeakReferenceMirrorHelper.fromInstance(
                    it, 0
                )
            }
            .toList()
        Log.d("AAAaddedToContext", addedToContext.toString())
        graph.context[KEYED_WEAK_REFERENCE.name] = addedToContext
        println(graph.context[KEYED_WEAK_REFERENCE.name])
        println(graph.context.getOrPut(KEYED_WEAK_REFERENCE.name) {
            addedToContext
        })
        return graph.context.getOrPut(KEYED_WEAK_REFERENCE.name) {
            addedToContext
        }
    }
}