package com.scouthub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.pm.PackageManager;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.FileProvider;
import com.google.android.material.tabs.TabLayout;
import android.widget.EditText;
import com.scouthub.R;
import com.scouthub.adapters.PostsAdapter;
import com.scouthub.adapters.ReelsAdapter;
import com.scouthub.models.Post;
import com.scouthub.models.Reel;
import com.scouthub.models.UserProfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AthleteProfileFragment extends Fragment {

    // Image handling constants
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_PERMISSION_CAMERA = 3;
    private static final int REQUEST_PERMISSION_STORAGE = 4;
    
    // Video handling constants
    private static final int REQUEST_VIDEO_CAPTURE = 5;
    private static final int REQUEST_VIDEO_PICK = 6;
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 7;
    
    private Uri currentImageUri;
    private File currentImageFile;
    private Uri currentVideoUri;
    private File currentVideoFile;
    
    // Track pending actions for permission handling
    private String pendingAction = null;
    
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView sportPositionTextView;
    private TextView ageTextView;
    private TextView heightWeightTextView;
    private TextView clubTextView;
    private TextView locationTextView;
    private TextView contactTextView;
    private Button editProfileButton;
    private TextView goalsCountTextView;
    private TextView assistsCountTextView;
    private TextView matchesCountTextView;
    private Button addMatchStatsButton;
    private Button addAchievementButton;
    private Button uploadFileButton;
    private Button recordVideoButton;
    private TabLayout profileTabLayout;
    private FrameLayout contentContainer;
    private LinearLayout overviewContent;
    private LinearLayout achievementsContent;
    private LinearLayout statsContent;
    private LinearLayout reelsContent;
    private RecyclerView achievementsRecyclerView;
    private RecyclerView statsRecyclerView;
    private RecyclerView reelsRecyclerView;
    
    private List<Post> achievementsList;
    private List<Post> statsList;
    private List<Reel> reelsList;
    private PostsAdapter achievementsAdapter;
    private PostsAdapter statsAdapter;
    private ReelsAdapter reelsAdapter;
    
    private UserProfile currentUserProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            android.util.Log.d("AthleteProfileFragment", "onCreateView started");
            
            // Use the profile layout with tabs (now the original file)
            View view = inflater.inflate(R.layout.fragment_athlete_profile, container, false);
            android.util.Log.d("AthleteProfileFragment", "Layout with tabs inflated successfully");

            initializeViews(view);
            android.util.Log.d("AthleteProfileFragment", "Views initialized");
            
            setupProfileData();
            android.util.Log.d("AthleteProfileFragment", "Profile data setup complete");
            
            setupRecyclerView();
            android.util.Log.d("AthleteProfileFragment", "RecyclerView setup complete");
            
            setupTabLayout();
            android.util.Log.d("AthleteProfileFragment", "Tab layout setup complete");

            return view;
            
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in onCreateView: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to simple layout
            try {
                View fallbackView = inflater.inflate(R.layout.fragment_athlete_profile_simple, container, false);
                android.util.Log.d("AthleteProfileFragment", "Fallback to simple layout successful");
                return fallbackView;
            } catch (Exception fallbackException) {
                android.util.Log.e("AthleteProfileFragment", "Fallback also failed: " + fallbackException.getMessage());
                
                // Last resort - basic text layout
                View basicFallback = inflater.inflate(R.layout.simple_profile_test, container, false);
                return basicFallback;
            }
        }
    }

    private void initializeViews(View view) {
        // Profile Header
        profileImageView = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.name);
        sportPositionTextView = view.findViewById(R.id.sport_position);
        ageTextView = view.findViewById(R.id.age);
        heightWeightTextView = view.findViewById(R.id.height_weight);
        clubTextView = view.findViewById(R.id.club);
        locationTextView = view.findViewById(R.id.location);
        contactTextView = view.findViewById(R.id.contact);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        
        // Performance Overview
        goalsCountTextView = view.findViewById(R.id.goals_count);
        assistsCountTextView = view.findViewById(R.id.assists_count);
        matchesCountTextView = view.findViewById(R.id.matches_count);
        
        // Action Buttons
        addMatchStatsButton = view.findViewById(R.id.add_match_stats_button);
        addAchievementButton = view.findViewById(R.id.add_achievement_button);
        
        // Navigation
        profileTabLayout = view.findViewById(R.id.profile_tab_layout);
        contentContainer = view.findViewById(R.id.content_container);
        overviewContent = view.findViewById(R.id.overview_content);
        achievementsContent = view.findViewById(R.id.achievements_content);
        statsContent = view.findViewById(R.id.stats_content);
        reelsContent = view.findViewById(R.id.reels_content);
        
        // Reels buttons
        uploadFileButton = view.findViewById(R.id.upload_file_button);
        recordVideoButton = view.findViewById(R.id.record_video_button);
        
        // RecyclerViews
        achievementsRecyclerView = view.findViewById(R.id.achievements_recycler_view);
        statsRecyclerView = view.findViewById(R.id.stats_recycler_view);
        reelsRecyclerView = view.findViewById(R.id.reels_recycler_view);
        
        android.util.Log.d("AthleteProfileFragment", "All views found successfully");
    }

    private void setupProfileData() {
        // Minimal profile setup to test basic functionality
        try {
            android.util.Log.d("AthleteProfileFragment", "Setting up profile data - NEW VERSION");
            currentUserProfile = new UserProfile();
            currentUserProfile.setName("John Doe");
            currentUserProfile.setSport("Football");
            currentUserProfile.setPosition("Forward");
            currentUserProfile.setAge("19");
            currentUserProfile.setHeight("175");
            currentUserProfile.setWeight("70");
            currentUserProfile.setClub("FC Barcelona Youth");
            currentUserProfile.setLocation("Sydney, Australia");
            currentUserProfile.setContact("+61 123 456 789");
            currentUserProfile.setProfileImageUrl("sample_profile_1"); // Match layout
            
            // Set profile information with null checks
            if (nameTextView != null) nameTextView.setText(currentUserProfile.getName());
            if (sportPositionTextView != null) sportPositionTextView.setText(currentUserProfile.getSport() + ": " + currentUserProfile.getPosition());
            if (ageTextView != null) ageTextView.setText("Age: " + currentUserProfile.getAge());
            if (heightWeightTextView != null) heightWeightTextView.setText("Height: " + currentUserProfile.getHeight() + " cm, Weight: " + currentUserProfile.getWeight() + " kg");
            if (clubTextView != null) clubTextView.setText("Club: " + currentUserProfile.getClub());
            if (locationTextView != null) locationTextView.setText("Location: " + currentUserProfile.getLocation());
            if (contactTextView != null) contactTextView.setText("Contact: " + currentUserProfile.getContact());
            
            // Performance Overview
            if (goalsCountTextView != null) goalsCountTextView.setText("24");
            if (assistsCountTextView != null) assistsCountTextView.setText("18");
            if (matchesCountTextView != null) matchesCountTextView.setText("42");
            
            // Load profile image
            if (profileImageView != null) {
                profileImageView.setBackgroundResource(R.drawable.sample_profile_1);
            }
            
            // Setup click listeners
            setupClickListeners();
            
            android.util.Log.d("AthleteProfileFragment", "Profile data setup complete - NEW VERSION");
            
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("AthleteProfileFragment", "Error in setupProfileData: " + e.getMessage());
        }
    }

    private void setupRecyclerView() {
        try {
            // Setup Achievements RecyclerView
            if (achievementsRecyclerView != null) {
                achievementsList = new ArrayList<>();
                achievementsList.add(new Post("1", "John Doe", "Top Scorer - Youth League 2024", "achievement", "2024-03-20", "sample_post_1"));
                achievementsList.add(new Post("2", "John Doe", "Best Player - Tournament Final", "achievement", "2024-03-15", "sample_post_2"));
                achievementsAdapter = new PostsAdapter(achievementsList);
                achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                achievementsRecyclerView.setAdapter(achievementsAdapter);
            }
            
            // Setup Stats RecyclerView
            if (statsRecyclerView != null) {
                statsList = new ArrayList<>();
                statsList.add(new Post("1", "John Doe", "Season Performance: 24 goals, 18 assists", "stat", "2024-03-25", "sample_post_1"));
                statsList.add(new Post("2", "John Doe", "Match Rating: 8.5/10", "stat", "2024-03-24", "sample_post_2"));
                statsAdapter = new PostsAdapter(statsList);
                statsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                statsRecyclerView.setAdapter(statsAdapter);
            }
            
            // Setup Reels RecyclerView
            if (reelsRecyclerView != null) {
                reelsList = new ArrayList<>();
                reelsList.add(new Reel("1", "Training Highlights", "John Doe", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", 60, false));
                reelsList.add(new Reel("2", "Match Goal", "John Doe", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 45, false));
                reelsAdapter = new ReelsAdapter(reelsList);
                reelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                reelsRecyclerView.setAdapter(reelsAdapter);
            }
            
            android.util.Log.d("AthleteProfileFragment", "RecyclerView setup complete");
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in setupRecyclerView: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupClickListeners() {
        editProfileButton.setOnClickListener(v -> {
            showEditProfileDialog();
        });

        addMatchStatsButton.setOnClickListener(v -> {
            // TODO: Open add match stats dialog
        });

        addAchievementButton.setOnClickListener(v -> {
            // TODO: Open add achievement dialog
        });

        uploadFileButton.setOnClickListener(v -> {
            showVideoPickerOptions();
        });

        recordVideoButton.setOnClickListener(v -> {
            dispatchTakeVideoIntent();
        });
    }
    
    private void setupTabLayout() {
        try {
            if (profileTabLayout != null) {
                profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        hideAllContent();
                        switch (tab.getPosition()) {
                            case 0:
                                if (overviewContent != null) overviewContent.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                if (achievementsContent != null) achievementsContent.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                if (statsContent != null) statsContent.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                if (reelsContent != null) reelsContent.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
                android.util.Log.d("AthleteProfileFragment", "Tab layout setup complete");
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in setupTabLayout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void hideAllContent() {
        try {
            if (overviewContent != null) overviewContent.setVisibility(View.GONE);
            if (achievementsContent != null) achievementsContent.setVisibility(View.GONE);
            if (statsContent != null) statsContent.setVisibility(View.GONE);
            if (reelsContent != null) reelsContent.setVisibility(View.GONE);
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in hideAllContent: " + e.getMessage());
        }
    }
    
    private void showEditProfileDialog() {
        try {
            android.util.Log.d("AthleteProfileFragment", "showEditProfileDialog called");
            
            // Create dialog for profile editing
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
            
            // Get dialog views
            ImageView profileImage = dialogView.findViewById(R.id.profile_image);
            ImageButton changePhotoButton = dialogView.findViewById(R.id.change_photo_button);
            EditText nameInput = dialogView.findViewById(R.id.name_input);
            EditText sportInput = dialogView.findViewById(R.id.sport_input);
            EditText positionInput = dialogView.findViewById(R.id.position_input);
            EditText ageInput = dialogView.findViewById(R.id.age_input);
            EditText heightInput = dialogView.findViewById(R.id.height_input);
            EditText weightInput = dialogView.findViewById(R.id.weight_input);
            EditText clubInput = dialogView.findViewById(R.id.club_input);
            EditText locationInput = dialogView.findViewById(R.id.location_input);
            EditText contactInput = dialogView.findViewById(R.id.contact_input);
            Button cancelButton = dialogView.findViewById(R.id.cancel_button);
            Button saveButton = dialogView.findViewById(R.id.save_button);
            ImageButton closeButton = dialogView.findViewById(R.id.close_button);
            
            android.util.Log.d("AthleteProfileFragment", "Dialog views found, setting up data");
            
            // Set current values with null checks
            if (currentUserProfile != null) {
                nameInput.setText(currentUserProfile.getName());
                sportInput.setText(currentUserProfile.getSport());
                positionInput.setText(currentUserProfile.getPosition());
                ageInput.setText(currentUserProfile.getAge());
                heightInput.setText(currentUserProfile.getHeight());
                weightInput.setText(currentUserProfile.getWeight());
                clubInput.setText(currentUserProfile.getClub());
                locationInput.setText(currentUserProfile.getLocation());
                contactInput.setText(currentUserProfile.getContact());
            }
            
            // Set profile image
            if (profileImage != null) {
                String profileImageName = currentUserProfile != null ? currentUserProfile.getProfileImageUrl() : "sample_profile_1";
                int drawableId = getResources().getIdentifier(profileImageName, "drawable", requireContext().getPackageName());
                if (drawableId != 0) {
                    profileImage.setBackgroundResource(drawableId);
                }
            }
            
            // Create dialog
            AlertDialog dialog = builder.setView(dialogView).create();
            dialog.show();
            
            // Handle click listeners
            if (changePhotoButton != null) {
                android.util.Log.d("AthleteProfileFragment", "Change photo button found, setting click listener");
                changePhotoButton.setOnClickListener(v -> {
                    android.util.Log.d("AthleteProfileFragment", "Change photo button clicked");
                    showImagePickerOptions();
                });
            } else {
                android.util.Log.e("AthleteProfileFragment", "Change photo button not found!");
            }
            
            if (closeButton != null) {
                closeButton.setOnClickListener(v -> dialog.dismiss());
            }
            
            if (cancelButton != null) {
                cancelButton.setOnClickListener(v -> dialog.dismiss());
            }
            
            if (saveButton != null) {
                saveButton.setOnClickListener(v -> {
                    // Update profile data
                    if (currentUserProfile != null) {
                        currentUserProfile.setName(nameInput.getText().toString().trim());
                        currentUserProfile.setSport(sportInput.getText().toString().trim());
                        currentUserProfile.setPosition(positionInput.getText().toString().trim());
                        currentUserProfile.setAge(ageInput.getText().toString().trim());
                        currentUserProfile.setHeight(heightInput.getText().toString().trim());
                        currentUserProfile.setWeight(weightInput.getText().toString().trim());
                        currentUserProfile.setClub(clubInput.getText().toString().trim());
                        currentUserProfile.setLocation(locationInput.getText().toString().trim());
                        currentUserProfile.setContact(contactInput.getText().toString().trim());
                        
                        // Update UI
                        updateProfileUI();
                        
                        // Dismiss dialog
                        dialog.dismiss();
                        
                        // Show success message
                        android.widget.Toast.makeText(requireContext(), "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in showEditProfileDialog: " + e.getMessage());
            e.printStackTrace();
            android.widget.Toast.makeText(requireContext(), "Error opening edit profile", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateProfileUI() {
        try {
            // Update all profile fields with new data
            if (currentUserProfile != null) {
                if (nameTextView != null) nameTextView.setText(currentUserProfile.getName());
                if (sportPositionTextView != null) sportPositionTextView.setText(currentUserProfile.getSport() + ": " + currentUserProfile.getPosition());
                if (ageTextView != null) ageTextView.setText("Age: " + currentUserProfile.getAge());
                if (heightWeightTextView != null) heightWeightTextView.setText("Height: " + currentUserProfile.getHeight() + " cm, Weight: " + currentUserProfile.getWeight() + " kg");
                if (clubTextView != null) clubTextView.setText("Club: " + currentUserProfile.getClub());
                if (locationTextView != null) locationTextView.setText("Location: " + currentUserProfile.getLocation());
                if (contactTextView != null) contactTextView.setText("Contact: " + currentUserProfile.getContact());
                
                // Update profile image if changed
                if (profileImageView != null && currentUserProfile.getProfileImageUrl() != null) {
                    String imageUrl = currentUserProfile.getProfileImageUrl();
                    
                    // Check if it's a URI (starts with file:// or content://) or a drawable name
                    if (imageUrl.startsWith("file://") || imageUrl.startsWith("content://")) {
                        // It's a URI, load it with Glide or directly set it
                        try {
                            profileImageView.setImageURI(Uri.parse(imageUrl));
                            android.util.Log.d("AthleteProfileFragment", "Profile image set from URI: " + imageUrl);
                        } catch (Exception e) {
                            android.util.Log.e("AthleteProfileFragment", "Error setting image from URI: " + e.getMessage());
                            // Fallback to default drawable
                            profileImageView.setBackgroundResource(R.drawable.sample_profile_1);
                        }
                    } else {
                        // It's a drawable name
                        int drawableId = requireContext().getResources().getIdentifier(
                            imageUrl, "drawable", requireContext().getPackageName());
                        if (drawableId != 0) {
                            profileImageView.setBackgroundResource(drawableId);
                            android.util.Log.d("AthleteProfileFragment", "Profile image set from drawable: " + imageUrl);
                        } else {
                            // Fallback to default
                            profileImageView.setBackgroundResource(R.drawable.sample_profile_1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in updateProfileUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showImagePickerOptions() {
        try {
            android.util.Log.d("AthleteProfileFragment", "showImagePickerOptions called");
            
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Choose Profile Picture")
                   .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                       android.util.Log.d("AthleteProfileFragment", "Image picker option selected: " + which);
                       switch (which) {
                           case 0:
                               android.util.Log.d("AthleteProfileFragment", "User selected Take Photo");
                               dispatchTakePictureIntent();
                               break;
                           case 1:
                               android.util.Log.d("AthleteProfileFragment", "User selected Choose from Gallery");
                               dispatchPickPictureIntent();
                               break;
                       }
                   })
                   .setNegativeButton("Cancel", (dialog, which) -> {
                       android.util.Log.d("AthleteProfileFragment", "User cancelled image picker");
                       dialog.dismiss();
                   })
                   .show();
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error showing image picker: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening image picker", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void dispatchTakePictureIntent() {
        try {
            android.util.Log.d("AthleteProfileFragment", "dispatchTakePictureIntent called");
            
            // Check camera permission
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                android.util.Log.d("AthleteProfileFragment", "Camera permission not granted, requesting...");
                // Request camera permission
                pendingAction = "take_picture";
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                return;
            }
            
            // For Android 13+, we don't need storage permission for app-specific files
            // Only check storage permission for Android < 13
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    android.util.Log.d("AthleteProfileFragment", "Storage permission not granted, requesting...");
                    // Request storage permission
                    pendingAction = "take_picture";
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                    return;
                }
            }
            
            android.util.Log.d("AthleteProfileFragment", "All permissions granted, proceeding with actuallyTakePicture");
            // All permissions granted, proceed with taking picture
            actuallyTakePicture();
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error dispatching camera intent: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening camera", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void actuallyTakePicture() {
        try {
            android.util.Log.d("AthleteProfileFragment", "actuallyTakePicture called");
            
            // Skip the resolveActivity check since it's unreliable on some devices
            // Try to launch camera directly without file output first
            try {
                android.util.Log.d("AthleteProfileFragment", "Trying to launch camera without file output");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                android.util.Log.d("AthleteProfileFragment", "Camera intent started without file output");
                return;
            } catch (Exception e) {
                android.util.Log.e("AthleteProfileFragment", "Error launching camera without file output: " + e.getMessage());
            }
            
            // If that fails, try with file output
            try {
                android.util.Log.d("AthleteProfileFragment", "Trying to launch camera with file output");
                
                // Create the File where the photo should go
                File photoFile = createImageFile();
                android.util.Log.d("AthleteProfileFragment", "Image file created: " + photoFile.getAbsolutePath());
                
                currentImageUri = FileProvider.getUriForFile(requireContext(), "com.scouthub.fileprovider", photoFile);
                android.util.Log.d("AthleteProfileFragment", "Image URI created: " + currentImageUri.toString());
                
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                android.util.Log.d("AthleteProfileFragment", "Camera intent started with file output");
                return;
            } catch (Exception e) {
                android.util.Log.e("AthleteProfileFragment", "Error launching camera with file output: " + e.getMessage());
            }
            
            // If all camera attempts fail, fall back to gallery
            android.util.Log.e("AthleteProfileFragment", "All camera attempts failed, falling back to gallery");
            android.widget.Toast.makeText(requireContext(), "Camera not available, opening gallery instead", android.widget.Toast.LENGTH_LONG).show();
            dispatchPickPictureIntent();
            
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in actuallyTakePicture: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening camera, trying gallery", android.widget.Toast.LENGTH_SHORT).show();
            dispatchPickPictureIntent();
        }
    }
    
    private void dispatchPickPictureIntent() {
        try {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error dispatching gallery intent: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening gallery", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "profile_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(null);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        
        // Save a file: path for use with ACTION_VIEW intents
        currentImageFile = image;
        return image;
    }
    
    private void handleCameraResult() {
        try {
            android.util.Log.d("AthleteProfileFragment", "handleCameraResult called");
            
            // Check if we have a URI from file output
            if (currentImageUri != null) {
                android.util.Log.d("AthleteProfileFragment", "Using file URI: " + currentImageUri.toString());
                // Update profile with the captured image URI
                if (currentUserProfile != null) {
                    currentUserProfile.setProfileImageUrl(currentImageUri.toString());
                    updateProfileUI();
                    android.widget.Toast.makeText(requireContext(), "Profile picture updated!", android.widget.Toast.LENGTH_SHORT).show();
                }
            } else {
                android.util.Log.d("AthleteProfileFragment", "No file URI, checking for thumbnail");
                // This happens when we launch camera without file output
                // The thumbnail will be handled in onActivityResult with data
                android.widget.Toast.makeText(requireContext(), "Profile picture updated!", android.widget.Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error handling camera result: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error updating profile picture", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handleGalleryResult(Intent data) {
        try {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                
                // Update profile image in main view
                if (profileImageView != null) {
                    profileImageView.setImageURI(selectedImageUri);
                }
                
                // Update profile data
                if (currentUserProfile != null) {
                    currentUserProfile.setProfileImageUrl(selectedImageUri.toString());
                }
                
                android.widget.Toast.makeText(requireContext(), "Profile picture updated!", android.widget.Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error handling gallery result: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error updating profile picture", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showVideoPickerOptions() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Choose Video")
                   .setItems(new CharSequence[]{"Record Video", "Choose from Gallery"}, (dialog, which) -> {
                       switch (which) {
                           case 0:
                               dispatchTakeVideoIntent();
                               break;
                           case 1:
                               dispatchPickVideoIntent();
                               break;
                       }
                   })
                   .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                   .show();
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error showing video picker: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening video picker", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void dispatchTakeVideoIntent() {
        try {
            android.util.Log.d("AthleteProfileFragment", "dispatchTakeVideoIntent called");
            
            // Check camera permission
            boolean cameraGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            boolean audioGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
            
            // For Android 13+, we don't need storage permission for app-specific files
            boolean storageGranted = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU || 
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            
            android.util.Log.d("AthleteProfileFragment", "Permission check - Camera: " + cameraGranted + ", Audio: " + audioGranted + ", Storage: " + storageGranted);
            
            if (!cameraGranted || !audioGranted || !storageGranted) {
                // Request all needed permissions at once
                ArrayList<String> permissionsNeeded = new ArrayList<>();
                if (!cameraGranted) permissionsNeeded.add(Manifest.permission.CAMERA);
                if (!audioGranted) permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
                if (!storageGranted) permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                
                android.util.Log.d("AthleteProfileFragment", "Requesting permissions: " + permissionsNeeded.toString());
                pendingAction = "take_video";
                ActivityCompat.requestPermissions(requireActivity(), 
                    permissionsNeeded.toArray(new String[0]), 
                    REQUEST_PERMISSION_CAMERA); // Use camera request code for simplicity
                return;
            }
            
            android.util.Log.d("AthleteProfileFragment", "All permissions granted, proceeding with actuallyTakeVideo");
            // All permissions granted, proceed with recording video
            actuallyTakeVideo();
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error dispatching video camera intent: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening video camera", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void actuallyTakeVideo() {
        try {
            android.util.Log.d("AthleteProfileFragment", "actuallyTakeVideo called");
            
            // Skip the resolveActivity check since it's unreliable on some devices
            // Try to launch video camera directly
            try {
                android.util.Log.d("AthleteProfileFragment", "Trying to launch video camera");
                
                // Create the File where the video should go
                File videoFile = createVideoFile();
                android.util.Log.d("AthleteProfileFragment", "Video file created: " + videoFile.getAbsolutePath());
                
                currentVideoUri = FileProvider.getUriForFile(requireContext(), "com.scouthub.fileprovider", videoFile);
                android.util.Log.d("AthleteProfileFragment", "Video URI created: " + currentVideoUri.toString());
                
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentVideoUri);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // High quality
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                android.util.Log.d("AthleteProfileFragment", "Video camera intent started");
                return;
            } catch (Exception e) {
                android.util.Log.e("AthleteProfileFragment", "Error launching video camera: " + e.getMessage());
            }
            
            // If video camera fails, fall back to gallery
            android.util.Log.e("AthleteProfileFragment", "Video camera failed, falling back to gallery");
            android.widget.Toast.makeText(requireContext(), "Video camera not available, opening gallery instead", android.widget.Toast.LENGTH_LONG).show();
            dispatchPickVideoIntent();
            
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error in actuallyTakeVideo: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening video camera, trying gallery", android.widget.Toast.LENGTH_SHORT).show();
            dispatchPickVideoIntent();
        }
    }
    
    private void dispatchPickVideoIntent() {
        try {
            Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickVideoIntent, REQUEST_VIDEO_PICK);
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error dispatching video gallery intent: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error opening video gallery", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private File createVideoFile() throws IOException {
        // Create a video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "reel_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(null);
        File video = File.createTempFile(videoFileName, ".mp4", storageDir);
        
        // Save a file: path for use with ACTION_VIEW intents
        currentVideoFile = video;
        return video;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == android.app.Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    android.util.Log.d("AthleteProfileFragment", "Camera result received");
                    // Check if we have thumbnail data (when launched without file output)
                    if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                        android.util.Log.d("AthleteProfileFragment", "Processing camera thumbnail");
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        if (thumbnail != null) {
                            // Update profile with the thumbnail
                            if (currentUserProfile != null) {
                                // Save the thumbnail to a file and use that URI
                                try {
                                    File thumbnailFile = createImageFile();
                                    java.io.FileOutputStream out = new java.io.FileOutputStream(thumbnailFile);
                                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.close();
                                    
                                    currentImageUri = FileProvider.getUriForFile(requireContext(), "com.scouthub.fileprovider", thumbnailFile);
                                    currentUserProfile.setProfileImageUrl(currentImageUri.toString());
                                    updateProfileUI();
                                    android.widget.Toast.makeText(requireContext(), "Profile picture updated with camera photo!", android.widget.Toast.LENGTH_SHORT).show();
                                    android.util.Log.d("AthleteProfileFragment", "Profile picture updated successfully");
                                } catch (Exception e) {
                                    android.util.Log.e("AthleteProfileFragment", "Error saving thumbnail: " + e.getMessage());
                                    android.widget.Toast.makeText(requireContext(), "Error saving photo", android.widget.Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else {
                        android.util.Log.d("AthleteProfileFragment", "No camera data, calling handleCameraResult");
                        handleCameraResult();
                    }
                    break;
                case REQUEST_IMAGE_PICK:
                    handleGalleryResult(data);
                    break;
                case REQUEST_VIDEO_CAPTURE:
                    handleVideoCameraResult();
                    break;
                case REQUEST_VIDEO_PICK:
                    handleVideoGalleryResult(data);
                    break;
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        android.util.Log.d("AthleteProfileFragment", "onRequestPermissionsResult called: requestCode=" + requestCode + ", pendingAction=" + pendingAction);
        
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                android.util.Log.d("AthleteProfileFragment", "Camera permission result received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    android.util.Log.d("AthleteProfileFragment", "Camera permission granted, pendingAction=" + pendingAction);
                    // Camera permission granted, check what action to perform
                    if ("take_picture".equals(pendingAction)) {
                        android.util.Log.d("AthleteProfileFragment", "Executing take_picture action");
                        actuallyTakePicture();
                    } else if ("take_video".equals(pendingAction)) {
                        android.util.Log.d("AthleteProfileFragment", "Executing take_video action after camera permission");
                        actuallyTakeVideo();
                    } else {
                        android.util.Log.d("AthleteProfileFragment", "No pending action for camera permission, pendingAction=" + pendingAction);
                    }
                    pendingAction = null; // Clear pending action
                } else {
                    android.util.Log.e("AthleteProfileFragment", "Camera permission denied");
                    android.widget.Toast.makeText(requireContext(), "Camera permission is required to take photos and videos", android.widget.Toast.LENGTH_LONG).show();
                    pendingAction = null; // Clear pending action
                }
                break;
                
            case REQUEST_PERMISSION_RECORD_AUDIO:
                android.util.Log.d("AthleteProfileFragment", "Audio permission result received");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    android.util.Log.d("AthleteProfileFragment", "Audio permission granted, pendingAction=" + pendingAction);
                    // Audio permission granted, proceed with recording video
                    if ("take_video".equals(pendingAction)) {
                        android.util.Log.d("AthleteProfileFragment", "Executing take_video action after audio permission");
                        actuallyTakeVideo();
                    } else {
                        android.util.Log.d("AthleteProfileFragment", "No pending action for audio permission, pendingAction=" + pendingAction);
                    }
                    pendingAction = null; // Clear pending action
                } else {
                    android.util.Log.e("AthleteProfileFragment", "Audio permission denied");
                    android.widget.Toast.makeText(requireContext(), "Audio permission is required to record videos with sound", android.widget.Toast.LENGTH_LONG).show();
                    pendingAction = null; // Clear pending action
                }
                break;
                
            case REQUEST_PERMISSION_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    android.util.Log.d("AthleteProfileFragment", "Storage permission granted");
                    // Storage permission granted, check what action to perform
                    if ("take_picture".equals(pendingAction)) {
                        android.util.Log.d("AthleteProfileFragment", "Executing take_picture action after storage permission");
                        actuallyTakePicture();
                    } else if ("take_video".equals(pendingAction)) {
                        android.util.Log.d("AthleteProfileFragment", "Executing take_video action after storage permission");
                        actuallyTakeVideo();
                    }
                    pendingAction = null; // Clear pending action
                } else {
                    android.util.Log.e("AthleteProfileFragment", "Storage permission denied");
                    android.widget.Toast.makeText(requireContext(), "Storage permission is required to save media files", android.widget.Toast.LENGTH_LONG).show();
                    pendingAction = null; // Clear pending action
                }
                break;
        }
    }
    
    private void handleVideoCameraResult() {
        try {
            if (currentVideoUri != null) {
                // Show caption dialog before adding the reel
                showCaptionDialog(currentVideoUri.toString(), true);
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error handling video camera result: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error adding video reel", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handleVideoGalleryResult(Intent data) {
        try {
            if (data != null && data.getData() != null) {
                Uri selectedVideoUri = data.getData();
                
                // Show caption dialog before adding the reel
                showCaptionDialog(selectedVideoUri.toString(), false);
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error handling video gallery result: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), "Error adding video reel", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showCaptionDialog(String videoUri, boolean isFromCamera) {
        try {
            // Create dialog for caption input
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_caption_input, null);
            
            // Get dialog views
            EditText captionInput = dialogView.findViewById(R.id.caption_input);
            Button cancelButton = dialogView.findViewById(R.id.cancel_button);
            Button postButton = dialogView.findViewById(R.id.post_button);
            
            // Create dialog
            AlertDialog dialog = builder.setView(dialogView).create();
            dialog.show();
            
            // Handle click listeners
            if (cancelButton != null) {
                cancelButton.setOnClickListener(v -> dialog.dismiss());
            }
            
            if (postButton != null) {
                postButton.setOnClickListener(v -> {
                    String caption = captionInput.getText().toString().trim();
                    if (caption.isEmpty()) {
                        caption = isFromCamera ? "New Video" : "Uploaded Video";
                    }
                    
                    // Add new reel with caption
                    addNewReel(videoUri, caption, true);
                    
                    // Dismiss dialog
                    dialog.dismiss();
                    
                    // Show success message
                    android.widget.Toast.makeText(requireContext(), "Video reel added successfully!", android.widget.Toast.LENGTH_SHORT).show();
                });
            }
            
            // Focus on caption input
            if (captionInput != null) {
                captionInput.requestFocus();
                // Show keyboard
                captionInput.post(() -> {
                    android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(captionInput, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                });
            }
            
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error showing caption dialog: " + e.getMessage());
            // Fallback: add reel without caption
            addNewReel(videoUri, isFromCamera ? "New Video" : "Uploaded Video", true);
            android.widget.Toast.makeText(requireContext(), "Video reel added successfully!", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private void addNewReel(String videoUri, String title, boolean isNew) {
        try {
            if (reelsList != null && reelsAdapter != null) {
                // Debug logging
                android.util.Log.d("AthleteProfileFragment", "Adding new reel - Title: " + title + ", URI: " + videoUri + ", User: " + currentUserProfile.getName());
                
                // Create a new reel with the video URI
                String uniqueId = "reel_" + System.currentTimeMillis();
                Reel newReel = new Reel(uniqueId, title, currentUserProfile.getName(), videoUri, 0, isNew);
                
                // Add to the beginning of the list
                reelsList.add(0, newReel);
                
                // Debug: Log the reel data
                android.util.Log.d("AthleteProfileFragment", "Created reel - ID: " + newReel.getId() + ", Caption: " + newReel.getCaption());
                
                // Notify adapter
                if (reelsRecyclerView != null) {
                    reelsRecyclerView.post(() -> {
                        reelsAdapter.notifyItemInserted(0);
                        reelsRecyclerView.scrollToPosition(0);
                    });
                }
                
                android.util.Log.d("AthleteProfileFragment", "New reel added: " + title);
            }
        } catch (Exception e) {
            android.util.Log.e("AthleteProfileFragment", "Error adding new reel: " + e.getMessage());
        }
    }
}
