package com.suntown.suntownshop.model;

/**
 * 收藏商品类
 * 
 * @author ken
 *
 */
public class FavoriteGoods extends CartGoods {
	private String shelfId;
	private String floorName;

	public FavoriteGoods(String barCode, String name, String spec,
			String imagePath, double price, String shelfId, String floorName) {
		setBarCode(barCode);
		setName(name);
		setSpec(spec);
		setImagePath(imagePath);
		setPrice(price);
		this.shelfId = shelfId;
		this.floorName = floorName;
	}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

}
