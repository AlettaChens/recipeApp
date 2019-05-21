package com.example.graduate.findingcooking.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.base.BaseActivity;
import com.example.graduate.findingcooking.bean.User;
import com.example.graduate.findingcooking.utils.MessageUtils;
import com.example.graduate.findingcooking.utils.RecipeSP;
import com.example.graduate.findingcooking.web.WebAPIManager;
import com.example.graduate.findingcooking.web.WebResponse;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.graduate.findingcooking.web.Constant.CODE_SUCCESS;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.nick_name_ed_login)
    EditText nickNameEdLogin;
    @BindView(R.id.pwd_ed_login)
    EditText pwdEdLogin;
    @BindView(R.id.tourist_rb_login)
    RadioButton touristRbLogin;
    @BindView(R.id.manager_rb_login)
    RadioButton managerRbLogin;
    @BindView(R.id.isManage_radio_group_login)
    RadioGroup isManageRadioGroupLogin;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.go_to_register)
    TextView goToRegister;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    private String userType = "用户";
    private Context context = LoginActivity.this;
    private RecipeSP recipeSP;
    private String TAG = this.getClass().getSimpleName();
    private LifecycleProvider<Lifecycle.Event> lifecycleProvider;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void onInit() {
        recipeSP = new RecipeSP(context);
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
    }

    @Override
    public void onBindData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();//必须要在OnCreate()里面实现，否则会出现java.lang.NullPointerException: Attempt to invoke virtual method 'int android.app.Activity.checkSelfPermission(java.lang.String)' on a null object reference
    }

    @OnCheckedChanged({R.id.manager_rb_login, R.id.tourist_rb_login})
    public void onLoginSelectCheck(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.manager_rb_login: {
                if (ischanged) {
                    userType = "管理员";
                }
                break;
            }
            case R.id.tourist_rb_login: {
                if (ischanged) {
                    userType = "用户";
                }
                break;
            }
        }
    }

    @OnClick({R.id.login_btn, R.id.go_to_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login(nickNameEdLogin.getText().toString(), pwdEdLogin.getText().toString());
                break;
            case R.id.go_to_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void login(String nickname, String password) {
        loadDialog(context, "登录中...");
        WebAPIManager.getInstance(LoginActivity.this).login(nickname, password, userType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe(new Observer<WebResponse<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WebResponse<User> webResponse) {
                        if (webResponse.getCode().equals(CODE_SUCCESS)) {
                            MessageUtils.showLongToast(context, "登陆成功");
                            UpdateUserInfo(webResponse.getData());
                            if (userType.equals("用户")) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, InspectActivity.class));
                            }
                            finish();
                        } else {
                            MessageUtils.showLongToast(context, "登陆失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MessageUtils.showLongToast(context, e.toString());
                        Log.i(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        loadDialogDismiss();
                    }
                });
    }


    private void UpdateUserInfo(User user) {
        recipeSP.putUserId(user.getUserId());
        recipeSP.putNickName(user.getNickname());
        recipeSP.putAvatarURL(user.getAvatarURL());
        recipeSP.putUserType(user.getUserType());
        recipeSP.userIsLogin(true);
    }

    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(LoginActivity.this);
        rxPermission.requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {

                } else if (permission.shouldShowRequestPermissionRationale) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("您拒绝需要的权限，这可能导致应用不可正常使用，请重新打开应用进行设置")
                            .setCancelable(false)
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }).create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("您拒绝需要的权限，这可能导致应用不可正常使用，请前往设置打开该权限")
                            .setCancelable(false)
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }).create().show();
                }
            }
        });
    }
}
