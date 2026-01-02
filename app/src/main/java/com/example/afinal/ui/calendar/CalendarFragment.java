package com.example.afinal.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.afinal.R;
import com.example.afinal.data.DiaryEntry;
import com.example.afinal.storage.DiaryFileStorage;
import com.example.afinal.storage.DateGroupUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private Calendar currentMonth = Calendar.getInstance();
    private GridView gridView;
    private TextView tvMonthTitle;
    private DiaryFileStorage storage;

    private final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = view.findViewById(R.id.gridView);
        tvMonthTitle = view.findViewById(R.id.tvMonthTitle);
        storage = new DiaryFileStorage(requireContext());

        // 初始化日历（当前月）
        setupCalendar();

        // 上/下月按钮
        view.findViewById(R.id.btnPrev).setOnClickListener(v -> changeMonth(-1));
        view.findViewById(R.id.btnNext).setOnClickListener(v -> changeMonth(1));
    }

    private void setupCalendar() {
        tvMonthTitle.setText(monthFormat.format(currentMonth.getTime()));
        loadDiaryAndRefresh();
    }

    private void changeMonth(int delta) {
        currentMonth.add(Calendar.MONTH, delta);
        setupCalendar();
    }

    private void loadDiaryAndRefresh() {
        storage.loadAll(entries -> {
            // 1. 分组统计
            Map<String, Integer> countMap = DateGroupUtil.groupByDateKey(entries);

            // 2. 生成当前月日期列表（含前后月填充，共6周=42天）
            List<Date> dates = generateCalendarDates(currentMonth);

            // 3. 更新适配器
            CalendarAdapter adapter = new CalendarAdapter(requireContext(), dates, countMap);
            gridView.setAdapter(adapter);
        });
    }

    /**
     * 生成当前月完整日历（7列×6行=42天）
     */
    private List<Date> generateCalendarDates(Calendar monthCal) {
        List<Date> dates = new ArrayList<>();

        // 复制避免修改原 Calendar
        Calendar cal = (Calendar) monthCal.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // 找到本月第一天是星期几（周日=1, 周一=2... 周六=7）
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int offset = firstDayOfWeek - Calendar.SUNDAY; // 假设周日为第一列

        // 向前补上月日期
        cal.add(Calendar.DAY_OF_MONTH, -offset);
        for (int i = 0; i < 42; i++) {
            dates.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }
}