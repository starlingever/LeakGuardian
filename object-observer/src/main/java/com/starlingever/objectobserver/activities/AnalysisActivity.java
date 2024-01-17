package com.starlingever.objectobserver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.starlingever.objectobserver.R;
import com.starlingever.objectobserver.adapter.HeapAnalysisGetHelper;

import shark.HeapAnalysis;

public class AnalysisActivity extends AppCompatActivity {
    TextView anaText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_layout);
        anaText = findViewById(R.id.analysis);
        Intent intent = getIntent();
        // 检查 Intent 中是否包含 "analysis" 键
        if (intent.hasExtra("analysis")) {
            // 从 Intent 中获取分析数据
            HeapAnalysis heapAnalysis = (HeapAnalysis) intent.getSerializableExtra("analysis");
            // 在这里使用 heapAnalysis 对象进行相应的操作
            String keyInfo = heapAnalysis.toString();
            anaText.setText(keyInfo);
        }
    }
}