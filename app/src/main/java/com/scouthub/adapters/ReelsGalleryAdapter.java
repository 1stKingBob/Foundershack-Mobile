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
import com.scouthub.models.Reel;
import com.bumptech.glide.Glide;
import java.util.List;

public class ReelsGalleryAdapter extends RecyclerView.Adapter<ReelsGalleryAdapter.ReelGalleryViewHolder> {

    private List<Reel> reelsList;
    private Context context;
    private OnReelActionListener listener;

    public interface OnReelActionListener {
        void onReelClick(Reel reel);
        void onReelDelete(Reel reel);
        void onUploadReel();
    }

    public ReelsGalleryAdapter(List<Reel> reelsList) {
        this.reelsList = reelsList;
    }

    public void setOnReelActionListener(OnReelActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReelGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_reel_gallery, parent, false);
        return new ReelGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelGalleryViewHolder holder, int position) {
        if (position == reelsList.size()) {
            // Show upload button
            holder.uploadLayout.setVisibility(View.VISIBLE);
            holder.reelLayout.setVisibility(View.GONE);
            
            holder.uploadButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUploadReel();
                }
            });
        } else {
            // Show reel
            holder.uploadLayout.setVisibility(View.GONE);
            holder.reelLayout.setVisibility(View.VISIBLE);
            
            Reel reel = reelsList.get(position);
            holder.captionTextView.setText(reel.getCaption());
            
            // Load video thumbnail (placeholder for now)
            Glide.with(context)
                    .load(R.drawable.ic_reels)
                    .into(holder.thumbnailImageView);
            
            holder.thumbnailImageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReelClick(reel);
                }
            });
            
            holder.deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReelDelete(reel);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reelsList.size() + 1; // +1 for upload button
    }

    static class ReelGalleryViewHolder extends RecyclerView.ViewHolder {
        View uploadLayout;
        View reelLayout;
        ImageView uploadButton;
        ImageView thumbnailImageView;
        TextView captionTextView;
        ImageView deleteButton;

        ReelGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            uploadLayout = itemView.findViewById(R.id.upload_layout);
            reelLayout = itemView.findViewById(R.id.reel_layout);
            uploadButton = itemView.findViewById(R.id.upload_button);
            thumbnailImageView = itemView.findViewById(R.id.thumbnail);
            captionTextView = itemView.findViewById(R.id.caption);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
