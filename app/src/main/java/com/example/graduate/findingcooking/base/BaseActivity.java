package com.example.graduate.findingcooking.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.graduate.findingcooking.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseActivity extends AppCompatActivity {

	protected final String TAG = getClass().getSimpleName();
	private SweetAlertDialog loadDialog;
	private Unbinder mUnbinder;

	private boolean isAutoClearFocus;

	public BaseActivity() {
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(getContentViewId());
		mUnbinder=ButterKnife.bind(this);
		onInit();
		onBindData();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 设置布局文件任务
	 *
	 * @return
	 */
	protected abstract int getContentViewId();

	/**
	 * 初始化配置控件
	 */
	public abstract void onInit();

	/**
	 * 控件的数据绑定以及后续的处理任务
	 */
	public abstract void onBindData();

	public void skip(Context context, Class clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	public void bundleSkip(Context context, Class clazz, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}

	public void bundleSkipForResult(Context context, Class clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}
	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mUnbinder.unbind();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();

	}

	public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
		super.onWindowAttributesChanged(params);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

	}

	protected boolean isAutoClearFocus() {
		return isAutoClearFocus;
	}

	public void setAutoClearFocus(boolean autoClearFocus) {
		isAutoClearFocus = autoClearFocus;
	}

	/**
	 * 加载中
	 * @param context
	 * @param title
	 */
	public void loadDialog(Context context, String title) {
		loadDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
		loadDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.BaseColor));
		loadDialog.setTitleText(title);
		loadDialog.setCancelable(true);
		loadDialog.show();
	}

	public void loadDialogDismiss() {
		if (loadDialog.isShowing()) {
			loadDialog.dismiss();
		}

	}

	/**
	 *  提示
	 * @param context
	 * @param title
	 * @param content
	 * @param type
	 */
	public void showMsgDialog(Context context, String title, String content, int type) {
		SweetAlertDialog msgDialog = new SweetAlertDialog(context, type);
		msgDialog.setTitleText(title);
		msgDialog.setContentText(content);
		msgDialog.show();
	}

	/**
	 * 网络超时 错误提示
	 * @param context
	 */
	public void errorDialog(Context context) {
		SweetAlertDialog msgDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
		msgDialog.setTitleText("提示");
		msgDialog.getProgressHelper().setBarColor(R.color.BaseColor);
		msgDialog.setContentText("网络超时，请重试");
		msgDialog.setConfirmText(context.getResources().getString(R.string.confirm));
		msgDialog.show();
		msgDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.dismiss();
			}
		});
	}

	/**
	 * 警告
	 * @param context
	 * @param title
	 * @param content
	 * @param confirmText
	 * @param listener
	 */
	public void showWarmDialog(Context context, String title, String content, String confirmText, SweetAlertDialog.OnSweetClickListener listener) {
		SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
		dialog.setTitleText(title);
		dialog.setContentText(content);
		dialog.setConfirmText(confirmText);
		dialog.setCancelText("取消");
		dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				sweetAlertDialog.dismiss();
			}
		});
		dialog.setConfirmClickListener(listener);
		dialog.show();
	}

}
