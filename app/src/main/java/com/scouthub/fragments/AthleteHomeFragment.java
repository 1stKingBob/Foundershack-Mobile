package com.scouthub.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.adapters.PostsAdapter;
import com.scouthub.adapters.StoriesAdapter;
import com.scouthub.models.Post;
import com.scouthub.models.Story;

import java.util.ArrayList;
import java.util.List;

public class AthleteHomeFragment extends Fragment {
    
    private EditText searchBar;
    private RecyclerView storiesRecyclerView;
    private RecyclerView postsRecyclerView;
    private VideoView videoThumbnail;
    
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
        // Click listeners are handled in the layout
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
