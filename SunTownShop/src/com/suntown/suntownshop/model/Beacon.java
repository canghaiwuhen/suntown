package com.suntown.suntownshop.model;

import java.util.Date;

/**
 * IBeacon�࣬��ʱʹ��mac��ַ��ǣ����������Ϊʹ��uuid��major��minor�����
 *
 * @author Ǯ��
 * @version 2015��5��21�� ����9:58:12
 *
 */
public class Beacon {
	private String address;
	private Date date;
	private int onpull;
	
	public Beacon(String address){
		this.address = address;
		this.date = new Date();
	}
	
	public Beacon(String address,Date date,int onpull){
		this.address = address;
		this.date = date;
		this.onpull = onpull;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public int getOnpull() {
		return onpull;
	}

	public void setOnpull(int onpull) {
		this.onpull = onpull;
	}
	
}
