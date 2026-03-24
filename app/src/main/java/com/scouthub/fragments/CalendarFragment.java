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
        
        // Setup search functionality
        setupSearchBar();
    }
    
    private void setupSearchBar() {
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
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
        String selectedTab = timeTabLayout.getTabAt(timeTabLayout.getSelectedTabPosition()).getText().toString();
        String searchQuery = searchBar.getText().toString().toLowerCase().trim();
        
        android.util.Log.d("CalendarFragment", "Filtering events for tab: " + selectedTab + ", search: '" + searchQuery + "'");
        
        List<CalendarEvent> filteredList = new ArrayList<>();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        java.util.Date today = new java.util.Date();
        
        try {
            for (CalendarEvent event : eventsList) {
                java.util.Date eventDate = dateFormat.parse(event.getDate());
                
                // First apply date filtering based on selected tab
                boolean matchesDateFilter = false;
                if (selectedTab.equals("Upcoming")) {
                    // Show events that are today or in the future
                    matchesDateFilter = (eventDate != null && !eventDate.before(today));
                } else if (selectedTab.equals("History")) {
                    // Show events that are in the past
                    matchesDateFilter = (eventDate != null && eventDate.before(today));
                } else {
                    // "All" tab - show all events
                    matchesDateFilter = true;
                }
                
                // Then apply search filter if there's a search query
                boolean matchesSearch = false;
                if (searchQuery.isEmpty()) {
                    matchesSearch = true; // No search query means all events match
                } else {
                    // Search in title, location, and type
                    String title = event.getTitle().toLowerCase();
                    String location = event.getLocation().toLowerCase();
                    String type = event.getType().toLowerCase();
                    
                    matchesSearch = title.contains(searchQuery) || 
                                   location.contains(searchQuery) || 
                                   type.contains(searchQuery);
                }
                
                // Add event only if it matches both filters
                if (matchesDateFilter && matchesSearch) {
                    filteredList.add(event);
                }
            }
            
            // Sort the filtered list by date and time
            java.util.Collections.sort(filteredList, new java.util.Comparator<CalendarEvent>() {
                @Override
                public int compare(CalendarEvent e1, CalendarEvent e2) {
                    try {
                        // Parse date and time together for proper sorting
                        String dateTime1 = e1.getDate() + " " + e1.getTime();
                        String dateTime2 = e2.getDate() + " " + e2.getTime();
                        
                        java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd h:mm a", java.util.Locale.getDefault());
                        java.util.Date fullDateTime1 = dateTimeFormat.parse(dateTime1);
                        java.util.Date fullDateTime2 = dateTimeFormat.parse(dateTime2);
                        
                        if (selectedTab.equals("Upcoming")) {
                            // For upcoming events, sort ascending (earliest first)
                            return fullDateTime1.compareTo(fullDateTime2);
                        } else if (selectedTab.equals("History")) {
                            // For history events, sort descending (most recent first)
                            return fullDateTime2.compareTo(fullDateTime1);
                        } else {
                            // For all events, sort ascending (earliest first)
                            return fullDateTime1.compareTo(fullDateTime2);
                        }
                    } catch (Exception e) {
                        // Fallback to date-only comparison if time parsing fails
                        try {
                            java.util.Date date1 = dateFormat.parse(e1.getDate());
                            java.util.Date date2 = dateFormat.parse(e2.getDate());
                            
                            if (selectedTab.equals("History")) {
                                return date2.compareTo(date1);
                            } else {
                                return date1.compareTo(date2);
                            }
                        } catch (Exception ex) {
                            return 0;
                        }
                    }
                }
            });
            
        } catch (Exception e) {
            android.util.Log.e("CalendarFragment", "Error filtering events: " + e.getMessage());
            // If filtering fails, show all events
            filteredList = new ArrayList<>(eventsList);
        }
        
        // Update the adapter with the filtered list
        calendarAdapter.updateList(filteredList);
        android.util.Log.d("CalendarFragment", "Filtered events count: " + filteredList.size());
    }

    private void loadMockData() {
        // Add events with realistic dates relative to today (2026-03-23)
        // Include same-day events with different times to demonstrate time sorting
        eventsList.add(new CalendarEvent("1", "FC Barcelona Trial", "trial", "2026-03-25", "10:00 AM", "Camp Nou"));
        eventsList.add(new CalendarEvent("2", "Team Training", "training", "2026-03-24", "3:00 PM", "Training Ground"));
        eventsList.add(new CalendarEvent("3", "Match vs Real Madrid", "match", "2026-03-23", "8:00 PM", "Santiago Bernabeu"));
        eventsList.add(new CalendarEvent("4", "Scout Meeting", "meeting", "2026-03-22", "2:00 PM", "Club Office"));
        eventsList.add(new CalendarEvent("5", "Arsenal Trial", "trial", "2026-03-30", "11:00 AM", "Emirates Stadium"));
        eventsList.add(new CalendarEvent("6", "Recovery Session", "training", "2026-03-21", "9:00 AM", "Recovery Center"));
        eventsList.add(new CalendarEvent("7", "Chelsea Scouting", "meeting", "2026-04-02", "2:00 PM", "Stamford Bridge"));
        eventsList.add(new CalendarEvent("8", "Tournament Final", "match", "2026-03-20", "7:00 PM", "Wembley Stadium"));
        
        // Add same-day events to test time sorting
        eventsList.add(new CalendarEvent("9", "Morning Practice", "training", "2026-03-23", "9:00 AM", "Training Ground"));
        eventsList.add(new CalendarEvent("10", "Lunch Meeting", "meeting", "2026-03-23", "12:30 PM", "Club Restaurant"));
        eventsList.add(new CalendarEvent("11", "Video Analysis", "training", "2026-03-23", "4:00 PM", "Analysis Room"));
        eventsList.add(new CalendarEvent("12", "Evening Recovery", "training", "2026-03-23", "6:00 PM", "Recovery Center"));
        
        // Add more same-day events for different dates
        eventsList.add(new CalendarEvent("13", "Breakfast Meeting", "meeting", "2026-03-24", "8:00 AM", "Club Office"));
        eventsList.add(new CalendarEvent("14", "Gym Session", "training", "2026-03-24", "11:00 AM", "Gym"));
        eventsList.add(new CalendarEvent("15", "Tactical Meeting", "meeting", "2026-03-25", "2:00 PM", "Conference Room"));
        eventsList.add(new CalendarEvent("16", "Press Conference", "meeting", "2026-03-25", "4:00 PM", "Media Center"));
        
        // Initial filter - will be called again when tabs are selected
        filterEvents();
    }
}
