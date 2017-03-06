package com.suntown.suntownshop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * ¶©µ¥Àà
 * 
 * @author Ç®¿­
 *
 */
public class Order {
	private String sid;
	private String storeName;
	private int payKind;
	private int payStatus;
	private int orderStatus;
	private int tStatus;
	private int stStatus;
	private String orderNo;
	private String date;
	private String owner;
	private double amount;
	private int evaStatus = 0;
	private LinkedHashMap<String, String> orderInfo;
	private ArrayList<OrderGoods> orderGoods;
	private ArrayList<OrderGoods> orderGoodsDBC; // Delivery Goods By Customer
	private ArrayList<OrderGoods> orderGoodsDBM; // Delivery Goods By Market
	public final static int EVA_STATUS_UNDO = 0;
	public final static int EVA_STATUS_DONE = 1;
	public final static int EVA_STATUS_CANTDO = 2;

	public Order() {

	}

	public Order(String orderNo, String date, String owner, double amount) {
		this.orderNo = orderNo;
		this.date = date;
		this.owner = owner;
		this.amount = amount;
	}

	public Order(String orderNo, String date, String owner, double amount,
			String sid, int payKind, int payStatus, int orderStatus,
			int tStatus, int stStatus, int evaStatus) {
		this.orderNo = orderNo;
		this.date = date;
		this.owner = owner;
		this.amount = amount;
		this.payKind = payKind;
		this.payStatus = payStatus;
		this.sid = sid;
		this.orderStatus = orderStatus;
		this.tStatus = tStatus;
		this.stStatus = stStatus;
		this.evaStatus = evaStatus;
	}

	public int getStStatus() {
		return stStatus;
	}

	public void setStStatus(int stStatus) {
		this.stStatus = stStatus;
	}

	public ArrayList<OrderGoods> getOrderGoodsDBC() {
		return orderGoodsDBC;
	}

	public void setOrderGoodsDBC(ArrayList<OrderGoods> orderGoodsDBC) {
		this.orderGoodsDBC = orderGoodsDBC;
	}

	public ArrayList<OrderGoods> getOrderGoodsDBM() {
		return orderGoodsDBM;
	}

	public void setOrderGoodsDBM(ArrayList<OrderGoods> orderGoodsDBM) {
		this.orderGoodsDBM = orderGoodsDBM;
	}

	public int getEvaStatus() {
		return evaStatus;
	}

	public void setEvaStatus(int evaStatus) {
		this.evaStatus = evaStatus;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int gettStatus() {
		return tStatus;
	}

	public void settStatus(int tStatus) {
		this.tStatus = tStatus;
	}

	public ArrayList<OrderGoods> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(ArrayList<OrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getPayKind() {
		return payKind;
	}

	public void setPayKind(int payKind) {
		this.payKind = payKind;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public LinkedHashMap<String, String> getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(LinkedHashMap<String, String> orderInfo) {
		this.orderInfo = orderInfo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
}
