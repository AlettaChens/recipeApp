package com.example.graduate.findingcooking.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.adapter.RecipeListAdapter;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class RecipeSearchActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rv_search_list)
    RecyclerView rvSearchList;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    RecipeListAdapter recipeListAdapter;
    List<Recipe> recipeList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recipe_search;
    }

    @Override
    public void onInit() {
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);

    }

    @Override
    public void onBindData() {

    }

    @OnClick({R.id.backend, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.tv_search:
                if (!etSearchContent.getText().toString().equals("")) {
                    searchRecipe();
                }
                break;
        }
    }

    private void searchRecipe() {
        loadDialog(RecipeSearchActivity.this, "搜索菜谱中...");
        WebAPIManager.getInstance(RecipeSearchActivity.this).searchByName(etSearchContent.getText().toString())
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse<List<Recipe>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse<List<Recipe>> listWebResponse) {
                        if (listWebResponse.getCode().equals(CODE_SUCCESS)) {
                            recipeList = listWebResponse.getData();
                            recipeListAdapter = new RecipeListAdapter(RecipeSearchActivity.this, recipeList, new RecipeListAdapter.RecipeClickListener() {
                                @Override
                                public void RecipeListItemClick(Recipe info) {
                                    Intent intent = new Intent(RecipeSearchActivity.this, RecipeDetailActivity.class);
                                    intent.putExtra("recipe", info);
                                    startActivity(intent);
                                }
                            });
                            rvSearchList.setLayoutManager(new LinearLayoutManager(RecipeSearchActivity.this));
                            rvSearchList.addItemDecoration(new DividerItemDecoration(RecipeSearchActivity.this, DividerItemDecoration.VERTICAL));
                            rvSearchList.setAdapter(recipeListAdapter);
                        } else {
                            MessageUtils.showLongToast(RecipeSearchActivity.this, "没有获取到任何与" + etSearchContent.getText().toString() + "相关的内容");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(RecipeSearchActivity.this, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        loadDialogDismiss();
                    }
                });


    }
}
