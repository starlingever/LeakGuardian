package com.starlingever.leakguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LeakActivityDemo extends AppCompatActivity implements View.OnClickListener{
    Button desButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_demo);
        TestSingleInstance singleInstance = TestSingleInstance.getSingleInstace(LeakActivityDemo.this);
        desButton = findViewById(R.id.destroy1);
        desButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == desButton) {
            Toast.makeText(this,"页面被销毁",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}