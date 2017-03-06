package com.suntown.suntownshop.listener;
/**
 * 排序选择监听接口
 *
 * @author 钱凯
 * @version 2015年7月15日 下午3:20:47
 *
 */
public interface OnOrderbySelectListener {
	public void onOrderbySelect(int id,String name);
	public void onCancel();
}
