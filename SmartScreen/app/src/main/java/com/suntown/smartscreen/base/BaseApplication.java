package com.suntown.smartscreen.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.suntown.smartscreen.utils.Constant;

import org.xutils.BuildConfig;
import org.xutils.DbManager;
import org.xutils.x;


/**
 *
 */

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static Context mAppContext;
    private DbManager.DaoConfig config;
    private DbManager db;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出Debug日志
        config = new DbManager.DaoConfig();
        config.setDbName(Constant.DBNAME).setDbVersion(1);//设置数据库版本号
        config.setDbUpgradeListener((db, oldVersion, newVersion) -> {
        });
        config.setTableCreateListener((db, table) -> {
        });
        config.setAllowTransaction(true);
        db = x.getDb(config);
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return config;
    }
}
