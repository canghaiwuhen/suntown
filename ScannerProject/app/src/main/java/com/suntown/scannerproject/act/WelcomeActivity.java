package com.suntown.scannerproject.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.suntown.scannerproject.MainActivity;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiClient;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.base.BaseApplication;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.query.bean.Person;
import com.suntown.scannerproject.scanner.bean.ShelfItemBean;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
                List<ShelfItemBean> shelfItemBeanList = db.selector(ShelfItemBean.class).findAll();
                if (null!=personList){
                    for (Person person : personList) {
                        long time = person.time;
                        if (currentTime-time>=3600 * 24 * 1000*7){
                            db.delete(person);
                        }
                    }
                }
                if (null!=shelfItemBeanList&&0<shelfItemBeanList.size()){
                    for (ShelfItemBean shelfItemBean : shelfItemBeanList) {
                        long time = shelfItemBean.time;
                        if (currentTime-time>=3600 * 24 * 1000*7){
                            db.delete(shelfItemBean);
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
                        userName = SPUtils.getString(WelcomeActivity.this, Constant.USER_CODE);
                        passWord = SPUtils.getString(WelcomeActivity.this, Constant.PSWD);
                        if ("".equals(userName)||"".equals(passWord)){
                            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                            finish();
                        }else{
                            initNet();
                        }

                });
            }
        });
        //开始播放动画
        rootView.startAnimation(animation);

    }



    private void initNet() {
        String serverip = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverip)) {
            serverip = ApiConstant.BASE_URL;
        }
        Log.i(TAG,"serverip:"+serverip);
        String ip = Constant.formatBASE_HOST(serverip);
        Log.i(TAG,"ip:"+ip);
        String usercode = SPUtils.getString(this, Constant.USER_CODE);
        String pswd = SPUtils.getString(this, Constant.PSWD);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.login(usercode,pswd).compose(RxSchedulers.io_main()).subscribe(string -> {
            String xml =  string.toString();
            xml=xml.replace("<ns:VerifyUserLoginResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
            xml=xml.replace("</ns:return></ns:VerifyUserLoginResponse>","");
            Log.i(TAG,"xml:"+xml);
            if ("0".equals(xml)) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable.toString());
            //                Utils.showToast(this,throwable.toString());
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();
        });
    }


}
