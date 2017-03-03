package com.suntown.smartscreen;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;
import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.base.BaseApplication;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.login.LoginActivity;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.Person;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.StatusBarUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;


public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";
    private String userName;
    private String passWord;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
        setContentView(rootView);
        initView();
    }

    protected void initView() {
        StatusBarUtil.setTranslucentBackground(this);

        new Thread(() -> {
            DbManager db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
            try {
                List<Person> personList = db.selector(Person.class).findAll();
                if (null!=personList){
                    for (Person person : personList) {
                        long time = person.time;
                        if (System.currentTimeMillis()-time>=3600 * 24 * 1000*7){
                            db.delete(person);
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
                Log.i(TAG,"数据清理失败");
            }
        }).start();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    Boolean guided = SPUtils.getBoolean(WelcomeActivity.this, Constant.GUIDED);
                    if (guided){
                        userName = SPUtils.getString(WelcomeActivity.this, Constant.USER_NAME);
                        passWord = SPUtils.getString(WelcomeActivity.this, Constant.PASS_WORD);
                        if ("".equals(userName)||"".equals(passWord)){
                            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                            finish();
                        }else{
                            initNet();
                        }
                    }else{
                        startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                        finish();
                    }
            }
        });
        //开始播放动画
        rootView.startAnimation(animation);
    }

    private void initNet() {
        ApiClient.getInstance().service.login(userName,passWord).compose(RxSchedulers.io_main()).subscribe(s -> {
            String json = s.replace("<ns:loginResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:loginResponse>", "");
            User user = new Gson().fromJson(json, User.class);
            if ("1".equals(user.RESULT)) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        }, throwable -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
