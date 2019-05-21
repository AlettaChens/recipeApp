package com.example.graduate.findingcooking.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.CommentListAdapter;
import com.example.graduate.findingcooking.adapter.DisplayPictureAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Comment;
import com.example.graduate.findingcooking.bean.Talk;
import com.example.graduate.findingcooking.dialog.CommentDialog;
import com.example.graduate.findingcooking.utils.GlideX;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.example.graduate.findingcooking.widget.FullyGridLayoutManager;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class TalkDetailActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.comment_publish)
    ImageView commentPublish;
    @BindView(R.id.ci_user_avatar)
    CircleImageView ciUserAvatar;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_talk_content)
    TextView tvTalkContent;
    @BindView(R.id.rv_talk_picture_list)
    RecyclerView rvTalkPictureList;
    @BindView(R.id.tv_title_comment)
    TextView tvTitleComment;
    @BindView(R.id.rv_talk_comment_list)
    RecyclerView rvTalkCommentList;
    CommentDialog commentDialog;
    Talk talk;
    RecipeSP recipeSP;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    CommentListAdapter commentListAdapter;
    DisplayPictureAdapter displayPictureAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_talk_detail;
    }

    @Override
    public void onInit() {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        recipeSP = new RecipeSP(TalkDetailActivity.this);
        talk = (Talk) getIntent().getSerializableExtra("talk");
    }

    @Override
    public void onBindData() {
        GlideX.getInstance().loadImage(TalkDetailActivity.this, talk.getFormu().getUserAvatar(), ciUserAvatar);
        tvUser.setText(talk.getFormu().getUserName());
        tvTime.setText(talk.getFormu().getDate());
        tvTalkContent.setText(talk.getFormu().getContent());
        getCommentList(talk.getFormu().getId());

        displayPictureAdapter = new DisplayPictureAdapter(TalkDetailActivity.this, talk.getImageList(), new DisplayPictureAdapter.DisplayClickListener() {
            @Override
            public void showDisplay(String url) {
                Intent intent = new Intent(TalkDetailActivity.this, PictureDisplayActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rvTalkPictureList.setLayoutManager(manager);
        rvTalkPictureList.setAdapter(displayPictureAdapter);
    }

    private void publishComment(String info) {
        WebAPIManager.getInstance(TalkDetailActivity.this).publishComment(info, recipeSP.getNickName(), talk.getFormu().getId())
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
                            MessageUtils.showLongToast(TalkDetailActivity.this, "发布成功");
                            getCommentList(talk.getFormu().getId());
                        } else {
                            MessageUtils.showLongToast(TalkDetailActivity.this, "发布失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(TalkDetailActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @SuppressLint("CheckResult")
    private void getCommentList(long forumId) {
        WebAPIManager.getInstance(TalkDetailActivity.this).getCommentList(forumId).subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WebResponse<List<Comment>>>() {
                    @Override
                    public void accept(WebResponse<List<Comment>> listWebResponse) throws Exception {
                        if (listWebResponse.getCode().equals(CODE_SUCCESS)) {
                            commentListAdapter = new CommentListAdapter(TalkDetailActivity.this, listWebResponse.getData());
                            rvTalkCommentList.setLayoutManager(new LinearLayoutManager(TalkDetailActivity.this));
                            rvTalkCommentList.addItemDecoration(new DividerItemDecoration(TalkDetailActivity.this, DividerItemDecoration.VERTICAL));
                            rvTalkCommentList.setAdapter(commentListAdapter);
                        } else {
                            MessageUtils.showLongToast(TalkDetailActivity.this, "获取失败");
                        }
                    }
                });
    }

    @OnClick({R.id.backend, R.id.comment_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.comment_publish:
                CommentDialog commentDialog = new CommentDialog(TalkDetailActivity.this, new CommentDialog.CommentFinishListener() {
                    @Override
                    public void commentFinish(String info) {
                        publishComment(info);
                    }
                });
                commentDialog.show();
                break;
        }
    }
}
