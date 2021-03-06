package com.suntown.suntownshop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.db.OrderDb;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.model.OrderGoods;
import com.suntown.suntownshop.utils.FileManager;
import com.suntown.suntownshop.utils.ImageUtil;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.JustifyTextView;
import com.suntown.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 订单详情
 *
 * @author 钱凯
 * @version 2015年3月21日 上午9:45:04
 *
 */
public class OrderDetailActivity extends Activity {
	Order mOrder;
	View viewLoading;
	View viewOrder;
	View viewDivider;
	private String mOrderNo;
	private TextView tvStoreName;
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
				.cacheInMemory(true).cacheOnDisk(true)
				// .considerExifParams(true)
				// .displayer(new SimpleBitmapDisplayer())
				.build();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initOptions();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		Intent intent = getIntent();
		viewLoading = findViewById(R.id.loading);
		viewOrder = findViewById(R.id.sv_order_detail);
		viewDivider = findViewById(R.id.view_divider);
		tvStoreName = (TextView) findViewById(R.id.tv_order_detail_store);
		if (intent.hasExtra("orderno")) {
			Bundle b = intent.getExtras();
			mOrderNo = b.getString("orderno");

		} else {
			Toast.makeText(this, getString(R.string.orderno_err),
					Toast.LENGTH_SHORT).show();
			finish();
		}
		initData();
	}

	private void initData() {
		viewOrder.setVisibility(View.GONE);
		viewDivider.setVisibility(View.GONE);
		viewLoading.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("formno", mOrderNo);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
		postAsyncTask.execute(params);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getHistoryOrderDetail";

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
						// 取得订单数据，开始解析
						mOrder = JsonParser.orderParse(jsonObj);
						initView();
					} else {
						Toast.makeText(getApplicationContext(), "该订单不存在或已取消!",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "服务器返回错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	private void showRefund() {
		Intent intent = new Intent(this, RefundActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", mOrder.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	private void showOrderEvaluate() {
		Intent intent = new Intent(this, EvaluateActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", mOrder.getOrderNo());
		if (mOrder.getEvaStatus() == mOrder.EVA_STATUS_UNDO) {
			b.putBoolean("status", true);
		} else {
			b.putBoolean("status", false);
		}
		intent.putExtras(b);
		startActivity(intent);
	}

	private void cancelOrder() {
		ConfirmDialog dialog = new ConfirmDialog(OrderDetailActivity.this,
				"确定要取消此订单吗?", getString(R.string.tips_text),
				getString(R.string.confirm_text),
				getString(R.string.cancel_text));
		if (dialog.ShowDialog()) {
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"suntownshop", 0);
			String mVoucher = mSharedPreferences.getString("m_voucher", "");
			showProgress(true);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("formno", mOrder.getOrderNo());
			params.put("logintoken", mVoucher);
			PostAsyncTask postAsyncTask = new PostAsyncTask(URL_CALCEL_ORDER,
					cancelOrderCallback);
			postAsyncTask.execute(params);
		}
	}

	private final static String URL_CALCEL_ORDER = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/closeOrder"; // Constants.DOMAIN_NAME

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
						Toast.makeText(getApplicationContext(), "取消订单成功!",
								Toast.LENGTH_SHORT).show();
						finish();
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
			mPDialog = new ProgressDialog(OrderDetailActivity.this);
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

	/**
	 * 根据条形码跳转到商品详情页
	 * 
	 * @param barCode
	 */
	private void showGoodsDetail(String barCode) {
		Intent intent = new Intent(OrderDetailActivity.this,
				GoodsDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("barCode", barCode);
		intent.putExtras(b);
		startActivity(intent);
	}

	private void initView() {
		try {

			ArrayList<OrderGoods> listDBC = mOrder.getOrderGoodsDBC();
			ArrayList<OrderGoods> listDBM = mOrder.getOrderGoodsDBM();
			tvStoreName.setText(mOrder.getStoreName());
			String time = mOrder.getDate();
			TextView tvTime = (TextView) findViewById(R.id.tv_order_detail_time);
			tvTime.setText(time);
			TextView tvOrderNo = (TextView) findViewById(R.id.tv_order_detail_no);
			tvOrderNo.setText(mOrder.getOrderNo());
			ImageView ivQrCode = (ImageView) findViewById(R.id.iv_qrcode);
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
					mOrder.getOrderNo(), 350);
			ivQrCode.setImageBitmap(qrCodeBitmap);
			ImageView ivBarCode = (ImageView) findViewById(R.id.iv_barcode);
			qrCodeBitmap = EncodingHandler.CreateBarDCode(mOrder.getOrderNo());
			ivBarCode.setImageBitmap(qrCodeBitmap);

			JustifyTextView tvBarCode = (JustifyTextView) findViewById(R.id.tv_barcode);
			tvBarCode.setText(mOrder.getOrderNo());
			double amount = mOrder.getAmount();
			TextView tvAmount = (TextView) findViewById(R.id.tv_order_detail_amount);
			tvAmount.setText("￥" + String.format("%.2f", amount));

			// 如果已支付完成则隐藏修改菜单
			ImageView ivTrash = (ImageView) findViewById(R.id.iv_trash);
			;
			Button btnPay = (Button) findViewById(R.id.btn_myorder_item_pay);
			;
			Button btnEva = (Button) findViewById(R.id.btn_myorder_item_evaluate);
			;
			Button btnRfd = (Button) findViewById(R.id.btn_myorder_item_refund);
			;
			// ImageView ivSignet = (ImageView)findViewById(R.id.iv_signet);;

			if (mOrder.getOrderStatus() == 0) {
				btnPay.setVisibility(View.VISIBLE);
				ivTrash.setVisibility(View.VISIBLE);
				btnEva.setVisibility(View.GONE);
				btnRfd.setVisibility(View.GONE);
				// ivSignet.setVisibility(View.GONE);
				btnPay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showOrderPay();
					}
				});

				ivTrash.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancelOrder();
					}
				});
			} else {
				btnPay.setVisibility(View.GONE);
				ivTrash.setVisibility(View.GONE);
				if (mOrder.getOrderStatus() == 1) {
					btnRfd.setVisibility(View.VISIBLE);
					btnRfd.setText("我要退货");
					// ivSignet.setVisibility(View.VISIBLE);
					btnRfd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showRefund();
						}
					});
				} else if (mOrder.getOrderStatus() == 3
						|| mOrder.getOrderStatus() == 4
						|| mOrder.getOrderStatus() == 5) {
					btnRfd.setVisibility(View.VISIBLE);
					if (mOrder.getOrderStatus() == 4) {
						btnRfd.setText("退货记录");
					} else {
						btnRfd.setText("退货记录");
					}
					btnRfd.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showRefundPath();
						}
					});
				} else {
					btnRfd.setVisibility(View.GONE);
					// ivSignet.setVisibility(View.GONE);
				}

				if (mOrder.getEvaStatus() == Order.EVA_STATUS_UNDO
						&& mOrder.getOrderStatus() == 1) {
					btnEva.setVisibility(View.VISIBLE);
					btnEva.setText("发表评价");
				} else if (mOrder.getEvaStatus() == Order.EVA_STATUS_DONE) {
					btnEva.setVisibility(View.VISIBLE);
					btnEva.setText("查看评价");
				} else {
					btnEva.setVisibility(View.GONE);
				}
				btnEva.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showOrderEvaluate();
					}
				});

			}
			LinearLayout llGoodsDeliverSelf = (LinearLayout) findViewById(R.id.ll_deliver_self);
			LinearLayout llGoodsDeliverMarket = (LinearLayout) findViewById(R.id.ll_deliver_market);
			View viewShipping = findViewById(R.id.view_goodsbyshipping);
			View viewSelf = findViewById(R.id.view_goodsbyself);
			int childCount = llGoodsDeliverSelf.getChildCount();
			if (childCount > 0) {
				llGoodsDeliverSelf.removeAllViews();
				;
			}
			childCount = llGoodsDeliverMarket.getChildCount();
			if (childCount > 0) {
				llGoodsDeliverMarket.removeAllViews();
			}
			// llGoodsDeliverSelf.removeAllViews();
			// llGoodsDeliverMarket.removeAllViews();
			if (listDBC.size() > 0) {
				viewSelf.setVisibility(View.VISIBLE);
				fillGoods(llGoodsDeliverSelf, listDBC, true);
			} else {
				viewSelf.setVisibility(View.GONE);
			}
			if (listDBM.size() > 0) {
				viewShipping.setVisibility(View.VISIBLE);
				fillGoods(llGoodsDeliverMarket, listDBM, false);
			} else {
				viewShipping.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "数据处理错误,请稍后重试...", Toast.LENGTH_LONG).show();
		}
		viewLoading.setVisibility(View.GONE);
		viewOrder.setVisibility(View.VISIBLE);
		viewDivider.setVisibility(View.VISIBLE);
	}

	private void showRefundPath() {
		// ArrayList<Order> list = maps.get(Integer.valueOf(orderType));
		Intent intent = new Intent(this, RefundDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("orderno", mOrder.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	private void fillGoods(LinearLayout ll, ArrayList<OrderGoods> list,
			boolean isDeliverSelf) {
		int count = list.size();
		int color;
		String deliverType;
		if (isDeliverSelf) {
			deliverType = "自带";
			color = getResources().getColor(R.color.greyfont);
		} else {
			deliverType = "配送";
			color = getResources().getColor(R.color.header_bg);
		}

		for (int i = 0; i < count; i++) {
			final CartGoods goods = list.get(i);

			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.order_detail_item, null);
			if (i == count - 1) {
				view.setBackgroundResource(R.color.white);
			}
			TextView tvName = (TextView) view
					.findViewById(R.id.tv_name_order_detail);
			TextView tvDeliverType = (TextView) view
					.findViewById(R.id.tv_delivertype);
			tvDeliverType.setTextColor(color);
			tvDeliverType.setText(deliverType);
			TextView tvPrice = (TextView) view
					.findViewById(R.id.tv_price_order_detail);
			TextView tvQuantity = (TextView) view
					.findViewById(R.id.tv_quantity_order_detail);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_order_detail);
			String imgPath = goods.getImagePath();
			if (imgPath == null || "".equals(imgPath)) {
				iv.setImageResource(R.drawable.picture_noimg_200x200);
			} else {
				imageLoader.displayImage("http://" + imgPath, iv, options);
			}
			tvName.setText(goods.getName());
			tvPrice.setText(String.format("%.2f", goods.getPrice()));
			tvQuantity.setText(goods.getQuantity() + "");
			ll.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showGoodsDetail(goods.getBarCode());
				}
			});
		}
	}

	private void showOrderPay() {
		Intent intent = new Intent(OrderDetailActivity.this,
				OrderPayActivity.class);
		Bundle b = new Bundle();
		b.putDouble("amount", mOrder.getAmount());
		b.putString("orderno", mOrder.getOrderNo());
		intent.putExtras(b);
		startActivity(intent);
	}

	public void close(View v) {
		finish();
	}

	private OnClickListener mCloseClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
