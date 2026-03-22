package com.scouthub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.scouthub.R;
import com.scouthub.adapters.PromisingPlayersAdapter;
import com.scouthub.adapters.ReelsGalleryAdapter;
import com.scouthub.models.User;
import com.scouthub.models.Reel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ScoutProfileFragment extends Fragment {
    
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView typeTextView;
    private TextView organizationTextView;
    private TextView locationTextView;
    private TextView bioTextView;
    private TextView scoutedPlayersTextView;
    private TextView successRateTextView;
    private TextView experienceTextView;
    private RecyclerView activityRecyclerView;
    private Button editProfileButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scout_profile, container, false);

        initializeViews(view);
        setupProfileData();

        return view;
    }

    private void initializeViews(View view) {
        profileImageView = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.name);
        typeTextView = view.findViewById(R.id.type);
        organizationTextView = view.findViewById(R.id.organization);
        locationTextView = view.findViewById(R.id.location);
        bioTextView = view.findViewById(R.id.bio);
        scoutedPlayersTextView = view.findViewById(R.id.scouted_players);
        successRateTextView = view.findViewById(R.id.success_rate);
        experienceTextView = view.findViewById(R.id.experience);
        activityRecyclerView = view.findViewById(R.id.activity_recycler_view);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
    }

    private void setupProfileData() {
        nameTextView.setText("Mike Scout");
        typeTextView.setText("Scout");
        organizationTextView.setText("FC Barcelona");
        locationTextView.setText("Barcelona, Spain");
        bioTextView.setText("Professional football scout with 12+ years of experience identifying and recruiting talented players for top-tier clubs. Specialized in youth development and talent identification.");
        
        // Set mock stats
        scoutedPlayersTextView.setText("156");
        successRateTextView.setText("87%");
        experienceTextView.setText("12 yrs");
        
        // Load profile image
        profileImageView.setBackgroundResource(R.drawable.sample_profile_3);
        
        editProfileButton.setOnClickListener(v -> {
            // TODO: Open edit profile dialog/activity
        });
    }
}
