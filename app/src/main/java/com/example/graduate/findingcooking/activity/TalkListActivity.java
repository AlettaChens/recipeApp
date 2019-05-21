package com.example.graduate.findingcooking.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.TalkListAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Talk;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class TalkListActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.publish_talk)
    ImageView publishTalk;
    @BindView(R.id.rv_talk)
    RecyclerView rvTalk;
    TalkListAdapter talkListAdapter;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    RecipeSP recipeSP;

    @Override
    protected int getContentViewId() {
        return R.layout.activty_talk_list;
    }

    @Override
    public void onInit() {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        recipeSP = new RecipeSP(TalkListActivity.this);
    }

    @Override
    public void onBindData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTalkList();
    }

    private void getTalkList() {
        WebAPIManager.getInstance(TalkListActivity.this).getForumList(recipeSP.getUserId()).subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse<List<Talk>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse<List<Talk>> listWebResponse) {
                        if(listWebResponse.getCode().equals(CODE_SUCCESS)){
                            talkListAdapter = new TalkListAdapter(TalkListActivity.this, listWebResponse.getData(), new TalkClickListener());
                            rvTalk.setLayoutManager(new LinearLayoutManager(TalkListActivity.this));
                            rvTalk.addItemDecoration(new DividerItemDecoration(TalkListActivity.this, DividerItemDecoration.VERTICAL));
                            rvTalk.setAdapter(talkListAdapter);
                        }else {
                            MessageUtils.showLongToast(TalkListActivity.this,"请求失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.backend, R.id.publish_talk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.publish_talk:
                skip(TalkListActivity.this, TalkPublishActivity.class);
                break;
        }
    }

    public class TalkClickListener implements TalkListAdapter.TalkClickListener {
        @Override
        public void TalkListItemClick(Talk talk) {
            Intent intent = new Intent(TalkListActivity.this, TalkDetailActivity.class);
            intent.putExtra("talk", talk);
            startActivity(intent);
        }

        @Override
        public void onStarChange(long forumId, String changeType) {
            updateStar(forumId, changeType);
        }
    }

    private void updateStar(long forumId, String changeType) {
        WebAPIManager.getInstance(TalkListActivity.this).updateStar(changeType, forumId, recipeSP.getUserId())
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
                        } else {
                            MessageUtils.showLongToast(TalkListActivity.this, "更新失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(TalkListActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
