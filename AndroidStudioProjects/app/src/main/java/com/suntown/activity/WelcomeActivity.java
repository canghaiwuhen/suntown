package com.suntown.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";
    private ImageView backGround;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        client = new OkHttpClient();
        backGround = (ImageView) findViewById(R.id.iv_background);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        playAnimation();
        SPUtils.put(this,Constant.SERIAL_NUM,1);

        JMessageClient.init(getApplicationContext());
        String registrationID = JPushInterface.getRegistrationID(this);
        SPUtils.put(this, Constant.REGISTRATION_ID,registrationID);

        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext()); // 初始化 JPush
        init();
    }

    private void init() {
        String versionName = getVersionName();
    }



    private String getVersionName(){
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        backGround.removeCallbacks(goNextUiRunnable);
    }

    private void playAnimation() {
        ObjectAnimator a = ObjectAnimator.ofFloat(backGround, "scaleX", 0.5f, 1f).setDuration(1000);
        ObjectAnimator b = ObjectAnimator.ofFloat(backGround, "scaleY", 0.5f, 1f).setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(a, b);
        animatorSet.start();
        backGround.postDelayed(goNextUiRunnable, 2000);
    }

    private Runnable goNextUiRunnable = () -> {
            //TODO 判断是否登录
        goNextUi();
    };

    private void goNextUi() {
        boolean guided = SPUtils.getBoolean(WelcomeActivity.this, Constant.GUIDED);
        if (guided){
            checkLogin();
        }else {
            startActivity(new Intent(this,GuideActivity.class));
            finish();
        }

    }

    private void checkLogin() {
        String memid = SPUtils.getString(this, Constant.MEMID);
        Log.i("WelcomeActivity", memid);
        if (memid.equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
//             e10adc3949ba59abbe56e057f20f883e
            String username = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
            String pwd = SPUtils.getString(this, Constant.LOGIN_PWD);
            Log.i(TAG,username+"  "+pwd);
            RequestBody formBody = new FormBody.Builder()
                    .add("type", "0")
                    .add("lgn", username)
                    .add("pwd", pwd)
                    .build();
            final Request request = new Request.Builder()
                    .url(Constant.format("login"))
                    .post(formBody)
                    .build();
            try {
                new Thread(() -> {
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Utils.showToast(WelcomeActivity.this,"登陆失败，请检查网络");
                                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                                finish();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream is = response.body().byteStream();
                                String json = null;
                                try {
                                    json = new Xml2Json(is).Pull2Xml();
                                    Log.i("loginActivity",json+"");
                                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                    Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                                    if("".equals(loginBean)){
//                                        Utils.showToast(WelcomeActivity.this,"网络错误,请重试");
                                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                    if (null == json){
                                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                    String result = loginBean.getRESULT();
                                    if (result.equals("0")){
                                        LoginBean.USERINFOBean userinfo = loginBean.getUSERINFO();
                                        String memid1 = userinfo.getMEMID();
                                        String logintoken = userinfo.getLOGINTOKEN();
                                        Log.i("LoginActivity", memid1);
                                        String sex = userinfo.getSEX();
                                        String sax = "";
                                        if (sex.equals("1")) {
                                            sax="女";
                                        }else if (sex.equals("0")){
                                            sax="男";
                                        }
                                        SPUtils.put(WelcomeActivity.this,Constant.SEX,sax);
                                        SPUtils.put(WelcomeActivity.this,Constant.MEMID, memid1);
                                        SPUtils.put(WelcomeActivity.this,Constant.LOGIN_TOKEN,logintoken);
                                        String avatar = userinfo.getAVATAR();
                                        if (!avatar.startsWith("http")) {
                                            avatar = "http://"+avatar;
                                        }
                                        SPUtils.put(WelcomeActivity.this,Constant.AVATAR,avatar);
                                        if (!"".equals(userinfo.getNICKNAME())) {
                                            SPUtils.put(WelcomeActivity.this, Constant.NICKNAME, userinfo.getNICKNAME());
                                            Log.i("WelcomeActivity","nickname:"+userinfo.getNICKNAME());
                                        }
                                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                        finish();
                                    }else {
                                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                                        finish();
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
    }
}
