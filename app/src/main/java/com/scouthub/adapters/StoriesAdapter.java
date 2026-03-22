package com.scouthub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.Story;
import com.bumptech.glide.Glide;
import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {

    private List<Story> storiesList;
    private Context context;

    public StoriesAdapter(List<Story> storiesList) {
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storiesList.get(position);
        
        holder.usernameTextView.setText(story.getUsername());
        
        // Load image using drawable resource
        int drawableId = context.getResources().getIdentifier(
            story.getImageUrl(), "drawable", context.getPackageName());
        holder.profileImageView.setImageResource(drawableId);
        
        // Set ring color based on viewed status
        if (story.isViewed()) {
            holder.storyRing.setBackgroundResource(R.drawable.story_ring_viewed);
        } else {
            holder.storyRing.setBackgroundResource(R.drawable.story_ring_unviewed);
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        View storyRing;
        TextView usernameTextView;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image);
            storyRing = itemView.findViewById(R.id.story_ring);
            usernameTextView = itemView.findViewById(R.id.username);
        }
    }
}
