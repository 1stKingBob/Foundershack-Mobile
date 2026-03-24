package com.scouthub.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.adapters.PostsAdapter;
import com.scouthub.adapters.StoriesAdapter;
import com.scouthub.fragments.CalendarFragment;
import com.scouthub.models.CalendarEvent;
import com.scouthub.models.Post;
import com.scouthub.models.Story;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class AthleteHomeFragment extends Fragment implements PostsAdapter.OnPostClickListener {
    
    // Request codes for camera/gallery
    private static final int REQUEST_PHOTO_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_PHOTO_PICK = 3;
    private static final int REQUEST_VIDEO_PICK = 4;
    
    private EditText searchBar;
    private RecyclerView storiesRecyclerView;
    private RecyclerView postsRecyclerView;
    private VideoView videoThumbnail;
    
    // Post creation views
    private EditText createPostText;
    private Button photoButton;
    private Button videoButton;
    private Button statsButton;
    private Button postButton;
    
    // Media preview views
    private LinearLayout mediaPreviewContainer;
    private ImageView imagePreview;
    private VideoView videoPreview;
    private LinearLayout statsInputContainer;
    private EditText goalsInput;
    private EditText assistsInput;
    private EditText minutesInput;
    private Button removeMediaButton;
    
    private String selectedPostType = "highlight"; // Default post type
    private Uri selectedMediaUri; // Selected photo/video URI
    
    private List<Story> storiesList;
    private List<Post> postsList;
    private StoriesAdapter storiesAdapter;
    private PostsAdapter postsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_athlete_home, container, false);
        
        initializeViews(view);
        setupRecyclerViews();
        setupClickListeners();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        storiesRecyclerView = view.findViewById(R.id.stories_recycler_view);
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        videoThumbnail = view.findViewById(R.id.video_thumbnail);
        
        // Initialize post creation views
        createPostText = view.findViewById(R.id.create_post_text);
        photoButton = view.findViewById(R.id.photo_button);
        videoButton = view.findViewById(R.id.video_button);
        statsButton = view.findViewById(R.id.stats_button);
        postButton = view.findViewById(R.id.post_button);
        
        // Initialize media preview views
        mediaPreviewContainer = view.findViewById(R.id.media_preview_container);
        imagePreview = view.findViewById(R.id.image_preview);
        videoPreview = view.findViewById(R.id.video_preview);
        statsInputContainer = view.findViewById(R.id.stats_input_container);
        goalsInput = view.findViewById(R.id.goals_input);
        assistsInput = view.findViewById(R.id.assists_input);
        minutesInput = view.findViewById(R.id.minutes_input);
        removeMediaButton = view.findViewById(R.id.remove_media_button);
        
        // Setup search functionality
        setupSearchBar();
    }
    
    private void setupSearchBar() {
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPosts();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
    
    private void filterPosts() {
        String searchQuery = searchBar.getText().toString().toLowerCase().trim();
        android.util.Log.d("AthleteHomeFragment", "Searching posts: '" + searchQuery + "'");
        
        List<Post> filteredList = new ArrayList<>();
        
        if (searchQuery.isEmpty()) {
            // If search is empty, show all posts
            filteredList = new ArrayList<>(postsList);
        } else {
            // Filter posts based on search query
            for (Post post : postsList) {
                String authorName = post.getAuthorName().toLowerCase();
                String content = post.getContent().toLowerCase();
                String type = post.getType().toLowerCase();
                
                if (authorName.contains(searchQuery) || 
                    content.contains(searchQuery) || 
                    type.contains(searchQuery)) {
                    filteredList.add(post);
                }
            }
        }
        
        // Update the adapter with filtered list
        postsAdapter.updateList(filteredList);
        android.util.Log.d("AthleteHomeFragment", "Filtered posts count: " + filteredList.size());
    }

    private void setupRecyclerViews() {
        // Setup Stories RecyclerView (Horizontal)
        storiesList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(storiesList);
        storiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        storiesRecyclerView.setAdapter(storiesAdapter);

        // Setup Posts RecyclerView (Vertical)
        postsList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postsList);
        postsAdapter.setOnPostClickListener(this);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setAdapter(postsAdapter);
    }
    
    private void setupVideoPlayer() {
        if (videoThumbnail != null) {
            try {
                // Use a sample video URL
                String videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
                videoThumbnail.setVideoURI(Uri.parse(videoUrl));
                
                videoThumbnail.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // Start playing when prepared
                        mediaPlayer.setLooping(true);
                        videoThumbnail.start();
                    }
                });
                
                videoThumbnail.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        // Loop the video
                        videoThumbnail.start();
                    }
                });
                
            } catch (Exception e) {
                // Fallback to static image if video fails
                videoThumbnail.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }
    }

    private void setupClickListeners() {
        // Setup post creation click listeners
        createPostText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Show post button when user starts typing
                postButton.setVisibility(View.VISIBLE);
            }
        });
        
        createPostText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show post button when there's text
                if (s.toString().trim().length() > 0 || mediaPreviewContainer.getVisibility() == View.VISIBLE) {
                    postButton.setVisibility(View.VISIBLE);
                } else {
                    postButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        
        // Post type selection buttons
        photoButton.setOnClickListener(v -> showPhotoOptions());
        
        videoButton.setOnClickListener(v -> showVideoOptions());
        
        statsButton.setOnClickListener(v -> showStatsInput());
        
        // Remove media button
        removeMediaButton.setOnClickListener(v -> clearMedia());
        
        // Post submission button
        postButton.setOnClickListener(v -> createPost());
    }
    
    private void showPhotoOptions() {
        selectedPostType = "photo";
        android.util.Log.d("AthleteHomeFragment", "Photo options selected");
        
        // Show dialog to choose camera or gallery
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo");
        String[] options = {"Take Photo", "Choose from Gallery"};
        
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                dispatchTakePictureIntent();
            } else {
                dispatchPickPhotoIntent();
            }
        });
        
        builder.show();
    }
    
    private void showVideoOptions() {
        selectedPostType = "video";
        android.util.Log.d("AthleteHomeFragment", "Video options selected");
        
        // Show dialog to choose camera or gallery
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Add Video");
        String[] options = {"Record Video", "Choose from Gallery"};
        
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                dispatchTakeVideoIntent();
            } else {
                dispatchPickVideoIntent();
            }
        });
        
        builder.show();
    }
    
    private void showStatsInput() {
        selectedPostType = "stats";
        android.util.Log.d("AthleteHomeFragment", "Stats input selected");
        
        // Clear any existing media
        clearMedia();
        
        // Show stats input container
        mediaPreviewContainer.setVisibility(View.VISIBLE);
        statsInputContainer.setVisibility(View.VISIBLE);
        
        // Focus on the text field and show keyboard
        createPostText.requestFocus();
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) 
            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(createPostText, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
        
        // Show post button
        postButton.setVisibility(View.VISIBLE);
        
        // Highlight stats button
        resetButtonStyles();
        statsButton.setBackgroundColor(getResources().getColor(R.color.scouthub_accent_green));
    }
    
    private void clearMedia() {
        // Hide all media previews
        mediaPreviewContainer.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);
        videoPreview.setVisibility(View.GONE);
        statsInputContainer.setVisibility(View.GONE);
        
        // Clear selected media URI
        selectedMediaUri = null;
        
        // Clear stats inputs
        goalsInput.setText("");
        assistsInput.setText("");
        minutesInput.setText("");
        
        // Reset button styles
        resetButtonStyles();
        
        // Hide post button if no text
        if (createPostText.getText().toString().trim().isEmpty()) {
            postButton.setVisibility(View.GONE);
        }
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_PHOTO_CAPTURE);
        }
    }
    
    private void dispatchPickPhotoIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_PHOTO_PICK);
    }
    
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    
    private void dispatchPickVideoIntent() {
        Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickVideoIntent, REQUEST_VIDEO_PICK);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        android.util.Log.d("AthleteHomeFragment", "onActivityResult - requestCode: " + requestCode + ", resultCode: " + resultCode);
        
        if (resultCode == android.app.Activity.RESULT_OK) {
            Uri mediaUri = data != null ? data.getData() : null;
            
            android.util.Log.d("AthleteHomeFragment", "Media URI received: " + (mediaUri != null ? mediaUri.toString() : "null"));
            
            if (mediaUri != null) {
                selectedMediaUri = mediaUri;
                
                switch (requestCode) {
                    case REQUEST_PHOTO_CAPTURE:
                    case REQUEST_PHOTO_PICK:
                        showImagePreview(mediaUri);
                        break;
                    case REQUEST_VIDEO_CAPTURE:
                    case REQUEST_VIDEO_PICK:
                        showVideoPreview(mediaUri);
                        break;
                }
            } else {
                android.util.Log.e("AthleteHomeFragment", "Media URI is null!");
            }
        } else {
            android.util.Log.d("AthleteHomeFragment", "Activity result was not OK: " + resultCode);
        }
    }
    
    private void showImagePreview(Uri imageUri) {
        android.util.Log.d("AthleteHomeFragment", "showImagePreview called with URI: " + imageUri.toString());
        
        // Store the URI first before clearing media
        selectedMediaUri = imageUri;
        
        // Clear other media (but not the URI we just set)
        mediaPreviewContainer.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);
        videoPreview.setVisibility(View.GONE);
        statsInputContainer.setVisibility(View.GONE);
        
        // Clear stats inputs
        goalsInput.setText("");
        assistsInput.setText("");
        minutesInput.setText("");
        
        // Reset button styles
        resetButtonStyles();
        
        // Show media container and image preview
        mediaPreviewContainer.setVisibility(View.VISIBLE);
        imagePreview.setVisibility(View.VISIBLE);
        imagePreview.setImageURI(imageUri);
        
        // Show post button
        postButton.setVisibility(View.VISIBLE);
        
        // Highlight photo button
        photoButton.setBackgroundColor(getResources().getColor(R.color.scouthub_accent_green));
        
        // Focus on text field
        createPostText.requestFocus();
        
        android.util.Log.d("AthleteHomeFragment", "Image preview setup complete. selectedMediaUri: " + selectedMediaUri);
    }
    
    private void showVideoPreview(Uri videoUri) {
        android.util.Log.d("AthleteHomeFragment", "showVideoPreview called with URI: " + videoUri.toString());
        
        // Store the URI first before clearing media
        selectedMediaUri = videoUri;
        
        // Clear other media (but not the URI we just set)
        mediaPreviewContainer.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);
        videoPreview.setVisibility(View.GONE);
        statsInputContainer.setVisibility(View.GONE);
        
        // Clear stats inputs
        goalsInput.setText("");
        assistsInput.setText("");
        minutesInput.setText("");
        
        // Reset button styles
        resetButtonStyles();
        
        // Show media container and video preview
        mediaPreviewContainer.setVisibility(View.VISIBLE);
        videoPreview.setVisibility(View.VISIBLE);
        videoPreview.setVideoURI(videoUri);
        videoPreview.start(); // Start video preview
        
        // Show post button
        postButton.setVisibility(View.VISIBLE);
        
        // Highlight video button
        videoButton.setBackgroundColor(getResources().getColor(R.color.scouthub_accent_green));
        
        // Focus on text field
        createPostText.requestFocus();
        
        android.util.Log.d("AthleteHomeFragment", "Video preview setup complete. selectedMediaUri: " + selectedMediaUri);
    }
    
    @Override
    public void onPostClick(Post post) {
        // Handle post click if needed
        android.util.Log.d("AthleteHomeFragment", "Post clicked: " + post.getContent());
    }
    
    @Override
    public void onAddToCalendarClick(Post post) {
        createCalendarEventFromPost(post);
    }
    
    private void createCalendarEventFromPost(Post post) {
        android.util.Log.d("AthleteHomeFragment", "Creating calendar event from post: " + post.getContent());
        
        try {
            // Create a CalendarEvent from the post
            String eventId = String.valueOf(System.currentTimeMillis());
            String title = "Trial Opportunity";
            String location = extractLocationFromPost(post.getContent());
            String date = extractDateFromPost(post.getDate());
            String time = "10:00 AM"; // Default time for trials
            String type = "trial";
            String notes = post.getContent();
            
            CalendarEvent calendarEvent = new CalendarEvent(eventId, title, type, date, time, location);
            
            // Navigate to CalendarFragment and pass the event
            if (getActivity() != null) {
                CalendarFragment calendarFragment = CalendarFragment.newInstance();
                
                // We need to add the event to the calendar fragment's data
                // For now, we'll just navigate to calendar and show a toast
                android.widget.Toast.makeText(requireContext(), 
                    "Event added to calendar: " + title, 
                    android.widget.Toast.LENGTH_SHORT).show();
                
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, calendarFragment)
                    .addToBackStack(null)
                    .commit();
            }
            
        } catch (Exception e) {
            android.util.Log.e("AthleteHomeFragment", "Error creating calendar event: " + e.getMessage());
            android.widget.Toast.makeText(requireContext(), 
                "Failed to add event to calendar", 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    
    private String extractLocationFromPost(String content) {
        // Try to extract location from post content
        if (content.toLowerCase().contains("stadium") || content.toLowerCase().contains("ground")) {
            // Simple extraction - look for common location indicators
            String[] words = content.split(" ");
            for (String word : words) {
                if (word.toLowerCase().contains("stadium") || 
                    word.toLowerCase().contains("ground") ||
                    word.toLowerCase().contains("field") ||
                    word.toLowerCase().contains("center")) {
                    return word;
                }
            }
        }
        return "TBD"; // Default if no location found
    }
    
    private String extractDateFromPost(String postDate) {
        // Use the post date or default to tomorrow
        if (postDate != null && !postDate.isEmpty()) {
            return postDate;
        } else {
            // Default to tomorrow for trials
            java.util.Date tomorrow = new java.util.Date();
            tomorrow.setTime(tomorrow.getTime() + (24 * 60 * 60 * 1000)); // Add 1 day
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
        }
    }
    
    private void resetButtonStyles() {
        // Reset all buttons to default style
        photoButton.setBackgroundColor(getResources().getColor(R.color.scouthub_card_background));
        videoButton.setBackgroundColor(getResources().getColor(R.color.scouthub_card_background));
        statsButton.setBackgroundColor(getResources().getColor(R.color.scouthub_card_background));
    }
    
    private void createPost() {
        String content = createPostText.getText().toString().trim();
        
        if (content.isEmpty() && selectedPostType.equals("highlight")) {
            android.widget.Toast.makeText(requireContext(), "Please enter some content", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        // For stats posts, validate that at least one stat is entered
        if (selectedPostType.equals("stats")) {
            String goals = goalsInput.getText().toString().trim();
            String assists = assistsInput.getText().toString().trim();
            String minutes = minutesInput.getText().toString().trim();
            
            if (goals.isEmpty() && assists.isEmpty() && minutes.isEmpty() && content.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Please enter some stats or content", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Format stats into content if provided
            StringBuilder statsContent = new StringBuilder();
            if (!goals.isEmpty()) {
                statsContent.append("Goals: ").append(goals).append("\n");
            }
            if (!assists.isEmpty()) {
                statsContent.append("Assists: ").append(assists).append("\n");
            }
            if (!minutes.isEmpty()) {
                statsContent.append("Minutes: ").append(minutes).append("\n");
            }
            
            if (statsContent.length() > 0) {
                if (!content.isEmpty()) {
                    content = content + "\n\n" + statsContent.toString().trim();
                } else {
                    content = statsContent.toString().trim();
                }
            }
        }
        
        android.util.Log.d("AthleteHomeFragment", "Creating post: type=" + selectedPostType + ", content=" + content);
        
        // Get current date
        String currentDate = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(new java.util.Date());
        
        // Create new post
        String postId = String.valueOf(postsList.size() + 1);
        String authorName = "You"; // Current user
        String imageUrl = "";
        String videoUrl = "";
        
        // Handle media URLs based on post type
        if (selectedPostType.equals("photo") && selectedMediaUri != null) {
            imageUrl = selectedMediaUri.toString();
            android.util.Log.d("AthleteHomeFragment", "Photo post - setting imageUrl: " + imageUrl);
        } else if (selectedPostType.equals("video") && selectedMediaUri != null) {
            videoUrl = selectedMediaUri.toString();
            android.util.Log.d("AthleteHomeFragment", "Video post - setting videoUrl: " + videoUrl);
        }
        
        android.util.Log.d("AthleteHomeFragment", "Creating post with imageUrl: " + imageUrl + ", videoUrl: " + videoUrl);
        
        Post newPost = new Post(postId, authorName, content, selectedPostType, currentDate, imageUrl, videoUrl);
        
        // Add to the beginning of the list (most recent first)
        postsList.add(0, newPost);
        
        // Refresh the adapter
        postsAdapter.notifyDataSetChanged();
        
        // Clear the input field and media
        createPostText.setText("");
        clearMedia();
        
        // Hide post button
        postButton.setVisibility(View.GONE);
        
        // Reset to default post type
        selectedPostType = "highlight";
        
        // Show success message
        android.widget.Toast.makeText(requireContext(), "Post created successfully!", android.widget.Toast.LENGTH_SHORT).show();
        
        // Hide keyboard
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) 
            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(createPostText.getWindowToken(), 0);
        
        android.util.Log.d("AthleteHomeFragment", "New post added. Total posts: " + postsList.size());
    }

    private void loadMockData() {
        // Load mock stories with realistic profile images
        storiesList.add(new Story("1", "John Doe", "home_profile_1", true));
        storiesList.add(new Story("2", "Jane Smith", "home_profile_2", false));
        storiesList.add(new Story("3", "Mike Johnson", "home_profile_3", false));
        storiesList.add(new Story("4", "Sarah Wilson", "home_profile_4", false));
        storiesList.add(new Story("5", "Tom Brown", "home_profile_1", false));
        storiesAdapter.notifyDataSetChanged();

        // Load mock posts with your sample images
        postsList.add(new Post("1", "FC Barcelona", "U18 Trials - Looking for talent! Join our youth academy and showcase your skills.", "trial", "2024-03-25", "sample_image_1"));
        postsList.add(new Post("2", "John Doe", "Amazing goal from yesterday's match! What a performance!", "highlight", "2024-03-24", "sample_image_2"));
        postsList.add(new Post("3", "Real Madrid", "Youth Academy Recruitment - Open trials for U16 and U18 teams", "trial", "2024-03-26", "sample_image_3"));
        postsList.add(new Post("4", "Jane Smith", "Training session highlights - Great teamwork today!", "highlight", "2024-03-23", "sample_image_1"));
        postsList.add(new Post("5", "Manchester United", "Scouting Day - Looking for young talent in the region", "trial", "2024-03-27", "sample_image_2"));
        postsAdapter.notifyDataSetChanged();
    }
}
