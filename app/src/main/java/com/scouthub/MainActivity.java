package com.scouthub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scouthub.fragments.AthleteHomeFragment;
import com.scouthub.fragments.ChatsFragment;
import com.scouthub.fragments.ReelsFragment;
import com.scouthub.fragments.CalendarFragment;
import com.scouthub.fragments.ScoutHomeFragment;
import com.scouthub.fragments.AthleteProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String userType = "athlete"; // Default to athlete, will be set from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        // Set initial fragment
        if (savedInstanceState == null) {
            loadFragment(new AthleteHomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = userType.equals("athlete") ? new AthleteHomeFragment() : new ScoutHomeFragment();
            } else if (itemId == R.id.nav_chats) {
                fragment = new ChatsFragment();
            } else if (itemId == R.id.nav_reels) {
                fragment = new ReelsFragment();
            } else if (itemId == R.id.nav_calendar) {
                fragment = new CalendarFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new AthleteProfileFragment();
            } else {
                return false;
            }
            
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void setUserType(String userType) {
        this.userType = userType;
        // Reload current fragment with new user type
        loadFragment(userType.equals("athlete") ? new AthleteHomeFragment() : new ScoutHomeFragment());
    }

    public String getUserType() {
        return userType;
    }
}
