package com.example.afinal.storage;

import android.content.Context;
import android.util.Log;

import com.example.afinal.AppExecutors;
import com.example.afinal.data.DiaryEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 日记本地存储：基于 JSON 文件的读写
 * - 文件名：entries.json
 * - 格式：List<DiaryEntry> 的 JSON 数组
 */
public class DiaryFileStorage {

    private static final String FILE_NAME = "entries.json";
    private static final String TAG = "DiaryFileStorage";

    private final Context context;
    private final Gson gson;

    public DiaryFileStorage(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
    }

    /**
     * 同步加载所有日记（仅用于测试！正常应通过回调）
     * 注意：此方法会阻塞，禁止在主线程调用
     */
    public List<DiaryEntry> loadAllSync() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            if (json.length() == 0) {
                return new ArrayList<>();
            }

            Type listType = new TypeToken<List<DiaryEntry>>(){}.getType();
            List<DiaryEntry> entries = gson.fromJson(json.toString(), listType);
            return entries != null ? entries : new ArrayList<>();

        } catch (IOException e) {
            Log.e(TAG, "Failed to load diary entries", e);
            return new ArrayList<>();
        }
    }

    /**
     * 异步加载所有日记（推荐方式）
     *
     * @param callback 回调（在主线程执行）
     */
    public void loadAll(OnLoadEntriesCallback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<DiaryEntry> entries = loadAllSync();
            // 切回主线程
            AppExecutors.getInstance().mainThread().execute(() ->
                    callback.onLoaded(entries)
            );
        });
    }

    /**
     * 异步添加一条日记
     *
     * @param entry 要保存的日记
     * @param callback 可选回调（如需通知 UI）
     */
    public void addEntry(DiaryEntry entry, Runnable callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<DiaryEntry> entries = loadAllSync();
            entries.add(entry);
            saveToFile(entries);

            if (callback != null) {
                AppExecutors.getInstance().mainThread().execute(callback);
            }
        });
    }

    /**
     * 全量保存日记列表到文件（私有，仅内部调用）
     */
    private void saveToFile(List<DiaryEntry> entries) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            gson.toJson(entries, osw);
            osw.flush();

        } catch (IOException e) {
            Log.e(TAG, "Failed to save diary entries", e);
        }
    }

    // 回调接口
    public interface OnLoadEntriesCallback {
        void onLoaded(List<DiaryEntry> entries);
    }
}