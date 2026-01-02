package com.example.afinal.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.afinal.R;
import com.example.afinal.ui.edit.EditFragment;
import com.example.afinal.ui.calendar.CalendarFragment;
import com.example.afinal.ui.archive.ArchiveFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final EditFragment editFragment = new EditFragment();
    private final CalendarFragment calendarFragment = new CalendarFragment();
    private final ArchiveFragment archiveFragment = new ArchiveFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.navigation_edit) {
                selectedFragment = editFragment;
            } else if (id == R.id.navigation_calendar) {
                selectedFragment = calendarFragment;
            } else if (id == R.id.navigation_archive) {
                selectedFragment = archiveFragment;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // 默认显示编辑页
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .commit();
    }
}