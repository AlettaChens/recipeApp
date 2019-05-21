package com.example.graduate.findingcooking.base;

import android.app.Application;
import android.content.res.Configuration;


public abstract class BaseApplication extends Application {

	protected final String TAG = getClass().getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
}
