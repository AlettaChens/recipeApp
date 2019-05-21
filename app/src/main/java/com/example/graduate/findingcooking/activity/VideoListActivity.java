package com.example.graduate.findingcooking.activity;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.VideoListAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Video;
import com.example.graduate.findingcooking.dialog.VideoNameDialog;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class VideoListActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.publish_video)
    ImageView publishVideo;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    String filename;
    VideoNameDialog videoNameDialog;
    VideoListAdapter videoListAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video_list;
    }

    @Override
    public void onInit() {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        videoNameDialog = new VideoNameDialog(VideoListActivity.this, new VideoNameDialog.RenameHandle() {
            @Override
            public void renameFinish(String info) {
                publishVideo(filename, info);
            }
        });
    }

    @Override
    public void onBindData() {
        getVideoList();
    }

    private void getVideoList() {
        WebAPIManager.getInstance(VideoListActivity.this).getVideoList("验证通过").subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse<List<Video>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse<List<Video>> listWebResponse) {
                        if (listWebResponse.getCode().equals(CODE_SUCCESS)) {
                            videoListAdapter = new VideoListAdapter(VideoListActivity.this, listWebResponse.getData(), new VideoListAdapter.VideoClickListener() {
                                @Override
                                public void VideoListItemClick(Video info) {
                                    Intent intent = new Intent(VideoListActivity.this, VideoDetailActivity.class);
                                    intent.putExtra("video", info);
                                    startActivity(intent);
                                }
                            });
                            rvVideo.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
                            rvVideo.addItemDecoration(new DividerItemDecoration(VideoListActivity.this, DividerItemDecoration.VERTICAL));
                            rvVideo.setAdapter(videoListAdapter);
                        } else {
                            MessageUtils.showLongToast(VideoListActivity.this, "获取视频列表失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(VideoListActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.backend, R.id.publish_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.publish_video:
                final CaptureConfiguration config = createCaptureConfiguration();
                Intent intent = new Intent(VideoListActivity.this, VideoCaptureActivity.class);
                intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
                startActivityForResult(intent, 101);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            filename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
            videoNameDialog.show();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            filename = null;
            MessageUtils.showLongToast(VideoListActivity.this, "取消录制上传");
        } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
            filename = null;
            MessageUtils.showLongToast(VideoListActivity.this, "视频录制失败");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void publishVideo(String videoPath, String videoName) {
        loadDialog(VideoListActivity.this, "视频上传中...");
        File video = new File(videoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), video);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", video.getName(), requestBody);
        WebAPIManager.getInstance(VideoListActivity.this).publishVideo(videoName, body)
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse webResponse) {
                        if (webResponse.getCode().equals(CODE_SUCCESS)) {
                            MessageUtils.showLongToast(VideoListActivity.this, "上传成功");
                            getVideoList();
                        } else {
                            MessageUtils.showLongToast(VideoListActivity.this, "上传失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(VideoListActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        loadDialogDismiss();
                    }
                });
    }

    private CaptureConfiguration createCaptureConfiguration() {
        PredefinedCaptureConfigurations.CaptureResolution resolution = PredefinedCaptureConfigurations.CaptureResolution.RES_720P;
        PredefinedCaptureConfigurations.CaptureQuality quality = PredefinedCaptureConfigurations.CaptureQuality.LOW;
        int fileDuration = CaptureConfiguration.NO_DURATION_LIMIT;
        int fileSize = CaptureConfiguration.NO_FILESIZE_LIMIT;
        CaptureConfiguration config = new CaptureConfiguration(resolution, quality, fileDuration, fileSize, true);
        return config;
    }

}
