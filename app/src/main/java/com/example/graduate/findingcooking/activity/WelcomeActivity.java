package com.example.graduate.findingcooking.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.utils.CalendarEventUtils;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class WelcomeActivity extends BaseActivity {
    RecipeSP recipeSP;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onInit() {
        recipeSP = new RecipeSP(WelcomeActivity.this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindData() {
        Observable.timer(3000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (recipeSP.getUserLoginStatue()) {
                    if (recipeSP.getUserType().equals("用户")) {
                        skip(WelcomeActivity.this, MainActivity.class);
                    } else {
                        skip(WelcomeActivity.this, LoginActivity.class);
                    }
                } else {
                    skip(WelcomeActivity.this, LoginActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
