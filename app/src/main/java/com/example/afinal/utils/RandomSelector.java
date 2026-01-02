package com.example.afinal.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 随机选择工具类
 */
public class RandomSelector {

    private static final Random random = new Random();

    /**
     * 从列表中随机选择指定数量的不重复元素
     * @param list 源列表（不可为 null）
     * @param count 期望数量（实际返回 min(count, list.size())）
     * @return 随机子列表
     */
    public static <T> List<T> selectRandom(List<T> list, int count) {
        if (list == null || list.isEmpty() || count <= 0) {
            return new ArrayList<>();
        }

        // 创建副本避免修改原列表
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy, random);

        int actualCount = Math.min(count, copy.size());
        return copy.subList(0, actualCount);
    }
}