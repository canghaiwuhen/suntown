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
import cn.jpush.im.android.api.JMessageClient;


/**
 * Created by pc on 2016/8/31.
 */

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    public static final String CONV_TITLE = "convTitle";
    public static final String TARGET_ID = "target_id";
    public static String PICTURE_DIR = "sdcard/Chart/pictures/";
    public static String FILE_DIR = "sdcard/Chat/recvFiles/";
    public static final int REQUEST_CODE_TAKE_PHOTO = 4;
    public static final int REQUEST_CODE_SELECT_PICTURE = 6;
    public static final int RESULT_CODE_SELECT_PICTURE = 8;
    public static final int REQUEST_CODE_SELECT_ALBUM = 10;
    public static final int RESULT_CODE_SELECT_ALBUM = 11;
    public static final int REQUEST_CODE_BROWSER_PICTURE = 12;
    public static final int RESULT_CODE_BROWSER_PICTURE = 13;
    public static final int REQUEST_CODE_CHAT_DETAIL = 14;
    public static final int RESULT_CODE_CHAT_DETAIL = 15;
    public static final int REQUEST_CODE_FRIEND_INFO = 16;
    public static final int RESULT_CODE_FRIEND_INFO = 17;
    public static final int REQUEST_CODE_CROP_PICTURE = 18;
    public static final int REQUEST_CODE_ME_INFO = 19;
    public static final int RESULT_CODE_ME_INFO = 20;
    public static final int REQUEST_CODE_ALL_MEMBER = 21;
    public static final int RESULT_CODE_ALL_MEMBER = 22;
    public static final int RESULT_CODE_SELECT_FRIEND = 23;
    public static final int REQUEST_CODE_SEND_LOCATION = 24;
    public static final int RESULT_CODE_SEND_LOCATION = 25;
    public static final int REQUEST_CODE_SEND_FILE = 26;
    public static final int RESULT_CODE_SEND_FILE = 27;
    public static final int REQUEST_CODE_EDIT_NOTENAME = 28;
    public static final int RESULT_CODE_EDIT_NOTENAME = 29;
    public static final int ON_GROUP_EVENT = 3004;
    public static final String MsgIDs = "msgIDs";
    public static final String POSITION = "position";
    public static final String GROUP_ID = "groupId";
    public static final String TARGET_APP_KEY = "targetAppKey";
    private static Context mAppContext;
    private DbManager.DaoConfig config;
    private DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
//        HistoryDB.createDB(mAppContext);
//        85191122
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出Debug日志
        JPushInterface.init(this);
        JMessageClient.init(mAppContext);
        JPushInterface.setDebugMode(false);
        String registrationID = JPushInterface.getRegistrationID(mAppContext);
        SPUtils.put(mAppContext, Constant.REGISTRATION_ID,registrationID);
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
