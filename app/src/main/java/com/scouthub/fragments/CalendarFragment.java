package com.scouthub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.scouthub.R;
import com.scouthub.adapters.CalendarAdapter;
import com.scouthub.models.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private EditText searchBar;
    private TabLayout timeTabLayout;
    private RecyclerView eventsRecyclerView;
    private FloatingActionButton addEventFab;
    
    private List<CalendarEvent> eventsList;
    private CalendarAdapter calendarAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupTabLayouts();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        timeTabLayout = view.findViewById(R.id.time_filter_tab_layout);
        eventsRecyclerView = view.findViewById(R.id.events_recycler_view);
        addEventFab = view.findViewById(R.id.add_event_fab);
    }

    private void setupRecyclerView() {
        eventsList = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(eventsList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(calendarAdapter);
    }

    private void setupTabLayouts() {
        // Time tabs
        timeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterEvents();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        // FAB click listener
        addEventFab.setOnClickListener(v -> {
            // TODO: Open add event dialog/activity
        });
    }

    private void filterEvents() {
        // TODO: Implement event filtering logic
        calendarAdapter.notifyDataSetChanged();
    }

    private void loadMockData() {
        eventsList.add(new CalendarEvent("1", "FC Barcelona Trial", "trial", "2024-03-25", "10:00 AM", "Camp Nou"));
        eventsList.add(new CalendarEvent("2", "Team Training", "training", "2024-03-24", "3:00 PM", "Training Ground"));
        eventsList.add(new CalendarEvent("3", "Match vs Real Madrid", "match", "2024-03-23", "8:00 PM", "Santiago Bernabeu"));
        eventsList.add(new CalendarEvent("4", "Scout Meeting", "meeting", "2024-03-22", "2:00 PM", "Club Office"));
        calendarAdapter.notifyDataSetChanged();
    }
}
