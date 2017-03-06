package com.suntown.suntownshop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureFillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteDirection;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;
import com.suntown.suntownshop.arcgis.view.Compass;
import com.suntown.suntownshop.model.FloorInfo;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.MyMath;
import com.suntown.suntownshop.widget.SynHorizontalScrollView;
import com.suntown.suntownshop.widget.SynHorizontalScrollView.OnScrollChangedListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
/**
 * 地图显示页面
 *
 * @author 钱凯
 * @version 2015年5月26日 下午1:20:17
 *
 */
public class MapActivity extends Activity implements OnStatusChangedListener,
		OnCheckedChangeListener, OnScrollChangedListener, OnTouchListener {
	private static MapView map;
	private MapViewHelper mMapViewHelper;
	GraphicsLayer routeLayer, hiddenSegmentsLayer, routeDirectionLayer;
	LocationDisplayManager ldm;
	RouteTask mRouteTask = null;
	RouteResult mResults = null;
	// Current route, route summary, and gps location
	Route curRoute = null;

	// Symbol used to make route segments "invisible"
	SimpleLineSymbol segmentHider = new SimpleLineSymbol(Color.WHITE, 5);
	// Symbol used to highlight route segments
	SimpleLineSymbol segmentShower = new SimpleLineSymbol(Color.RED, 5);
	private List<LocatorGeocodeResult> locatorResults = null;
	Exception mException = null;

	private ArrayList<String> floors; 
	private ArrayList<FloorInfo> floorList;
	private HashMap<String, ParcelableGoods> goodsMap;
	private ArrayList<LocatorGeocodeResult> shelfList = new ArrayList<LocatorGeocodeResult>();
	private int searchTime = 0;
	final SpatialReference wm = SpatialReference.create(2437);
	final SpatialReference egs = SpatialReference.create(4326);

	private boolean mIsVip = false;
	private String mMyLocationTitle;
	private String mMyLocation;
	private String mMyLocationFloor;
	private boolean isPathShow = false;
	// Handler for processing the results
	final Handler mHandler = new Handler();
	// 指北针
	private Compass compass;

	private SynHorizontalScrollView hsvFloor;
	private RadioGroup rgFloor;
	private int mCurrentCheckedRadioLeft;
	private int mRgMargin;
	private int mCheckedIndex = 0;
	private boolean isBtnClick = false;
	private static final String MapServerRoot = "http://www.suntowngis.com:6080/arcgis/rest/services/";
	private String mShopId;
	private boolean hasOtherFloor = false;
	final Runnable mMarkAndRoute = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			showProgress(false, null);
			boolean canRoute = false;
			for (FloorInfo floorInfo : floorList) {
				ArrayList<LocatorGeocodeResult> shelfList = floorInfo
						.getShelfList();
				if (shelfList.size() > 0) {
					StopGraphic points[] = new StopGraphic[shelfList.size()];
					int index = 1;
					for (LocatorGeocodeResult result : shelfList) {
						String address = result.getAddress();
						if (index >= shelfList.size()
								&& !address.equals(floorInfo.getStartPoint())) {
							break;
						}

						Point p = (Point) GeometryEngine.project(
								result.getLocation(), wm, egs);
						if (address.equals(floorInfo.getStartPoint())) {
							points[0] = new StopGraphic(p);

							System.out.println("起始点x:" + p.getX() + " y:"
									+ p.getY());

						} else if (floorInfo.getEndPoint() != null
								&& address.equals(floorInfo.getEndPoint())) {
							points[shelfList.size() - 1] = new StopGraphic(p);
						} else {
							points[index++] = new StopGraphic(p);

						}

					}
					if (points[0] != null && points.length >= 2) {
						canRoute = true;
						routeCount++;
					}
					floorInfo.setPoints(points);
				}

			}
			clearAll();
			if (!canRoute) {
				Toast.makeText(MapActivity.this, "没有找到足够的点，无法导航",
						Toast.LENGTH_SHORT).show();
			} else {
				QueryDirections();
			}
		}
	};
	private int routeCount = 0;
	private int routeIndex = 0;

	private void QueryDirections() {

		// Show that the route is calculating
		showProgress(true, "正在搜索导航路线...");
		// Spawn the request off in a new thread to keep UI responsive
		for (final FloorInfo floorInfo : floorList) {
			if (floorInfo.getPoints() != null
					&& floorInfo.getPoints().length >= 2) {

				Thread t = new Thread() {
					@Override
					public void run() {
						try {
							// Start building up routing parameters
							RouteTask routeTask = RouteTask
									.createOnlineRouteTask(
											floorInfo.getRouteServerPath(),
											null);

							RouteParameters rp = routeTask
									.retrieveDefaultRouteTaskParameters();
							NAFeaturesAsFeature rfaf = new NAFeaturesAsFeature();
							// Convert point to EGS (decimal degrees)
							// Create the stop points (start at our location, go
							// to pressed location)
							// StopGraphic point1 = new StopGraphic(mLocation);
							// StopGraphic point2 = new StopGraphic(p);
							rfaf.setFeatures(floorInfo.getPoints());
							rfaf.setCompressedRequest(true);
							rp.setStops(rfaf);

							// Set the routing service output SR to our map
							// service's SR
							rp.setOutSpatialReference(wm);
							rp.setFindBestSequence(true);
							rp.setPreserveFirstStop(true);
							if (floorInfo.getEndPoint() != null) {
								rp.setPreserveLastStop(true);
							}
							// Solve the route and use the results to update UI
							// when received
							floorInfo.setRouteResult(routeTask.solve(rp));

						} catch (Exception e) {
							mException = e;
							// mHandler.post(mUpdateResults);
						}
						routeIndex++;
						if (routeIndex >= routeCount) {
							mHandler.post(mSetCheckMap);
							mHandler.post(mUpdateResults);
						}
					}
				};
				// Start the operation
				t.start();
			}
		}

	}

	final Runnable mSetCheckMap = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int mapIndex = getFloorIndexByName(mMyLocationFloor);
			if (mapIndex > -1) {
				setCheckAt(mapIndex);
			}
		}
	};

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateUI();
		}
	};

	// 计算箭头旋转角度
	private double getDegree(Point p1, Point p2) {
		double degree = 0;
		if (p2.getX() == p1.getX()) {
			if (p2.getY() > p1.getY()) {
				degree = -90.0;
			} else if (p2.getY() < p1.getY()) {
				degree = 90.0;
			} else {
				degree = 0;
			}
		} else if (p2.getX() > p1.getX()) {
			degree = -Math.toDegrees(Math.atan((p2.getY() - p1.getY())
					/ (p2.getX() - p1.getX())));
		} else {
			degree = 180 - Math.toDegrees(Math.atan((p2.getY() - p1.getY())
					/ (p2.getX() - p1.getX())));
		}
		return degree;
	}

	/**
	 * Updates the UI after a successful rest response has been received.
	 */
	void updateUI() {

		showProgress(false, null);
		clearAll();
		mResults = floorList.get(mCheckedIndex).getRouteResult();

		if (mResults == null) {
			// Toast.makeText(this, "本层没有导航信息", Toast.LENGTH_LONG).show();

			return;
		}
		ArrayList<LocatorGeocodeResult> shelfList = floorList
				.get(mCheckedIndex).getShelfList();
		//MultiPoint bounds = new MultiPoint();
		for (LocatorGeocodeResult result : shelfList) {
			String address = result.getAddress();
			ArrayList<ParcelableGoods> goodsList = floorList.get(mCheckedIndex)
					.getGoodsMap().get(address);
			Point p = (Point) GeometryEngine.project(result.getLocation(), wm,
					egs);
			if (goodsList != null && goodsList.size() > 0) {
				String names = "";
				for (ParcelableGoods goods : goodsList) {
					if ("".equals(names)) {
						names = "【" + goods.getName() + "】";
					} else {
						names = names + " 【" + goods.getName() + "】";
					}
				}
				mMapViewHelper.addMarkerGraphic(p.getY(), p.getX(),
						goodsList.size() + "件商品", names,
						R.drawable.icon_tip_goods,
						getResources().getDrawable(R.drawable.icon_mark_goods),
						false, 1);
				//bounds.add(result.getLocation());
			}
		}
		curRoute = mResults.getRoutes().get(0);
		// Symbols for the route and the destination (blue line, checker flag)
		SimpleLineSymbol routeSymbol = new SimpleLineSymbol(Color.BLUE, 5);
		// Drawable drawable = getResources().getDrawable(R.drawable.arrow);

		int count = ((Polyline) curRoute.getRouteGraphic().getGeometry())
				.getPointCount();
		if (count > 0) {

			Graphic routeGraphic = new Graphic(curRoute.getRouteGraphic()
					.getGeometry(), routeSymbol);
			// Add the full route graphics, start and destination graphic to the
			// routeLayer

			routeLayer.addGraphics(new Graphic[] { routeGraphic });
			Point p = (Point) GeometryEngine.project(((Polyline) curRoute
					.getRouteGraphic().getGeometry()).getPoint(0), wm, egs);
			// map.centerAt(((Polyline)
			// curRoute.getRouteGraphic().getGeometry())
			// .getPoint(0), true);
			
			
			mMapViewHelper.addMarkerGraphic(p.getY(), p.getX(), "起始点",
					floorList.get(mCheckedIndex).getStartPoint(),
					R.drawable.icon_tip_start,
					getResources().getDrawable(R.drawable.icon_mark_start),
					false, 1);
			//bounds.add(((Polyline) curRoute
			//		.getRouteGraphic().getGeometry()).getPoint(0));
			p = (Point) GeometryEngine.project(((Polyline) curRoute
					.getRouteGraphic().getGeometry()).getPoint(count - 1), wm,
					egs);
			System.out.println("终点x:" + p.getX() + " y:" + p.getY());
			String endPoint = floorList.get(mCheckedIndex).getEndPoint();
			if (endPoint == null || "".equals(endPoint)) {
				endPoint = "终点";
			}
			mMapViewHelper.addMarkerGraphic(p.getY(), p.getX(), "终点", endPoint,
					R.drawable.icon_tip_end,
					getResources().getDrawable(R.drawable.icon_mark_end),
					false, 1);
			//bounds.add(((Polyline) curRoute
			//		.getRouteGraphic().getGeometry()).getPoint(count - 1));
			map.setExtent(curRoute.getRouteGraphic().getGeometry(), 100, true);
		}
	}

	// 重绘导航方向
	private void refreshDirection(boolean isSubMapAngle) {
		if (curRoute != null) {
			int count = ((Polyline) curRoute.getRouteGraphic().getGeometry())
					.getPointCount();
			Graphic[] graphics = new Graphic[count - 1];
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.route_direction);
			Bitmap mBitmapRotate;
			Matrix matrix = new Matrix();
			double mapAngle = 0;
			if (isSubMapAngle) {
				mapAngle = map.getRotationAngle();
			}
			for (int i = 0; i < count - 1; i++) {
				Point p1 = ((Polyline) curRoute.getRouteGraphic().getGeometry())
						.getPoint(i);
				Point p2 = ((Polyline) curRoute.getRouteGraphic().getGeometry())
						.getPoint(i + 1);
				double degre = getDegree(p1, p2) - mapAngle;

				matrix.setRotate((float) degre);
				mBitmapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				Drawable drawable = new BitmapDrawable(mBitmapRotate);
				PictureMarkerSymbol pmSymbol = new PictureMarkerSymbol(drawable);

				graphics[i] = new Graphic(p1, pmSymbol);

			}

			routeDirectionLayer.removeAll();

			routeDirectionLayer.addGraphics(graphics);
		}
	}

	/*
	 * Clear the graphics and empty the directions list
	 */

	public void clearAll() {

		// Removing the graphics from the layer
		routeLayer.removeAll();
		// hiddenSegmentsLayer.removeAll();
		mMapViewHelper.removeAllGraphics();
		mResults = null;

	}

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show, String msg) {
		if (show) {
			mPDialog = new ProgressDialog(this);
			// 实例化
			mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置进度条风格，风格为圆形，旋转的
			// pDialog.setTitle("Google");
			// 设置ProgressDialog 标题
			mPDialog.setMessage(msg);
			// 设置ProgressDialog 提示信息
			// pDialog.setIcon(R.drawable.ic_launcher);
			// 设置ProgressDialog 标题图标
			// mypDialog.setButton();
			// 设置ProgressDialog 的一个Button
			mPDialog.setIndeterminate(false);
			// 设置ProgressDialog 的进度条是否不明确
			mPDialog.setCancelable(false);
			// 设置ProgressDialog 是否可以按退回按键取消
			mPDialog.show();
		} else {
			if (mPDialog != null && mPDialog.isShowing()) {
				mPDialog.dismiss();
				mPDialog = null;
			}
		}
	}

	private int mSearchCount = 0;

	// 检查本层以上楼层或以下楼层是否有商品需要导航
	private boolean hasGoodsUpOrDown(int index, boolean isUp) {
		if (isUp) {
			for (int i = index + 1; i < floorList.size(); i++) {
				if (floorList.get(i).getGoodsMap().size() > 0) {
					return true;
				}
			}
		} else {
			for (int i = index - 1; i >= 0; i--) {
				if (floorList.get(i).getGoodsMap().size() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	private void queryLocator() {
		int locationIndex = getFloorIndexByName(mMyLocationFloor);
		if (locationIndex < 0) {
			Toast.makeText(this, "起始点楼层错误，请选择其他起始地重试...", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String startPoint;
		String endPoint;
		for (int i = 0; i < floorList.size(); i++) {
			FloorInfo floorInfo = floorList.get(i);
			HashMap<String, ArrayList<ParcelableGoods>> goodsMap = floorInfo
					.getGoodsMap();
			if (locationIndex > i) { // 出发地高于本楼层
				startPoint = floorList.get(i + 1).getFloorNo() + "-"
						+ floorInfo.getFloorNo() + "梯口";
				// 如果低于本楼层的其他楼层还有商品要导航则本楼层需设置出发点和目的地
				if (hasGoodsUpOrDown(i, false)) {
					endPoint = floorInfo.getFloorNo() + "-"
							+ floorList.get(i - 1).getFloorNo() + "梯口";
					goodsMap.put(startPoint, null);
					floorInfo.setStartPoint(startPoint);
					goodsMap.put(endPoint, null);
					floorInfo.setEndPoint(endPoint);
				} else if (goodsMap.size() > 0) { // 如果本楼层有商品需要导航则本楼层设置出发点
					goodsMap.put(startPoint, null);
					floorInfo.setStartPoint(startPoint);
				}
			} else if (locationIndex < i) { // 出发地低于本楼层
				startPoint = floorList.get(i - 1).getFloorNo() + "-"
						+ floorInfo.getFloorNo() + "梯口";
				// 高于本楼层还有商品需要导航
				if (hasGoodsUpOrDown(i, true)) {
					endPoint = floorInfo.getFloorNo() + "-"
							+ floorList.get(i + 1).getFloorNo() + "梯口";
					goodsMap.put(startPoint, null);
					floorInfo.setStartPoint(startPoint);
					goodsMap.put(endPoint, null);
					floorInfo.setEndPoint(endPoint);
				} else if (goodsMap.size() > 0) {
					goodsMap.put(startPoint, null);
					floorInfo.setStartPoint(startPoint);
				}
			} else { // 出发地等于本楼层
				startPoint = mMyLocation; // 本楼层出发点为总出发地
				goodsMap.put(startPoint, null);
				floorInfo.setStartPoint(startPoint);
				if (hasGoodsUpOrDown(i, true)) { // 高于本楼层还有商品需要导航
					endPoint = floorInfo.getFloorNo() + "-"
							+ floorList.get(i + 1).getFloorNo() + "梯口";
					goodsMap.put(endPoint, null);
					floorInfo.setEndPoint(endPoint);
				} else if (hasGoodsUpOrDown(i, false)) { // 低于本楼层还有商品需要导航
					endPoint = floorInfo.getFloorNo() + "-"
							+ floorList.get(i - 1).getFloorNo() + "梯口";
					goodsMap.put(endPoint, null);
					floorInfo.setEndPoint(endPoint);
				}
			}
			/*
			 * 
			 * if (mMyLocationFloor.equals(floorInfo.getName())) { if
			 * (goodsMap.size() > 0 || hasOtherFloor) {
			 * goodsMap.put(mMyLocation, null);
			 * floorInfo.setStartPoint(mMyLocation); } if (hasOtherFloor) {
			 * goodsMap.put("一楼自动扶梯上", null); floorInfo.setEndPoint("一楼自动扶梯上");
			 * } } else if (goodsMap.size() > 0) { goodsMap.put("二楼电梯上", null);
			 * floorInfo.setStartPoint("二楼电梯上"); }
			 */
			mSearchCount = mSearchCount + goodsMap.size();
		}
		System.out.println("共查找目标数：" + mSearchCount);
		if (mSearchCount > 0) {
			showProgress(true, "正在查找商品位置...");
			for (final FloorInfo floorInfo : floorList) {
				HashMap<String, ArrayList<ParcelableGoods>> goodsMap = floorInfo
						.getGoodsMap();
				System.out.println("本层目标数:" + goodsMap.size());
				List<Entry<String, ArrayList<ParcelableGoods>>> list = new ArrayList<Entry<String, ArrayList<ParcelableGoods>>>(
						goodsMap.entrySet());
				System.out.println("列表目标数:" + list.size());
				for (Entry<String, ArrayList<ParcelableGoods>> entry : list) {
					final String address = entry.getKey();
					System.out.println("查找:" + address);
					Thread t = new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int wkid = map.getSpatialReference().getID();

							Locator locator = Locator
									.createOnlineLocator(floorInfo
											.getLocatorServerPath());
							// Create suggestion parameter
							LocatorFindParameters params = new LocatorFindParameters(
									address);

							// ArrayList<String> outFields = new
							// ArrayList<String>();
							// outFields.add("*");
							// params.setOutFields(outFields);
							// Set the location to be used for proximity based
							// suggestion
							// params.setLocation(map.getCenter(),map.getSpatialReference());
							// Set the radial search distance in meters
							// params.setDistance(50000.0);
							try {
								locatorResults = locator.find(params);
								// HashMap<String, String> params = new
								// HashMap<String,
								// String>();
								// params.put("货架编号", address);
								// locatorResults = locator.geocode(params,
								// null,
								// map.getSpatialReference());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println("查找：" + address + " 出错:"
										+ e.getMessage());
								e.printStackTrace();
							}
							if (locatorResults != null
									&& locatorResults.size() > 0) {
								// Add the first result to the map and zoom to
								// it
								for (LocatorGeocodeResult result : locatorResults) {
									System.out.println("查找：" + address + " 结果："
											+ result.getAddress());

									if (result.getAddress() != null
											&& address.equals(result
													.getAddress())) {

										floorInfo.getShelfList().add(result);
										// shelfList.add(result);
										break;
									}
								}
							}
							searchTime++;
							System.out.println("已查找次数：" + searchTime);
							if (searchTime >= mSearchCount) {
								mHandler.post(mMarkAndRoute);
							}
						}
					};
					t.start();
				}
			}
		}
	}

	// 根据楼层名称获得楼层序号
	private int getFloorIndexByName(String name) {
		for (int i = 0; i < floorList.size(); i++) {
			FloorInfo floorInfo = floorList.get(i);
			if (name.equals(floorInfo.getName())) {
				return i;
			}
		}
		return -1;
	}

	// 根据楼层名称获得楼层
	private FloorInfo getFloorInfoByName(String name) {
		if (floorList != null && floorList.size() > 0) {
			for (FloorInfo floorInfo : floorList) {
				if (name.equals(floorInfo.getName())) {
					return floorInfo;
				}
			}
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ArcGISRuntime.setClientId("TZgA94VjocV8S3Uj");
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mIsVip = mSharedPreferences.getBoolean("isvip", false);
		mShopId = mSharedPreferences.getString("shopid", "");
		String shopName = mSharedPreferences.getString("shopfullname", "");
		String floorNames = mSharedPreferences.getString("floornames", null);
		String floorNos = mSharedPreferences.getString("floors", null);
		if (floorNames == null || floorNos == null || "".equals(floorNos)
				|| "".equals(floorNames)) {
			Toast.makeText(this, "该超市没有地图", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		if (!"".equals(shopName)) {
			tvTitle.setText(shopName);
		}
		String[] floorNameArray = FormatValidation.StringToArray(floorNames,
				";");
		String[] floorNoArray = FormatValidation.StringToArray(floorNos, ";");
		map = (MapView) findViewById(R.id.map);
		hsvFloor = (SynHorizontalScrollView) findViewById(R.id.hsv_floor);
		rgFloor = (RadioGroup) findViewById(R.id.rg_floor);
		mRgMargin = Constants.displayWidth / 2 - MyMath.dip2px(this, 60);
		rgFloor.setPadding(mRgMargin, 0, mRgMargin, 0);
		rgFloor.setOnCheckedChangeListener(this);
		hsvFloor.setOnTouchListener(this);
		hsvFloor.setOnScrollChangedListener(this);

		map.setMapBackground(0xffffff, 0xffffff, 0, 0);
		mMapViewHelper = new MapViewHelper(map);

		floorList = new ArrayList<FloorInfo>();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int btnWidth = MyMath.dip2px(this, 40);

		for (int i = 0; i < floorNameArray.length; i++) {
			String name = floorNameArray[i];
			String number = floorNoArray[i];
			System.out.println("楼层号：" + number + "楼层名：" + name);
			RadioButton rb = (RadioButton) inflater.inflate(
					R.layout.radio_button_floor, null);
			rb.setWidth(btnWidth);
			rb.setHeight(btnWidth);
			int id = View.generateViewId();
			rb.setId(id);
			rb.setText(name);
			FloorInfo floorInfo = new FloorInfo(name, id, number);
			// ArcGISTiledMapServiceLayer tiledLayer = new
			// ArcGISTiledMapServiceLayer(
			// MapServerRoot +"ST_"+ mShopId + "_" + name + "/MapServer");
			ArcGISDynamicMapServiceLayer dynamicLayer = new ArcGISDynamicMapServiceLayer(
					MapServerRoot + "ST_" + mShopId + "_" + name + "/MapServer");
			floorInfo.setBaseLayer(dynamicLayer);
			floorInfo.setRouteServerPath(MapServerRoot + "ST_" + mShopId + "_"
					+ name + "_ND/NAServer/Route");
			floorInfo.setLocatorServerPath(MapServerRoot + "ST_" + mShopId
					+ "_" + name + "_QUERY/GeocodeServer");
			dynamicLayer.setVisible(false);
			map.addLayer(dynamicLayer);
			floorList.add(floorInfo);
			rgFloor.addView(rb);
			rb.setOnTouchListener(this);
		}
		setCheckAt(0);
		// Set the MapView to allow the user to rotate the map when as part of a
		// pinch gesture.
		map.setAllowRotationByPinch(true);

		// Enabled wrap around map.
		map.enableWrapAround(true);
		compass = new Compass(this, null, map);

		LinearLayout compassContainer = new LinearLayout(this);
		compassContainer.setPadding(20, 20, 20, 20);
		compassContainer.addView(compass);
		compassContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				map.setRotationAngle(0);
				compass.setRotationAngle(0);

			}
		});
		map.addView(compassContainer);

		// Add the route graphic layer (shows the full route)
		routeLayer = new GraphicsLayer();
		map.addLayer(routeLayer);
		routeDirectionLayer = new GraphicsLayer();
		map.addLayer(routeDirectionLayer);
		// Initialize the RouteTask

		map.setOnStatusChangedListener(this);
		Intent intent = getIntent();

		if (intent.hasExtra("goodslist")) {
			if (intent.hasExtra("location")) {
				mMyLocation = intent.getStringExtra("location");
				mMyLocationTitle = intent.getStringExtra("title");
				mMyLocationFloor = intent.getStringExtra("floor");
			}
			ArrayList<ParcelableGoods> goodsList = intent
					.getParcelableArrayListExtra("goodslist");
			goodsMap = new HashMap<String, ParcelableGoods>();
			for (ParcelableGoods goods : goodsList) {
				if (goods.getShelfId() != null && goods.getFloorName() != null
						&& !"".equals(goods.getShelfId())
						&& !"".equals(goods.getFloorName())) {
					String floorName = goods.getFloorName();
					FloorInfo floorInfo = getFloorInfoByName(floorName);
					if (floorInfo != null) {
						floorInfo.addGoods(goods.getShelfId(), goods);
						if (!hasOtherFloor
								&& !mMyLocationFloor
										.equals(floorInfo.getName())) {
							hasOtherFloor = true;
						}
					}
					goodsMap.put(goods.getShelfId(), goods);
				}
			}

		}
	}

	public void close(View v) {
		finish();
	}

	@Override
	public void onStatusChanged(Object arg0, STATUS status) {
		// TODO Auto-generated method stub
		System.out.println(status);
		switch (status) {

		case LAYER_LOADED:
			if (!isPathShow && goodsMap != null && goodsMap.size() > 0) {
				shelfList.clear();
				searchTime = 0;
				queryLocator();
				isPathShow = true;
			}
			break;
		}
	}

	public void onScan(View v) {

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub

		floorList.get(mCheckedIndex).getBaseLayer().setVisible(false);
		mCheckedIndex = getCheckedIndex(checkedId);

		floorList.get(mCheckedIndex).getBaseLayer().setVisible(true);
		hsvFloor.smoothScrollTo(MyMath.dip2px(this, 40 * mCheckedIndex), 0);
		mHandler.post(mUpdateResults);
	}

	private void moveFloor(int num) {
		int index = getCheckedIndex(rgFloor.getCheckedRadioButtonId());
		index = index + num;
		if (index >= 0 && index <= floorList.size()) {
			setCheckAt(index);
		}
	}

	private void setCheckAt(int index) {
		if (index < floorList.size()) {

			int id = floorList.get(index).getId();
			rgFloor.check(id);

		}
	}

	private int getCheckedIndex(int id) {
		int index = 0;
		for (int i = 0; i < floorList.size(); i++) {
			FloorInfo floorInfo = floorList.get(i);
			if (floorInfo.getId() == id) {
				index = i;
				break;
			}
		}
		return index;
	}

	public void moveFloor(View v) {

		switch (v.getId()) {
		case R.id.btn_left:
			moveFloor(-1);
			break;
		case R.id.btn_right:
			moveFloor(1);
			break;
		}

	}

	private int scrollTimes = 0;

	@Override
	public void onScroll(int left, int top, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if (!isBtnClick) {
			double level;
			System.out.println("now:" + left + " old:" + oldl);
			level = (double) left / (double) MyMath.dip2px(this, 40);
			System.out.println("level:" + level);
			level = new BigDecimal(Double.toString(level)).setScale(0,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			int index = (int) level;
			if (mCheckedIndex != index) {
				setCheckAt(index);
				System.out.println("滚动到：" + index);
			}
		}
	}

	private float moveX;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// 判断是否按钮点击，如果不是则进入滑动事件处理，如果是按钮点击则忽略滑动事件
		if (v.getId() == R.id.hsv_floor) {
			isBtnClick = false;
		} else {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isBtnClick = true;
				moveX = event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				if (Math.abs(event.getX() - moveX) > 15) {
					isBtnClick = false;
				} else {
					isBtnClick = true;
				}
				break;

			}
		}
		return false;
	}

}
