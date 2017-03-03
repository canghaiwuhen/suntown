package com.suntowns.labeltest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suntowns.labeltest.utils.Constant;
import com.suntowns.labeltest.utils.PhoneNumUtils;
import com.suntowns.labeltest.utils.SPUtils;
import com.suntowns.labeltest.utils.String2MD5;
import com.suntowns.labeltest.utils.Utils;
import com.suntowns.labeltest.utils.Xml2Json;


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
public class LoginActivity extends Activity {
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.et_login_psw)
    EditText etLoginPsw;
    @BindView(R.id.tv_register)
    TextView tvRegister;
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
    }


    //提交数据
    public void confirm_login(View view) {
        loginuserName = etLoginUsername.getText().toString();
        String loginPsw = etLoginPsw.getText().toString();
        boolean sequence = PhoneNumUtils.isPhoneNumberValid(loginuserName);
        pwd = String2MD5.MD5(loginPsw);
        Log.i("pwd", pwd +"");
        if(!sequence){
            Utils.showToast(LoginActivity.this, "此号码不存在，请重新输入");
        }
        if (loginuserName.equals("")|| loginPsw.equals("")){
            Utils.showToast(LoginActivity.this,"账号或密码不能为空");
            return;
        }
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return ;
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
                            Utils.showToast(LoginActivity.this,"登陆失败，请检查网络");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream is = response.body().byteStream();
                            String json = null;
                            try {
                                json = new Xml2Json(is).Pull2Xml();
                                Log.i("loginActivity",json+"");
                                if (json==null){
                                    Utils.showToast(LoginActivity.this,"登陆失败,请重试");
                                    return;
                                }
                                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                if("".equals(loginBean)){
                                    Utils.showToast(LoginActivity.this,"登陆失败,请重试");
                                    return;
                                }
                                Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                                String result = loginBean.getRESULT();
                                if (result.equals("0")){
                                    String sax = "";
                                    LoginBean.USERINFOBean userinfo = loginBean.getUSERINFO();
                                    String memid = userinfo.getMEMID();
                                    String sex = userinfo.getSEX();
                                    if (sex.equals("1")) {
                                        sax="女";
                                    }else if (sex.equals("0")){
                                        sax="男";
                                    }
                                    String logintoken = userinfo.getLOGINTOKEN();
                                    Log.i("LoginActivity",memid);
                                    SPUtils.put(LoginActivity.this,Constant.SEX,sax);
                                    SPUtils.put(LoginActivity.this,Constant.NICKNAME,userinfo.getNICKNAME());
                                    SPUtils.put(LoginActivity.this,Constant.MEMID,memid);
                                    SPUtils.put(LoginActivity.this,Constant.LOGIN_USER_NAME,loginuserName);
                                    SPUtils.put(LoginActivity.this,Constant.LOGIN_PWD,pwd);
                                    SPUtils.put(LoginActivity.this,Constant.LOGIN_TOKEN,logintoken);
                                    if (!"".equals(nickName)) {
                                        SPUtils.put(LoginActivity.this, Constant.NICKNAME, nickName);
                                    }
//                                    Utils.showToast(LoginActivity.this,"登陆成功");
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }else {
                                    Utils.showToast(LoginActivity.this,"登陆失败，请确认帐号密码");
                                    runOnUiThread(()-> {
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

    @OnClick(R.id.tv_register)
    public void onClick() {
        startActivityForResult(new Intent(this,RegisterActivity.class),250);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==250&&resultCode==200&&data!=null){
            String userName = data.getStringExtra(Constant.USER_NAME);
            String passWord = data.getStringExtra(Constant.PASSWORD);
            nickName = data.getStringExtra(Constant.NICKNAME);
            etLoginUsername.setText(userName);
            etLoginPsw.setText(passWord);
        }
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
}
