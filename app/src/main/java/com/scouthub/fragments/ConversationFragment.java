package com.scouthub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.adapters.MessageAdapter;
import com.scouthub.models.Chat;
import com.scouthub.models.Message;
import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment {
    
    private ImageView profileImage;
    private TextView nameTextView;
    private TextView statusTextView;
    private ImageButton backButton;
    private ImageButton moreButton;
    
    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messagesList;
    
    private EditText messageInput;
    private ImageButton attachButton;
    private ImageButton sendButton;
    
    private Chat currentChat;

    public ConversationFragment() {
        // Required empty public constructor
    }

    public static ConversationFragment newInstance(Chat chat) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putParcelable("chat", chat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentChat = getArguments().getParcelable("chat");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        initializeViews(view);
        setupChatInfo();
        setupRecyclerView();
        loadMockMessages();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        // Header
        profileImage = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.name);
        statusTextView = view.findViewById(R.id.status);
        backButton = view.findViewById(R.id.back_button);
        moreButton = view.findViewById(R.id.more_button);
        
        // Messages
        messagesRecyclerView = view.findViewById(R.id.messages_recycler_view);
        
        // Input
        messageInput = view.findViewById(R.id.message_input);
        attachButton = view.findViewById(R.id.attach_button);
        sendButton = view.findViewById(R.id.send_button);
    }

    private void setupChatInfo() {
        if (currentChat != null) {
            nameTextView.setText(currentChat.getName());
            statusTextView.setText("Active now");
            
            // Set profile image based on chat's profile image resource
            int drawableId = getContext().getResources().getIdentifier(
                currentChat.getProfileImageUrl(), "drawable", getContext().getPackageName());
            if (drawableId != 0) {
                profileImage.setBackgroundResource(drawableId);
            } else {
                // Fallback to default profile
                profileImage.setBackgroundResource(R.drawable.sample_profile_1);
            }
        }
    }

    private void setupRecyclerView() {
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagesList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecyclerView.setAdapter(messageAdapter);
    }

    private void loadMockMessages() {
        if (currentChat != null) {
            // Add some mock messages
            messagesList.add(new Message("1", "Hey! How are you doing?", "2:25 PM", false, "2", currentChat.getName()));
            messagesList.add(new Message("2", "I'm good! Just finished training", "2:26 PM", true, "1", "Me"));
            messagesList.add(new Message("3", "That's great! How did it go?", "2:27 PM", false, "2", currentChat.getName()));
            messagesList.add(new Message("4", "Pretty well, working on some new techniques", "2:28 PM", true, "1", "Me"));
            messagesList.add(new Message("5", "Awesome! Keep it up!", "2:30 PM", false, "2", currentChat.getName()));
            
            messageAdapter.notifyDataSetChanged();
            
            // Scroll to bottom
            messagesRecyclerView.scrollToPosition(messagesList.size() - 1);
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            // Go back to chats list
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        sendButton.setOnClickListener(v -> {
            sendMessage();
        });

        attachButton.setOnClickListener(v -> {
            // TODO: Implement attachment functionality
        });

        moreButton.setOnClickListener(v -> {
            // TODO: Implement more options
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create new message
            Message newMessage = new Message(
                String.valueOf(System.currentTimeMillis()),
                messageText,
                getCurrentTime(),
                true,
                "1",
                "Me"
            );

            // Add message to list
            messageAdapter.addMessage(newMessage);

            // Clear input
            messageInput.setText("");

            // Scroll to bottom
            messagesRecyclerView.scrollToPosition(messagesList.size() - 1);

            // TODO: Send message to backend
        }
    }

    private String getCurrentTime() {
        // Simple time formatting - in real app, use proper date formatting
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        int minute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
        String ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12;
        if (hour == 0) hour = 12;
        return String.format("%d:%02d %s", hour, minute, ampm);
    }
}
