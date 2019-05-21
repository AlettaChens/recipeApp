package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Video;
import com.example.graduate.findingcooking.utils.RecipeSP;

import java.util.List;


public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.RecipeHolder> {
    private Context context;
    private List<Video> videoList;
    private VideoClickListener videoClickListener;
    private String videoState;
    private VideoStateListener videoStateListener;
    private RecipeSP recipeSP;

    public VideoListAdapter(Context context, List<Video> recipeList, VideoClickListener videoClickListener) {
        this.context = context;
        this.videoList = recipeList;
        this.videoClickListener = videoClickListener;
        recipeSP = new RecipeSP(context);
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_list, viewGroup, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder recipeHolder, int i) {
        RecipeHolder recommendHolder = recipeHolder;
        recommendHolder.update(videoList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView video_name;
        private TextView video_update_time;
        private int position;
        private CheckBox checkState;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.video_name);
            video_update_time = itemView.findViewById(R.id.video_update_time);
            checkState = itemView.findViewById(R.id.checkState);
            if (recipeSP.getUserType().equals("用户")) {
                checkState.setVisibility(View.GONE);
            } else {
                checkState.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoClickListener != null) {
                        videoClickListener.VideoListItemClick(videoList.get(position));
                    }
                }
            });

            checkState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        videoState = "验证通过";
                    } else {
                        videoState = "待验证";
                    }
                    if (videoClickListener != null) {
                        videoStateListener.onVideoStateChange(videoState, videoList.get(position).getId());
                    }
                }
            });
        }

        public void update(Video item, int i) {
            position = i;
            video_name.setText(item.getTitle());
            video_update_time.setText(item.getDate());
        }
    }

    public void setVideoStateListener(VideoStateListener videoStateListener) {
        this.videoStateListener = videoStateListener;
    }

    public interface VideoClickListener {
        void VideoListItemClick(Video info);
    }

    public interface VideoStateListener {
        void onVideoStateChange(String state, long videoId);
    }
}
