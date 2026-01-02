package com.example.afinal.ui.archive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.data.DiaryEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryCardAdapter extends RecyclerView.Adapter<DiaryCardAdapter.ViewHolder> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 EEEE", Locale.getDefault());
    private List<DiaryEntry> entries = new ArrayList<>();

    public void setEntries(List<DiaryEntry> entries) {
        this.entries = entries != null ? entries : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryEntry entry = entries.get(position);
        holder.tvDate.setText(dateFormat.format(new Date(entry.getTimestamp())));
        holder.tvMood.setText(getMoodLabel(entry.getMood()));
        holder.tvContent.setText(entry.getContent());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    private String getMoodLabel(String moodKey) {
        switch (moodKey) {
            case "grateful": return "感恩";
            case "energetic": return "有动力";
            case "calm": return "平静";
            case "happy": return "开心";
            case "loved": return "被爱";
            case "accomplished": return "有收获";
            case "connected": return "有效社交";
            case "hopeful": return "充满希望";
            default: return "小确幸";
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvMood; // 注意：ID 是 tvMood，不是 tpvMood
        TextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMood = itemView.findViewById(R.id.tvMood);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}