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
import com.scouthub.R;
import com.scouthub.adapters.PostsAdapter;
import com.scouthub.models.Post;

import java.util.ArrayList;
import java.util.List;

public class ScoutHomeFragment extends Fragment {

    private EditText searchBar;
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> postsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scout_home, container, false);

        initializeViews(view);
        setupRecyclerView();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
    }

    private void setupRecyclerView() {
        postsList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postsList);
        postsAdapter.setOnPostClickListener(new PostsAdapter.OnPostClickListener() {
            @Override
            public void onAddToCalendarClick(Post post) {
                // TODO: Add trial to scout's calendar
            }

            @Override
            public void onPostClick(Post post) {
                // TODO: Open post details or athlete profile
            }
        });
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setAdapter(postsAdapter);
    }

    private void loadMockData() {
        // Load posts for scouts using your sample images
        postsList.add(new Post("1", "FC Barcelona", "U18 Trials - Looking for talent", "trial", "2024-03-25", "sample_image_1"));
        postsList.add(new Post("2", "John Doe", "Amazing goal from yesterday's match!", "highlight", "2024-03-24", "sample_image_2"));
        postsList.add(new Post("3", "Real Madrid", "Youth Academy Recruitment", "trial", "2024-03-26", "sample_image_3"));
        postsList.add(new Post("4", "Jane Smith", "Training session highlights", "highlight", "2024-03-23", "sample_image_1"));
        postsAdapter.notifyDataSetChanged();
    }
}
