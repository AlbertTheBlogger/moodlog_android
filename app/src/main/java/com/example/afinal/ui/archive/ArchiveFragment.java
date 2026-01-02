package com.example.afinal.ui.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.data.DiaryEntry;
import com.example.afinal.storage.DiaryFileStorage;
import com.example.afinal.utils.RandomSelector;

import java.util.List;

public class ArchiveFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiaryCardAdapter adapter;
    private DiaryFileStorage storage;
    private List<DiaryEntry> allEntries = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        Button btnRefresh = view.findViewById(R.id.btnRefresh);

        adapter = new DiaryCardAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        storage = new DiaryFileStorage(requireContext());

        // 首次加载
        loadAllDiaries();

        btnRefresh.setOnClickListener(v -> showRandomBatch());
    }

    private void loadAllDiaries() {
        storage.loadAll(entries -> {
            allEntries = entries;
            showRandomBatch(); // 加载完成后立即展示第一批
        });
    }

    private void showRandomBatch() {
        if (allEntries == null || allEntries.isEmpty()) {
            adapter.setEntries(null);
            Toast.makeText(getContext(), "暂无日记", Toast.LENGTH_SHORT).show();
            return;
        }

        // 随机抽取 3~5 条
        int count = 3 + (int) (Math.random() * 3); // 3, 4, or 5
        List<DiaryEntry> randomEntries = RandomSelector.selectRandom(allEntries, count);
        adapter.setEntries(randomEntries);
    }
}