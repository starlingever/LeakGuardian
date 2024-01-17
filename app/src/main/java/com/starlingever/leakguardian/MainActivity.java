package com.starlingever.leakguardian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonLeakActivity;
    Button buttonNoLeakActivity;
    Button buttonAbout;
    Button buttonLeakService;
    Button buttonLeakFragment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button = (Button) findViewById(R.id.testButton);
//        button.setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this,LeakActivityDemo.class);
//            startActivity(intent);
//        });
        buttonLeakActivity = findViewById(R.id.button_leak_activity);
        buttonNoLeakActivity = findViewById(R.id.button_no_leak_activity);
        buttonAbout = findViewById(R.id.about);
        buttonLeakService = findViewById(R.id.service);
        buttonLeakFragment = findViewById(R.id.fragment1);

        buttonLeakActivity.setOnClickListener(this);
        buttonNoLeakActivity.setOnClickListener(this);
        buttonAbout.setOnClickListener(this);
        buttonLeakService.setOnClickListener(this);
        buttonLeakFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLeakActivity) {
            showDialog("确认跳转", "确定要跳转到目标页面吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LeakActivityDemo.class);
                    startActivity(intent);
                    showToast("当前页面可能泄漏");
                }
            });
        } else if (v == buttonNoLeakActivity) {
            showDialog("确认跳转", "确定要跳转到目标页面吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LeakActivityDemo2.class);
                    startActivity(intent);
                    showToast("当前页面不会泄漏");
                }
            });
        } else if (v == buttonAbout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于"); // 设置对话框标题
            builder.setMessage("LeakGuardian能够检测内存泄漏，自动转储堆快照，并最终进行泄漏对象的引用链分析，输出分析报告");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v == buttonLeakService) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("SERVICE"); // 设置对话框标题
            builder.setMessage("请继续关注后续的版本~");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v == buttonLeakFragment) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("FRAGMENT"); // 设置对话框标题
            builder.setMessage("请继续关注后续的版本");
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", positiveClickListener)
                .setNegativeButton("取消", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}