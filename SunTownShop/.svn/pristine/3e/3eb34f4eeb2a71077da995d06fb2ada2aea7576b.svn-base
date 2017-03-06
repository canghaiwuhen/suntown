package com.suntown.suntownshop.model;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 商品类目类
 * @author Administrator
 *
 */
public class Category {
	private int catID;
	private String name;
	private String icon;
	private LinkedHashMap<Integer, Category> subCategorys;
	private int subCatsNum;

	public Category(int catID, String name, String icon) {
		this.catID = catID;
		this.name = name;
		this.icon = icon;
		subCatsNum = 0;
	}

	public void AddSubCategory(Category cat) {
		if (subCatsNum == 0) {
			subCategorys = new LinkedHashMap<Integer, Category>();
		}
		subCategorys.put(cat.getCatID(), cat);
		subCatsNum++;
	}

	public String getSubCatNames() {
		StringBuilder sb = new StringBuilder();
		if (subCategorys != null) {
			Set<Entry<Integer, Category>> sets = subCategorys.entrySet();
			for (Entry<Integer, Category> entry : sets) {
				Category category = entry.getValue();
				sb.append(category.getName());
				sb.append("/");
			}
		}
		return sb.toString();
	}

	public LinkedHashMap<Integer, Category> getSubCategorys() {
		return subCategorys;
	}

	public void setSubCategorys(LinkedHashMap<Integer, Category> subCategorys) {
		this.subCategorys = subCategorys;
	}

	public int getSubCategorysCount() {
		return subCatsNum;
	}

	public int getCatID() {
		return catID;
	}

	public void setCatID(int catID) {
		this.catID = catID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
