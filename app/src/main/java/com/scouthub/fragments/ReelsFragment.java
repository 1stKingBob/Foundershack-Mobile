package com.scouthub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.scouthub.R;
import com.scouthub.adapters.ReelsAdapter;
import com.scouthub.models.Reel;

import java.util.ArrayList;
import java.util.List;

public class ReelsFragment extends Fragment {

    private ViewPager2 viewPager;
    private ReelsAdapter reelsAdapter;
    private List<Reel> reelsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reels, container, false);

        initializeViews(view);
        setupViewPager();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        viewPager = view.findViewById(R.id.viewpager_reels);
    }

    private void setupViewPager() {
        reelsList = new ArrayList<>();
        reelsAdapter = new ReelsAdapter(reelsList);
        viewPager.setAdapter(reelsAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }

    private void loadMockData() {
        // Copy videos to internal storage first, then use those URIs
        try {
            // Copy first video
            String video1Path = copyAssetToInternalStorage("High School soccer action #soccer #shorts #shortvideo.mp4");
            String video2Path = copyAssetToInternalStorage("High school kid scores in cup final  #soccer #futbol #highschool #indiana #explore #skills #final.mp4");
            
            if (video1Path != null) {
                reelsList.add(new Reel("1", "High School Soccer Action - Amazing goal! ⚽", "Your Name", video1Path, 60, false));
            }
            if (video2Path != null) {
                reelsList.add(new Reel("2", "High School Finals - Championship moment! 🏆", "Your Name", video2Path, 45, true));
            }
        } catch (Exception e) {
            android.util.Log.e("ReelsFragment", "Error copying videos: " + e.getMessage());
            // Fallback to remote videos if local copy fails
            reelsList.add(new Reel("1", "High School Soccer Action - Amazing goal! ⚽", "Your Name", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", 60, false));
            reelsList.add(new Reel("2", "High School Finals - Championship moment! 🏆", "Your Name", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 45, true));
        }
        reelsAdapter.notifyDataSetChanged();
    }
    
    private String copyAssetToInternalStorage(String assetFileName) {
        try {
            android.content.Context context = requireContext();
            java.io.File outFile = new java.io.File(context.getFilesDir(), assetFileName);
            
            // If file already exists, return its URI
            if (outFile.exists()) {
                android.util.Log.d("ReelsFragment", "File already exists: " + outFile.getAbsolutePath());
                return android.net.Uri.fromFile(outFile).toString();
            }
            
            // Copy file from assets to internal storage
            java.io.InputStream in = context.getAssets().open(assetFileName);
            java.io.FileOutputStream out = new java.io.FileOutputStream(outFile);
            
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            
            android.util.Log.d("ReelsFragment", "Copied video to: " + outFile.getAbsolutePath());
            return android.net.Uri.fromFile(outFile).toString();
            
        } catch (Exception e) {
            android.util.Log.e("ReelsFragment", "Error copying asset " + assetFileName + ": " + e.getMessage());
            return null;
        }
    }
}
