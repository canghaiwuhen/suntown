package com.suntown.cloudmonitoring.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.suntown.cloudmonitoring.BuildConfig;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.ExampleUtil;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.StatusBarCompat;
import com.suntown.cloudmonitoring.utils.Utils;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by pc on 2016/8/31.
 */

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static Context mAppContext;
    private DbManager.DaoConfig config;
    private DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
//        HistoryDB.createDB(mAppContext);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出Debug日志
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        String registrationID = JPushInterface.getRegistrationID(this);
        SPUtils.put(this, Constant.REGISTRATION_ID,registrationID);
//        if(config==null) {
            config = new DbManager.DaoConfig();
            config.setDbName(Constant.DBNAME).setDbVersion(1);//设置数据库版本号
            config.setDbUpgradeListener((db, oldVersion, newVersion) -> {
            });
            config.setTableCreateListener((db, table) -> {
            });
            config.setAllowTransaction(true);
//        }
        db = x.getDb(config);

    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return config;
    }
}
