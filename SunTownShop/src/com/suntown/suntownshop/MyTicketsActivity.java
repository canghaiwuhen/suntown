package com.suntown.suntownshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
/**
 * 选择礼券类型页面
 *
 * @author 钱凯
 * @version 2015年4月21日 上午9:43:23
 *
 */
public class MyTicketsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_select);
	}
	
	public void close(View v){
		finish();
	}
	
	public void myCoupons(View v){
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		if (!isLogin || "".equalsIgnoreCase(userId)) {
			Intent intent = new Intent(this,
					LoginActivity.class);
			startActivity(intent);
			return;
		} else {
			Intent intent = new Intent(this,
					MyCouponsActivity.class);
			intent.putExtra("tickettype", 1);
			startActivity(intent);
		}
	}
	
	public void ticketLive(View v){
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		if (!isLogin || "".equalsIgnoreCase(userId)) {
			Intent intent = new Intent(this,
					LoginActivity.class);
			startActivity(intent);
			return;
		} else {
			Intent intent = new Intent(this,
					MyCouponsActivity.class);
			intent.putExtra("tickettype", 0);
			startActivity(intent);
		}
	}
}
