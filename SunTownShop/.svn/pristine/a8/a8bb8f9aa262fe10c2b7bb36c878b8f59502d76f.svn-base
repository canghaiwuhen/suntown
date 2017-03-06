package com.suntown.suntownshop.receiver;

import com.suntown.suntownshop.service.LocalService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;
/**
 * 通知清除广播监听
 *
 * @author 钱凯
 * @version 2015年4月21日 上午10:03:20
 *
 */
public class NotificationCanceledBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//登记消息可推送
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				"suntownshop", 0);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean("ismsgpushshow", false);
		mEditor.commit();
		System.out.println("通知清除了");
		
	}

}
