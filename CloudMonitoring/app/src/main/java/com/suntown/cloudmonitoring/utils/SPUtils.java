package com.suntown.cloudmonitoring.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * SharedPrefrences的工具类
 * @author Administrator
 *
 */
public class SPUtils {
	/**
	 * 万能的put方法
	 * @param context
	 * @param key
	 * @param value
	 */

	public static void put(Context context, String key, Object value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		//instanceof 用于判断数据类型的
		if (value instanceof String) {
			edit.putString(key, (String) value);
		} else if (value instanceof Integer) {
			edit.putInt(key, (int) value);
		} else if (value instanceof Boolean) {
			edit.putBoolean(key, (boolean) value);
		}
		edit.commit();
	}
	
	/**
	 * 获取字符串
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
	/**
	 * 获取整数
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, 0);
	}
	/**
	 * 获取Boolean
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	/**
	 * 移除某个key值已经对应的值
	 */
	public static void remove(Context context,String key) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.apply();
	}

	/**
	 * 清除所有数据
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.apply();
	}



}
