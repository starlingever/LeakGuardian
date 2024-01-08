/**
 * @Author：LingSida
 * @Package：com.starlingever.leakguardian_core
 * @Project：LeakGuardian
 * @name：LeakGuardianDirectoryProvider
 * @Date：2024/1/8 13:56
 * @Filename：LeakGuardianDirectoryProvider
 */
package com.starlingever.leakguardian_core;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.starlingever.objectobserver.utils.GlobalData;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class LeakGuardianDirectoryProvider implements HeapDirectoryProvider {
    private final Context context;
    // 最大允许保存的文件个数
    private static final int MAX_STORED_HEAP_DUMPS = 7;
    // 保存文件的后缀名
    private static final String HPROF_SUFFIX = ".hprof";

    private static final String PENDING_HEAPDUMP_SUFFIX = "_pending" + HPROF_SUFFIX;
    // 最多分析十分钟
    private static final int ANALYSIS_MAX_DURATION_MS = 10 * 60 * 1000;
    // 最大堆大小
    private final int maxStoredHeapDumps;
    // 外部写入权限是否被授予
    private volatile boolean writeExternalStorageGranted;
    // 是否允许显示Notification
    private volatile boolean permissionNotificationDisplayed;


    public LeakGuardianDirectoryProvider(Context context, int maxStoredHeapDumps) {
        if (maxStoredHeapDumps < 1) {
            throw new IllegalStateException("maxStoredHeapDumps must be at least 1");
        }
        this.context = context.getApplicationContext();
        this.maxStoredHeapDumps = maxStoredHeapDumps;
    }

    @NonNull
    @Override
    public List<File> listFiles(@NonNull FilenameFilter filter) {
        List<File> files = new ArrayList<>();
        assert externalStorageDirectory() != null;
        File[] externalFiles = externalStorageDirectory().listFiles(filter);
        if (externalFiles != null) {
            files.addAll(Arrays.asList(externalFiles));
        }

        File[] appFiles = appStorageDirectory().listFiles(filter);
        if (appFiles != null) {
            files.addAll(Arrays.asList(appFiles));
        }
        return files;
    }

    private File appStorageDirectory() {
        File appFilesDirectory = context.getFilesDir();
        return new File(appFilesDirectory, "leakguardian");
    }

    private File externalStorageDirectory() {
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        return new File(downloadsDirectory, "leakguardian-" + context.getPackageName());
    }

    @Override
    public File newHeapDumpFile() {
        List<File> pendingHeapDumps = listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(PENDING_HEAPDUMP_SUFFIX);
            }
        });
        cleanupOldHeapDumps();
        File storageDirectory = externalStorageDirectory();
        if (!directoryWritableAfterMkdirs(storageDirectory)) {
            storageDirectory = appStorageDirectory();
        }

        if (!directoryWritableAfterMkdirs(appStorageDirectory())) {
            Log.d(GlobalData.DUMP,"内外存储都不允许转存");
            return null;
        }
        return new File(storageDirectory, UUID.randomUUID().toString() + PENDING_HEAPDUMP_SUFFIX);
    }


    private boolean hasStoragePermission() {
        if (writeExternalStorageGranted) {
            return true;
        }
        writeExternalStorageGranted =
                context.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        return writeExternalStorageGranted;
    }

    private boolean directoryWritableAfterMkdirs(File directory) {
        boolean success = directory.mkdirs();
        return (success || directory.exists()) && directory.canWrite();
    }

    private void cleanupOldHeapDumps() {
        /*依据时间先后顺序把旧文件删除*/
        List<File> hprofFiles = listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(HPROF_SUFFIX);
            }
        });
        // 要删除的文件个数
        int filesToRemove = hprofFiles.size() - maxStoredHeapDumps;
        if (filesToRemove > 0) {
            // 先按照时间对文件优先级进行排序
            Log.d("Removing %d heap dumps", String.valueOf(filesToRemove));
            // Sort with oldest modified first.
            Collections.sort(hprofFiles, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.valueOf(lhs.lastModified()).compareTo(rhs.lastModified());
                }
            });
            for (int i = 0; i < filesToRemove; i++) {
                boolean deleted = hprofFiles.get(i).delete();
                if (!deleted) {
                    Log.d("删除不了文件%s", hprofFiles.get(i).getPath());
                }
            }
        }
    }


    @Override
    public void clearHeapDirectory() {
        List<File> allFilesExceptPending = listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return !filename.endsWith(PENDING_HEAPDUMP_SUFFIX);
            }
        });
        for (File file : allFilesExceptPending) {
            boolean deleted = file.delete();
            if (!deleted) {
                Log.d("删除不了文件 %s", file.getPath());
            }
        }

    }


}
