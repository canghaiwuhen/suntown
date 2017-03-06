package com.suntown.suntownshop.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suntown.suntownshop.model.Beacon;
import com.suntown.suntownshop.utils.FileManager;

/**
 * Beacon数据库操作类
 *
 * @author 钱凯
 * @version 2015年4月29日 下午1:42:15
 *
 */
public class BeaconDb {
	private  static String DB_NAME ;
	private final static String TB_NAME = "beacon";
	private SQLiteDatabase db;
	private Context mContext;
	public BeaconDb(Context context) {
		this.mContext = context;
		DB_NAME = FileManager.getDataPath(context)
				+ "/suntownshop.db";
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		if (checkColumnExists(db, TB_NAME, "name")) {
			db.execSQL("drop table " + TB_NAME);
		}
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,address VARCHAR,lasttime DATETIME DEFAULT CURRENT_TIMESTAMP)");

		// 增加是否正在请求消息字段，0--未推送 1--推送中 2--今日已推送
		//
		if (!checkColumnExists(db, TB_NAME, "onpull")) {
			db.execSQL("alter table " + TB_NAME
					+ " add column onpull integer DEFAULT '0'");
			ContentValues cv = new ContentValues();
			cv.put("onpull", 0);
			try {
				db.update(TB_NAME, cv, "onpull is ? ", new String[] { null });
			} catch (Exception e) {
				System.out.println("数据库操作错误，添加字段userid:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查表中某列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	private boolean checkColumnExists(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db
					.rawQuery(
							"select * from sqlite_master where name = ? and sql like ?",
							new String[] { tableName, "%" + columnName + "%" });
			result = null != cursor && cursor.moveToFirst();
		} catch (Exception e) {
			System.out.println("数据库操作出错:" + e.getMessage());
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

	public boolean insertBeacon(Beacon beacon) {
		boolean isDone = false;
		try {

			Cursor c = db
					.rawQuery("SELECT * FROM " + TB_NAME + " WHERE address= ?",
							new String[] { beacon.getAddress() });
			if (c.moveToNext()) {
				return updateBeacon(beacon);
			}
			c.close();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			db.execSQL(
					"INSERT INTO " + TB_NAME + " VALUES (NULL, ?,?,?)",
					new Object[] { beacon.getAddress(),
							format.format(beacon.getDate()),String.valueOf(beacon.getOnpull()) });
			isDone = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isDone;
	}

	public boolean updateBeacon(Beacon beacon) {
		boolean isDone = false;
		try {
			ContentValues cv = new ContentValues();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			cv.put("lasttime", format.format(beacon.getDate()));
			cv.put("onpull", beacon.getOnpull());
			db.update(TB_NAME, cv, "address = ?",
					new String[] { beacon.getAddress() });
			isDone = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isDone;
	}

	public boolean updateBeacon(String address,int onpull) {
		boolean isDone = false;
		try {
			ContentValues cv = new ContentValues();
			cv.put("onpull", onpull);
			db.update(TB_NAME, cv, "address = ?",
					new String[] { address });
			isDone = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isDone;
	}
	
	public Beacon getBeacon(String address) {
		Beacon beacon = null;
		Cursor c = db.rawQuery("SELECT address,lasttime,onpull FROM " + TB_NAME
				+ " where address=?", new String[] { address });
		if (c.moveToNext()) {

			try {
				String strDate = c.getString(c.getColumnIndex("lasttime"));
				int onpull = c.getInt(c.getColumnIndex("onpull"));
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = format.parse(strDate);
				beacon = new Beacon(address, date,onpull);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return beacon;
	}

	public void Close() {
		db.close();
	}

}
