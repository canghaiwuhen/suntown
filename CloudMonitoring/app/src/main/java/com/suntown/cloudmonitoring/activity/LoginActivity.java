package com.suntown.cloudmonitoring.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.api.ApiConstant;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.LoginBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private InputMethodManager systemService;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    //登录
    public void btn_confirm_login(View view) {
        String userName = etUsername.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();
        loginJM(userName);
        if (userName.equals("") || passWord.equals("")) {
            Utils.showToast(LoginActivity.this, "账号或密码不能为空");
            return;
        }
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return;
        }
        dialog = new LoadingDialog(this);
        dialog.show();
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put(Constant.USER_NAME, userName);
        loginParams.put(Constant.PASS_WORD, passWord);
        Log.i(TAG,"userName:"+userName+",passWord:"+passWord);
        ApiClient.getInstance().mApiService.login(loginParams).compose(RxSchedulers.io_main()).subscribe(loginBean -> {
            int resultCode = loginBean.getResultCode();
            if (resultCode == 1) {
                Utils.showToast(this, "登录成功");
                LoginBean.UserBean user = loginBean.getUser();
                String userid = user.getUserid();
                String baseUrl = ApiConstant.BASE_URL;
                SPUtils.put(LoginActivity.this, Constant.SERVER_IP, baseUrl);
                SPUtils.put(LoginActivity.this, Constant.USER_ID, userid);
                SPUtils.put(LoginActivity.this, Constant.USER_NAME, userName);
                SPUtils.put(LoginActivity.this, Constant.PASS_WORD, passWord);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            } else {
                Utils.showToast(this, "登录失败，请重试!");
                dialog.dismiss();
            }
        }, throwable -> {
            dialog.dismiss();
            Log.i(TAG,"throwable:"+throwable);
            Utils.showToast(this, "连接服务器失败，请检查网络!");
        });
    }

    private void loginJM(String userName) {
        JMessageClient.login(userName, Constant.JMPSW, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String LoginDesc) {
                if (responseCode == 0) {
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    Log.i(TAG, "JM登录成功");
                } else {
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    Log.i(TAG, "JM登录失败");
                    registerJM(userName);
                }
            }
        });
    }

    private void registerJM(String userName) {
        JMessageClient.register(userName, Constant.JMPSW, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                if (responseCode == 0) {
                    Log.i(TAG,  ", JM注册失败 ");
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                } else {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "JM注册失败 ");
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
