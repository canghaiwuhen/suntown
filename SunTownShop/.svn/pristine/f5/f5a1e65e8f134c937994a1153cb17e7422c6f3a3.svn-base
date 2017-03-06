package com.suntown.suntownshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.db.OrderDb;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 订单列表页面
 *
 * @author 钱凯
 * @version 2015年1月17日 上午10:47:54
 *
 */
public class MyOrdersActivity extends Activity {
	private ListView listview;
	// private HashMap<Integer, ArrayList<Order>> maps;
	private ArrayList<Order> list;
	private View loading;
	private View viewEmpty;
	private int orderType;
	private TextView tvEmpty;
	private PostAsyncTask postAsyncTask;
	String userId;
	private Button[] tvs;
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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initOptions();
		setContentView(R.layout.activity_orders);
		listview = (ListView) findViewById(R.id.lv_orders);
		loading = findViewById(R.id.loading);
		viewEmpty = findViewById(R.id.empty_chopcart);
		ImageView ivEmpty = (ImageView) findViewById(R.id.iv_emptyimg);
		ivEmpty.setImageResource(R.drawable.order_list_empty);
		tvEmpty = (TextView) findViewById(R.id.tv_emptymsg);
		tvEmpty.setText(getString(R.string.shopcart_empty_msg));
		Intent intent = getIntent();
		if (!intent.hasExtra("userId")) {
			Toast.makeText(this, "请重新登录！", Toast.LENGTH_SHORT).show();
			finish();
		}
		Bundle b = intent.getExtras();
		userId = b.getString("userId");
		orderType = b.getInt("ordertype");
		/*
		 * if(orderType==0){ TextView tvTitle=
		 * (TextView)findViewById(R.id.tv_orders_title);
		 * tvTitle.setText("未完成订单"); View viewType =
		 * findViewById(R.id.view_type); viewType.setVisibility(View.GONE); }
		 */
		TextView tvTitle = (TextView) findViewById(R.id.tv_orders_title);
		tvs = new Button[4];
		tvs[0] = (Button) findViewById(R.id.tv_unpay);
		tvs[1] = (Button) findViewById(R.id.tv_done);
		tvs[2] = (Button) findViewById(R.id.tv_close);
		tvs[3] = (Button) findViewById(R.id.tv_refund);
		tvs[orderType].setTextColor(getResources().getColor(R.color.orange));
		tvs[orderType].setBackground(getResources().getDrawable(
				R.drawable.style_line_bottom_orange));
		// maps = new HashMap<Integer, ArrayList<Order>>();
		list = new ArrayList<Order>();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
				Order order = list.get(position);
				Intent intent = new Intent(MyOrdersActivity.this,
						OrderDetailActivity.class);
				Bundle b = new Bundle();
				b.putString("orderno", order.getOrderNo());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getOrdersById(userId);
		super.onResume();
	}

	@SuppressLint("NewApi")
	public void refresh(View v) {
		for (int i = 0; i < tvs.length; i++) {

			tvs[i].setTextColor(getResources().getColor(R.color.black));
			tvs[i].setBackground(getResources().getDrawable(
					R.drawable.style_line_bottom_3));
			if (tvs[i].getId() == v.getId()) {
				orderType = i;
				tvs[i].setTextColor(getResources().getColor(R.color.orange));
				tvs[i].setBackground(getResources().getDrawable(
						R.drawable.style_line_bottom_orange));
				getOrdersById(userId);
			}
		}

	}

	private void getOrdersById(String uId) {
		listview.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		viewEmpty.setVisibility(View.GONE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", uId);
		params.put("formstatus", String.valueOf(orderType));
		if (postAsyncTask != null) {
			postAsyncTask.cancel(true);
		}
		postAsyncTask = new PostAsyncTask(URL, callback);
		postAsyncTask.execute(params);
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getHistoryOrder";

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
					// ArrayList<Order> list;
					if (sendState == 0) {
						// 取得订单数据，开始解析
						JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
						list = JsonParser.ordersParse(jsonArray);
					} else {
						list = new ArrayList<Order>();
					}
					System.out.println("type:" + orderType + " size:"
							+ list.size());
					// maps.put(Integer.valueOf(orderType), list);
					initViews();
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
	private int cancelIndex = -1;

	private void cancelOrder(int index) {
		ConfirmDialog dialog = new ConfirmDialog(MyOrdersActivity.this,
				"确定要取消此订单吗?", getString(R.string.tips_text),
				getString(R.string.confirm_text),
				getString(R.string.cancel_text));
		if (dialog.ShowDialog()) {
			// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
			cancelIndex = index;
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"suntownshop", 0);
			String mVoucher = mSharedPreferences.getString("m_voucher", "");
			Order order = list.get(index);
			showProgress(true);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("formno", order.getOrderNo());
			params.put("logintoken", mVoucher);

			PostAsyncTask postAsyncTask = new PostAsyncTask(URL_CALCEL_ORDER,
					cancelOrderCallback);
			postAsyncTask.execute(params);
		}
	}

	private final static String URL_CALCEL_ORDER = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/closeOrder";// Constants.DOMAIN_NAME

	private OnCompleteCallback cancelOrderCallback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						// 取得订单数据，开始解析
						if (cancelIndex >= 0) {
							// ArrayList<Order> list =
							// maps.get(Integer.valueOf(orderType));
							list.remove(cancelIndex);
							cancelIndex = -1;
						}
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "取消订单成功!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"取消订单失败，请稍后重试...", Toast.LENGTH_SHORT).show();
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

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(MyOrdersActivity.this);
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

	private void initViews() {
		listview.setAdapter(adapter);
		loading.setVisibility(View.GONE);
		if (list == null || list.size() <= 0) {
			listview.setVisibility(View.GONE);
			tvEmpty.setText("没有" + tvs[orderType].getText() + "的订单");
			viewEmpty.setVisibility(View.VISIBLE);
		} else {
			loading.setVisibility(View.GONE);
			viewEmpty.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}

	}

	static class ViewHolder {
		TextView tvOrderNo;
		TextView tvTime;
		TextView tvAmount;
		TextView tvStoreName;
		TextView tvOrderStatus;
		LinearLayout viewImgs;
		View viewModify;

		Button btnPay;
		Button btnEva;
		Button btnRfd;
		Button btnCancel;
		ImageView ivSignet;
	}

	private BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.orders_item, null);
				holder = new ViewHolder();
				holder.tvOrderNo = (TextView) convertView
						.findViewById(R.id.tv_order_no);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_order_time);
				holder.tvAmount = (TextView) convertView
						.findViewById(R.id.tv_order_amount);
				holder.tvStoreName = (TextView) convertView
						.findViewById(R.id.tv_storename);
				holder.tvOrderStatus = (TextView) convertView
						.findViewById(R.id.tv_orderstatus);
				holder.viewImgs = (LinearLayout) convertView
						.findViewById(R.id.view_imgs);
				holder.viewModify = convertView.findViewById(R.id.view_modify);

				holder.btnCancel = (Button) convertView
						.findViewById(R.id.btn_myorder_item_cancel);
				holder.btnPay = (Button) convertView
						.findViewById(R.id.btn_myorder_item_pay);
				holder.btnEva = (Button) convertView
						.findViewById(R.id.btn_myorder_item_evaluate);
				holder.btnRfd = (Button) convertView
						.findViewById(R.id.btn_myorder_item_refund);
				holder.ivSignet = (ImageView) convertView
						.findViewById(R.id.iv_signet);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
			Order order = list.get(position);
			String storeName = order.getStoreName();
			if (storeName == null || "".equals(storeName)) {
				storeName = "未知门店";
			}
			holder.tvStoreName.setText(storeName);
			holder.tvOrderNo.setText(order.getOrderNo());
			holder.tvTime.setText(order.getDate());
			holder.tvAmount.setText("￥"
					+ String.format("%.2f", order.getAmount()));
			LinkedHashMap<String, String> infos = order.getOrderInfo();
			List<Entry<String, String>> data = new ArrayList<Entry<String, String>>(
					infos.entrySet());
			holder.viewImgs.removeAllViews();
			for (int i = 0; i < data.size(); i++) {
				String imgPath = data.get(i).getValue();
				ImageView iv = new ImageView(getApplicationContext());

				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				LayoutParams params = iv.getLayoutParams();
				params.height = Constants.displayWidth * 4 / 30;
				params.width = Constants.displayWidth * 4 / 30;

				iv.setLayoutParams(params);
				iv.setBackgroundResource(R.drawable.style_line_around);
				if (imgPath == null || "".equals(imgPath)) {
					iv.setImageResource(R.drawable.picture_noimg_200x200);
				} else {
					imageLoader.displayImage("http://" + imgPath, iv, options);
				}
				LinearLayout ll = new LinearLayout(getApplicationContext());
				ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				ll.setBackgroundResource(R.drawable.style_padding_around);
				ll.addView(iv);

				holder.viewImgs.addView(ll);
			}

			// 如果已支付完成则隐藏修改菜单
			if (order.getOrderStatus() == 0) {
				holder.btnPay.setVisibility(View.VISIBLE);
				holder.btnCancel.setVisibility(View.VISIBLE);
				holder.btnEva.setVisibility(View.GONE);
				holder.btnRfd.setVisibility(View.GONE);
				holder.ivSignet.setVisibility(View.GONE);
				holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
						.getResources().getColor(R.color.red));
				holder.tvOrderStatus.setText("待付款");
				holder.btnPay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showOrderPay(position);
					}
				});

				holder.btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancelOrder(position);
					}
				});
			} else {
				holder.btnPay.setVisibility(View.GONE);
				holder.btnCancel.setVisibility(View.GONE);
				if (order.getOrderStatus() == 1) {
					holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
							.getResources().getColor(R.color.greyfont));
					holder.tvOrderStatus.setText("已完成");
					holder.btnRfd.setVisibility(View.VISIBLE);
					holder.btnRfd.setText("申请退货");
					holder.ivSignet.setVisibility(View.GONE);
					holder.btnRfd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showRefund(position);
						}
					});
				} else if (order.getOrderStatus() == 2) {
					holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
							.getResources().getColor(R.color.greyfont));
					holder.tvOrderStatus.setText("已关闭");
					holder.btnRfd.setVisibility(View.GONE);
					holder.ivSignet.setVisibility(View.GONE);

				} else {
					if (order.getOrderStatus() == 3) {
						holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
								.getResources().getColor(R.color.red));
						holder.tvOrderStatus.setText("退货中");
					} else if (order.getOrderStatus() == 4) {
						holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
								.getResources().getColor(R.color.greyfont));
						holder.tvOrderStatus.setText("已退货");
					} else if(order.getOrderStatus()==5) {
						holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
								.getResources().getColor(R.color.red));
						holder.tvOrderStatus.setText("被拒绝退货");
					}else{
						holder.tvOrderStatus.setTextColor(MyOrdersActivity.this
								.getResources().getColor(R.color.greyfont));
						holder.tvOrderStatus.setText("已取消退货");
					}
					holder.btnRfd.setVisibility(View.VISIBLE);
					holder.btnRfd.setText("退货记录");
					holder.btnRfd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showRefundPath(position);
						}
					});
				}

				if (order.getEvaStatus() == order.EVA_STATUS_UNDO
						&& order.getOrderStatus() == 1) {
					holder.btnEva.setVisibility(View.VISIBLE);
					holder.btnEva.setText("发表评价");
				} else if (order.getEvaStatus() == order.EVA_STATUS_DONE) {
					holder.btnEva.setVisibility(View.VISIBLE);
					holder.btnEva.setText("查看评价");
				} else {
					holder.btnEva.setVisibility(View.GONE);
				}
				holder.btnEva.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showOrderEvaluate(position);
					}
				});

			}

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
			// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
			return list.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
			return list.size();
		}
	};

	private void showRefundPath(int index) {
		// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
		Order order = list.get(index);
		Intent intent = new Intent(MyOrdersActivity.this,
				RefundDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", order.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	private void showRefund(int index) {
		// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
		Order order = list.get(index);
		Intent intent = new Intent(MyOrdersActivity.this, RefundActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", order.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	private void showOrderEvaluate(int index) {
		// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
		Order order = list.get(index);
		Intent intent = new Intent(MyOrdersActivity.this,
				EvaluateActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", order.getOrderNo());
		if (order.getEvaStatus() == order.EVA_STATUS_UNDO) {
			b.putBoolean("status", true);
		} else {
			b.putBoolean("status", false);
		}
		intent.putExtras(b);
		startActivity(intent);
	}

	private void showOrderPay(int index) {
		// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
		Order order = list.get(index);
		Intent intent = new Intent(MyOrdersActivity.this,
				OrderPayActivity.class);
		Bundle b = new Bundle();
		b.putDouble("amount", order.getAmount());
		b.putString("orderno", order.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	public void close(View v) {
		finish();
	}

}
