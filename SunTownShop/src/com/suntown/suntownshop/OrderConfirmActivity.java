package com.suntown.suntownshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.FragmentPage1.ViewHolder;
import com.suntown.suntownshop.adapter.CouponUseAdapter;
import com.suntown.suntownshop.adapter.CouponUseAdapter.CouponChangeListener;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Coupon;
import com.suntown.suntownshop.model.OrderGoods;
import com.suntown.suntownshop.model.Receiver;
import com.suntown.suntownshop.model.Store;
import com.suntown.suntownshop.utils.JsonBuilder;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
/**
 * 确认订单页面
 *
 * @author 钱凯
 * @version 2015年6月8日 下午4:09:58
 *
 */
public class OrderConfirmActivity extends Activity implements OnClickListener {
	private String mUserId;
	private String mLoginToken;
	private LinearLayout llGoodsDeliverSelf;
	private LinearLayout llGoodsDeliverMarket;
	private Receiver mReceiver;
	private ArrayList<Coupon> coupons;
	private LinkedHashMap<String, Coupon> useCoupons;
	private CouponUseAdapter adapter;
	private ListView lvCoupons;
	private double mAmount;
	private double payAmount;
	private View viewAddress;
	private TextView tvAmount;
	private View loading;
	private View main;
	private View showCoupons;
	private ImageView cbShowCoupons;
	private ImageView ivShowCoupons;
	private TextView tvShowCouponsCount;
	private boolean isCouponOk = false;
	private boolean isAddressOk = false;
	private boolean isShowCoupon = false;
	private String mStoreId;
	private String mStoreName;
	private TextView tvShopNameS;
	private TextView tvShopNameM;

	/**
	 * imageloader相关
	 */
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.picture_loading_200x200)
				.showImageForEmptyUri(R.drawable.picture_noimg_200x200)
				.showImageOnFail(R.drawable.picture_holder_200x200)
				.cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		mUserId = sharedPreferences.getString("userId", "");
		mLoginToken = sharedPreferences.getString("m_voucher", "");

		mStoreId = sharedPreferences.getString("shopid", "");
		mStoreName = sharedPreferences.getString("shopfullname", "");
		if (!isLogin || "".equalsIgnoreCase(mUserId)) {
			Intent intent = new Intent(OrderConfirmActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		initOptions();
		Intent intent = getIntent();
		main = findViewById(R.id.main);
		loading = findViewById(R.id.loading);
		loading.setVisibility(View.VISIBLE);

		main.setVisibility(View.GONE);
		mAmount = intent.getDoubleExtra("amount", 0.0);
		payAmount = mAmount;
		tvAmount = (TextView) findViewById(R.id.tv_order_pay_amount);
		tvAmount.setText(String.format("%.2f", mAmount));
		llGoodsDeliverSelf = (LinearLayout) findViewById(R.id.ll_deliver_self);
		llGoodsDeliverMarket = (LinearLayout) findViewById(R.id.ll_deliver_market);
		lvCoupons = (ListView) findViewById(R.id.lv_coupons);
		viewAddress = findViewById(R.id.view_address);
		viewAddress.setOnClickListener(this);
		useCoupons = new LinkedHashMap<String, Coupon>();
		showCoupons = findViewById(R.id.view_coupons);
		cbShowCoupons = (ImageView) findViewById(R.id.cb_showcoupon);
		ivShowCoupons = (ImageView) findViewById(R.id.iv_showcoupon);
		tvShowCouponsCount = (TextView) findViewById(R.id.tv_showcouponcount);
		tvShopNameS = (TextView) findViewById(R.id.tv_shopname_s);
		tvShopNameM = (TextView) findViewById(R.id.tv_shopname_m);
		showCoupons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCoupons();
			}
		});
		initCoupon();
		initAddress();
		initData();
	}

	private void showCoupons() {
		if (coupons != null && coupons.size() > 0) {
			isShowCoupon = !isShowCoupon;
			if (!isShowCoupon) {
				lvCoupons.setVisibility(View.GONE);
				ivShowCoupons.setImageResource(R.drawable.icon_arrow_down);
				cbShowCoupons.setImageResource(R.drawable.radio_n);
			} else {
				lvCoupons.setVisibility(View.VISIBLE);
				ivShowCoupons.setImageResource(R.drawable.icon_arrow_up);
				cbShowCoupons.setImageResource(R.drawable.radio_y);
			}
		}
	}

	private final static String URL_COUPON = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getMemticket";

	private void initCoupon() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", mUserId);
		params.put("logintoken", mLoginToken);
		params.put("type", "0");
		params.put("startIndex", "1");
		params.put("length", "1000");
		params.put("tickettype", "1");
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_COUPON,
				callbackCoupon);
		postAsyncTask.execute(params);
	}

	private void calculateAmount() {
		double discount = 0.0;
		Set<Entry<String, Coupon>> sets = useCoupons.entrySet();
		for (Entry<String, Coupon> entry : sets) {
			Coupon coupon = entry.getValue();
			discount += coupon.getDenomination();
		}
		payAmount = mAmount - discount;
		if (payAmount < 0) {
			payAmount = 0;
		}
		tvAmount.setText(String.format("%.2f", payAmount));
	}

	private OnCompleteCallback callbackCoupon = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			// showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");

					if (sendState == 0) {
						// 取得订单数据，开始解析
						JSONArray jsonArray = jsonObj.getJSONArray("TICKET");
						coupons = JsonParser.couponsParse(jsonArray);

						if (coupons.size() > 0) {
							CouponChangeListener listener = new CouponChangeListener() {

								@Override
								public void onChange() {
									// TODO Auto-generated method stub
									calculateAmount();
								}
							};
							adapter = new CouponUseAdapter(
									OrderConfirmActivity.this, coupons,
									useCoupons, listener);
							lvCoupons.setAdapter(adapter);
							tvShowCouponsCount.setText(coupons.size() + "张可用");
						} else {
							tvShowCouponsCount.setText("0张可用");
							tvShowCouponsCount
									.setBackgroundResource(R.color.bg_color);
						}
					}

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "优惠券获取错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();

					e.printStackTrace();
				}
				isCouponOk = true;
				if (isAddressOk) {
					initViews();
				}
			} else {
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	private void initAddress() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", mUserId);
		params.put("logintoken", mLoginToken);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
		postAsyncTask.execute(params);
	}

	private void initViews() {
		loading.setVisibility(View.GONE);
		main.setVisibility(View.VISIBLE);

		if (mStoreId == null || "".equals(mStoreId) || mStoreName == null
				|| "".equals(mStoreName)) {
			tvShopNameS.setText("请指定超市");
			tvShopNameM.setText("请指定超市");
			getStores();
		} else {
			tvShopNameS.setText(mStoreName);
			tvShopNameM.setText(mStoreName);
		}
	}

	private void getStores() {
		if (listStore.size() > 0) {
			selectStore();
		} else {
			showProgress(true);
			HashMap<String, String> params = new HashMap<String, String>();
			// params.put("strMsg", strJson);
			PostAsyncTask postAsyncTask = new PostAsyncTask(URL_GET_ALLSHOP,
					getAllshopCallback);
			postAsyncTask.execute(params);
		}
	}

	private void fillAddress() {
		TextView tvName = (TextView) findViewById(R.id.tv_receiver_name);
		TextView tvPhone = (TextView) findViewById(R.id.tv_receiver_phone);
		TextView tvAddress = (TextView) findViewById(R.id.tv_receiver_address);
		if (mReceiver != null) {
			tvName.setText(mReceiver.getName());
			tvPhone.setText(mReceiver.getPhone());
			tvAddress.setText(mReceiver.getAddress());
			isAddressOk = true;
			if (isCouponOk) {
				initViews();
			}
		} else {
			ConfirmDialog dialog = new ConfirmDialog(OrderConfirmActivity.this,
					"没有收货地址，现在设置吗?", getString(R.string.tips_text),
					getString(R.string.confirm_text),
					getString(R.string.cancel_text));
			if (dialog.ShowDialog()) {
				Intent intent = new Intent(OrderConfirmActivity.this,
						AddressManageActivity.class);
				startActivity(intent);
				finish();
			} else {
				finish();
			}
		}
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getAllAddress";

	private OnCompleteCallback callback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			// showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						// 取得地址数据，开始解析
						JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObj = (JSONObject) jsonArray.opt(i);
							int id = jsonObj.getInt("ID");
							String memid = jsonObj.getString("MEMID");
							String name = jsonObj.getString("RECEIVER");
							String phone = jsonObj.getString("PHONE");
							String address = jsonObj.getString("ADDRESS");
							boolean isDefault = jsonObj.getInt("ISDEFAULT") == 1 ? true
									: false;
							if (isDefault) {
								mReceiver = new Receiver(id, memid, name,
										phone, address, isDefault);
								break;
							}
							if (i == 0) {
								mReceiver = new Receiver(id, memid, name,
										phone, address, isDefault);
							}
						}

						fillAddress();

					} else {
						Toast.makeText(getApplicationContext(),
								"登录状态已失效，请重新登录...", Toast.LENGTH_SHORT).show();
						finish();
					}

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "服务器返回错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();

					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initData() {
		ShopCartDb scdb = new ShopCartDb(OrderConfirmActivity.this, mUserId);
		ArrayList<CartGoods> listSelf = scdb
				.getAllChecked(CartGoods.DEVILER_TYPE_SELF);
		ArrayList<CartGoods> listMarket = scdb
				.getAllChecked(CartGoods.DEVILER_TYPE_MARKET);
		scdb.Close();
		if (listSelf.size() > 0) {
			llGoodsDeliverSelf.setVisibility(View.VISIBLE);
			fillGoods(llGoodsDeliverSelf, listSelf);
		} else {
			llGoodsDeliverSelf.setVisibility(View.GONE);
		}
		if (listMarket.size() > 0) {
			llGoodsDeliverMarket.setVisibility(View.VISIBLE);
			fillGoods(llGoodsDeliverMarket, listMarket);
		} else {
			llGoodsDeliverMarket.setVisibility(View.GONE);
		}
	}

	private void fillGoods(LinearLayout ll, ArrayList<CartGoods> list) {
		int count = list.size();
		View view;
		int goodsCount = 0;
		double goodsAmount = 0.0;
		for (int i = 0; i < count; i++) {
			final CartGoods goods = list.get(i);

			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.order_detail_item, null);
			TextView tvName = (TextView) view
					.findViewById(R.id.tv_name_order_detail);

			TextView tvPrice = (TextView) view
					.findViewById(R.id.tv_price_order_detail);
			TextView tvQuantity = (TextView) view
					.findViewById(R.id.tv_quantity_order_detail);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_order_detail);
			TextView tvDeliver = (TextView) view
					.findViewById(R.id.tv_delivertype);
			tvDeliver.setVisibility(View.GONE);
			String imgPath = goods.getImagePath();
			if (imgPath == null || "".equals(imgPath)) {
				iv.setImageResource(R.drawable.picture_noimg_200x200);
			} else {
				imageLoader.displayImage("http://" + imgPath, iv, options);
			}
			tvName.setText(goods.getName());
			tvPrice.setText(String.format("%.2f", goods.getPrice()));
			tvQuantity.setText(goods.getQuantity() + "");
			goodsCount = goodsCount + goods.getQuantity();
			goodsAmount = goodsAmount
					+ (goods.getPrice() * goods.getQuantity());
			ll.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showGoodsDetail(goods.getBarCode());
				}
			});
		}
		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.order_detail_subtotal, null);
		TextView tvSubCount = (TextView) view.findViewById(R.id.tv_subcount);
		TextView tvSubAmount = (TextView) view.findViewById(R.id.tv_subamount);
		tvSubCount.setText(goodsCount + "件");
		tvSubAmount.setText(String.format("%.2f", goodsAmount));
		ll.addView(view);
	}

	private boolean buildOrder(String userId) {
		try {
			if (mReceiver == null) {
				Toast.makeText(this, "没有收货地址,请设置收货地址", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			ShopCartDb scdb = new ShopCartDb(OrderConfirmActivity.this, mUserId);
			ArrayList<CartGoods> goods = scdb.getAllChecked();
			scdb.Close();

			if (!goods.isEmpty()) {

				String strJson = JsonBuilder.makeOrderJson("", mStoreId,
						userId, "0", mReceiver.getId(), useCoupons, goods);
				System.out.println("odermsg------>" + strJson);
				if (strJson != null && !"".equals(strJson)) {
					showProgress(true);
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("strMsg", strJson);
					PostAsyncTask postAsyncTask = new PostAsyncTask(
							URL_BUILDORDER, buildOrderCallback);
					postAsyncTask.execute(params);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private String URL_GET_ALLSHOP = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getAllShop";
	private OnCompleteCallback getAllshopCallback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub

			showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				JSONArray jsonArray;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					if (jsonObj != null) {
						int state = jsonObj.getInt("RESULT");
						if (state == 0) {
							jsonArray = jsonObj.getJSONArray("SHOP");
							String storeId;
							String storeSName;
							String storeFName;
							String floors;
							String floorNames;
							JSONArray jsonFloors;
							listStore.clear();
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObj = jsonArray.optJSONObject(i);
								storeId = jsonObj.getString("SID");
								storeSName = jsonObj.getString("SNAME");
								storeFName = jsonObj.getString("FNAME");
								jsonFloors = jsonObj.getJSONArray("FLOOR");
								floors = "";
								floorNames = "";
								if (jsonFloors != null
										|| jsonFloors.length() > 0) {
									for (int j = 0; j < jsonFloors.length(); j++) {
										jsonObj = jsonFloors.optJSONObject(j);
										if (j > 0) {
											floors += ";";
											floorNames += ";";
										}
										floors += jsonObj.getString("FLOORNO");
										floorNames += jsonObj
												.getString("FLOORNAME");
									}
								}
								Store store = new Store(storeId, storeSName,
										storeFName, floors, floorNames);
								listStore.add(store);
							}
							if (listStore.size() > 0) {
								selectStore();
							}
						}
					}
				} catch (Exception e) {
					Toast.makeText(OrderConfirmActivity.this,
							"服务器返回错误，请稍后重试...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(OrderConfirmActivity.this, "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
				System.out.println(msg);
			}

		}
	};

	private final static String URL_BUILDORDER = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/bulidOrder";

	private OnCompleteCallback buildOrderCallback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub

			showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					System.out.println("buildOrder------>" + msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						String orderNo = jsonObj.getString("FORMNO");

						try {
							ShopCartDb scdb = new ShopCartDb(
									OrderConfirmActivity.this, mUserId);
							scdb.deleteAllChecked();
							scdb.Close();
							Intent intent = new Intent(
									OrderConfirmActivity.this,
									OrderPayActivity.class);
							Bundle b = new Bundle();
							b.putDouble("amount", payAmount);
							b.putString("orderno", orderNo);
							intent.putExtras(b);
							startActivity(intent);
							finish();

						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(OrderConfirmActivity.this,
									e.getMessage(), Toast.LENGTH_LONG).show();
						}
						Toast.makeText(OrderConfirmActivity.this, "生成订单成功!",
								Toast.LENGTH_SHORT).show();
					} else if (sendState == -1) {

						Toast.makeText(OrderConfirmActivity.this,
								"价格发生变化，请重试!", Toast.LENGTH_SHORT).show();
					} else {

						Toast.makeText(OrderConfirmActivity.this,
								"生成订单失败，请重试!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(OrderConfirmActivity.this,
							"服务器返回错误，请稍后重试...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(OrderConfirmActivity.this, "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
				System.out.println(msg);
			}

		}
	};

	/**
	 * 根据条形码跳转到商品详情页
	 * 
	 * @param barCode
	 */
	private void showGoodsDetail(String barCode) {
		Intent intent = new Intent(this, GoodsDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("barCode", barCode);
		intent.putExtras(b);
		startActivity(intent);
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

	public void close(View v) {
		finish();
	}

	public void confirm(View v) {
		if (mStoreId == null || "".equals(mStoreId)) {
			Toast.makeText(this, "请选择您所在的超市", Toast.LENGTH_SHORT).show();
			getStores();
		} else {
			buildOrder(mUserId);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_address:
			Intent intent = new Intent(OrderConfirmActivity.this,
					AddressManageActivity.class);
			intent.putExtra("select", true);
			if (mReceiver != null) {
				intent.putExtra("id", mReceiver.getId());
			}
			startActivityForResult(intent,
					AddressManageActivity.INTENT_CODE_SELECTING);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == AddressManageActivity.INTENT_CODE_SELECTING) {
			int id = data.getIntExtra("id", 0);
			String memid = data.getStringExtra("memid");
			String name = data.getStringExtra("name");
			String phone = data.getStringExtra("phone");
			String address = data.getStringExtra("address");
			boolean isDefault = data.getBooleanExtra("isdefault", false);
			mReceiver = new Receiver(id, memid, name, phone, address, isDefault);
			fillAddress();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private PopupWindow pw;
	private ArrayList<Store> listStore = new ArrayList<Store>();

	static class ViewHolder {
		TextView tvShopName;
	}

	private BaseAdapter adapterStore = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) OrderConfirmActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.shop_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvShopName = (TextView) convertView
						.findViewById(R.id.tv_shop);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvShopName
					.setText(listStore.get(position).getFullName());
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listStore.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listStore.size();
		}
	};

	private void selectStore() {

		LayoutInflater inflater = (LayoutInflater) OrderConfirmActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.select_shop_popup_orderconfirm, null);

		if (pw == null) {
			// 生成PopupWindow对象
			pw = new PopupWindow(mView, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			pw.setOutsideTouchable(true);
		} else {
			pw.setContentView(mView);
		}
		pw.setFocusable(true);

		pw.setBackgroundDrawable(new ColorDrawable(0x7f000000));
		ListView lv = (ListView) mView.findViewById(R.id.lv_shop);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
				Store store = listStore.get(position);
				SharedPreferences mSharedPreferences = getSharedPreferences(
						"suntownshop", 0);
				SharedPreferences.Editor mEditor = mSharedPreferences.edit();
				mEditor.putString("shopid", store.getId());
				mEditor.putString("shopfullname", store.getFullName());
				mEditor.putString("floors", store.getFloors());
				mEditor.putString("floornames", store.getFloorNames());
				mEditor.commit();
				tvShopNameS.setText(store.getFullName());
				tvShopNameM.setText(store.getFullName());
			}
		});

		lv.setAdapter(adapterStore);
		// 在指定位置弹出窗口
		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
			}
		});

		pw.showAtLocation(main, Gravity.CENTER, 0, 0);
	}
}
