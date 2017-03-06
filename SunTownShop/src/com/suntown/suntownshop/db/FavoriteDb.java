package com.suntown.suntownshop.db;

import java.util.ArrayList;

import com.suntown.suntownshop.model.FavoriteGoods;
import com.suntown.suntownshop.utils.FileManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 收藏数据库操作类
 * 
 * @author Ken
 *
 */
public class FavoriteDb {
	private static String DB_NAME;
	private final static String TB_NAME = "favorite";
	private SQLiteDatabase db;
	private String userid;
	private Context mContext;

	public FavoriteDb(Context context, String userid) {
		this.mContext = context;
		DB_NAME = FileManager.getDataPath(context) + "/suntownshop.db";
		this.userid = userid;
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode VARCHAR,name VARCHAR,imagepath VARCHAR,spec VARCHAR,price integer,userid VARCHAR)");
		if (!checkColumnExists(db, TB_NAME, "userid")) {
			db.execSQL("alter table " + TB_NAME + " add column userid VARCHAR");
			ContentValues cv = new ContentValues();
			cv.put("userid", userid);
			try {
				db.update(TB_NAME, cv, "userid is ? ", new String[] { null });
			} catch (Exception e) {
				System.out.println("数据库操作错误，更新userid:" + e.getMessage());
				e.printStackTrace();
			}
		}

		if (!checkColumnExists(db, TB_NAME, "sfid")) {
			db.execSQL("alter table " + TB_NAME + " add column sfid VARCHAR");
		}

		if (!checkColumnExists(db, TB_NAME, "floorname")) {
			db.execSQL("alter table " + TB_NAME
					+ " add column floorname VARCHAR");
		}

		if (userid != null && !"".equals(userid)) {
			ContentValues cv = new ContentValues();
			cv.put("userid", userid);
			try {
				db.update(TB_NAME, cv, "userid=? ", new String[] { "" });
			} catch (Exception e) {
				System.out.println("数据库操作错误，更新userid:" + e.getMessage());
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

	public void clear() {
		db.execSQL("delete from " + TB_NAME);
		db.delete("sqlite_sequence", "name = ?", new String[] { TB_NAME });
	}

	public void delete(String barCode) {
		db.delete(TB_NAME, "userid=? and barcode = ?", new String[] { userid,
				barCode });
	}

	public void update(String barCode, String name, String imagepath,
			String spec, double price, String shelfId, String floorName) {

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("spec", spec);
		cv.put("price", price);
		cv.put("imagepath", imagepath);
		cv.put("sfid", shelfId);
		cv.put("floorname", floorName);
		try {
			db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
					userid, barCode });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insert(String barCode, String name, String imgPath,
			String spec, double price, String shelfId, String floorName) {
		if (!isInFavorite(barCode)) {
			db.execSQL("INSERT INTO " + TB_NAME
					+ " VALUES (NULL, ?, ?,?,?,?,?,?,?)", new Object[] {
					barCode, name, imgPath, spec, price, userid, shelfId,
					floorName });
		} else {
			update(barCode, name, imgPath, spec, price, shelfId, floorName);
		}

	}

	public boolean isInFavorite(String barCode) {
		boolean result = false;
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " WHERE userid=? and  barcode= ?", new String[] { userid,
				barCode });
		if (c.moveToNext()) {
			result = true;
		}
		c.close();
		return result;
	}

	/**
	 * 获取所有收藏夹信息的ArrayList
	 * 
	 * @return
	 */
	public ArrayList getAll() {
		ArrayList<FavoriteGoods> goodsList = new ArrayList<FavoriteGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME + " where userid=?",
				new String[] { userid });
		while (c.moveToNext()) {
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			String shelfId = c.getString(c.getColumnIndex("sfid"));
			String floorName = c.getString(c.getColumnIndex("floorname"));
			FavoriteGoods goods = new FavoriteGoods(barCode, name, spec,
					imagepath, price, shelfId, floorName);
			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}

	/**
	 * 关闭收藏夹数据库
	 */
	public void Close() {
		db.close();
	}

}
