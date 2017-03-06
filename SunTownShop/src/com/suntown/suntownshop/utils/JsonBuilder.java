package com.suntown.suntownshop.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Coupon;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.model.OrderGoods;

/**
 * JSON字符串生成类
 * 
 * @author 钱凯
 *
 */
public class JsonBuilder {
	/**
	 * 根据传入的MAP生成所有BARCODE的JSON字符串
	 * 
	 * @param map
	 * @return
	 * @throws JSONException
	 */
	public static String makeBarcodes(LinkedHashMap<String, CartGoods> list)
			throws JSONException {
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		Set<Entry<String, CartGoods>> sets = list.entrySet();
		for (Entry<String, CartGoods> entry : sets) {
			String barCode = entry.getKey();
			jsonMember = new JSONObject();
			jsonMember.put("BARCODE", barCode);
			jsonMembers.put(jsonMember);
		}
		return jsonMembers.toString();
	}

	/**
	 * 根据传入的MAP生成提交订单的JSON字符串
	 * 
	 * @param formno
	 *            订单号
	 * @param sid
	 *            店铺ID
	 * @param memid
	 *            会员ID
	 * @param money
	 *            总金额
	 * @param paykind
	 *            支付方式
	 * @param list
	 *            订单内容
	 * @return
	 * @throws JSONException
	 */
	public static String makeOrderJson(String formno, String sid, String memid,
			String money, String paykind, LinkedHashMap<String, CartGoods> list)
			throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("FORMNO", formno);
		jsonObj.put("SID", sid);
		jsonObj.put("MEMID", memid);
		jsonObj.put("MONEY", money);
		jsonObj.put("PAYKIND", paykind);
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		Set<Entry<String, CartGoods>> sets = list.entrySet();
		for (Entry<String, CartGoods> entry : sets) {
			String barCode = entry.getKey();
			CartGoods goods = entry.getValue();
			jsonMember = new JSONObject();
			jsonMember.put("BARCODE", barCode);
			jsonMember.put("PRICE", String.valueOf(goods.getPrice()));
			jsonMember.put("NUM", String.valueOf(goods.getQuantity()));
			jsonMembers.put(jsonMember);
		}
		jsonObj.put("RECORD", jsonMembers);
		return jsonObj.toString();
	}

	/**
	 * 根据传入的ArrayList生成提交订单的JSON字符串
	 * 
	 * @param formno
	 * @param sid
	 * @param memid
	 * @param paykind
	 * @param list
	 * @return
	 * @throws JSONException
	 */
	public static String makeOrderJson(String formno, String sid, String memid,
			String paykind, int addrid, LinkedHashMap<String,Coupon> coupons,
			ArrayList<CartGoods> list) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		double money = 0.0;
		double discount = 0.0;
		for (CartGoods goods : list) {
			jsonMember = new JSONObject();
			jsonMember.put("BARCODE", goods.getBarCode());
			jsonMember.put("PRICE", String.valueOf(goods.getPrice()));
			jsonMember.put("NUM", String.valueOf(goods.getQuantity()));
			jsonMember.put("DTYPE", String.valueOf(goods.getDeliverType()));
			jsonMembers.put(jsonMember);
			money += (goods.getPrice() * goods.getQuantity());
		}
		jsonObj.put("RECORD", jsonMembers);
		jsonMembers = new JSONArray();
		Set<Entry<String, Coupon>> sets = coupons
				.entrySet();
		for (Entry<String, Coupon> entry : sets) {
			Coupon coupon = entry.getValue();
			jsonMember = new JSONObject();
			jsonMember.put("TNO", coupon.getId());
			jsonMembers.put(jsonMember);
			discount += coupon.getDenomination();
			
		}
		jsonObj.put("TICKET", jsonMembers);
		jsonObj.put("FORMNO", formno);
		jsonObj.put("SID", sid);
		jsonObj.put("MEMID", memid);
		double amount = money-discount;
		if(amount<0){
			amount = 0;
		}
		jsonObj.put("MONEY", String.format("%.2f", amount));
		jsonObj.put("PAYKIND", paykind);
		jsonObj.put("ADDRID", String.valueOf(addrid));
		return jsonObj.toString();
	}

	/**
	 * 生成评价内容JSON
	 * 
	 * @param order
	 * @param memid
	 * @param token
	 * @return
	 * @throws JSONException
	 */
	public static String makeEvaluateJson(Order order, String memid,
			String token) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("FORMNO", order.getOrderNo());
		jsonObj.put("MEMID", memid);
		jsonObj.put("TOKEN", token);
		ArrayList<OrderGoods> goodsList = order.getOrderGoods();
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		for (OrderGoods goods : goodsList) {
			jsonMember = new JSONObject();
			jsonMember.put("BARCODE", goods.getBarCode());
			jsonMember.put("STAR", goods.getEvaRate());
			jsonMember.put("EVATXT", goods.getEvaText());
			jsonMembers.put(jsonMember);
		}
		jsonObj.put("LISTCOMMENT", jsonMembers);
		return jsonObj.toString();
	}

	/**
	 * 生成退货信息JSON
	 * 
	 * @param orderNo
	 * @param uId
	 * @param token
	 * @param refundTxt
	 * @param refundImgList
	 * @return
	 * @throws JSONException
	 */
	public static String makeRefundJson(String orderNo, String uId,
			String token, String refundTxt, ArrayList<String> refundImgList)
			throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("formno", orderNo);
		jsonObj.put("memid", uId);
		jsonObj.put("logintoken", token);
		jsonObj.put("rebacktxt", refundTxt);
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		for (String path : refundImgList) {
			if (path != null && !"".equals(path)) {
				String content = FileManager.getFileBase64(path);
				System.out.println(content);
				if (content != null && !"".equals(content)) {
					jsonMember = new JSONObject();
					jsonMember.put("img", content);
					jsonMembers.put(jsonMember);
				}
			}
		}
		jsonObj.put("listimage", jsonMembers);
		return jsonObj.toString();
	}
	/**
	 * 生成数据分析Json
	 * @param dataType	数据类型 0:过期的购物车数据
	 * @param memid	
	 * @param list
	 * @return
	 * @throws JSONException
	 */
	public static String makeDAJson(int dataType, String memid,
			ArrayList<CartGoods> list) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("datype", String.valueOf(dataType));
		jsonObj.put("memid", memid);
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		for (CartGoods goods : list) {
			jsonMember = new JSONObject();
			jsonMember.put("barcode", goods.getBarCode());
			jsonMembers.put(jsonMember);
		}
		jsonObj.put("list", jsonMembers);
		return jsonObj.toString();
	}
	/**
	 * 生成删除预购清单内商品的JSON数据
	 * @param userId
	 * @param barcodes
	 * @return
	 * @throws JSONException
	 */
	public static String makeDelPrepare(String userId,String[] barcodes) throws JSONException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("memid", userId);
		JSONArray jsonMembers = new JSONArray();
		JSONObject jsonMember;
		for(String barcode:barcodes){
			jsonMember = new JSONObject();
			jsonMember.put("barcode", barcode);
			jsonMembers.put(jsonMember);
		}
		jsonObj.put("list", jsonMembers);
		return jsonObj.toString();
		
	}
}
