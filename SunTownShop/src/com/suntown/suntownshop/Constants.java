package com.suntown.suntownshop;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
/**
 * ȫ�ֳ���������
 *
 * @author Ǯ��
 * @version 2015��9��21�� ����9:34:05
 *
 */
public final class Constants {
	public static int displayWidth;
	public static int displayHeight;
	public static String DOMAIN_NAME = "http://www.suntowngis.com:8080/";
//	public static String DOMAIN_NAME = "http://192.168.0.12:8080/";
	// ���ﳵˢ�¹㲥ˢ�½�����ACTION
	public final static String ACTION_SHOPCART_CHANGED = "com.suntown.suntownshop.Action.SHOPCART_CHANGED";

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class wx {
		public final static String APP_ID = "wx367430e9dda3f2e4";
		public final static String APP_SECRET = "510637356679e94896237e7144c0a3d7";
		// �̻���
		public static final String MCH_ID = "1234382502";

		// API��Կ�����̻�ƽ̨����
		public static final String API_KEY = "mTe3S3oKwqn8CyzCHxxkTF1jkEc3wFRz";
	}

	public static boolean isConnect(Context context) {
		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// �жϵ�ǰ�����Ƿ��Ѿ�����
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return false;
	}

	public static void setNetWork(Context context) {
		Intent intent = null;
		// �ж��ֻ�ϵͳ�İ汾 ��API����10 ����3.0�����ϰ汾
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}

	public static void setBlueTooth(Context context) {
		Intent intent = null;
		// �ж��ֻ�ϵͳ�İ汾 ��API����10 ����3.0�����ϰ汾
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.bluetooth.BluetoothSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}

	/**
	 * �����жϷ����Ƿ�����.
	 * 
	 * @param context
	 * @param className
	 *            �жϵķ�������
	 * @return true ������ false ��������
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(150);
		System.out.println("�������еķ�����:" + serviceList.size());
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * �˳������µ�¼
	 * 
	 * @param context
	 */
	public static void reLogin(Context context) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				"suntownshop", 0);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean("islogin", false);
		mEditor.putString("userId", "");
		mEditor.putString("showname", "");
		mEditor.putString("nickname", "");
		mEditor.putString("mobile", "");
		mEditor.putString("avatar", "");
		mEditor.putString("m_voucher", "");
		mEditor.putBoolean("isvip", false);
		mEditor.commit();
		// ˢ�¹��ﳵ
		context.sendBroadcast(new Intent(Constants.ACTION_SHOPCART_CHANGED));

		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
}
