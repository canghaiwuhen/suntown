package com.suntown.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.PhoneNumUtils;
import com.suntown.utils.SPUtils;
import com.suntown.utils.String2MD5;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    private OkHttpClient client;
    private String loginuserName;
    private String nickName;
    private String pwd;
    private InputMethodManager systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        client = new OkHttpClient();
        ButterKnife.bind(this);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getVersionName();
    }

    private void getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
//            int version = info.versionCode;
            Log.i(TAG,"version:"+version);
            tvVersion.setText("v"+version);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //提交数据
    public void confirm_login(View view) {
        loginuserName = etLoginUsername.getText().toString();
        String loginPsw = etLoginPsw.getText().toString();
        boolean sequence = PhoneNumUtils.isPhoneNumberValid(loginuserName);
        pwd = String2MD5.MD5(loginPsw);
        Log.i("pwd", pwd + "");
        if (!sequence) {
            Utils.showToast(LoginActivity.this, "此号码不存在，请重新输入");
        }
        if (loginuserName.equals("") || loginPsw.equals("")) {
            Utils.showToast(LoginActivity.this, "账号或密码不能为空");
            return;
        }
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return;
        }
        RequestBody formBody = new FormBody.Builder()
                .add("type", "0")
                .add("lgn", loginuserName)
                .add("pwd", pwd)
                .build();
        //e10adc3949ba59abbe56e057f20f883e
        final Request request = new Request.Builder()
                .url(Constant.format("login"))
                .post(formBody)
                .build();
        try {
            new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(LoginActivity.this, "登陆失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json = "";
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            Log.i("loginActivity", json + "");
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if (null==loginBean) {
                                Utils.showToast(LoginActivity.this, "网络异常");
                                return;
                            }
                            String result = loginBean.getRESULT();
                            if ("" == json) {
                                Utils.showToast(LoginActivity.this, "该用户不存在");
                                return;
                            }
                            if (result.equals("1")) {
                                Utils.showToast(LoginActivity.this, "登录失败");
                                return;
                            }
                            Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                            if (result.equals("0")) {
                                String sax = "";
                                LoginBean.USERINFOBean userinfo = loginBean.getUSERINFO();
                                String memid = userinfo.getMEMID();
                                String sex = userinfo.getSEX();
                                if (sex.equals("1")) {
                                    sax = "女";
                                } else if (sex.equals("0")) {
                                    sax = "男";
                                }
                                String logintoken = userinfo.getLOGINTOKEN();
                                Log.i("LoginActivity", memid);

                                SPUtils.put(LoginActivity.this, Constant.SEX, sax);
                                SPUtils.put(LoginActivity.this, Constant.NICKNAME, userinfo.getNICKNAME());
                                SPUtils.put(LoginActivity.this, Constant.MEMID, memid);
                                SPUtils.put(LoginActivity.this, Constant.LOGIN_USER_NAME, loginuserName);
                                SPUtils.put(LoginActivity.this, Constant.LOGIN_PWD, pwd);
                                SPUtils.put(LoginActivity.this, Constant.LOGIN_TOKEN, logintoken);
                                String avatar = userinfo.getAVATAR();
                                if (!avatar.startsWith("http:")) {
                                    avatar = "http://" + avatar;
                                }
                                SPUtils.put(LoginActivity.this, Constant.AVATAR, "".equals(avatar) ? "" : avatar);

                                if (!"".equals(nickName)) {
                                    SPUtils.put(LoginActivity.this, Constant.NICKNAME, nickName);
                                }
//                                    Utils.showToast(LoginActivity.this,"登陆成功");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Utils.showToast(LoginActivity.this, "登陆失败，请确认帐号密码");
                                runOnUiThread(() -> {
                                    etLoginPsw.setText("");
                                });
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.tv_register,R.id.tv_loser_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 250);
                break;
            case R.id.tv_loser_psw:
                startActivity(new Intent(this,LoserPswActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 250 && resultCode == 200 && data != null) {
            String userName = data.getStringExtra(Constant.USER_NAME);
            String passWord = data.getStringExtra(Constant.PASSWORD);
            nickName = data.getStringExtra(Constant.NICKNAME);
            etLoginUsername.setText(userName);
            etLoginPsw.setText(passWord);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
