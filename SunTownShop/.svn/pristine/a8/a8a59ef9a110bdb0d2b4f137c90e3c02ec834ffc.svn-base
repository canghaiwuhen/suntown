package com.suntown.suntownshop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.suntown.suntownshop.adapter.GoodsListAdapter;
import com.suntown.suntownshop.adapter.GridGoodsListAdapter;
import com.suntown.suntownshop.adapter.GridGoodsListAdapter.OnAddToRouterListener;
import com.suntown.suntownshop.db.RouteGoodsDb;
import com.suntown.suntownshop.listener.OnClassSelectListener;
import com.suntown.suntownshop.listener.OnImageMoveListener;
import com.suntown.suntownshop.listener.OnMoveViewListener;
import com.suntown.suntownshop.listener.OnOrderbySelectListener;
import com.suntown.suntownshop.listener.OnShopSelectListener;
import com.suntown.suntownshop.model.Category;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.model.Store;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.service.LocalService;
import com.suntown.suntownshop.utils.ImageMoveAnimation;
import com.suntown.suntownshop.utils.IsChineseOrNot;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.PopMenuClassSelect;
import com.suntown.suntownshop.widget.PopMenuOrderbySelect;
import com.suntown.suntownshop.widget.PopMenuShopSelect;
import com.suntown.suntownshop.widget.PullUpRefreshListView;
import com.suntown.suntownshop.widget.PullUpRefreshListView.OnRefreshListener;
import com.suntown.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 特殊商品列表，如每日特价，会员商品。待改造
 * 
 * @author Ken
 * @version 2015年3月4日 上午10:06:58
 *
 */
public class SpecialGoodsListActivity extends Activity implements
		OnItemClickListener, OnLongClickListener, OnClickListener {
	private PullToRefreshGridView gridView;
	private PullUpRefreshListView goodsListView;
	private ArrayList<Goods> list;
	private GridGoodsListAdapter adapter;
	private GoodsListAdapter goodsAdapter;
	private TextView tvTitle;
	private View mLoading;
	private View viewMain;
	private final static int MSG_GETGOODSLIST_COMPLETE = 1;
	private final static int MSG_ERR_NETWORKERR = -1;
	private final static int MSG_GETKINDS_COMPLETE = 3;
	private final static int MSG_GET_LOCATION_GOODS = 4;
	private final static int LOAD_ONCE_LEN = 30;
	private int mLoadTimes = 0;

	private ImageView ivRoute;
	private String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getgoods_upt_day?startIndex=1&length=100";
	private final String URL_GOODSDETAIL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Getgoods_info?Barcode=";
	// 商品分类接口
	private String URL_KINDS = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Getgoods_all_kind";
	private String userId;
	private ArrayList<ParcelableGoods> routeList;
	private TextView tvRouteNum;
	private PopupWindow pw;
	private ImageMoveAnimation imageMoveAnim;
	private View viewClass;
	private View viewOrderby;
	private TextView tvClass;
	private TextView tvOrderby;
	private LinkedHashMap<Integer, Category> categorys;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list_special);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		userId = mSharedPreferences.getString("userId", "");
		tvClass = (TextView) findViewById(R.id.tv_class);
		tvOrderby = (TextView) findViewById(R.id.tv_orderby);
		viewClass = findViewById(R.id.view_class);
		viewClass.setOnClickListener(this);
		viewOrderby = findViewById(R.id.view_orderby);
		viewOrderby.setOnClickListener(this);
		// gridView = (PullToRefreshGridView) findViewById(R.id.gv_goodslist);
		goodsListView = (PullUpRefreshListView) findViewById(R.id.lv_category_goodslist);
		goodsListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadGoodsMore();
			}
		});
		goodsListView.setOnItemClickListener(this);
		// anim_mask_layout = createAnimLayout();
		ivRoute = (ImageView) findViewById(R.id.iv_route);
		ivRoute.setOnLongClickListener(this);
		tvRouteNum = (TextView) findViewById(R.id.tv_route_num);
		// initIndicator();
		/*
		 * gridView.setOnRefreshListener(new OnRefreshListener2() {
		 * 
		 * @Override public void onPullDownToRefresh(PullToRefreshBase
		 * refreshView) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onPullUpToRefresh(PullToRefreshBase
		 * refreshView) { // TODO Auto-generated method stub loadGoodsMore(); }
		 * });
		 */
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		mLoading = findViewById(R.id.loading);
		viewMain = findViewById(R.id.view_main);
		Intent intent = getIntent();
		if (intent.hasExtra("url")) {
			tvTitle.setText(intent.getStringExtra("title"));
			int noId = intent.getIntExtra("noid", 0);
			if (noId > 0) {
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancel(noId);
			}
			URL = intent.getStringExtra("url");
			System.out.println(URL);

			list = new ArrayList<Goods>();
			goodsAdapter = new GoodsListAdapter(SpecialGoodsListActivity.this,
					list);
			goodsAdapter.setOnMoveViewListener(listener);
			goodsListView.setAdapter(goodsAdapter);
			// gridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
			// adapter = new GridGoodListAdapter(SpecialGoodsListActivity.this,
			// list);
			// gridView.setAdapter(adapter);
			viewMain.setVisibility(View.GONE);
			GetJsonRunnable getJsonRunnable = new GetJsonRunnable(URL,
					MSG_GETGOODSLIST_COMPLETE, handler);
			new Thread(getJsonRunnable).start();
		} else {
			if (intent.hasExtra("type")) {
				int type = intent.getIntExtra("type", 0);
				tvTitle.setText(type == 0 ? "每日特价" : "会员商品");
				URL = type == 0 ? Constants.DOMAIN_NAME
						+ "axis2/services/sunteslwebservice/getgoods_upt_day?length="
						+ LOAD_ONCE_LEN + "&startIndex="
						: Constants.DOMAIN_NAME
								+ "axis2/services/sunteslwebservice/getgoods_mem_day?length="
								+ LOAD_ONCE_LEN + "&startIndex=";
				initViews();
			} else {
				finish();
			}
		}
		RouteGoodsDb db = new RouteGoodsDb(SpecialGoodsListActivity.this,
				userId);
		routeList = db.getAll();
		db.Close();
		refreshRouteGoods();

	}

	private void refreshRouteGoods() {
		if (routeList.size() > 0) {
			tvRouteNum.setVisibility(View.VISIBLE);
			tvRouteNum.setText("" + routeList.size());
		} else {
			tvRouteNum.setVisibility(View.GONE);
		}
	}

	private void initViews() {
		list = new ArrayList<Goods>();
		goodsAdapter = new GoodsListAdapter(SpecialGoodsListActivity.this, list);
		goodsAdapter.setOnMoveViewListener(listener);
		goodsListView.setAdapter(goodsAdapter);
		viewMain.setVisibility(View.GONE);
		loadCategory();
		loadGoodsMore();
	}

	/**
	 * 加载类目
	 */
	private void loadCategory() {
		GetJsonRunnable getKindsRunnable = new GetJsonRunnable(URL_KINDS,
				MSG_GETKINDS_COMPLETE, handler);
		System.out.println("开始获取类目");
		new Thread(getKindsRunnable).start();
	}

	private void loadGoodsMore() {
		GetJsonRunnable getJsonRunnable = new GetJsonRunnable(URL
				+ (mLoadTimes * LOAD_ONCE_LEN + 1), MSG_GETGOODSLIST_COMPLETE,
				handler);
		new Thread(getJsonRunnable).start();
		mLoadTimes++;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			Bundle bundle;
			String strMsg;
			JSONObject jsonObj;
			JSONArray jsonArray;
			mLoading.setVisibility(View.GONE);
			viewMain.setVisibility(View.VISIBLE);
			switch (msg.what) {
			case MSG_GET_LOCATION_GOODS:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				try {
					jsonObj = new JSONObject(strMsg);
					jsonArray = jsonObj.getJSONArray("INFO");
					if (jsonArray.length() > 0) {
						jsonObj = (JSONObject) jsonArray.opt(0);
						String barCode = jsonObj.getString("BARCODE");
						String gName = jsonObj.getString("GNAME");
						String shelfId = jsonObj.getString("SFID");
						String floorName = jsonObj.getString("FLOORNAME");
						ConfirmDialog dialog = new ConfirmDialog(
								SpecialGoodsListActivity.this, "确定要从" + gName
										+ "附近开始导航吗?",
								getString(R.string.tips_text),
								getString(R.string.confirm_text),
								getString(R.string.cancel_text));
						if (dialog.ShowDialog()) {
							navigate(gName, shelfId, floorName);
						}

					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.qrcode_cantfind),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:" + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				}
				break;
			case MSG_GETGOODSLIST_COMPLETE:
				// gridView.onRefreshComplete();
				goodsListView.onRefreshComplete();
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				int count = 0;
				System.out.println(strMsg);
				try {
					jsonObj = new JSONObject(strMsg);
					jsonArray = jsonObj.getJSONArray("INFO");
					count = jsonArray.length();
					if (count > 0) {
						for (int i = 0; i < count; i++) {
							jsonObj = (JSONObject) jsonArray.opt(i);
							String barCode = jsonObj.getString("BARCODE");
							String gCode = jsonObj.getString("GCODE");
							String gName = jsonObj.getString("GNAME");
							String gKind = jsonObj.getString("KIND");
							String gUnit = jsonObj.getString("UNIT");
							String gOriPrice = jsonObj.getString("ORIPRICE");
							String gMemPrice = jsonObj.getString("MEMPRICE");
							String gUptPrice = jsonObj.getString("UPTPRICE");
							String gSpec = jsonObj.getString("SPEC");
							String gClass = jsonObj.getString("GCLASS");
							String gProvider = jsonObj.getString("PROVIDER");
							String gBrand = jsonObj.getString("BRAND");
							String gOrigin = jsonObj.getString("ORIGIN");
							String gImgPath = jsonObj.getString("IMGPATH");
							int priceType = jsonObj.getInt("PRICETYPE");
							int deliverType = jsonObj.getInt("DELIVERYMODE");
							String shelfId = jsonObj.getString("SFID");
							String floorNo = jsonObj.getString("FLOORNO");
							String floorName = jsonObj.getString("FLOORNAME");
							Goods goods = new Goods(barCode, gCode, gName,
									gKind, gUnit, gOriPrice, gMemPrice,
									gUptPrice, gSpec, gClass, gProvider,
									gBrand, gOrigin, gImgPath, priceType,
									deliverType);
							goods.setShelfId(shelfId);
							goods.setFloorNo(floorNo);
							goods.setFloorName(floorName);
							// adapter.goodsList.add(goods);
							goodsAdapter.goodsList.add(goods);
						}
						goodsAdapter.notifyDataSetChanged();
						// adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(SpecialGoodsListActivity.this, "找不到商品",
								Toast.LENGTH_SHORT).show();
					}
					if (count < LOAD_ONCE_LEN) {
						// gridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
						goodsListView.setRefreshable(false);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SpecialGoodsListActivity.this,
							"ERROR:分类商品解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
			case MSG_GETKINDS_COMPLETE:
				System.out.println("获取类目成功，开始解析");
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				try {
					categorys = JsonParser.CategoryParse(strMsg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SpecialGoodsListActivity.this,
							"ERROR:商品分类解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				System.out.println("解析完成，显示类目");
				break;
			case MSG_ERR_NETWORKERR:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(SpecialGoodsListActivity.this, "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void close(View v) {
		if (this.isTaskRoot()) {
			Intent intent = new Intent(this, MainTabActivity.class);
			startActivity(intent);
		}
		finish();
	}

	/**
	 * 初始化刷新时的相应提示
	 */
	private void initIndicator() {
		ILoadingLayout startLabels = gridView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在加载...");// 刷新时
		endLabels.setReleaseLabel("松开加载更多");// 下来达到一定距离时，显示的提示
	}
	/**
	 * 导航
	 * @param v
	 */
	public void navigate(View v) {
		if (routeList == null || routeList.size() == 0) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.select_location_popup, null);

		if (pw == null) {
			// 生成PopupWindow对象
			pw = new PopupWindow(mView, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			pw.setOutsideTouchable(true);
		} else {
			pw.setContentView(mView);
		}
		View locationGate = mView.findViewById(R.id.location_gate);
		View locationScan = mView.findViewById(R.id.location_scan);
		View viewOk = mView.findViewById(R.id.view_ok);
		View viewCancel = mView.findViewById(R.id.view_cancel);
		final RadioButton rbGate = (RadioButton) mView
				.findViewById(R.id.rb_gate);
		final RadioButton rbScan = (RadioButton) mView
				.findViewById(R.id.rb_scan);
		rbGate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rbScan.setChecked(false);
				}
			}
		});
		rbScan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rbGate.setChecked(false);
				}
			}
		});
		viewOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
				if (rbGate.isChecked()) {
					navigate("1号入口", "1号入口", "1F");
				} else {

					Intent openCameraIntent = new Intent(
							SpecialGoodsListActivity.this,
							CaptureActivity.class);
					startActivityForResult(openCameraIntent, 0);
				}
			}
		});
		viewCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
			}
		});
		pw.setFocusable(true);

		pw.setBackgroundDrawable(new ColorDrawable(0x7f000000));

		pw.showAtLocation(viewMain, Gravity.CENTER, 0, 0);
	}

	private void navigate(String title, String location, String floorName) {
		if (title == null || location == null || floorName == null
				|| "".equals(title) || "".equals(location)
				|| "".equals(floorName)) {
			Toast.makeText(this, "找不到该商品的位置，请更换起始地重试...", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Intent i = new Intent(this, MapActivity.class);
		i.putParcelableArrayListExtra("goodslist", routeList);
		i.putExtra("location", location);
		i.putExtra("title", title);
		i.putExtra("floor", floorName);
		startActivity(i);
		RouteGoodsDb db = new RouteGoodsDb(SpecialGoodsListActivity.this,
				userId);
		db.clearAll();
		routeList = db.getAll();
		db.Close();
		refreshRouteGoods();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestCode:" + requestCode + " resultCode:"
				+ resultCode);
		if (resultCode == Activity.RESULT_OK) {

			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			String format = bundle.getString("format");
			System.out.println("Scan Result---------->" + scanResult);
			if (format.equalsIgnoreCase("QR_CODE")) { // 二维码
				String UTF_Str = "";
				String GB_Str = "";
				boolean is_cN = false;
				try {
					UTF_Str = new String(scanResult.getBytes("ISO-8859-1"),
							"UTF-8");
					System.out.println("这是转了UTF-8的" + UTF_Str);
					is_cN = IsChineseOrNot.isChineseCharacter(UTF_Str);
					// 防止有人特意使用乱码来生成二维码来判断的情况
					boolean b = IsChineseOrNot.isSpecialCharacter(scanResult);
					if (b) {
						is_cN = true;
					}
					System.out.println("是为:" + is_cN);
					if (!is_cN) {
						GB_Str = new String(scanResult.getBytes("ISO-8859-1"),
								"GB2312");
						System.out.println("这是转了GB2312的" + GB_Str);
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (is_cN) {
					scanResult = UTF_Str;
				} else {
					scanResult = GB_Str;
				}

				int l = scanResult.indexOf("BC:");
				// int len = scanResult.length();
				int r = scanResult.indexOf(";"); //
				String barCode = "";
				if (l >= 0 && r > l) {
					barCode = scanResult.substring(l + 3, r);
					findLocation(barCode);
				} else {
					Toast.makeText(this, getString(R.string.qrcode_cantfind),
							Toast.LENGTH_SHORT).show();
				}
			} else { // 条码
				findLocation(scanResult);
			}

		}
	}

	private void findLocation(String barcode) {
		showProgress(true);
		com.suntown.suntownshop.runnable.GetJsonRunnable getJsonRunnable = new com.suntown.suntownshop.runnable.GetJsonRunnable(
				URL_GOODSDETAIL + barcode, MSG_GET_LOCATION_GOODS, handler);
		new Thread(getJsonRunnable).start();
	}

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(this);
			// 实例化
			mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置进度条风格，风格为圆形，旋转的
			// pDialog.setTitle("Google");
			// 设置ProgressDialog 标题
			mPDialog.setMessage(getString(R.string.wait_a_minute));
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

	private Goods moveGoods;
	private OnMoveViewListener listener = new OnMoveViewListener() {

		@Override
		public void onMove(View v, Goods goods) {
			// TODO Auto-generated method stub
			moveGoods = goods;
			if (imageMoveAnim == null) {
				imageMoveAnim = new ImageMoveAnimation(
						SpecialGoodsListActivity.this);
			}
			imageMoveAnim.setAnim(v, ivRoute, imageMoveListener);
			// setAnim(v);
		}
	};

	private OnImageMoveListener imageMoveListener = new OnImageMoveListener() {

		@Override
		public void onMoveEnd() {
			// TODO Auto-generated method stub
			if (moveGoods != null) {
				RouteGoodsDb db = new RouteGoodsDb(
						SpecialGoodsListActivity.this, userId);
				db.insertGoods(moveGoods.getBarCode(), moveGoods.getName(),
						moveGoods.getShelfId(), moveGoods.getFloorName());
				routeList = db.getAll();
				db.Close();
				refreshRouteGoods();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.product_list_listview_cell_imageview:

			// setAnim(v);
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_route:
			ConfirmDialog dialog = new ConfirmDialog(this, "确定要清除导航任务中的所有商品吗?",
					getString(R.string.tips_text),
					getString(R.string.confirm_text),
					getString(R.string.cancel_text));
			if (dialog.ShowDialog()) {
				RouteGoodsDb db = new RouteGoodsDb(
						SpecialGoodsListActivity.this, userId);
				db.clearAll();
				routeList = db.getAll();
				db.Close();
				refreshRouteGoods();
			}

			break;
		}
		return false;
	}

	private OnClassSelectListener classSelectListener = new OnClassSelectListener() {

		@Override
		public void onClassSelect(Category category) {
			// TODO Auto-generated method stub
			tvClass.setText(category.getName());
			tvClass.setTextColor(getResources().getColor(
					R.color.item_text_normal));
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			tvClass.setTextColor(getResources().getColor(
					R.color.item_text_normal));
		}
	};
	private PopMenuClassSelect popMenuClassSelect;

	private void selectClass(View v) {
		if (categorys != null && !categorys.isEmpty()) {
			tvClass.setTextColor(getResources().getColor(
					R.color.item_text_press));
			if (popMenuClassSelect == null) {
				popMenuClassSelect = new PopMenuClassSelect(this,
						R.drawable.menu_window_left,
						R.drawable.menu_window_mid,
						R.drawable.menu_window_right, categorys,
						classSelectListener);
			}
			popMenuClassSelect.show(v, this);
		}
	}

	private LinkedHashMap<Integer, String> orderbys;
	private PopMenuOrderbySelect popMenuOrderbySelect;
	private OnOrderbySelectListener orderbySelectListener = new OnOrderbySelectListener() {

		@Override
		public void onOrderbySelect(int id, String name) {
			// TODO Auto-generated method stub
			tvOrderby.setText(name);
			tvOrderby.setTextColor(getResources().getColor(
					R.color.item_text_normal));
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			tvOrderby.setTextColor(getResources().getColor(
					R.color.item_text_normal));
		}
	};

	private void selectOrderby(View v) {
		if (orderbys != null && !orderbys.isEmpty()) {
			tvOrderby.setTextColor(getResources().getColor(
					R.color.item_text_press));
			if (popMenuOrderbySelect == null) {
				popMenuOrderbySelect = new PopMenuOrderbySelect(this,
						R.drawable.menu_window_left,
						R.drawable.menu_window_mid,
						R.drawable.menu_window_right, orderbys,
						orderbySelectListener);
			}
			popMenuOrderbySelect.show(v, this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_class:
			selectClass(v);
			break;
		case R.id.view_orderby:
			if (orderbys == null) {
				orderbys = new LinkedHashMap<Integer, String>();
				orderbys.put(0, "综合排序");
				orderbys.put(1, "人气排序");
				orderbys.put(2, "价格从高到低");
				orderbys.put(3, "价格从低到高");
			}

			selectOrderby(v);
			break;
		}
	}
}
