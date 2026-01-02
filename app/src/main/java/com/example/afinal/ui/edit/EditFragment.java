package com.example.afinal.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton; // ✅ 关键导入

import com.example.afinal.R;
import com.example.afinal.data.DiaryEntry;
import com.example.afinal.storage.DiaryFileStorage;

public class EditFragment extends Fragment {

    private EditText editTextContent;
    private String selectedMood = "grateful";
    private final int[] moodButtonIds = {
            R.id.btn_grateful, R.id.btn_energetic, R.id.btn_calm, R.id.btn_happy,
            R.id.btn_loved, R.id.btn_accomplished, R.id.btn_connected, R.id.btn_hopeful
    };
    private final String[] moodKeys = {
            "grateful", "energetic", "calm", "happy",
            "loved", "accomplished", "connected", "hopeful"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextContent = view.findViewById(R.id.editTextContent);
        MaterialButton buttonSave = view.findViewById(R.id.buttonSave); // ✅

        for (int i = 0; i < moodButtonIds.length; i++) {
            final int index = i;
            MaterialButton btn = view.findViewById(moodButtonIds[i]); // ✅
            btn.setOnClickListener(v -> {
                selectedMood = moodKeys[index];
                highlightSelectedButton(view);
            });
        }
        highlightSelectedButton(view);

        buttonSave.setOnClickListener(v -> saveDiary());
    }

    private void highlightSelectedButton(View root) {
        for (int i = 0; i < moodButtonIds.length; i++) {
            MaterialButton btn = root.findViewById(moodButtonIds[i]); // ✅
            if (moodKeys[i].equals(selectedMood)) {
                // 选中：主色边框 + 主色文字
                btn.setStrokeColorResource(R.color.colorPrimary);
                btn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                // 未选中：灰色边框 + 次要文字
                btn.setStrokeColorResource(android.R.color.darker_gray);
                btn.setTextColor(getResources().getColor(R.color.text_secondary, null));
            }
        }
    }

    private void saveDiary() {
        String content = editTextContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(getContext(), "请输入日记内容", Toast.LENGTH_SHORT).show();
            return;
        }

        DiaryEntry entry = new DiaryEntry(content, selectedMood, System.currentTimeMillis());
        DiaryFileStorage storage = new DiaryFileStorage(requireContext());

        storage.addEntry(entry, () -> {
            editTextContent.setText("");
            Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
        });
    }
}