package com.example.graduate.findingcooking.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.activity.RecipePublishActivity;
import com.example.graduate.findingcooking.activity.RecipeSearchActivity;
import com.example.graduate.findingcooking.adapter.ClassAdapter;
import com.example.graduate.findingcooking.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


public class ClassFragment extends BaseFragment {

    @BindView(R.id.recipe_class_title)
    RelativeLayout recipeClassTitle;
    @BindView(R.id.tl_class)
    TabLayout tlClass;
    @BindView(R.id.vp_class)
    ViewPager vpClass;
    @BindView(R.id.search)
    ImageView search;
    List<Fragment> fragments;
    List<String> titles;
    ClassAdapter classAdapter;
    @BindView(R.id.addRecipe)
    ImageView addRecipe;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_class;
    }

    @Override
    public void onInit() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add("热菜");
        titles.add("凉菜");
        titles.add("干锅");
        titles.add("火锅");
        titles.add("清蒸");
        titles.add("甜品");
        titles.add("小吃");
        titles.add("家常");
        for (String title : titles) {
            Bundle args = new Bundle();
            args.putString("title", title);
            RecipeListFragment fragment = new RecipeListFragment();
            fragment.setArguments(args);
            fragments.add(fragment);
        }
        classAdapter = new ClassAdapter(getChildFragmentManager(), fragments, titles);
    }

    @Override
    public void onBindData() {
        vpClass.setAdapter(classAdapter);
        tlClass.setupWithViewPager(vpClass);
    }


    @OnClick({R.id.addRecipe, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addRecipe:
                skip(getActivity(), RecipePublishActivity.class);
                break;
            case R.id.search:
                skip(getActivity(), RecipeSearchActivity.class);
                break;
        }
    }
}
