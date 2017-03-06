package com.suntown.suntownshop.model;

/**
 * 购物车商品类
 * 
 * @author Administrator
 *
 */
public class CartGoods {
	private int index;
	private String BarCode;
	private String Name;
	private String ImagePath;
	private double price;
	private int quantity;
	private String Spec;
	private boolean isChanged = false;
	private int priceState = 0;
	private int deliverType = 0;
	public final static int PRICE_STATE_NOCHANGE = 0;
	public final static int PRICE_STATE_UP = 1;
	public final static int PRICE_STATE_DOWN = 2;
	public final static int DEVILER_TYPE_SELF = 0;
	public final static int DEVILER_TYPE_MARKET = 1;

	public int getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(int deliverType) {
		this.deliverType = deliverType;
	}

	public int getPriceState() {
		return priceState;
	}

	public void setPriceState(int priceState) {
		this.priceState = priceState;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public String getImagePath() {
		return ImagePath;
	}

	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}

	public CartGoods() {
		super();
	}

	public CartGoods(int index, String barCode, String name, String imagePath,
			String spec, double price, int quantity, int deliverType) {
		this.index = index;
		this.BarCode = barCode;
		this.Name = name;
		this.ImagePath = imagePath;
		this.Spec = spec;
		this.price = price;
		this.quantity = quantity;
		this.deliverType = deliverType;
	}

	public String getSpec() {
		return Spec;
	}

	public void setSpec(String spec) {
		Spec = spec;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBarCode() {
		return BarCode;
	}

	public void setBarCode(String barCode) {
		BarCode = barCode;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
