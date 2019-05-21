package com.example.graduate.findingcooking.utils;

import android.content.Context;
import android.widget.Toast;

public class MessageUtils {
	public static void showShortToast(Context context, String pMsg) {
		if (context != null && pMsg != null) Toast.makeText(context.getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String pMsg) {
		if (context != null && pMsg != null) Toast.makeText(context.getApplicationContext(), pMsg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, int strRes) {
		if (context != null) Toast.makeText(context.getApplicationContext(), strRes, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, int strRes) {
		if (context != null) Toast.makeText(context.getApplicationContext(), strRes, Toast.LENGTH_LONG).show();
	}
}
