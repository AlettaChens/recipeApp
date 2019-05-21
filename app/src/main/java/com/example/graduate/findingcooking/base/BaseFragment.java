package com.example.graduate.findingcooking.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.example.graduate.findingcooking.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseFragment extends android.support.v4.app.Fragment {
	protected final String TAG = getClass().getSimpleName();
	private SweetAlertDialog loadDialog;
	private Unbinder mUnbinder;

	private View view;
	private boolean isBinding = false;

	public BaseFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onAttachFragment(Fragment childFragment) {
		super.onAttachFragment(childFragment);

	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(getContentViewId(), container, false);
		mUnbinder=ButterKnife.bind(this, view);
		isBinding = true;
		return view;
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
	public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(context, attrs, savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (isBinding) {
			onInit();
			onBindData();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mUnbinder.unbind();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}


	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}


	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}


	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

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
