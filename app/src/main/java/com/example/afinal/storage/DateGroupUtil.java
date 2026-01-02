package com.example.afinal.storage;

import com.example.afinal.data.DiaryEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 工具类：将 DiaryEntry 按 "yyyy-MM-dd" 分组并统计数量
 */
public class DateGroupUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * 将日记列表按日期分组
     * @return Map<日期字符串, 日记数量>
     */
    public static Map<String, Integer> groupByDateKey(List<DiaryEntry> entries) {
        Map<String, Integer> countMap = new HashMap<>();
        for (DiaryEntry entry : entries) {
            String dateKey = DATE_FORMAT.format(new Date(entry.getTimestamp()));
            countMap.put(dateKey, countMap.getOrDefault(dateKey, 0) + 1);
        }
        return countMap;
    }
}