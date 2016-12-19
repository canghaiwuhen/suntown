package com.suntown.scannerproject.base;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;

import org.xutils.BuildConfig;
import org.xutils.DbManager;
import org.xutils.x;


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
        db = x.getDb(config);
        String deviceId = SPUtils.getString(mAppContext, Constant.DEVICE_NUM);
        if ("".equals(deviceId)) {
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId();
            SPUtils.put(mAppContext, Constant.DEVICE_NUM,DEVICE_ID);
        }

    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return config;
    }
}
