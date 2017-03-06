package com.suntown.suntownshop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntown.suntownshop.adapter.PrepareToBuyAdapter;
import com.suntown.suntownshop.adapter.PrepareToBuyAdapter.OnGoodsActionListener;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.IsChineseOrNot;
import com.suntown.suntownshop.utils.JsonBuilder;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.SwipeListView;
import com.suntown.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 预购清单
 *
 * @author 钱凯
 * @version 2015年6月21日 上午9:45:34
 *
 */
public class PrepareToBuyActivity extends Activity {
	private View loading;
	private View content;
	private View viewEmpty;
	private String mUserId;
	private String mLoginToken;
	private boolean mIsVip;
	private SwipeListView listView;
	private ArrayList<ParcelableGoods> list;
	private PrepareToBuyAdapter adapter;
	private int mDelIndex;
	private PopupWindow pw;
	private Button btnNav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare_list);

		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mUserId = sharedPreferences.getString("userId", "");
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		mLoginToken = sharedPreferences.getString("m_voucher", "");
		mIsVip = sharedPreferences.getBoolean("isvip", false);
		if (!isLogin || "".equals(mUserId)) {
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			finish();
		}
		loading = findViewById(R.id.loading);
		content = findViewById(R.id.content);
		viewEmpty = findViewById(R.id.empty);
		btnNav = (Button) findViewById(R.id.btn_nav);
		listView = (SwipeListView) findViewById(R.id.lv_goods);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				showGoodsDetail(list.get(position).getBarCode());
			}
		});
		ImageView ivEmpty = (ImageView) findViewById(R.id.iv_emptyimg);
		ivEmpty.setImageResource(R.drawable.order_list_empty);
		TextView tvEmpty = (TextView) findViewById(R.id.tv_emptymsg);
		tvEmpty.setText("您还没有预购清单");
		init();
	}

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

	private void init() {
		loading.setVisibility(View.VISIBLE);
		content.setVisibility(View.GONE);
		viewEmpty.setVisibility(View.GONE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", mUserId);
		params.put("logintoken", mLoginToken);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
		postAsyncTask.execute(params);
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getModelFormno";

	// private final static String URL =
	// "http://192.168.0.78:8080/axis2/services/sunteslwebservice/getModelFormno";
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
						jsonObj = jsonObj.getJSONObject("INFO");
						JSONArray jsonArray = jsonObj.getJSONArray("LIST");
						list = JsonParser.pGoodsParse(jsonArray);
						adapter = new PrepareToBuyAdapter(
								PrepareToBuyActivity.this, list, mIsVip,
								listView.getRightViewWidth(), listener);
						listView.setAdapter(adapter);
						content.setVisibility(View.VISIBLE);
						viewEmpty.setVisibility(View.GONE);
					} else {
						content.setVisibility(View.GONE);
						viewEmpty.setVisibility(View.VISIBLE);
					}
					loading.setVisibility(View.GONE);

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

	private OnGoodsActionListener listener = new OnGoodsActionListener() {

		@Override
		public void onDelete(int index) {
			// TODO Auto-generated method stub
			deleteGoods(index);
		}

		@Override
		public void onCheckedChanged(int count) {
			// TODO Auto-generated method stub
			if (count > 0) {
				btnNav.setText("一键导航(" + count + ")");
			} else {
				btnNav.setText("一键导航");
			}
		}
	};

	private void deleteGoods(int index) {
		ConfirmDialog dialog = new ConfirmDialog(this, "确定要删除该商品吗?",
				getString(R.string.tips_text),
				getString(R.string.confirm_text),
				getString(R.string.cancel_text));
		if (dialog.ShowDialog()) {
			mDelIndex = index;
			try {
				String strJson = JsonBuilder.makeDelPrepare(mUserId,
						new String[] { list.get(index).getBarCode() });
				showProgress(true);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("strMsg", strJson);
				PostAsyncTask postAsyncTask = new PostAsyncTask(
						URL_DELETEGOODS, callbackDeleteGoods);
				postAsyncTask.execute(params);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("生成JSON错误" + e.getMessage());
				e.printStackTrace();
			}
		}
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

	private final static String URL_DELETEGOODS = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/deleModelFormno";
	// private final static String URL_DELETEGOODS =
	// "http://192.168.0.78:8080/axis2/services/sunteslwebservice/deleModelFormno";
	private OnCompleteCallback callbackDeleteGoods = new OnCompleteCallback() {

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
					// ArrayList<Order> list;
					if (sendState == 0) {
						list.remove(mDelIndex);
						adapter.notifyDataSetChanged();
						Toast.makeText(PrepareToBuyActivity.this, "商品删除成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(PrepareToBuyActivity.this, "商品删除失败",
								Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					Toast.makeText(PrepareToBuyActivity.this,
							"服务器返回错误，请稍后重试...", Toast.LENGTH_SHORT).show();

					e.printStackTrace();
				}
			} else {
				Toast.makeText(PrepareToBuyActivity.this, "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	public void navigate(View v) {
		if (adapter.getCheckedList().size() > 0) {
			LayoutInflater inflater = (LayoutInflater) PrepareToBuyActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
								PrepareToBuyActivity.this,
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

			pw.showAtLocation(content, Gravity.CENTER, 0, 0);

		}
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
		i.putParcelableArrayListExtra("goodslist", adapter.getCheckedList());
		i.putExtra("location", location);
		i.putExtra("title", title);
		i.putExtra("floor", floorName);
		startActivity(i);
	}

	public void close(View v) {
		finish();
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
					Toast.makeText(PrepareToBuyActivity.this,
							getString(R.string.qrcode_cantfind),
							Toast.LENGTH_SHORT).show();
				}
			} else { // 条码
				findLocation(scanResult);
			}

		}
	}

	private void findLocation(String barcode) {
		showProgress(true);
		GetJsonRunnable getJsonRunnable = new GetJsonRunnable(URL_GET_GOODS
				+ barcode, MSG_GETGOODDETAIL_COMPLETE, handler);
		new Thread(getJsonRunnable).start();
	}

	private final static int MSG_GETGOODDETAIL_COMPLETE = 1;
	private final static int MSG_ERR_NETWORKERR = -1;
	private String URL_GET_GOODS = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Getgoods_info?Barcode=";
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			Bundle bundle;
			String strMsg;
			JSONObject jsonObj;
			JSONArray jsonArray;
			boolean isDone = false;
			switch (msg.what) {

			case MSG_GETGOODDETAIL_COMPLETE:
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
								PrepareToBuyActivity.this, "确定要从" + gName
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
			case MSG_ERR_NETWORKERR:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(getApplicationContext(), "ERROR:" + strMsg,
						Toast.LENGTH_LONG).show();
				break;
			}

			super.handleMessage(msg);
		}

	};
}
