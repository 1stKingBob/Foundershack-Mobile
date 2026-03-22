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
        
        holder.authorNameTextView.setText(post.getAuthorName());
        holder.contentTextView.setText(post.getContent());
        holder.dateTextView.setText(post.getDate());
        
        // Load image if available
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            holder.postImageView.setVisibility(View.VISIBLE);
            int drawableId = context.getResources().getIdentifier(
                post.getImageUrl(), "drawable", context.getPackageName());
            holder.postImageView.setImageResource(drawableId);
        } else {
            holder.postImageView.setVisibility(View.GONE);
        }
        
        // Show Add to Calendar button for trial posts
        if ("trial".equals(post.getType())) {
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
        ImageView postImageView;
        Button addToCalendarButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorNameTextView = itemView.findViewById(R.id.author_name);
            contentTextView = itemView.findViewById(R.id.content);
            dateTextView = itemView.findViewById(R.id.date);
            postImageView = itemView.findViewById(R.id.post_image);
            addToCalendarButton = itemView.findViewById(R.id.add_to_calendar_button);
        }
    }
}
