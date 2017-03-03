package com.suntown.activity;

import android.app.Application;
import android.content.Context;

import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MyApplication extends Application{

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
//        JPushInterface.init(getApplicationContext());


    }


    public static Context getAppContext() {
        return mAppContext;
    }

}
