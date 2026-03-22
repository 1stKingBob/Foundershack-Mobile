package com.scouthub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.User;
import com.bumptech.glide.Glide;
import java.util.List;

public class PromisingPlayersAdapter extends RecyclerView.Adapter<PromisingPlayersAdapter.PlayerViewHolder> {

    private List<User> playersList;
    private Context context;
    private OnPlayerActionListener listener;

    public interface OnPlayerActionListener {
        void onViewProfile(User player);
        void onOpenChat(User player);
    }

    public PromisingPlayersAdapter(List<User> playersList) {
        this.playersList = playersList;
    }

    public void setOnPlayerActionListener(OnPlayerActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_promising_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        User player = playersList.get(position);
        
        holder.nameTextView.setText(player.getName());
        
        // Load profile image
        Glide.with(context)
                .load(player.getProfileImageUrl())
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(holder.profileImageView);
        
        // Set click listeners
        holder.viewProfileButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewProfile(player);
            }
        });
        
        holder.openChatButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpenChat(player);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        Button viewProfileButton;
        Button openChatButton;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            nameTextView = itemView.findViewById(R.id.name);
            viewProfileButton = itemView.findViewById(R.id.view_profile_button);
            openChatButton = itemView.findViewById(R.id.open_chat_button);
        }
    }
}
