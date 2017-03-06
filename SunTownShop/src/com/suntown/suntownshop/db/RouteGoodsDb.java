package com.suntown.suntownshop.db;

import java.util.ArrayList;

import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.utils.FileManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * �����������ݿ������
 *
 * @author Ǯ��
 * @version 2015��7��21�� ����9:57:07
 *
 */
public class RouteGoodsDb {
	private static String DB_NAME;
	private final static String TB_NAME = "routegoods";
	private SQLiteDatabase db;
	private String userId;
	private Context context;

	public RouteGoodsDb(Context context, String userId) {
		DB_NAME = FileManager.getDataPath(context) + "/suntownshop.db";
		this.context = context;
		this.userId = userId;
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode VARCHAR,name VARCHAR,sfid VARCHAR,floorname VARCHAR,userid VARCHAR)");
		// ���û���Ʒ�ҿ�����ǰ�û���
		if (userId != null && !"".equals(userId)) {
			ContentValues cv = new ContentValues();
			cv.put("userid", userId);
			try {
				db.update(TB_NAME, cv, "userid=? ", new String[] { "" });
			} catch (Exception e) {
				System.out.println("���ݿ�������󣬸���userid:" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����һ��������Ʒ
	 * 
	 * @param barCode
	 * @param name
	 * @param sfid
	 * @param floorName
	 */
	public void insertGoods(String barCode, String name, String sfid,
			String floorName) {
		try {

			Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
					+ " WHERE userid=? and barcode= ?", new String[] { userId,
					barCode });
			if (c.moveToNext()) {
				return;
			}
			c.close();

			db.execSQL("INSERT INTO " + TB_NAME + " VALUES (NULL, ?,?, ?,?,?)",
					new Object[] { barCode, name, sfid, floorName, userId });
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("���뵼����Ʒ����" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ȡ�õ��������е�������Ʒ
	 * 
	 * @return
	 */
	public ArrayList getAll() {
		ArrayList<ParcelableGoods> goodsList = new ArrayList<ParcelableGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME + " where userid=?",
				new String[] { userId });
		while (c.moveToNext()) {
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String sfid = c.getString(c.getColumnIndex("sfid"));
			String floorName = c.getString(c.getColumnIndex("floorname"));

			ParcelableGoods goods = new ParcelableGoods(barCode, name, sfid,
					floorName);

			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}

	/**
	 * ������û��µ����е�����Ʒ
	 */
	public void clearAll() {
		db.delete(TB_NAME, "userid=?", new String[] { userId });
	}

	/**
	 * �رյ����������ݿ�
	 */
	public void Close() {
		db.close();
	}
}