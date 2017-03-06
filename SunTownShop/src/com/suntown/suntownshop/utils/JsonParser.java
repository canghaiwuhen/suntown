package com.suntown.suntownshop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntown.suntownshop.model.Category;
import com.suntown.suntownshop.model.Coupon;
import com.suntown.suntownshop.model.Evaluate;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.model.OrderGoods;
import com.suntown.suntownshop.model.ParcelableGoods;

/**
 * JSON字符串解析类
 * 
 * @author Administrator
 *
 */
public class JsonParser {
	public final static int KIND_L = 1;
	public final static int KIND_M = 2;
	public final static int KIND_S = 3;
	private final static String ROWS_L = "LROWS";
	private final static String ROWS_M = "MROWS";
	private final static String ROWS_S = "SROWS";
	private final static String RECORD_L = "LRECORD";
	private final static String RECORD_M = "MRECORD";
	private final static String RECORD_S = "SRECORD";
	private final static String KID_L = "LKID";
	private final static String KID_M = "MKID";
	private final static String KID_S = "SKID";
	private final static String KNAME_L = "LKNAME";
	private final static String KNAME_M = "MKNAME";
	private final static String KNAME_S = "SKNAME";

	/**
	 * 商品类目JSON解析入口
	 * 
	 * @param json
	 *            商品类目JSON字符串
	 * @return
	 * @throws JSONException
	 */
	public static LinkedHashMap<Integer, Category> CategoryParse(String json)
			throws JSONException {
		try {
			JSONObject jsonObj = new JSONObject(json);
			int subrows = jsonObj.getInt(ROWS_L);
			if (subrows > 0) {
				JSONArray jsonArray = jsonObj.getJSONArray(RECORD_L);
				return CategoryParse(jsonArray, KIND_L);
			} else {
				return null;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 差异化子类解析，递归查询
	 * 
	 * @param jsonArray
	 *            子类JSON
	 * @param kind
	 *            子类级别
	 * @return 返回所有子类的MAP
	 * @throws JSONException
	 */
	private static LinkedHashMap<Integer, Category> CategoryParse(
			JSONArray jsonArray, int kind) throws JSONException {
		String rows = ROWS_M;
		String records = RECORD_M;
		String kid = KID_L;
		String kname = KNAME_L;
		switch (kind) {
		case KIND_L:
			rows = ROWS_M;
			records = RECORD_M;
			kid = KID_L;
			kname = KNAME_L;
			break;
		case KIND_M:
			rows = ROWS_S;
			records = RECORD_S;
			kid = KID_M;
			kname = KNAME_M;
			break;
		case KIND_S:
			rows = ROWS_S;
			records = RECORD_S;
			kid = KID_S;
			kname = KNAME_S;
			break;
		}
		LinkedHashMap<Integer, Category> catHashMap = new LinkedHashMap<Integer, Category>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.opt(i);
			// System.out.println("obj----->"+jsonObj.toString());
			// System.out.println("kid---->"+kid);
			int catID = jsonObj.getInt(kid);
			String name = jsonObj.getString(kname);
			String icon = "";
			if (kid.equals(KID_L)) {
				icon = jsonObj.getString("IMGPATH"); // 分类图片，保留
			}
			Category cat = new Category(catID, name, icon);
			int subrows = kind > KIND_M ? 0 : jsonObj.getInt(rows);
			if (subrows > 0) {
				JSONArray subArray = jsonObj.getJSONArray(records);
				LinkedHashMap<Integer, Category> subHashMap = CategoryParse(
						subArray, kind + 1);
				cat.setSubCategorys(subHashMap);
			}
			catHashMap.put(catID, cat);
		}
		return catHashMap;
	}

	/**
	 * 使用于各类目级别命名无差异化的JSON解析，不需传人子类级别
	 * 
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	private static LinkedHashMap<Integer, Category> CategoryParse(
			JSONArray jsonArray) throws JSONException {
		LinkedHashMap<Integer, Category> catHashMap = new LinkedHashMap<Integer, Category>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.opt(i);
			int catID = jsonObj.getInt("CatID");
			String name = jsonObj.getString("CatName");
			String icon = jsonObj.getString("ICon");
			Category cat = new Category(catID, name, icon);
			String subCat = jsonObj.getString("SubCategories");
			if (subCat != null && !subCat.equals("null")) {
				JSONArray subArray = jsonObj.getJSONArray("SubCategories");
				LinkedHashMap<Integer, Category> subHashMap = CategoryParse(subArray);
				cat.setSubCategorys(subHashMap);
			}
			catHashMap.put(catID, cat);
		}
		return catHashMap;
	}

	/**
	 * 订单列表JSON解析
	 * 
	 * @param jsonOrders
	 *            所有订单的JSONArray
	 * @return
	 * @throws JSONException
	 * @throws ParseException
	 */
	public static ArrayList<Order> ordersParse(JSONArray jsonOrders)
			throws JSONException, ParseException {
		ArrayList<Order> list = new ArrayList<Order>();

		if (jsonOrders == null || jsonOrders.length() == 0)
			return list;
		int ordersCount = jsonOrders.length();
		String sid;
		int payKind;
		int payStatus;
		int orderStatus;
		int tStatus;
		String orderNo;
		String date;
		String owner;
		String storeName;
		double amount;
		Order order;
		JSONObject jsonOrder;

		for (int i = 0; i < ordersCount; i++) {
			jsonOrder = (JSONObject) jsonOrders.opt(i);
			sid = jsonOrder.getString("SID");
			payKind = jsonOrder.getInt("PAYKIND");
			payStatus = jsonOrder.getInt("PAYSTATUS");
			orderStatus = jsonOrder.getInt("FORMSTATUS");
			tStatus = jsonOrder.getInt("TSTATUS");
			orderNo = jsonOrder.getString("FORMNO");
			date = jsonOrder.getString("ADDDATE");
			storeName = jsonOrder.getString("FNAME");
			SimpleDateFormat formatDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			date = formatDate.format(formatDate.parse(date));
			owner = jsonOrder.getString("MEMID");
			amount = jsonOrder.getDouble("MONEY");
			int evaStatus = jsonOrder.getInt("EVASTATUS");
			int stStatus = jsonOrder.getInt("SHIPTSTATUS");
			order = new Order(orderNo, date, owner, amount, sid, payKind,
					payStatus, orderStatus, tStatus, stStatus, evaStatus);
			LinkedHashMap<String, String> orderInfo = new LinkedHashMap<String, String>();
			JSONArray jsonOrderInfos = jsonOrder.getJSONArray("ORDERINFO");
			for (int j = 0; j < jsonOrderInfos.length(); j++) {
				JSONObject info = (JSONObject) jsonOrderInfos.opt(j);
				String barCode = info.getString("BARCODE");
				String imgPath = info.getString("IMGPATH");
				orderInfo.put(barCode, imgPath);
			}
			order.setOrderInfo(orderInfo);
			order.setStoreName(storeName);
			list.add(order);
		}
		return list;
	}

	public static Order orderParse(JSONObject jsonObj) throws JSONException,
			ParseException {
		String sid = jsonObj.getString("SID");
		String storeName= jsonObj.getString("FNAME");
		int payKind = jsonObj.getInt("PAYKIND");
		int payStatus = jsonObj.getInt("PAYSTATUS");
		int orderStatus = jsonObj.getInt("FORMSTATUS");
		int tStatus = jsonObj.getInt("TSTATUS");
		String orderNo = jsonObj.getString("FORMNO");
		String date = jsonObj.getString("ADDDATE");
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		date = formatDate.format(formatDate.parse(date));
		String owner = jsonObj.getString("MEMID");
		double amount = jsonObj.getDouble("MONEY");
		int evaStatus = jsonObj.getInt("EVASTATUS");
		int stStatus = jsonObj.getInt("SHIPTSTATUS");
		Order order = new Order(orderNo, date, owner, amount, sid, payKind,
				payStatus, orderStatus, tStatus, stStatus, evaStatus);
		order.setStoreName(storeName);
		JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
		ArrayList<OrderGoods> list = new ArrayList<OrderGoods>();
		ArrayList<OrderGoods> listDBC = new ArrayList<OrderGoods>();
		ArrayList<OrderGoods> listDBM = new ArrayList<OrderGoods>();
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObj = (JSONObject) jsonArray.opt(i);
			OrderGoods goods = new OrderGoods(i, orderNo,
					jsonObj.getString("BARCODE"), jsonObj.getString("GNAME"),
					jsonObj.getString("IMGPATH"), jsonObj.getString("SPEC"),
					jsonObj.getDouble("PRICE"), jsonObj.getInt("NUM"),
					jsonObj.getInt("DTYPE"));
			int rate = jsonObj.getInt("CLASS");
			if (rate > 0) {
				goods.setEvaRate(rate);
			}
			goods.setEvaText(jsonObj.getString("EVATXT"));
			list.add(goods);
			if (goods.getDeliverType() == 0) {
				listDBC.add(goods);
			} else {
				listDBM.add(goods);
			}
		}
		order.setOrderGoods(list);
		order.setOrderGoodsDBC(listDBC);
		order.setOrderGoodsDBM(listDBM);
		return order;
	}

	public static ArrayList<Evaluate> evaluateParse(JSONArray jsonEvas)
			throws JSONException {
		ArrayList<Evaluate> list = new ArrayList<Evaluate>();
		if (jsonEvas == null || jsonEvas.length() == 0)
			return list;
		int count = jsonEvas.length();
		int id;
		String nickname;
		String avatar;
		int rate;
		String evaText;
		String evaDate;
		String memid;
		Evaluate eva;
		JSONObject jsonEva;

		for (int i = 0; i < count; i++) {
			jsonEva = (JSONObject) jsonEvas.opt(i);
			id = jsonEva.getInt("ID");
			nickname = jsonEva.getString("NICKNAME");
			avatar = jsonEva.getString("AVATAR");
			rate = jsonEva.getInt("CLASS");
			evaText = jsonEva.getString("EVATXT");
			evaDate = jsonEva.getString("ADDDATE");
			memid = jsonEva.getString("MEMID");
			eva = new Evaluate(id, memid, nickname, avatar, rate, evaText,
					evaDate);
			list.add(eva);
		}
		return list;
	}

	/**
	 * 优惠券列表JSON解析
	 * 
	 * @param jsonOrders
	 *            所有优惠券的JSONArray
	 * @return
	 * @throws JSONException
	 * @throws ParseException
	 */
	public static ArrayList<Coupon> couponsParse(JSONArray jsonOrders)
			throws JSONException, ParseException {
		ArrayList<Coupon> list = new ArrayList<Coupon>();

		if (jsonOrders == null || jsonOrders.length() == 0)
			return list;
		int ordersCount = jsonOrders.length();
		String id;
		double money;
		String endDate;
		String useDate;
		String strCause;
		String typeName;
		int couponType;
		int sendCause;
		Coupon coupon;
		JSONObject jsonOrder;

		for (int i = 0; i < ordersCount; i++) {
			jsonOrder = (JSONObject) jsonOrders.opt(i);
			id = jsonOrder.getString("TNO");
			endDate = jsonOrder.getString("ENDDATE");
			useDate = jsonOrder.getString("USEDATE");
			strCause = jsonOrder.getString("SENDCAUSE");

			if (strCause != null && !"".equals(strCause)) {
				sendCause = jsonOrder.getInt("SENDCAUSE");
			} else {
				sendCause = -1;
			}
			money = jsonOrder.getDouble("MONEY");
			couponType = jsonOrder.getInt("TICKETTYPE");
			typeName = jsonOrder.getString("TYPENAME");
			coupon = new Coupon(id, money, "", endDate, useDate, sendCause,
					couponType, typeName);
			list.add(coupon);
		}
		return list;
	}
	/**
	 * 商品数据解析，返回序列化的商品列表，以便在Activity之间传递
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<ParcelableGoods> pGoodsParse(JSONArray jsonArray)
			throws JSONException {
		ArrayList<ParcelableGoods> list = new ArrayList<ParcelableGoods>();
		JSONObject jsonObj;
		String barCode;
		String gCode;
		String gName;
		String gKind;
		String gUnit;
		String gOriPrice;
		String gMemPrice;
		String gUptPrice;
		String gSpec;
		String gClass;
		String gProvider;
		String gBrand;
		String gOrigin;
		String gImgPath;
		int priceType;
		double evaluate;
		int deliverType;
		String shelfId;
		String floorNo;
		String floorName;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObj = (JSONObject) jsonArray.opt(i);
			barCode = jsonObj.getString("BARCODE");
			gCode = jsonObj.getString("GCODE");
			gName = jsonObj.getString("GNAME");
			gKind = jsonObj.getString("KIND");
			gUnit = jsonObj.getString("UNIT");
			gOriPrice = jsonObj.getString("ORIPRICE");
			gMemPrice = jsonObj.getString("MEMPRICE");
			gUptPrice = jsonObj.getString("UPTPRICE");
			gSpec = jsonObj.getString("SPEC");
			gClass = jsonObj.getString("GCLASS");
			gProvider = jsonObj.getString("PROVIDER");
			gBrand = jsonObj.getString("BRAND");
			gOrigin = jsonObj.getString("ORIGIN");
			gImgPath = jsonObj.getString("IMGPATH");
			priceType = jsonObj.getInt("PRICETYPE");
			evaluate = jsonObj.getDouble("AVERAGE");
			deliverType = jsonObj.getInt("DELIVERYMODE");
			shelfId = jsonObj.getString("SFID");
			floorNo = jsonObj.getString("FLOORNO");
			floorName = jsonObj.getString("FLOORNAME");
			ParcelableGoods goods = new ParcelableGoods(barCode, gCode, gName, gKind, gUnit,
					gOriPrice, gMemPrice, gUptPrice, gSpec, gClass, gProvider,
					gBrand, gOrigin, gImgPath, priceType, deliverType,evaluate,shelfId,floorNo,floorName);

			list.add(goods);
		}
		return list;
	}
	/**
	 * 商品数据解析
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<Goods> goodsParse(JSONArray jsonArray)
			throws JSONException {
		ArrayList<Goods> list = new ArrayList<Goods>();
		JSONObject jsonObj;
		String barCode;
		String gCode;
		String gName;
		String gKind;
		String gUnit;
		String gOriPrice;
		String gMemPrice;
		String gUptPrice;
		String gSpec;
		String gClass;
		String gProvider;
		String gBrand;
		String gOrigin;
		String gImgPath;
		int priceType;
		double evaluate;
		int deliverType;
		String shelfId;
		String floorNo;
		String floorName;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObj = (JSONObject) jsonArray.opt(i);
			barCode = jsonObj.getString("BARCODE");
			gCode = jsonObj.getString("GCODE");
			gName = jsonObj.getString("GNAME");
			gKind = jsonObj.getString("KIND");
			gUnit = jsonObj.getString("UNIT");
			gOriPrice = jsonObj.getString("ORIPRICE");
			gMemPrice = jsonObj.getString("MEMPRICE");
			gUptPrice = jsonObj.getString("UPTPRICE");
			gSpec = jsonObj.getString("SPEC");
			gClass = jsonObj.getString("GCLASS");
			gProvider = jsonObj.getString("PROVIDER");
			gBrand = jsonObj.getString("BRAND");
			gOrigin = jsonObj.getString("ORIGIN");
			gImgPath = jsonObj.getString("IMGPATH");
			priceType = jsonObj.getInt("PRICETYPE");
			evaluate = jsonObj.getDouble("AVERAGE");
			deliverType = jsonObj.getInt("DELIVERYMODE");
			shelfId = jsonObj.getString("SFID");
			floorNo = jsonObj.getString("FLOORNO");
			floorName = jsonObj.getString("FLOORNAME");
			Goods goods = new Goods(barCode, gCode, gName, gKind, gUnit,
					gOriPrice, gMemPrice, gUptPrice, gSpec, gClass, gProvider,
					gBrand, gOrigin, gImgPath, priceType, deliverType,evaluate,shelfId,floorNo,floorName);

			list.add(goods);
		}
		return list;
	}
}
