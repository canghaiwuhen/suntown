package com.suntown.suntownshop.model;
/**
 * �����켣�࣬δʹ��
 *
 * @author Ǯ��
 * @version 2015��9��21�� ����10:01:36
 *
 */
public class Path {
	private String msg;
	private String time;

	public Path(String msg, String time) {
		this.msg = msg;
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
