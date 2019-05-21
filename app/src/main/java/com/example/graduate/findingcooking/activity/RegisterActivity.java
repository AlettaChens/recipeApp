package com.example.graduate.findingcooking.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;


public class RegisterActivity extends BaseActivity {
	@BindView(R.id.nick_name_ed_reg)
	EditText nickNameEdReg;
	@BindView(R.id.pwd_ed_reg)
	EditText pwdEdReg;
	@BindView(R.id.register_btn)
	Button registerBtn;
	@BindView(R.id.backend)
	ImageView backend;
	private Context context = RegisterActivity.this;
	private LifecycleProvider<Lifecycle.Event> lifecycleProvider;


	@Override
	protected int getContentViewId() {
		return R.layout.activity_register;
	}

	@Override
	public void onInit() {
		lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
	}

	@Override
	public void onBindData() {

	}

	@OnClick(R.id.register_btn)
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.register_btn: {
				register();
				break;
			}
		}
	}

	private void register() {
		loadDialog(context, "注册中...");
		WebAPIManager.getInstance(context).register(nickNameEdReg.getText().toString(), pwdEdReg.getText().toString(), "用户").subscribeOn(Schedulers.io())
				.compose(lifecycleProvider.bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WebResponse>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onNext(WebResponse webResponse) {
				if (webResponse.getCode().equals(CODE_SUCCESS) && !webResponse.getMessage().equals("注册失败或者用户已存在")) {
					MessageUtils.showLongToast(context, "注册成功");
				} else {
					MessageUtils.showLongToast(context, "注册失败或者用户已存在");
				}
			}

			@Override
			public void onError(Throwable e) {
				MessageUtils.showLongToast(context, e.toString());
			}

			@Override
			public void onComplete() {
				loadDialogDismiss();
			}
		});
	}


	@OnClick(R.id.backend)
	public void onClick() {
		onBackPressed();
	}
}
