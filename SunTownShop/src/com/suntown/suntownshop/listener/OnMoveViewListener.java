package com.suntown.suntownshop.listener;

import com.suntown.suntownshop.model.Goods;

import android.view.View;
/**
 * 移动动画监听接口
 *
 * @author 钱凯
 * @version 2015年7月3日 下午1:53:55
 *
 */
public interface OnMoveViewListener {
	public void onMove(View v,Goods goods);
}
