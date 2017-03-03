package com.suntown.smartscreen.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.suntown.smartscreen.MainActivity;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.String2MD5;
import com.suntown.smartscreen.utils.Utils;

import butterknife.BindView;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View{

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private String passWord;
    private String name;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
//        mPresenter.login("suntown", "0xb704456d16d69e1fb069a63fdcbe145f");
        btnConfirm.setOnClickListener(view -> {
            name = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String msg = TextUtils.isEmpty(name) ? "用户名不能为空!" : TextUtils.isEmpty(pass) ? "密码不能为空!" : "";
            if (!TextUtils.isEmpty(msg)){
//                Utils.showToast(LoginActivity.this,msg);
                showMsg(msg);
                return;
            }
            passWord = "0x"+ String2MD5.MD5(pass);
            Log.i(TAG,"passWord:"+ passWord +",name:"+ name);
            mPresenter.login(name, passWord);
        });
    }



    @Override
    public void loginSuccess(User user) {
        SPUtils.put(this, Constant.USER_NAME, name);
        SPUtils.put(this, Constant.PASS_WORD, passWord);
        SPUtils.put(this, Constant.USER_ID,user.USERID);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this,msg);
    }
}
