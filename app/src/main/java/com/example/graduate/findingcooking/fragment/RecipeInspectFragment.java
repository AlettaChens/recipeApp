package com.example.graduate.findingcooking.fragment;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.activity.RecipeDetailActivity;
import com.example.graduate.findingcooking.adapter.RecipeListAdapter;
import com.example.graduate.findingcooking.base.BaseFragment;
import com.example.graduate.findingcooking.bean.Recipe;
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

public class RecipeInspectFragment extends BaseFragment {

    @BindView(R.id.rv_inspect_recipe)
    RecyclerView rvInspectRecipe;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    RecipeListAdapter recipeListAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_recipe_inspect;
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
        getInspectRecipeList();
    }

    private void getInspectRecipeList() {
        WebAPIManager.getInstance(getActivity()).getRecipeList("待验证")
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
                            recipeListAdapter = new RecipeListAdapter(getActivity(), listWebResponse.getData(), new RecipeItem());
                            rvInspectRecipe.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvInspectRecipe.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rvInspectRecipe.setAdapter(recipeListAdapter);
                        } else {
                            MessageUtils.showLongToast(getActivity(), "获取失败");
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

    public class RecipeItem implements RecipeListAdapter.RecipeClickListener {

        @Override
        public void RecipeListItemClick(Recipe info) {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipe", info);
            startActivity(intent);
        }
    }
}
