package com.scouthub.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.Post;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> postsList;
    private Context context;
    private OnPostClickListener listener;

    public interface OnPostClickListener {
        void onAddToCalendarClick(Post post);
        void onPostClick(Post post);
    }

    public PostsAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    public void setOnPostClickListener(OnPostClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Post> newList) {
        this.postsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        
        // Show header for trial/recruitment posts
        if ("trial".equals(post.getType()) || "recruitment".equals(post.getType())) {
            holder.postHeaderTextView.setVisibility(View.VISIBLE);
            holder.postHeaderTextView.setText("trial".equals(post.getType()) ? "Trial Opportunity" : "Recruitment");
        } else {
            holder.postHeaderTextView.setVisibility(View.GONE);
        }
        
        holder.authorNameTextView.setText(post.getAuthorName());
        holder.contentTextView.setText(post.getContent());
        holder.dateTextView.setText(post.getDate());
        
        // Hide all media views initially
        holder.postImageView.setVisibility(View.GONE);
        holder.postVideoView.setVisibility(View.GONE);
        
        // Handle image display
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            android.util.Log.d("PostsAdapter", "Processing image for post: " + post.getId() + ", imageUrl: " + post.getImageUrl());
            
            if (post.getImageUrl().startsWith("file://") || post.getImageUrl().startsWith("content://")) {
                // Handle actual image URI from camera/gallery
                holder.postImageView.setVisibility(View.VISIBLE);
                try {
                    android.util.Log.d("PostsAdapter", "Setting image URI: " + post.getImageUrl());
                    holder.postImageView.setImageURI(Uri.parse(post.getImageUrl()));
                } catch (Exception e) {
                    android.util.Log.e("PostsAdapter", "Failed to set image URI: " + e.getMessage());
                    // Fallback to drawable if URI fails
                    int drawableId = context.getResources().getIdentifier(
                        post.getImageUrl(), "drawable", context.getPackageName());
                    if (drawableId != 0) {
                        holder.postImageView.setImageResource(drawableId);
                    } else {
                        holder.postImageView.setVisibility(View.GONE);
                    }
                }
            } else {
                // Handle drawable resource
                int drawableId = context.getResources().getIdentifier(
                    post.getImageUrl(), "drawable", context.getPackageName());
                if (drawableId != 0) {
                    holder.postImageView.setVisibility(View.VISIBLE);
                    holder.postImageView.setImageResource(drawableId);
                    android.util.Log.d("PostsAdapter", "Set drawable resource: " + drawableId);
                } else {
                    android.util.Log.d("PostsAdapter", "Drawable resource not found: " + post.getImageUrl());
                }
            }
        } else {
            android.util.Log.d("PostsAdapter", "No image to display for post: " + post.getId());
        }
        
        // Handle video display
        if (post.getVideoUrl() != null && !post.getVideoUrl().isEmpty()) {
            holder.postVideoView.setVisibility(View.VISIBLE);
            try {
                holder.postVideoView.setVideoURI(Uri.parse(post.getVideoUrl()));
                holder.postVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // Start playing when prepared
                        mediaPlayer.setLooping(true);
                        holder.postVideoView.start();
                    }
                });
            } catch (Exception e) {
                holder.postVideoView.setVisibility(View.GONE);
            }
        }
        
        // Show Add to Calendar button for trial posts
        if ("trial".equals(post.getType()) || "recruitment".equals(post.getType())) {
            holder.addToCalendarButton.setVisibility(View.VISIBLE);
            holder.addToCalendarButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCalendarClick(post);
                }
            });
        } else {
            holder.addToCalendarButton.setVisibility(View.GONE);
        }
        
        // Set click listener for the entire post
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPostClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView authorNameTextView;
        TextView contentTextView;
        TextView dateTextView;
        TextView postHeaderTextView;
        ImageView postImageView;
        VideoView postVideoView;
        Button addToCalendarButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorNameTextView = itemView.findViewById(R.id.author_name);
            contentTextView = itemView.findViewById(R.id.content);
            dateTextView = itemView.findViewById(R.id.date);
            postHeaderTextView = itemView.findViewById(R.id.post_header);
            postImageView = itemView.findViewById(R.id.post_image);
            postVideoView = itemView.findViewById(R.id.post_video);
            addToCalendarButton = itemView.findViewById(R.id.add_to_calendar_button);
        }
    }
}
