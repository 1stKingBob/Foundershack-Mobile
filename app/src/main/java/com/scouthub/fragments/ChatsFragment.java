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
import com.google.android.material.tabs.TabLayout;
import com.scouthub.R;
import com.scouthub.adapters.ChatAdapter;
import com.scouthub.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private EditText searchBar;
    private TabLayout tabLayout;
    private RecyclerView chatsRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupTabLayout();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        tabLayout = view.findViewById(R.id.tab_layout);
        chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
    }

    private void setupRecyclerView() {
        chatsList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsList);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatsRecyclerView.setAdapter(chatAdapter);
        
        // Set click listener
        chatAdapter.setOnChatClickListener(chat -> {
            // Navigate to conversation fragment
            ConversationFragment conversationFragment = ConversationFragment.newInstance(chat);
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, conversationFragment)
                    .addToBackStack(null)
                    .commit();
            }
        });
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Filter chats based on selected tab
                filterChats(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterChats(int tabPosition) {
        // TODO: Implement chat filtering logic
        chatAdapter.notifyDataSetChanged();
    }

    private void loadMockData() {
        chatsList.add(new Chat("1", "John Doe", "Hey, are you coming to the trials tomorrow? 🏈", "2:30 PM", "player", "sample_profile_1"));
        chatsList.add(new Chat("2", "Mike Scout", "I saw your profile, very impressive! Let's discuss opportunities 🌟", "1:15 PM", "scout", "sample_profile_3"));
        chatsList.add(new Chat("3", "FC Barcelona", "Trial invitation sent! Check your email for details 📧", "Yesterday", "club", "sample_profile_4"));
        chatsList.add(new Chat("4", "Jane Smith", "Great match yesterday! Your performance was outstanding ⭐", "Yesterday", "player", "sample_profile_5"));
        chatsList.add(new Chat("5", "Real Madrid Scout", "Interested in your skills. Can we schedule a call? 🤝", "2 days ago", "scout", "sample_profile_6"));
        chatAdapter.notifyDataSetChanged();
    }
}
