package com.suntown.suntownshop.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suntown.suntownshop.utils.FileManager;

/**
 * 搜索记录数据库操作类
 *
 * @author Ken
 * @version 2015年3月10日 下午3:11:19
 *
 */
public class SearchHistoryDb {
	private  static String DB_NAME;
	private final static String TB_NAME = "searchhistory";
	private SQLiteDatabase db;
	private String userid;
	private Context context;

	public SearchHistoryDb(Context context, String userid) {
		DB_NAME = FileManager.getDataPath(context)
				+ "/suntownshop.db";
		this.context = context;
		this.userid = userid;
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,keyword VARCHAR,lasttime DATETIME,times integer,userid VARCHAR)");
	}

	public void updateHistory(String keyword,int times){
		ContentValues cv = new ContentValues();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cv.put("lasttime",format.format(new Date()) );
		cv.put("times", times);
		db.update(TB_NAME, cv, "userid=? and keyword = ?", new String[] {
				userid, keyword });
	}
	
	public void insertHistory(String keyword) {
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " WHERE userid=? and keyword= ?", new String[] { userid,
				keyword });
		if (c.moveToNext()) {
			int times = c.getInt(c.getColumnIndex("times"));
			updateHistory(keyword, times + 1);
			return;
		}
		c.close();
		db.execSQL("INSERT INTO " + TB_NAME
				+ " VALUES (NULL, ?,datetime('now','localtime'), ?,?)",
				new Object[] { keyword, 1, userid });
	}
	
	public List<Map<String,String>> getHistory(String keyword){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Cursor c = db.rawQuery("SELECT keyword FROM " + TB_NAME + " where userid=? and keyword like ? order by lasttime desc",
				new String[] { userid,"%"+keyword+"%" });
		while (c.moveToNext()) {
			String temp = c.getString(c.getColumnIndex("keyword"));
			Map<String,String> map =new HashMap<String, String>();
			map.put("keyword", temp);
			list.add(map);
		}
		c.close();
		return list;
	}
	
	public List<Map<String,String>> getHistory(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Cursor c = db.rawQuery("SELECT keyword FROM " + TB_NAME + " where userid=? order by lasttime desc",
				new String[] { userid });
		while (c.moveToNext()) {
			String keyword = c.getString(c.getColumnIndex("keyword"));
			Map<String,String> map =new HashMap<String, String>();
			map.put("keyword", keyword);
			list.add(map);
		}
		c.close();
		return list;
	}
	
	public void clear() {
		db.execSQL("delete from " + TB_NAME);
		db.delete("sqlite_sequence", "name = ?", new String[] { TB_NAME });
	}
	
	public void close(){
		db.close();
	}
}
