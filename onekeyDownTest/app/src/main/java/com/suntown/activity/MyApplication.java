package com.suntown.activity;

import android.app.Application;

import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
        JPushInterface.init(getApplicationContext());
        String registrationID = JPushInterface.getRegistrationID(this);
        SPUtils.put(this, Constant.REGISTRATION_ID,registrationID);
    }

    private void setupDatabase() {
        //创建数据库
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

    }

}
