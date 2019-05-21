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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;

public class RecipeListFragment extends BaseFragment {
    @BindView(R.id.rv_recipe_list)
    RecyclerView rvRecipeList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_recipe_list;
    }

    private String recipeType;
    private LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    private RecipeListAdapter recipeListAdapter;
    private List<Recipe> recipeList;


    @Override
    public void onInit() {
        if (getArguments() != null) {
            recipeType = getArguments().getString("title");
        }
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
    }

    @Override
    public void onBindData() {
        doGetRecipeList();
    }

    private void doGetRecipeList() {
        WebAPIManager.getInstance(getActivity()).searchByType(recipeType)
                .subscribeOn(Schedulers.io())
                .compose(lifecycleProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebResponse<List<Recipe>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebResponse<List<Recipe>> listWebResponse) {
                        if (listWebResponse.getCode().equals(CODE_SUCCESS)&&isAdded()) {
                            recipeList = listWebResponse.getData();
                            recipeListAdapter = new RecipeListAdapter(getActivity(), recipeList, new RecipeItem());
                            rvRecipeList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvRecipeList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rvRecipeList.setAdapter(recipeListAdapter);
                        } else {
                            MessageUtils.showLongToast(getActivity(), "加载失败");
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
