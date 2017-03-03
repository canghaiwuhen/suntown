package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.bean.LoginBean;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.PushBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import rx.functions.Action1;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";
    private Handler mHandler;
    private DbManager db;
    private long currentTime = System.currentTimeMillis();
    private long lastSevenTime = currentTime - 3600 * 24 * 1000*7;
    private String passWord;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
        setContentView(rootView);
        mHandler = new Handler();
        //初始化渐变动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        //删除数据库中大于7天数据
        new Thread(() -> {
            db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
            try {
                List<Person> personList = db.selector(Person.class).findAll();
                if (null!=personList){
                    for (Person person : personList) {
                        long time = person.time;
                        if (currentTime-time>=3600 * 24 * 1000*7){
                            db.delete(person);
                        }
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
                Log.i(TAG,"数据清理失败");
            }
        }).start();
        animation.setAnimationListener(new Animation.AnimationListener() {



            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.post(() -> {
                    Boolean guided = SPUtils.getBoolean(WelcomeActivity.this, Constant.GUIDED);
                    if (guided){
                        userName = SPUtils.getString(WelcomeActivity.this, Constant.USER_NAME);
                        passWord = SPUtils.getString(WelcomeActivity.this, Constant.PASS_WORD);
                        if ("".equals(userName)||"".equals(passWord)){
                            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                            finish();
                        }else{
                            initNet();
                            JMessageClient.login(userName, Constant.JMPSW, new BasicCallback() {
                                @Override
                                public void gotResult(int responseCode, String LoginDesc) {
                                    if (responseCode == 0) {
                                        Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                                        Log.i(TAG, "JM登录成功");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                        Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                                        Log.i(TAG, "JM登录失败");
                                        registerJM(userName);
                                    }
                                }
                            });
                        }
                    }else{
                        startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                        finish();
                    }
                });
            }
        });
        //开始播放动画
        rootView.startAnimation(animation);

    }

    private void registerJM(String userName) {
        JMessageClient.register(this.userName, Constant.JMPSW, new BasicCallback() {
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


    private void initNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.USER_NAME, userName);
        params.put(Constant.PASS_WORD, passWord);
        ApiClient.getInstance().mApiService.login(params).compose(RxSchedulers.io_main()).subscribe(loginBean -> {
            int resultCode = loginBean.getResultCode();
            LoginBean.UserBean user = loginBean.getUser();
            if (1==resultCode) {
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
//            Utils.showToast(this,user.toString());
        }, throwable -> {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
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
}
