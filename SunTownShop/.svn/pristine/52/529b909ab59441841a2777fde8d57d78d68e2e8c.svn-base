package com.suntown.suntownshop.model;

import com.suntown.suntownshop.utils.MyMath;

/**
 * 商品类
 * 
 * @author Administrator
 *
 */
public class Goods {
	private String barCode;
	private String gCode;
	private String name;
	private String kind;
	private String unit;
	private String oriPrice;
	private String MemPrice;
	private String UptPrice;
	private String Spec;
	private String GClass;
	private String Provider;
	private String Brand;
	private String Origin;
	private String ImgPath;
	private int priceType;
	private int deliverType = 0;
	private double evaluate;
	private String shelfId;
	private String floorNo;
	private String floorName;
	public Goods() {

	}

	public Goods(String barCode, String gCode, String gName, String kind,
			String unit, String oriPrice, String memPrice, String uptPrice,
			String spec, String gClass, String provider, String brand,
			String origin, String imgPath, int type,int deliverType,double evaluate,String shelfId,String floorNo,String floorName) {
		this.barCode = barCode;
		this.gCode = gCode;
		this.name = gName;
		this.kind = kind;
		this.unit = unit;
		this.oriPrice = oriPrice;
		this.MemPrice = memPrice;
		this.UptPrice = uptPrice;
		this.Spec = spec;
		this.GClass = gClass;
		this.Provider = provider;
		this.Brand = brand;
		this.Origin = origin;
		this.ImgPath = imgPath;
		this.priceType = type;
		this.deliverType =deliverType;
		this.evaluate = evaluate;
		this.shelfId = shelfId;
		this.floorNo = floorNo;
		this.floorName = floorName;
	}
	
	public Goods(String barCode, String gCode, String gName, String kind,
			String unit, String oriPrice, String memPrice, String uptPrice,
			String spec, String gClass, String provider, String brand,
			String origin, String imgPath, int type,int deliverType) {
		this.barCode = barCode;
		this.gCode = gCode;
		this.name = gName;
		this.kind = kind;
		this.unit = unit;
		this.oriPrice = oriPrice;
		this.MemPrice = memPrice;
		this.UptPrice = uptPrice;
		this.Spec = spec;
		this.GClass = gClass;
		this.Provider = provider;
		this.Brand = brand;
		this.Origin = origin;
		this.ImgPath = imgPath;
		this.priceType = type;
		this.deliverType =deliverType;
	}

	
	
	
	
	public String getgCode() {
		return gCode;
	}

	public void setgCode(String gCode) {
		this.gCode = gCode;
	}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}

	public double getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(double evaluate) {
		this.evaluate = evaluate;
	}

	public int getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(int deliverType) {
		this.deliverType = deliverType;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getGCode() {
		return gCode;
	}

	public void setGCode(String gCode) {
		this.gCode = gCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(String oriPrice) {
		oriPrice = oriPrice;
	}

	public String getMemPrice() {
		return MemPrice;
	}

	public void setMemPrice(String memPrice) {
		MemPrice = memPrice;
	}

	public String getUptPrice() {
		return UptPrice;
	}

	public void setUptPrice(String uptPrice) {
		UptPrice = uptPrice;
	}

	public String getSpec() {
		return Spec;
	}

	public void setSpec(String spec) {
		Spec = spec;
	}

	public String getGClass() {
		return GClass;
	}

	public void setGClass(String class1) {
		GClass = class1;
	}

	public String getProvider() {
		return Provider;
	}

	public void setProvider(String provider) {
		Provider = provider;
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getOrigin() {
		return Origin;
	}

	public void setOrigin(String origin) {
		Origin = origin;
	}

	public String getImgPath() {
		return ImgPath;
	}

	public void setImgPath(String imgPath) {
		ImgPath = imgPath;
	}
	/**
	 * 取得数值型原价
	 * @return
	 */
	public double getOriPriceInNumc(){
		double curOriPrice = oriPrice==null||"".equals(oriPrice)?0:Double.parseDouble(oriPrice);
		double memPrice = MemPrice==null||"".equals(MemPrice)?0:Double.parseDouble(MemPrice);
		double uptPrice = UptPrice==null||"".equals(UptPrice)?0:Double.parseDouble(UptPrice);
		if(priceType==2||priceType==0){
			return uptPrice;
		}else if(curOriPrice>=uptPrice){
			return curOriPrice;
		}else{
			return -1;
		}
	}
	
	public double getCurPrice(){
		double curOriPrice = oriPrice==null||"".equals(oriPrice)?0:Double.parseDouble(oriPrice);
		double memPrice = MemPrice==null||"".equals(MemPrice)?0:Double.parseDouble(MemPrice);
		double uptPrice = UptPrice==null||"".equals(UptPrice)?0:Double.parseDouble(UptPrice);
		double price = uptPrice;
		switch (priceType) {
		case 0:
		case 1:
		case 3:
			price = uptPrice > 0 ? uptPrice : curOriPrice;
			break;
		case 2:
			price = memPrice > 0 ? memPrice : uptPrice;
			break;
		}
		return price;
	}
	
	/**
	 * 获得当前实际价格，考虑vip价格 
	 * @param isVip
	 * @return
	 */
	public double getCurPrice(boolean isVip) {
		String[] prices = new String[] { oriPrice, MemPrice, UptPrice };
		double curOriPrice = oriPrice==null||"".equals(oriPrice)?0:Double.parseDouble(oriPrice);
		double curMemPrice = MemPrice==null||"".equals(MemPrice)?0:Double.parseDouble(MemPrice);
		double curUptPrice = UptPrice==null||"".equals(UptPrice)?0:Double.parseDouble(UptPrice);
		double price = curUptPrice;
		switch (priceType) {
		case 0:
		case 1:
		case 3:
			price = curUptPrice > 0 ? curUptPrice : curOriPrice;
			break;
		case 2:
			price = isVip && curMemPrice > 0 ? curMemPrice : curUptPrice;
			break;
		}
		return price;
	}

	public String getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	
	
}
