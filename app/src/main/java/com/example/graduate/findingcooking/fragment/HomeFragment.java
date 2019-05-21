package com.example.graduate.findingcooking.fragment;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.activity.RecipeDetailActivity;
import com.example.graduate.findingcooking.activity.TalkListActivity;
import com.example.graduate.findingcooking.activity.TimerListActivity;
import com.example.graduate.findingcooking.activity.VideoListActivity;
import com.example.graduate.findingcooking.adapter.HomeAdapter;
import com.example.graduate.findingcooking.base.BaseFragment;
import com.example.graduate.findingcooking.bean.Recipe;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
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


public class HomeFragment extends BaseFragment {

    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    HomeAdapter homeAdapter;
    RecipeSP recipeSP;
    LifecycleProvider<Lifecycle.Event> lifecycleProvider;
    List<Recipe> recipeList;
    List<Integer> picList;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onInit() {
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
        if (picList == null) {
            picList = new ArrayList<>();
        }
        picList.add(R.mipmap.recipe01);
        picList.add(R.mipmap.recipe02);
        picList.add(R.mipmap.recipe03);
        picList.add(R.mipmap.recipe04);
        picList.add(R.mipmap.recipe05);
        picList.add(R.mipmap.recipe06);
        recipeSP = new RecipeSP(getActivity());
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);

    }

    @Override
    public void onBindData() {
        if (recipeSP.getUserId() != null && recipeList.size() == 0) {
            doGetRecommendRecipeList();
        }
    }

    private void doGetRecommendRecipeList() {
        loadDialog(getActivity(), "加载中...");
        WebAPIManager.getInstance(getActivity()).recommendRecipe(recipeSP.getUserId())
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
                            homeAdapter = new HomeAdapter(recipeList, getActivity(), picList, new Classify(), new Recommend());
                            rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvHome.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rvHome.setAdapter(homeAdapter);
                        } else {
                            MessageUtils.showLongToast(getActivity(), "数据加载失败，请检查网络");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(getActivity(), e.toString());
                    }

                    @Override
                    public void onComplete() {
                        loadDialogDismiss();
                    }
                });
    }

    public class Recommend implements HomeAdapter.RecommendClickListener {
        @Override
        public void RecommendListItemClick(Recipe info) {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipe", info);
            startActivity(intent);
        }
    }

    public class Classify implements HomeAdapter.ClassifyClickListener {
        @Override
        public void onCityClick() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri url = Uri.parse("https://search.jd.com/Search?keyword=%E7%BD%91%E4%B8%8A%E4%B9%B0%E7%94%9F%E9%B2%9C&enc=utf-8&cu=true&utm_source=baidu-search&utm_medium=cpc&utm_campaign=t_262767352_baidusearch&utm_term=36137881263_0_3581987bb3664b898b2424369632a74e");
            intent.setData(url);
            startActivity(intent);
        }

        @Override
        public void onAllClick() {
            skip(getActivity(), TalkListActivity.class);
        }

        @Override
        public void onActiveClick() {
            skip(getActivity(), TimerListActivity.class);
        }

        @Override
        public void onVideoClick() {
            skip(getActivity(), VideoListActivity.class);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        picList.clear();
        recipeList.clear();
    }
}
