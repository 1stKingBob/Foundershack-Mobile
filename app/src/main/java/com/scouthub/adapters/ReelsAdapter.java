package com.scouthub.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.Reel;
import java.util.List;

public class ReelsAdapter extends RecyclerView.Adapter<ReelsAdapter.ReelViewHolder> {

    private List<Reel> reelsList;
    private Context context;

    public ReelsAdapter(List<Reel> reelsList) {
        this.reelsList = reelsList;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        Reel reel = reelsList.get(position);
        
        // Debug logging
        android.util.Log.d("ReelsAdapter", "Position: " + position + ", Caption: " + reel.getCaption() + ", Author: " + reel.getAuthorName());
        
        holder.captionTextView.setText(reel.getCaption());
        holder.authorNameTextView.setText(reel.getAuthorName());
        
        // Load and play video
        try {
            String videoUrl = reel.getVideoUrl();
            android.util.Log.d("ReelsAdapter", "Loading video: " + videoUrl);
            
            // Check if it's a local URI (file://) or remote URL (http://)
            if (videoUrl != null) {
                if (videoUrl.startsWith("file://") || videoUrl.startsWith("content://")) {
                    // Local video file
                    android.util.Log.d("ReelsAdapter", "Loading local video: " + videoUrl);
                    holder.playerView.setVideoURI(Uri.parse(videoUrl));
                } else if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://")) {
                    // Remote video URL
                    android.util.Log.d("ReelsAdapter", "Loading remote video: " + videoUrl);
                    holder.playerView.setVideoURI(Uri.parse(videoUrl));
                } else {
                    // Fallback to sample video for demo purposes
                    android.util.Log.d("ReelsAdapter", "Using fallback sample video");
                    String sampleVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
                    holder.playerView.setVideoURI(Uri.parse(sampleVideoUrl));
                }
                
                // Set up media controller for better playback control
                holder.playerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        android.util.Log.d("ReelsAdapter", "Video prepared, starting playback");
                        // Start playing automatically
                        mediaPlayer.setLooping(true);
                        holder.playerView.start();
                    }
                });
                
                holder.playerView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                        android.util.Log.e("ReelsAdapter", "Video playback error - what: " + what + ", extra: " + extra);
                        // Show fallback background if video fails
                        holder.playerView.setBackgroundResource(R.drawable.sample_video_thumbnail);
                        return true;
                    }
                });
                
                holder.playerView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        android.util.Log.d("ReelsAdapter", "Video playback completed");
                        // Loop the video
                        holder.playerView.start();
                    }
                });
                
            } else {
                android.util.Log.e("ReelsAdapter", "Video URL is null");
                // Fallback to drawable background if no video URL
                holder.playerView.setBackgroundResource(R.drawable.sample_video_thumbnail);
            }
            
        } catch (Exception e) {
            android.util.Log.e("ReelsAdapter", "Error loading video: " + e.getMessage());
            // Fallback to drawable background if video fails
            holder.playerView.setBackgroundResource(R.drawable.sample_video_thumbnail);
            e.printStackTrace();
        }
        
        // Setup like button
        holder.likeButton.setImageResource(reel.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        holder.likeButton.setOnClickListener(v -> {
            reel.setLiked(!reel.isLiked());
            holder.likeButton.setImageResource(reel.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        });
        
        // Setup save button
        holder.saveButton.setOnClickListener(v -> {
            // TODO: Implement save functionality
        });
    }

    @Override
    public int getItemCount() {
        return reelsList.size();
    }

    static class ReelViewHolder extends RecyclerView.ViewHolder {
        VideoView playerView;
        TextView captionTextView;
        TextView authorNameTextView;
        ImageButton likeButton;
        ImageButton saveButton;

        ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.player_view);
            captionTextView = itemView.findViewById(R.id.caption);
            authorNameTextView = itemView.findViewById(R.id.author_name);
            likeButton = itemView.findViewById(R.id.like_button);
            saveButton = itemView.findViewById(R.id.save_button);
        }
    }
}
