package com.example.afinal.ui.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.afinal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 日历 GridView 适配器
 */
public class CalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<Date> dates; // 当前月所有日期（含前后月填充）
    private final Map<String, Integer> diaryCountMap; // 日期 -> 日记数量
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
    private final SimpleDateFormat keyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // 背景色分级
    private final int[] BG_COLORS = {
            Color.TRANSPARENT,          // 0 篇
            0x40FF9800,                 // 1 篇（25% 透明度）
            0x80FF9800,                 // 2 篇（50%）
            0xFF9800                    // 3+ 篇（不透明）
    };

    public CalendarAdapter(Context context, List<Date> dates, Map<String, Integer> diaryCountMap) {
        this.context = context;
        this.dates = dates;
        this.diaryCountMap = diaryCountMap;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Date getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
            holder = new ViewHolder();
            holder.tvDay = convertView.findViewById(R.id.tvDay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Date date = dates.get(position);
        String dayText = dayFormat.format(date);
        String dateKey = keyFormat.format(date);

        // 设置日期数字
        holder.tvDay.setText(dayText);

        // 获取日记数量（默认 0）
        int count = diaryCountMap.getOrDefault(dateKey, 0);

        // 确定背景色级别
        int colorIndex = Math.min(count, 3); // 0,1,2,3+
        int bgColor = BG_COLORS[colorIndex];

        // 应用背景色
        holder.tvDay.setBackgroundColor(bgColor);


        return convertView;
    }

    static class ViewHolder {
        TextView tvDay;
    }
}