package com.example.graduate.findingcooking.fragment;


import android.app.AlertDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.activity.AboutActivity;
import com.example.graduate.findingcooking.base.BaseFragment;
import com.example.graduate.findingcooking.dialog.PhotoSelectDialog;
import com.example.graduate.findingcooking.utils.DataCleanManager;
import com.example.graduate.findingcooking.utils.GlideX;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class PersonFragment extends BaseFragment {

    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.iv_remove_cache)
    ImageView ivRemoveCache;
    @BindView(R.id.right_arrow)
    ImageView rightArrow;
    @BindView(R.id.rl_remove_cache)
    RelativeLayout rlRemoveCache;
    @BindView(R.id.iv_share_friend)
    ImageView ivShareFriend;
    @BindView(R.id.rl_share_friend)
    RelativeLayout rlShareFriend;
    @BindView(R.id.iv_about)
    ImageView ivAbout;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    PhotoSelectDialog photoSelectDialog;
    RecipeSP recipeSP;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    @BindView(R.id.cacheText)
    TextView cacheText;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_preson;
    }

    @Override
    public void onInit() {
        recipeSP = new RecipeSP(getActivity());
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
    }

    @Override
    public void onBindData() {
        if (recipeSP != null) {
            GlideX.getInstance().loadImage(getActivity(),recipeSP.getAvatarURL(),userAvatar);
            userName.setText(recipeSP.getNickName());
        }
        try {
            String data = DataCleanManager.getTotalCacheSize(getActivity());
            cacheText.setText(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.user_avatar, R.id.rl_remove_cache, R.id.rl_share_friend, R.id.rl_about, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_avatar:
                showPhotoChoice();
                break;
            case R.id.rl_remove_cache:
                removeCache();
                break;
            case R.id.rl_share_friend:
                shareClass();
                break;
            case R.id.rl_about:
                skip(getActivity(), AboutActivity.class);
                break;
            case R.id.btn_logout:
                clearSP();
                getActivity().finish();
                break;
        }
    }

    private void showPhotoChoice() {
        photoSelectDialog = new PhotoSelectDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectDialog.dismiss();
                switch (v.getId()) {
                    case R.id.btn_take_photo:
                        PictureSelector.create(PersonFragment.this)
                                .openCamera(PictureMimeType.ofImage())
                                .enableCrop(true)
                                .compress(true)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.btn_pick_photo:
                        PictureSelector.create(PersonFragment.this)
                                .openGallery(PictureMimeType.ofImage())
                                .enableCrop(true)
                                .compress(true)
                                .selectionMode(PictureConfig.SINGLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    default:
                        break;
                }
            }
        });
        photoSelectDialog.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    File file = new File(images.get(0).getCompressPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    uploadUserAvatar(body);
                    break;
            }
        }
    }

    private void uploadUserAvatar(MultipartBody.Part body) {
        loadDialog(getActivity(), "更新头像数据中...");
        WebAPIManager.getInstance(getActivity()).uploadUserAvatar(recipeSP.getUserId(), body).subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WebResponse<String> s) {
                if (s.getCode().equals(CODE_SUCCESS)) {
                    recipeSP.putAvatarURL(s.getData());
                    GlideX.getInstance().loadImage(getActivity(),s.getData(),userAvatar);
                    MessageUtils.showShortToast(getActivity(), "更新成功");
                } else {
                    MessageUtils.showShortToast(getActivity(), "更新失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                MessageUtils.showShortToast(getActivity(), e.toString());
                Log.e("test",e.toString());
            }

            @Override
            public void onComplete() {
                loadDialogDismiss();
            }
        });
    }

    private void clearSP() {
        recipeSP.putAvatarURL("");
        recipeSP.putUserType("");
        recipeSP.putNickName("");
        recipeSP.putUserId(1);
        recipeSP.userIsLogin(false);
    }

    private void shareClass() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐");
        intent.putExtra(Intent.EXTRA_TEXT, "嗨，推荐一个超级好用的菜谱app给你,http://123.207.117.247:8080/food-0.0.1-SNAPSHOT/file/apk/app-debug.apk");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "推荐给好友"));
    }

    private void removeCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataCleanManager.clearAllCache(getActivity());
                cacheText.setText("0.00k");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setTitle("缓存清理确认");
        builder.setMessage("确认要清除缓存吗？");
        builder.show();
    }
}
