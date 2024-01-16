/**
 *@Author：LingSida
 *@Package：com.starlingever.objectobserver.adapter
 *@Project：LeakGuardian
 *@name：MainAnalyzerHelperKT
 *@Date：2024/1/14  20:54
 *@Filename：MainAnalyzerHelperKT
 */
package com.starlingever.objectobserver.adapter

import android.app.Application
import shark.AndroidMetadataExtractor
import shark.AndroidObjectInspectors
import shark.AndroidReferenceMatchers
import shark.ConstantMemoryMetricsDualSourceProvider
import shark.DualSourceProvider
import shark.HeapAnalysis
import shark.HeapAnalyzer
import shark.HprofHeapGraph.Companion.openHeapGraph
import shark.KeyedWeakReferenceFinder
import shark.LeakingObjectFinder
import shark.MetadataExtractor
import shark.ObjectInspector
import shark.ProguardMappingReader
import shark.ReferenceMatcher
import shark.ThrowingCancelableFileSourceProvider
import java.io.File
import java.io.IOException


class MainAnalyzerHelperKT {
    private val FILE_NAME = "leakGuardianMapping.txt"


    fun getSourceProvider(
        heapDumpFile: File,
        isCanceled: Boolean
    ): DualSourceProvider {
        return ConstantMemoryMetricsDualSourceProvider(
            ThrowingCancelableFileSourceProvider(
                heapDumpFile
            ) {
                if (isCanceled) {
                    throw RuntimeException("Analysis canceled")
                }
            })
    }

    fun getProGuardMappingReader(application: Application): ProguardMappingReader? {
        try {
            return ProguardMappingReader(application.assets.open(FILE_NAME))
        } catch (e: IOException) {
            return null
        }
    }

    fun getCloseableHeapGraph(
        sourceProvider: DualSourceProvider,
        proguardMappingReader: ProguardMappingReader
    ): Any {
        val closeableHeapGraph = try {
            sourceProvider.openHeapGraph(proguardMapping = proguardMappingReader?.readProguardMapping())
        } catch (throwable: Throwable) {
            println("null CloseableHeapGraph")
        }
        return closeableHeapGraph;
    }

    fun getLeakingObjectFinder(): LeakingObjectFinder {
        return KeyedWeakReferenceFinder
//        return KeyedWeakReferenceFinderHelper
    }

    fun getReferenceMatchers(): List<ReferenceMatcher> {
        return AndroidReferenceMatchers.appDefaults
    }

    fun getObjectInspectors(): List<ObjectInspector> {
        return AndroidObjectInspectors.appDefaults
    }

    fun getMetadataExtractor(): MetadataExtractor {
        return AndroidMetadataExtractor
    }

}