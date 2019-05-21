package com.example.graduate.findingcooking.utils;

import android.content.Context;

import com.example.graduate.findingcooking.base.BaseSPUtil;

public class RecipeSP extends BaseSPUtil {
    public RecipeSP(Context context) {
        super(context, "recipe_sp");
    }

    public void putUserId(long userId) {
        putLong("userId", userId);
    }

    public Long getUserId() {
        return getLong("userId",1);
    }

    public void putNickName(String nickname) {
        putString("nickname", nickname);
    }

    public String getNickName() {
        return getString("nickname", null);
    }

    public void putAvatarURL(String url) {
        putString("url", url);
    }

    public String getAvatarURL() {
        return getString("url", null);
    }

    public void putUserType(String userType) {
        putString("userType", userType);
    }

    public String getUserType() {
        return getString("userType", null);
    }

    public void userIsLogin(boolean isLogining) {
        putBoolean("isLogin", isLogining);
    }

    public Boolean getUserLoginStatue() {
        return getBoolean("isLogin", false);
    }

}
