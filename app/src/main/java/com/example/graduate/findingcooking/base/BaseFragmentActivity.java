package com.example.graduate.findingcooking.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public abstract class BaseFragmentActivity extends BaseActivity {

    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }


    protected void showFragment(int viewId, Fragment fragment) {
        showFragment(viewId, fragment, false, true);
    }

    protected void showFragment(int viewId, Fragment fragment, boolean isAddToBackStack) {
        showFragment(viewId, fragment, isAddToBackStack, true);
    }

    protected void showFragment(int viewId, Fragment fragment, boolean isAddToBackStack, boolean isAllowStateLoss) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(viewId);
        if (currentFragment != null && currentFragment.isAdded() && currentFragment != fragment) {
            transaction.remove(currentFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(viewId, fragment);
        }
        if (isAddToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        if (isAllowStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

}
