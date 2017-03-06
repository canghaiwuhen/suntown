package com.suntown.suntownshop.utils;

import java.text.DecimalFormat;

import android.content.Context;

/**
 * 数字特殊操作类
 * 
 * @author Administrator
 *
 */
public class MyMath {
	/**
	 * 返回非零的最低值，如果三个价格都为0或空，则返回0
	 * 
	 * @param prices
	 * @return
	 */
	public static double getMiniNoZero(String[] prices) {
		double price = 0.0;
		for (String text : prices) {
			double curPrice = (text == null || "".equals(text)) ? 0.0 : Double
					.parseDouble(text);
			if ((curPrice > 0) && ((price > curPrice) || price == 0.0))
				price = curPrice;
		}
		// DecimalFormat df = new DecimalFormat("#.00");
		// return Double.valueOf(String.format("%.2f",
		// (double)(1+Math.random()*10)));

		return price;
	}

	public static double getCurPrice(String OriPrice,String MemPrice,String UptPrice,boolean isVip,int priceType) {
		double oriPrice = OriPrice==null||"".equals(OriPrice)?0:Double.parseDouble(OriPrice);
		double memPrice = MemPrice==null||"".equals(MemPrice)?0:Double.parseDouble(MemPrice);
		double uptPrice = UptPrice==null||"".equals(UptPrice)?0:Double.parseDouble(UptPrice);
		double price = oriPrice;
		switch (priceType) {
		case 0:
		case 1:
		case 3:
			price = uptPrice > 0 ? uptPrice : oriPrice;
			break;
		case 2:
			price = isVip && memPrice > 0 ? memPrice : uptPrice;
			break;
		}
		return price;
	}
	
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int getRandom(int min,int max){
		return (int)(min+Math.random()*(max-min+1));
	}
}
