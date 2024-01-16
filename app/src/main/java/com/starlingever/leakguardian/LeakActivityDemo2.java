package com.starlingever.leakguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LeakActivityDemo2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_demo2);
    }
}