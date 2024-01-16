package com.starlingever.leakguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LeakActivityDemo2 extends AppCompatActivity implements View.OnClickListener {
    Button desButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_demo2);
        desButton = findViewById(R.id.destroy);
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