package com.suntown.suntownshop.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.model.OrderGoods;
import com.suntown.suntownshop.utils.FileManager;
/**
 * 订单和订单详细商品数据库操作类
 * @author Administrator
 *
 */
public class OrderDb {
	private  static String DB_NAME ;
	private final static String TB_NAME_ORDER = "orders";
	private final static String TB_NAME_GOODS = "ordergoods";
	private SQLiteDatabase db;
	private Context mContext;
	public OrderDb(Context context) {
		this.mContext = context;
		DB_NAME = FileManager.getDataPath(context)
				+ "/suntownshop.db";
		db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		
		db.execSQL("create table if not exists "
				+ TB_NAME_ORDER
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,orderno VARCHAR, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,name VARCHAR,amount integer)");
		db.execSQL("create table if not exists "
				+ TB_NAME_GOODS
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,orderno VARCHAR, barcode VARCHAR,name VARCHAR,imagepath VARCHAR,spec VARCHAR,price integer,quantity integer)");
	}
	
	public void insertOrder(String orderno, String name,double amount) {
		try {

			Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME_ORDER
					+ " WHERE orderno= ?", new String[] { orderno });
			if (c.moveToNext()) {
				c.close();
				return;
			}
			c.close();
			ContentValues cv = new ContentValues();
			cv.put("orderno", orderno);
			cv.put("name", name);
			cv.put("amount", amount);
			db.insert(TB_NAME_ORDER, null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void insertGoods(String orderno, String barcode, String name,
			String imagepath, String spec, double price, int quantity) {
		try {

			Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME_GOODS
					+ " WHERE orderno= ? and barcode= ?", new String[] {
					orderno, barcode });
			if (c.moveToNext()) {
				c.close();
				return;
			}
			c.close();
			db.execSQL("INSERT INTO " + TB_NAME_GOODS
					+ " VALUES (NULL, ?, ?,?,?,?,?,?)", new Object[] { orderno,
					barcode, name, imagepath, spec, price, quantity });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Order getOrderByOrderNo(String orderNo){
		Order order = null;
		Cursor c = db.rawQuery(
				"SELECT name,datetime(timestamp,'localtime'),amount FROM "
						+ TB_NAME_ORDER + " where orderno=?",
				new String[] { orderNo });
		if(c.moveToNext()){
			String name = c.getString(c.getColumnIndex("name"));
			String date = c.getString(c
					.getColumnIndex("datetime(timestamp,'localtime')"));
			double amount = c.getDouble(c.getColumnIndex("amount"));
			
			order = new Order(orderNo, date, name,amount);
		}
		return order;
	}
	
	public ArrayList<Order> getOrdersByName(String name) {
		ArrayList<Order> list = new ArrayList<Order>();
		Cursor c = db.rawQuery(
				"SELECT orderno,datetime(timestamp,'localtime'),amount FROM "
						+ TB_NAME_ORDER + " where name=? order by timestamp desc",
				new String[] { name });
		while (c.moveToNext()) {
			String orderno = c.getString(c.getColumnIndex("orderno"));
			String date = c.getString(c
					.getColumnIndex("datetime(timestamp,'localtime')"));
			double amount = c.getDouble(c.getColumnIndex("amount"));
			Order order = new Order(orderno, date, name,amount);
			list.add(order);
		}
		c.close();
		return list;
	}

	public ArrayList<OrderGoods> getGoodsByOrderNo(String orderNo) {
		ArrayList<OrderGoods> list = new ArrayList<OrderGoods>();
		Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME_GOODS
				+ " where orderno=?", new String[] { orderNo });
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));  
			String barCode = c.getString(c.getColumnIndex("barcode"));
			String name = c.getString(c.getColumnIndex("name"));
			String spec = c.getString(c.getColumnIndex("spec"));
			String imagepath = c.getString(c.getColumnIndex("imagepath"));
			double price = c.getDouble(c.getColumnIndex("price"));
			int quantity = c.getInt(c.getColumnIndex("quantity"));
			OrderGoods goods = new OrderGoods(_id, orderNo, barCode, name, imagepath, spec, price, quantity,0);
			list.add(goods);
		}
		c.close();
		return list;
	}
	
	public void close(){
		db.close();
	}
}
