/**
 * @Author：LingSida
 * @Package：com.starlingever.objectobserver.analyzer
 * @Project：LeakGuardian
 * @name：MainAnalyzer
 * @Date：2024/1/11 10:45
 * @Filename：MainAnalyzer
 */
package com.starlingever.objectobserver.analyzer;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.starlingever.objectobserver.HeapDump;
import com.starlingever.objectobserver.InternalLeakGuardian;
import com.starlingever.objectobserver.adapter.MainAnalyzerHelperKT;
import com.starlingever.objectobserver.utils.GlobalData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Set;

import shark.CloseableHeapGraph;
import shark.ConstantMemoryMetricsDualSourceProvider;
import shark.DualSourceProvider;
import shark.HeapAnalysis;
import shark.HeapAnalyzer;
import shark.HprofHeapGraph;
import shark.HprofIndex;
import shark.HprofRecordTag;
import shark.KeyedWeakReferenceFinder;
import shark.MetadataExtractor;
import shark.OnAnalysisProgressListener;
import shark.ProguardMapping;
import shark.ProguardMappingReader;
import shark.ThrowingCancelableFileSourceProvider;

public class MainAnalyzer {
    private final String PROGUARD_MAPPING_FILE_NAME = "leakGuardianMapping.txt";
    public File heapDumpFile;
    private static MainAnalyzer mainAnalyzer;

    static {
        try {
            mainAnalyzer = new MainAnalyzer();
        } catch (IOException e) {
            Log.d(GlobalData.ANAL, "初始化MainAnalyzer之问题1");
            throw new RuntimeException(e);
        } catch (ParseException e) {
            Log.d(GlobalData.ANAL, "初始化MainAnalyzer之问题2");
            throw new RuntimeException(e);
        }
    }

    // boolean isCanceled = false;
    Application application = InternalLeakGuardian.getInstance().application;
    // ProguardMappingReader proguardMappingReader = new ProguardMappingReader(application.getAssets().open(PROGUARD_MAPPING_FILE_NAME));
    // ProguardMapping proguardMapping = proguardMappingReader.readProguardMapping();


    private HeapAnalyzer heapAnalyzer = new HeapAnalyzer(new OnAnalysisProgressListener() {
        @Override
        public void onAnalysisProgress(@NonNull Step step) {
            // 这里暂时没有重写的逻辑
        }
    });


    private MainAnalyzer() throws IOException, ParseException {
    }

    public static MainAnalyzer getInstance() {
        return mainAnalyzer;
    }

    public HeapAnalysis runAnalysis(HeapDump heapDump) throws IOException, ParseException {
        Log.d(GlobalData.ANAL, "正进行到了Main分析器的runAnalysis方法");
        heapDumpFile = heapDump.heapDumpFile;
        if (heapDumpFile.exists()) {
            return analyzeHeap(heapDumpFile);
        } else {
            Log.d(GlobalData.DUMP, "要分析的堆文件不存在！");
            return null;
        }
    }

    private HeapAnalysis analyzeHeap(File heapDumpFile) {
        Log.d(GlobalData.ANAL, "进入最终分析环节1");
        MainAnalyzerHelperKT mainAnalyzerHelperKT = new MainAnalyzerHelperKT();
        CloseableHeapGraph heapGraph = (CloseableHeapGraph) mainAnalyzerHelperKT.getCloseableHeapGraph(mainAnalyzerHelperKT.getSourceProvider(heapDumpFile,false), mainAnalyzerHelperKT.getProGuardMappingReader(application));
        Log.d(GlobalData.ANAL, "进入最终分析环节2");
        HeapAnalysis heapAnalysis = heapAnalyzer.analyze(
                heapDumpFile,
                heapGraph, // 直接将closeableGraph作为参数传递给analyze方法
                mainAnalyzerHelperKT.getLeakingObjectFinder(), // 这里需要自定义自己的 KeyedWeakReferenceFinder
                mainAnalyzerHelperKT.getReferenceMatchers(),
                true,
                mainAnalyzerHelperKT.getObjectInspectors(),
                mainAnalyzerHelperKT.getMetadataExtractor());
        Log.d(GlobalData.ANAL, "堆分析结果为" + heapAnalysis);
        return heapAnalysis;
    }
}
