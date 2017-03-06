package com.suntown.suntownshop.model;
/**
 * 收货信息类
 *
 * @author 钱凯
 * @version 2015年4月14日 下午12:40:39
 *
 */
public class Receiver {
	private int id;
	private String memid;
	private String name;
	private String phone;
	private String address;
	private boolean isDefault;
	
	
	public Receiver(int id, String memid, String name, String phone,
			String address,boolean isDefault) {
		this.id = id;
		this.memid = memid;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.isDefault = isDefault;
	}

	
	public boolean isDefault() {
		return isDefault;
	}


	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMemid() {
		return memid;
	}

	public void setMemid(String memid) {
		this.memid = memid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
