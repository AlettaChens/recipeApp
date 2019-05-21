package com.example.graduate.findingcooking.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseFragmentActivity;
import com.example.graduate.findingcooking.fragment.RecipeInspectFragment;
import com.example.graduate.findingcooking.fragment.VideoInspectFragment;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class InspectActivity extends BaseFragmentActivity {

    @BindView(R.id.rb_recipe)
    RadioButton rbRecipe;
    @BindView(R.id.rb_video)
    RadioButton rbVideo;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_manage_inspect;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onBindData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragment(R.id.fl_inspect_view, new RecipeInspectFragment());
    }


    @OnCheckedChanged({R.id.rb_recipe, R.id.rb_video})
    public void onLoginSelectCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_recipe: {
                if (ischanged) {
                    showFragment(R.id.fl_inspect_view, new RecipeInspectFragment());
                }
                break;
            }
            case R.id.rb_video: {
                if (ischanged) {
                    showFragment(R.id.fl_inspect_view, new VideoInspectFragment());
                }
                break;
            }
        }
    }
}
