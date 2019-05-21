package com.example.graduate.findingcooking.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.NotificationEvent;
import com.example.graduate.findingcooking.bean.Ingredient;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.dialog.NotificationDialog;
import com.example.graduate.findingcooking.utils.CalendarEventUtils;
import com.example.graduate.findingcooking.utils.GlideX;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;


import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class RecipeDetailActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.inspectRecipe)
    CheckBox inspectRecipe;
    @BindView(R.id.settingTimer)
    ImageView settingTimer;
    @BindView(R.id.recipe_pic)
    ImageView recipePic;
    @BindView(R.id.recipeName)
    TextView recipeName;
    @BindView(R.id.recipeType)
    TextView recipeType;
    @BindView(R.id.recipeIngredient)
    TextView recipeIngredient;
    @BindView(R.id.cv_recipe_picture)
    CardView cvRecipePicture;
    private String RecipeState;
    private Recipe recipe;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    RecipeSP recipeSP;
    String convertedListStr = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recipe_detail;
    }

    @Override
    public void onInit() {
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
        recipeSP = new RecipeSP(RecipeDetailActivity.this);
        if (recipeSP.getUserType().equals("用户")) {
            settingTimer.setVisibility(View.VISIBLE);
            inspectRecipe.setVisibility(View.GONE);
        } else {
            settingTimer.setVisibility(View.GONE);
            inspectRecipe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindData() {
        GlideX.getInstance().loadImage(RecipeDetailActivity.this, recipe.getFood().getFoodURL(), recipePic);
        recipeName.setText("菜品名称:" + recipe.getFood().getFoodName());
        recipeType.setText(recipe.getFood().getFoodType());
        StringBuilder sb = new StringBuilder();
        if (null != recipe.getIngredients() && recipe.getIngredients().size() > 0) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                sb.append(ingredient.getIngredient());
                sb.append("、");
            }
            convertedListStr = sb.toString().substring(0, sb.toString().length() - 1);
        }
        recipeIngredient.setText("原材料:" + convertedListStr);

        historyPush();
    }

    @SuppressLint("CheckResult")
    private void historyPush() {
        WebAPIManager.getInstance(RecipeDetailActivity.this).historyPush(recipe.getFood().getId(), recipeSP.getUserId())
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WebResponse>() {
                    @Override
                    public void accept(WebResponse webResponse) throws Exception {
                        if (webResponse.getCode().equals(CODE_SUCCESS)) {

                        } else {
                            MessageUtils.showLongToast(RecipeDetailActivity.this, "浏览记录失败");
                        }
                    }
                });
    }

    @OnCheckedChanged({R.id.inspectRecipe})
    public void onInspectStateCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.inspectRecipe: {
                if (ischanged) {
                    RecipeState = "验证通过";
                } else {
                    RecipeState = "待验证";
                }
                updateRecipeState(RecipeState, recipe.getFood().getId());
                break;
            }
        }
    }

    private void updateRecipeState(String recipeState, long foodId) {
        WebAPIManager.getInstance(RecipeDetailActivity.this).updateRecipeState(recipeState, foodId)
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
                            MessageUtils.showLongToast(RecipeDetailActivity.this, "更新成功");
                        } else {
                            MessageUtils.showLongToast(RecipeDetailActivity.this, "更新失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(RecipeDetailActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.backend, R.id.settingTimer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.settingTimer:
                setTimer();
                break;
        }
    }

    public void setTimer() {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setContent("采购" + convertedListStr);
        NotificationDialog notificationDialog = new NotificationDialog(this, notificationEvent);
        notificationDialog.setOnUpdateListener(new NotificationDialog.OnUpdateListener() {
            @Override
            public void onUpdate(boolean isNew, NotificationEvent notificationEvent) {
                if (isNew) {
                    CalendarEventUtils.insertEvent(RecipeDetailActivity.this, notificationEvent);
                    MessageUtils.showLongToast(RecipeDetailActivity.this, "成功设置提醒");
                }
            }
        });
        notificationDialog.show();
    }
}
