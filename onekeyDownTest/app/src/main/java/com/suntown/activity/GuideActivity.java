package com.suntown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class GuideActivity extends Activity {

    @BindView(R.id.guide_vp)
    ViewPager guideVp;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        guideVp.setAdapter(pagerAdapter);
        guideVp.setOnClickListener(v -> {
            Utils.showToast(this,"123");
        });
        client = new OkHttpClient();
    }

    int[] picRes = new int[]{R.drawable.welocme1, R.drawable.welocme2, R.drawable.welocme3, R.drawable.welocme4};
    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return picRes == null ? 0 : picRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setImageResource(picRes[position]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            if(position==picRes.length-1){
                iv.setOnClickListener(v -> {
                    SPUtils.put(GuideActivity.this,Constant.GUIDED,true);
                    checkLogin();
                });
            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    private void checkLogin() {
        String memid = SPUtils.getString(this, Constant.MEMID);
        Log.i("WelcomeActivity", memid);
        if (memid.equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
//             e10adc3949ba59abbe56e057f20f883e
            RequestBody formBody = new FormBody.Builder()
                    .add("type", "0")
                    .add("lgn", SPUtils.getString(this, Constant.LOGIN_USER_NAME))
                    .add("pwd", SPUtils.getString(this, Constant.LOGIN_PWD))
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
                            Utils.showToast(GuideActivity.this, "登陆失败，请检查网络");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream is = response.body().byteStream();
                            String json = null;
                            try {
                                json = new Xml2Json(is).Pull2Xml();
                                Log.i("loginActivity", json + "");
                                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                                String result = loginBean.getRESULT();
                                if (result.equals("0")) {
                                    LoginBean.USERINFOBean userinfo = loginBean.getUSERINFO();
                                    String memid1 = userinfo.getMEMID();
                                    String logintoken = userinfo.getLOGINTOKEN();
                                    Log.i("LoginActivity", memid1);
                                    SPUtils.put(GuideActivity.this, Constant.SEX, userinfo.getSEX());
                                    SPUtils.put(GuideActivity.this, Constant.NICKNAME, userinfo.getNAME());
                                    SPUtils.put(GuideActivity.this, Constant.MEMID, memid1);
                                    SPUtils.put(GuideActivity.this, Constant.LOGIN_TOKEN, logintoken);
                                    if (!"".equals(userinfo.getNICKNAME())) {
                                        SPUtils.put(GuideActivity.this, Constant.NICKNAME, userinfo.getNICKNAME());
                                        Log.i("WelcomeActivity", "nickname:" + userinfo.getNICKNAME());
                                    }
                                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(GuideActivity.this, LoginActivity.class));
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

//    @OnClick(R.id.guide_btn)
//    public void onClick() {
//        SPUtils.put(GuideActivity.this, Constant.GUIDED, true);
//        checkLogin();
//    }
}



