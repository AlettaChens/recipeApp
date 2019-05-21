package com.example.graduate.findingcooking.fragment;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.activity.VideoDetailActivity;
import com.example.graduate.findingcooking.activity.VideoListActivity;
import com.example.graduate.findingcooking.adapter.VideoListAdapter;
import com.example.graduate.findingcooking.base.BaseFragment;
import com.example.graduate.findingcooking.bean.Video;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class VideoInspectFragment extends BaseFragment {
    @BindView(R.id.rv_video_inspect)
    RecyclerView rvVideoInspect;
    VideoListAdapter videoListAdapter;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_video_inspect;
    }

    @Override
    public void onInit() {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
    }

    @Override
    public void onBindData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getInspectVideo();
    }

    private void getInspectVideo() {
        WebAPIManager.getInstance(getActivity()).getVideoList("待验证").subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse<List<Video>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse<List<Video>> listWebResponse) {
                        if (listWebResponse.getCode().equals(CODE_SUCCESS)) {
                            videoListAdapter = new VideoListAdapter(getActivity(), listWebResponse.getData(), new VideoListAdapter.VideoClickListener() {
                                @Override
                                public void VideoListItemClick(Video info) {
                                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                                    intent.putExtra("video", info);
                                    startActivity(intent);
                                }
                            });
                            videoListAdapter.setVideoStateListener(new VideoStateChangeListener());
                            rvVideoInspect.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvVideoInspect.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rvVideoInspect.setAdapter(videoListAdapter);
                        } else {
                            MessageUtils.showLongToast(getActivity(), "获取视频列表失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(getActivity(), e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public class VideoStateChangeListener implements VideoListAdapter.VideoStateListener {
        @Override
        public void onVideoStateChange(String state, long videoId) {
            WebAPIManager.getInstance(getActivity()).updateVideoState(state, videoId).subscribeOn(Schedulers.io())
                    .compose(lifecycleProvider.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<WebResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(WebResponse webResponse) {
                            if (webResponse.getCode().equals(CODE_SUCCESS)) {
                                MessageUtils.showLongToast(getActivity(), "更新成功");
                                getInspectVideo();
                            } else {
                                MessageUtils.showLongToast(getActivity(), "更新失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            MessageUtils.showLongToast(getActivity(), e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
