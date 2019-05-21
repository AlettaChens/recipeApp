package com.example.graduate.findingcooking.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.dialog.PhotoSelectDialog;
import com.example.graduate.findingcooking.utils.GlideX;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
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

public class RecipePublishActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.addRecipeUrl)
    ImageView addRecipeUrl;
    @BindView(R.id.cv_recipe_picture)
    CardView cvRecipePicture;
    @BindView(R.id.recipeName)
    EditText recipeName;
    @BindView(R.id.recipeType)
    Spinner recipeType;
    @BindView(R.id.cv_recipe_baseInfo)
    CardView cvRecipeBaseInfo;
    @BindView(R.id.et_ingredient)
    EditText etIngredient;
    @BindView(R.id.recipeAddIngredient)
    Button recipeAddIngredient;
    @BindView(R.id.recipeIngredientText)
    TextView recipeIngredientText;
    @BindView(R.id.cv_recipe_ingredients)
    CardView cvRecipeIngredients;
    @BindView(R.id.publish_recipe)
    TextView publishRecipe;
    private PhotoSelectDialog photoSelectDialog;
    private File recipeFile;
    private List<String> ingredients;
    private LifecycleProvider<Lifecycle.Event> lifecycleProvider;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_publish_recipe;
    }

    @Override
    public void onInit() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recipe_type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeType.setAdapter(adapter);
        recipeType.setPrompt("菜品种类");
        ingredients.clear();
    }

    @Override
    public void onBindData() {

    }

    private void showPhotoChoice() {
        photoSelectDialog = new PhotoSelectDialog(RecipePublishActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelectDialog.dismiss();
                switch (v.getId()) {
                    case R.id.btn_take_photo:
                        PictureSelector.create(RecipePublishActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .enableCrop(true)
                                .compress(true)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.btn_pick_photo:
                        PictureSelector.create(RecipePublishActivity.this)
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
        photoSelectDialog.showAtLocation(RecipePublishActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.backend, R.id.addRecipeUrl, R.id.recipeAddIngredient, R.id.publish_recipe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.addRecipeUrl:
                showPhotoChoice();
                break;
            case R.id.recipeAddIngredient:
                recipeIngredientText.append(ingredients.size() > 0 ? "、" + etIngredient.getText().toString() : "" + etIngredient.getText().toString());
                ingredients.add(etIngredient.getText().toString());
                etIngredient.setText("");
                break;
            case R.id.publish_recipe:
                publishRecipe();
                break;
        }
    }

    private void publishRecipe() {
        loadDialog(RecipePublishActivity.this, "菜谱上传中...");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), recipeFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", recipeFile.getName(), requestBody);
        WebAPIManager.getInstance(RecipePublishActivity.this).publishRecipe(body, ingredients, recipeName.getText().toString(), recipeType.getSelectedItem().toString())
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void onNext(WebResponse webResponse) {
                        if (webResponse.getCode().equals(CODE_SUCCESS)) {
                            MessageUtils.showLongToast(RecipePublishActivity.this, "上传成功");
                        } else {
                            MessageUtils.showLongToast(RecipePublishActivity.this, "上传失败");
                        }
                        ingredients.clear();
                        recipeIngredientText.setText("");
                        recipeName.setText("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(RecipePublishActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        loadDialogDismiss();
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    recipeFile = new File(images.get(0).getCompressPath());
                    GlideX.getInstance().loadImage(RecipePublishActivity.this, recipeFile, addRecipeUrl);
                    break;
            }
        }
    }
}
