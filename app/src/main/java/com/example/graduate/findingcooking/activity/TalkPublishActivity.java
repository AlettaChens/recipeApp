package com.example.graduate.findingcooking.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.GridImageAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.dialog.PhotoSelectDialog;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.example.graduate.findingcooking.widget.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.util.ArrayList;
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

public class TalkPublishActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.publish_talk)
    TextView publishTalk;
    @BindView(R.id.rv_upload_pic)
    RecyclerView rvUploadPic;
    @BindView(R.id.et_talk)
    EditText etTalk;
    RecipeSP recipeSP;
    GridImageAdapter adapter;
    List<LocalMedia> selectList;
    PhotoSelectDialog photoSelectDialog;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_publish_talk;
    }

    @Override
    public void onInit() {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        recipeSP = new RecipeSP(TalkPublishActivity.this);
        selectList = new ArrayList<>();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rvUploadPic.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                showPhotoChoice();
            }
        });
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        rvUploadPic.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            PictureSelector.create(TalkPublishActivity.this).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    private void showPhotoChoice() {
        photoSelectDialog = new PhotoSelectDialog(TalkPublishActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectDialog.dismiss();
                switch (v.getId()) {
                    case R.id.btn_take_photo:
                        PictureSelector.create(TalkPublishActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .maxSelectNum(9)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .compress(true)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.btn_pick_photo:
                        PictureSelector.create(TalkPublishActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .compress(true)
                                .maxSelectNum(9)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    default:
                        break;
                }
            }
        });
        photoSelectDialog.showAtLocation(TalkPublishActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onBindData() {

    }

    @OnClick({R.id.backend, R.id.publish_talk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.publish_talk:
                publishTalk();
                break;
        }
    }

    private void publishTalk() {
        List<MultipartBody.Part> pictures = new ArrayList<>();
        for (LocalMedia picture : selectList) {
            File file = new File(picture.getCompressPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            pictures.add(body);
        }
        WebAPIManager.getInstance(TalkPublishActivity.this).publishTalk(etTalk.getText().toString(), recipeSP.getNickName(), recipeSP.getAvatarURL(), pictures)
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
                            MessageUtils.showLongToast(TalkPublishActivity.this, "发布成功");
                            finish();
                        } else {
                            MessageUtils.showLongToast(TalkPublishActivity.this, "发布失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(TalkPublishActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(images);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}

