package com.example.graduate.findingcooking.activity;

import android.view.View;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.utils.GlideX;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureDisplayActivity extends BaseActivity {
    @BindView(R.id.backend)
    ImageView backend;
    @BindView(R.id.picture_display)
    ImageView pictureDisplay;
    private String url;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_picture_display;
    }

    @Override
    public void onInit() {
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void onBindData() {
        GlideX.getInstance().loadImage(PictureDisplayActivity.this, url, pictureDisplay);
    }


    @OnClick({R.id.backend, R.id.picture_display})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backend:
                onBackPressed();
                break;
            case R.id.picture_display:
                finish();
                break;
        }
    }
}
