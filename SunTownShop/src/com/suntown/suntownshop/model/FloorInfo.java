package com.suntown.suntownshop.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.StopGraphic;

/**
 * 楼层信息类
 *
 * @author 钱凯
 * @version 2015年6月5日 下午5:16:57
 *
 */
public class FloorInfo {
	// 楼层名
	private String name;
	// 对应的滑动按钮ID
	private int id;
	//对应的图层
	//private ArcGISTiledMapServiceLayer baseLayer;
	private ArcGISDynamicMapServiceLayer baseLayer;
	//导航路线图层
	private GraphicsLayer routeLayer;
	//标记图层
	private MapViewHelper markLayer;
	private String routeServerPath;
	private String locatorServerPath;
	private HashMap<String, ArrayList<ParcelableGoods>> goodsMap;
	private ArrayList<LocatorGeocodeResult> shelfList;
	private StopGraphic[] points;
	private RouteResult routeResult;
	private String startPoint;
	private String endPoint;
	private String floorNo;
	public FloorInfo(String name, int id,String floorNo) {
		this.name = name;
		this.id = id;
		this.floorNo = floorNo;
		this.goodsMap = new HashMap<String, ArrayList<ParcelableGoods>>();
		this.shelfList = new ArrayList<LocatorGeocodeResult>();
	}

	public void addGoods(String key,ParcelableGoods goods){
		ArrayList<ParcelableGoods> list = goodsMap.get(key);
		if(list==null){
			list = new ArrayList<ParcelableGoods>();
		}
		list.add(goods);
		goodsMap.put(key, list);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArcGISDynamicMapServiceLayer getBaseLayer() {
		return baseLayer;
	}

	public void setBaseLayer(ArcGISDynamicMapServiceLayer baseLayer) {
		this.baseLayer = baseLayer;
	}

	public GraphicsLayer getRouteLayer() {
		return routeLayer;
	}

	public void setRouteLayer(GraphicsLayer routeLayer) {
		this.routeLayer = routeLayer;
	}

	public String getRouteServerPath() {
		return routeServerPath;
	}

	public void setRouteServerPath(String routeServerPath) {
		this.routeServerPath = routeServerPath;
	}

	public String getLocatorServerPath() {
		return locatorServerPath;
	}

	public void setLocatorServerPath(String locatorServerPath) {
		this.locatorServerPath = locatorServerPath;
	}

	public MapViewHelper getMarkLayer() {
		return markLayer;
	}

	public void setMarkLayer(MapViewHelper markLayer) {
		this.markLayer = markLayer;
	}

	public HashMap<String, ArrayList<ParcelableGoods>> getGoodsMap() {
		return goodsMap;
	}

	public void setGoodsMap(HashMap<String, ArrayList<ParcelableGoods>> goodsMap) {
		this.goodsMap = goodsMap;
	}

	public ArrayList<LocatorGeocodeResult> getShelfList() {
		return shelfList;
	}

	public void setShelfList(ArrayList<LocatorGeocodeResult> shelfList) {
		this.shelfList = shelfList;
	}

	public StopGraphic[] getPoints() {
		return points;
	}

	public void setPoints(StopGraphic[] points) {
		this.points = points;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public RouteResult getRouteResult() {
		return routeResult;
	}

	public void setRouteResult(RouteResult routeResult) {
		this.routeResult = routeResult;
	}

	public String getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}

	
}
