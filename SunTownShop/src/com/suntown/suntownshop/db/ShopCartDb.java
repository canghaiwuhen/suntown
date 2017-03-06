package com.suntown.suntownshop.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import com.suntown.suntownshop.Constants;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.utils.FileManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 购物车数据库操作类
 * 
 * @author Ken
 *
 */
public class ShopCartDb {
	private static String DB_NAME;
	private final static String TB_NAME = "shopcart";
	private SQLiteDatabase db;
	private String userid;
	private Context context;
	
	public ShopCartDb(Context context,String userid) {
		DB_NAME = FileManager.getDataPath(context)
				+ "/suntownshop.db";
		this.context = context;
		this.userid = userid;
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		db.execSQL("create table if not exists "
				+ TB_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode VARCHAR,name VARCHAR,imagepath VARCHAR,spec VARCHAR,price integer,quantity integer,ischecked boolean,userid VARCHAR)");

		//增加商品运送方式字段 0为自带，1为配送
		if (!checkColumnExists(db, TB_NAME, "deliver")) {
			db.execSQL("alter table " + TB_NAME + " add column deliver integer DEFAULT '0'");
			ContentValues cv = new ContentValues();
			cv.put("deliver", 0);
			try {
				db.update(TB_NAME, cv, "deliver is ? ", new String[] { null });
			} catch (Exception e) {
				System.out.println("数据库操作错误，添加字段userid:" + e.getMessage());
				e.printStackTrace();
			}
		}
		//增加插入时间字段
		if (!checkColumnExists(db, TB_NAME, "timestamp")) {
			db.execSQL("create table if not exists tb_temp(_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode VARCHAR,name VARCHAR,imagepath VARCHAR,spec VARCHAR,price integer,quantity integer,ischecked boolean,userid VARCHAR,deliver integer DEFAULT '0',timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
			db.execSQL("insert into tb_temp select *,'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' from "+TB_NAME);
			db.execSQL("drop table "+TB_NAME);

			db.execSQL("alter table tb_temp rename to "+TB_NAME);

		}
		//无用户商品挂靠到当前用户下 
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

	public ArrayList getAllChecked() {
		ArrayList<CartGoods> goodsList = new ArrayList<CartGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " where userid=? and ischecked=?",
				new String[] { userid, "1" });
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			int deliver = c.getInt(c.getColumnIndex("deliver"));
			CartGoods goods = new CartGoods(_id, barCode, name, imagepath,
					spec, price, quantity,deliver);
			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}

	public ArrayList getAllChecked(int deliverType) {
		ArrayList<CartGoods> goodsList = new ArrayList<CartGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " where userid=? and ischecked=? and deliver=?",
				new String[] { userid, "1" ,String.valueOf(deliverType)});
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			int deliver = c.getInt(c.getColumnIndex("deliver"));
			CartGoods goods = new CartGoods(_id, barCode, name, imagepath,
					spec, price, quantity,deliver);
			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}
	
	public ArrayList getGoodsByTime(String date){
		ArrayList<CartGoods> goodsList = new ArrayList<CartGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " where userid=? and (timestamp is null or datetime(timestamp,'localtime')<?)",
				new String[] { userid, "'"+date+"'"});
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			int deliver = c.getInt(c.getColumnIndex("deliver"));
			CartGoods goods = new CartGoods(_id, barCode, name, imagepath,
					spec, price, quantity,deliver);
			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}
	
	public void deleteGoodsByTime(String date){
		db.delete(TB_NAME, "userid=? and (timestamp is null or datetime(timestamp,'localtime')<?)", new String[] { userid,
				"'"+date+"'" });
		refreshCart();
	}
	
	public void deleteAllChecked() {
		db.delete(TB_NAME, "userid=? and ischecked = ?", new String[] { userid,
				"1" });
		refreshCart();
	}

	public void clearStateAll() {
		db.execSQL("update " + TB_NAME + " set ischecked='0' where userid=?",
				new String[] { userid });
	}

	public void changeState(String barCode, boolean isChecked) {
		ContentValues cv = new ContentValues();
		cv.put("ischecked", isChecked);
		db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
				userid, barCode });
	}

	/**
	 * 从购物车中删除一个商品
	 * 
	 * @param barCode
	 *            商品条码
	 * @return
	 */
	public boolean deleteGoods(String barCode) {
		boolean isDone = false;
		try {
			db.delete(TB_NAME, "userid=? and barcode = ?", new String[] {
					userid, barCode });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDone;
	}

	/**
	 * 更新购物车中一条商品的所有信息
	 * 
	 * @param barcode
	 *            商品条码
	 * @param name
	 *            商品名
	 * @param spec
	 *            规格
	 * @param price
	 *            价格
	 * @param quantity
	 *            数量
	 * @return
	 */
	public boolean updateGoods(String barcode, String name, String imagepath,
			String spec, double price, int quantity) {
		boolean isDone = false;
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("spec", spec);
		cv.put("price", price);
		cv.put("imagepath", imagepath);
		cv.put("quantity", quantity);
		try {
			db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
					userid, barcode });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDone;
	}

	/**
	 * 更新购物车中一条商品的数量
	 * 
	 * @param barcode
	 *            商品条码
	 * @param quantity
	 *            数量
	 * @return
	 */
	public boolean updateGoods(String barcode, int quantity) {
		boolean isDone = false;
		ContentValues cv = new ContentValues();
		cv.put("quantity", quantity);
		try {
			db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
					userid, barcode });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDone;
	}

	/**
	 * 更新购物车中一条商品的价格
	 * 
	 * @param barcode
	 * @param price
	 * @return
	 */
	public boolean updateGoods(String barcode, double price) {
		boolean isDone = false;
		ContentValues cv = new ContentValues();
		cv.put("price", price);
		try {
			db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
					userid, barcode });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDone;
	}
	
	/**
	 * 更新购物车中一条商品的配送方式
	 * 
	 * @param barcode
	 * @param price
	 * @return
	 */
	public boolean updateDeliver(String barcode, int deliver) {
		boolean isDone = false;
		ContentValues cv = new ContentValues();
		cv.put("deliver", deliver);
		try {
			db.update(TB_NAME, cv, "userid=? and barcode = ?", new String[] {
					userid, barcode });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDone;
	}

	/**
	 * 向购物车中插入一条商品信息
	 * 
	 * @param barcode
	 *            商品条码
	 * @param name
	 *            商品名
	 * @param spec
	 *            规格
	 * @param price
	 *            价格
	 * @param quantity
	 *            数量
	 * @return 成功返回true
	 */
	public boolean insertGoods(String barcode, String name, String imagepath,
			String spec, double price, int quantity,int deliver) {
		boolean isDone = false;
		try {

			Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME
					+ " WHERE userid=? and barcode= ?", new String[] { userid,
					barcode });
			if (c.moveToNext()) {
				int curQuantity = c.getInt(c.getColumnIndex("quantity"));
				return updateGoods(barcode, curQuantity + quantity);
			}
			c.close();
			db.execSQL("INSERT INTO " + TB_NAME
					+ " VALUES (NULL, ?,?, ?,?,?,?,?,?,?,NULL)", new Object[] {
					barcode, name, imagepath, spec, price, quantity, false,
					userid,deliver });
			isDone = true;
			refreshCart();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return isDone;
	}

	/**
	 * 获取所有购物车信息的ArrayList
	 * 
	 * @return
	 */
	public ArrayList getAll() {
		ArrayList<CartGoods> goodsList = new ArrayList<CartGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME + " where userid=?",
				new String[] { userid });
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			int deliver = c.getInt(c.getColumnIndex("deliver"));
			CartGoods goods = new CartGoods(_id, barCode, name, imagepath,
					spec, price, quantity,deliver);
			
			goodsList.add(goods);
		}
		c.close();
		return goodsList;
	}

	/**
	 * 返回所有购物车信息的MAP
	 * 
	 * @return
	 */
	public LinkedHashMap<String, CartGoods> getAllByMap() {
		LinkedHashMap<String, CartGoods> goodsMap = new LinkedHashMap<String, CartGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME + " where userid=?",
				new String[] { userid });
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			int deliver = c.getInt(c.getColumnIndex("deliver"));
			CartGoods goods = new CartGoods(_id, barCode, name, imagepath,
					spec, price, quantity,deliver);
			goodsMap.put(barCode, goods);
		}
		c.close();
		return goodsMap;
	}
	/**
	 * 获取购物车商品总数
	 * @return
	 */
	public int getCount() {

		Cursor c = db.rawQuery("SELECT _id as count FROM " + TB_NAME
				+ " where userid=?", new String[] { userid });
		int count = c.getCount();
		return count;
	}

	/**
	 * 关闭购物车数据库
	 */
	public void Close() {
		db.close();
	}
	
	private void refreshCart(){
		context.sendBroadcast(new Intent(Constants.ACTION_SHOPCART_CHANGED));
	}
}
