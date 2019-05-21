package com.example.graduate.findingcooking.activity;

import android.content.pm.ActivityInfo;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Video;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoDetailActivity extends BaseActivity {
    @BindView(R.id.videoPlayer)
    JCVideoPlayerStandard videoPlayer;
    Video video;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onInit() {
        video=(Video)getIntent().getSerializableExtra("video");
    }

    @Override
    public void onBindData() {
        videoPlayer.setUp(video.getVideoURL(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, video.getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
