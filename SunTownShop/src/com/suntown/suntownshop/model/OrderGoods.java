package com.suntown.suntownshop.model;

/**
 * 订单详细商品类
 * 
 * @author Administrator
 *
 */
public class OrderGoods extends CartGoods {
	private String orderNo;
	private float evaRate = 5;
	private String evaText = "";
	private String evaDate = "";

	public OrderGoods(int index, String orderNo, String barCode, String name,
			String imagePath, String spec, double price, int quantity,int deliverType) {
		setIndex(index);
		setBarCode(barCode);
		setName(name);
		setImagePath(imagePath);
		setSpec(spec);
		setPrice(price);
		setQuantity(quantity);
		setDeliverType(deliverType);
		this.orderNo = orderNo;
	}

	public float getEvaRate() {
		return evaRate;
	}

	public void setEvaRate(float evaRate) {
		this.evaRate = evaRate;
	}

	public String getEvaText() {
		return evaText;
	}

	public void setEvaText(String evaText) {
		this.evaText = evaText;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getEvaDate() {
		return evaDate;
	}

	public void setEvaDate(String evaDate) {
		this.evaDate = evaDate;
	}

	
}
