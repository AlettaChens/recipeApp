package com.example.graduate.findingcooking.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;


public abstract class BaseSPUtil {
	private String spName;
	private Context context;
	private SharedPreferences sp;

	public BaseSPUtil( Context context,String spName) {
		this.spName = spName;
		this.context = context;
		sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

	protected void putString(@NonNull String key, @NonNull String value) {
		sp.edit().putString(key, value).apply();
	}

	protected void putFloat(@NonNull String key, float value) {
		sp.edit().putFloat(key, value).apply();
	}

	protected void putBoolean(@NonNull String key, boolean value) {
		sp.edit().putBoolean(key, value).apply();
	}

	protected void putInt(@NonNull String key, int value) {
		sp.edit().putInt(key, value).apply();
	}

	protected void putLong(@NonNull String key, long value) {
		sp.edit().putLong(key, value).apply();
	}

	protected String getString(@NonNull String key, @NonNull String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	protected int getInt(@NonNull String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	protected long getLong(@NonNull String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	protected float getFloat(@NonNull String key, int defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	protected boolean getBoolean(@NonNull String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}
}
