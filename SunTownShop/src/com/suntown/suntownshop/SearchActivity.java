package com.suntown.suntownshop;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntown.suntownshop.adapter.GoodsListAdapter;
import com.suntown.suntownshop.db.RouteGoodsDb;
import com.suntown.suntownshop.db.SearchHistoryDb;
import com.suntown.suntownshop.listener.OnImageMoveListener;
import com.suntown.suntownshop.listener.OnMoveViewListener;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.ImageMoveAnimation;
import com.suntown.suntownshop.utils.IsChineseOrNot;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.GoodsViewGroup;
import com.suntown.suntownshop.widget.PullUpRefreshListView;
import com.suntown.suntownshop.widget.PullUpRefreshListView.OnRefreshListener;
import com.suntown.suntownshop.widget.XCFlowLayout;
import com.suntown.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 搜索
 *
 * @author 钱凯
 * @version 2015年3月21日 上午9:52:52
 *
 */
public class SearchActivity extends Activity implements OnClickListener,
		OnLongClickListener {
	private View loading;
	private PullUpRefreshListView listView;
	private ListView lvKeyword;
	private View viewKeyword;
	private View viewHotKeyword;
	private TextView tvClearHistory;
	private EditText etSearch;
	private List<Map<String, String>> keywordList;
	private GoodsListAdapter adapter;
	private ImageView ivRoute;
	private View viewGoodsList;
	private String URL_SEARCH = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Getalikegoods_info?gname=";
	private final static int MSG_SEARCH_COMPLETE = 0;
	private final static int MSG_ERR_NETWORKERR = -1;
	private final static int MSG_GET_LOCATION_GOODS = 4;
	private boolean isInput = true;
	private int searchType = 0;
	private int searchKindId = 0;
	private int loadOnceCount = 20;
	private int startIndex = 1;
	private String curKeyword;
	private XCFlowLayout mFlowLayout;
	private final String URL_GOODSDETAIL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Getgoods_info?Barcode=";
	private String userId;
	private ArrayList<ParcelableGoods> routeList;
	private TextView tvRouteNum;
	private PopupWindow pw;
	private ImageMoveAnimation imageMoveAnim;

	private String mKeywords[] = { "燕麦片", "Q蒂", "旺仔牛奶", "美好时光海苔", "湿纸巾",
			"旺仔小馒头", "好时牛奶巧克力", "金龙鱼调和油", "啤酒" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		userId = mSharedPreferences.getString("userId", "");
		Intent intent = getIntent();
		if (intent.hasExtra("type") && intent.hasExtra("kid")) {
			searchType = intent.getIntExtra("type", 0);
			searchKindId = intent.getIntExtra("kid", 0);
		}
		ivRoute = (ImageView) findViewById(R.id.iv_route);
		ivRoute.setOnLongClickListener(this);
		tvRouteNum = (TextView) findViewById(R.id.tv_route_num);
		viewGoodsList = findViewById(R.id.view_goodslist);
		listView = (PullUpRefreshListView) findViewById(R.id.lv_goodslist);
		viewKeyword = findViewById(R.id.view_keyword);
		tvClearHistory = (TextView) findViewById(R.id.tv_clear_keywrod);
		tvClearHistory.setOnClickListener(this);
		viewHotKeyword = findViewById(R.id.flowlayout_keyword);
		etSearch = (EditText) findViewById(R.id.et_title);
		etSearch.addTextChangedListener(watcher);
		lvKeyword = (ListView) findViewById(R.id.lv_keyword);
		lvKeyword.setOnItemClickListener(keywordClick);
		loading = findViewById(R.id.loading);
		lvKeyword.setVisibility(View.VISIBLE);
		viewKeyword.setVisibility(View.GONE);
		viewHotKeyword.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
		viewGoodsList.setVisibility(View.GONE);
		adapter = new GoodsListAdapter(this, new ArrayList<Goods>());
		listView.setAdapter(adapter);
		listView.setonRefreshListener(refreshListener);
		initChildViews();
		RouteGoodsDb db = new RouteGoodsDb(this, userId);
		routeList = db.getAll();
		db.Close();
		refreshRouteGoods();
	}

	private void initChildViews() {
		// TODO Auto-generated method stub
		mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout_keyword);
		MarginLayoutParams lp = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		lp.topMargin = 5;
		lp.bottomMargin = 5;
		for (int i = 0; i < mKeywords.length; i++) {
			TextView view = new TextView(this);
			view.setText(mKeywords[i]);
			view.setTextSize(getResources().getDimensionPixelSize(
					R.dimen.keyword_textsize) / 2);
			view.setTextColor(getResources().getColor(R.color.black));
			view.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.framestyle_label_keyword));
			mFlowLayout.addView(view, lp);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isInput = false;
					String keyword = ((TextView) v).getText().toString();
					etSearch.setText(keyword);
					etSearch.setSelection(keyword.length());
					onSearch(v);
					isInput = true;
				}
			});
		}
	}

	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			GetJsonRunnable mGetRecomdRunnable;
			try {
				mGetRecomdRunnable = new GetJsonRunnable(URL_SEARCH
						+ URLEncoder.encode(curKeyword, "UTF-8") + "&type="
						+ searchType + "&kid=" + searchKindId + "&startIndex="
						+ startIndex + "&length=" + loadOnceCount,
						MSG_SEARCH_COMPLETE, handler);
				new Thread(mGetRecomdRunnable).start();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	private OnItemClickListener keywordClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			isInput = false;
			String keyword = keywordList.get(position).get("keyword");
			etSearch.setText(keyword);
			etSearch.setSelection(keyword.length());
			onSearch(view);
			isInput = true;
		}
	};

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (isInput) {
				viewKeyword.setVisibility(View.VISIBLE);
				viewHotKeyword.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				viewGoodsList.setVisibility(View.GONE);
				String keyword = etSearch.getText().toString();
				if (keyword != null && !"".equals(keyword)) {
					SearchHistoryDb db = new SearchHistoryDb(
							SearchActivity.this, "1");
					keywordList = db.getHistory(keyword);
					db.close();
				} else {
					keywordList = new ArrayList<Map<String, String>>();
				}
				SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,
						keywordList, R.layout.keyword_item,
						new String[] { "keyword" },
						new int[] { R.id.tv_keyword });
				lvKeyword.setAdapter(adapter);

			}
		}
	};

	private void search(String text) {
		if (text != null && !"".equals(text)) {

			curKeyword = text;
			GetJsonRunnable mGetRecomdRunnable;
			try {
				hideInput();
				adapter.goodsList.clear();
				startIndex = 1;
				mGetRecomdRunnable = new GetJsonRunnable(URL_SEARCH
						+ URLEncoder.encode(curKeyword, "UTF-8") + "&type="
						+ searchType + "&kid=" + searchKindId + "&startIndex="
						+ startIndex + "&length=" + loadOnceCount,
						MSG_SEARCH_COMPLETE, handler);
				new Thread(mGetRecomdRunnable).start();
				viewKeyword.setVisibility(View.GONE);
				viewHotKeyword.setVisibility(View.GONE);
				viewGoodsList.setVisibility(View.GONE);
				loading.setVisibility(View.VISIBLE);
				SearchHistoryDb db = new SearchHistoryDb(SearchActivity.this,
						"1");
				db.insertHistory(text);
				db.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void onSearch(View v) {
		String text = etSearch.getText().toString();
		search(text);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle;
			String strMsg;
			switch (msg.what) {
			case MSG_GET_LOCATION_GOODS:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				try {
					JSONObject jsonObj = new JSONObject(strMsg);
					JSONArray jsonArray = jsonObj.getJSONArray("INFO");
					if (jsonArray.length() > 0) {
						jsonObj = (JSONObject) jsonArray.opt(0);
						String barCode = jsonObj.getString("BARCODE");
						String gName = jsonObj.getString("GNAME");
						String shelfId = jsonObj.getString("SFID");
						String floorName = jsonObj.getString("FLOORNAME");
						ConfirmDialog dialog = new ConfirmDialog(
								SearchActivity.this, "确定要从" + gName
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
			case MSG_SEARCH_COMPLETE:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				try {
					JSONObject jsonObj = new JSONObject(strMsg);
					JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
					int rows = jsonArray.length();
					listView.setRefreshable(rows < loadOnceCount ? false : true);
					if (rows == 0) {
						Toast.makeText(getApplicationContext(), "没有找到相关商品",
								Toast.LENGTH_LONG).show();
					} else {
						startIndex = startIndex + rows;
						for (int i = 0; i < rows; i++) {
							jsonObj = (JSONObject) jsonArray.opt(i);
							String barCode = jsonObj.getString("BARCODE");
							String gName = jsonObj.getString("GNAME");
							String gCode = jsonObj.getString("GCODE");
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
							Goods goods = new Goods(barCode, gCode, gName,
									gKind, gUnit, gOriPrice, gMemPrice,
									gUptPrice, gSpec, gClass, gProvider,
									gBrand, gOrigin, gImgPath, priceType,
									deliverType);
							adapter.goodsList.add(goods);
						}
						adapter.notifyDataSetChanged();
					}
					listView.onRefreshComplete();
					viewKeyword.setVisibility(View.GONE);
					viewHotKeyword.setVisibility(View.GONE);
					viewGoodsList.setVisibility(View.VISIBLE);
					loading.setVisibility(View.GONE);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:推荐商品解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				// Toast.makeText(getApplicationContext(), strMsg,
				// Toast.LENGTH_SHORT).show();
				break;
			case MSG_ERR_NETWORKERR:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_LONG).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	private void hideInput() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法

		imm.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hideInput();
	}

	public void clearHistory(View v) {
		SearchHistoryDb db = new SearchHistoryDb(SearchActivity.this, "1");
		db.clear();
		db.close();
		keywordList = new ArrayList<Map<String, String>>();

		SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,
				keywordList, R.layout.keyword_item, new String[] { "keyword" },
				new int[] { R.id.tv_keyword });
		lvKeyword.setAdapter(adapter);
	}

	public void close(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_clear_keywrod:
			SearchHistoryDb db = new SearchHistoryDb(SearchActivity.this, "1");
			db.clear();
			db.close();
			keywordList = new ArrayList<Map<String, String>>();

			SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this,
					keywordList, R.layout.keyword_item,
					new String[] { "keyword" }, new int[] { R.id.tv_keyword });
			lvKeyword.setAdapter(adapter);
			break;
		}
	}

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

					Intent openCameraIntent = new Intent(SearchActivity.this,
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

		pw.showAtLocation(listView, Gravity.CENTER, 0, 0);
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
		RouteGoodsDb db = new RouteGoodsDb(SearchActivity.this, userId);
		db.clearAll();
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
				imageMoveAnim = new ImageMoveAnimation(SearchActivity.this);
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
				RouteGoodsDb db = new RouteGoodsDb(SearchActivity.this, userId);
				db.insertGoods(moveGoods.getBarCode(), moveGoods.getName(),
						moveGoods.getShelfId(), moveGoods.getFloorName());
				routeList = db.getAll();
				db.Close();
				refreshRouteGoods();
			}
		}
	};

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
				RouteGoodsDb db = new RouteGoodsDb(this, userId);
				db.clearAll();
				routeList = db.getAll();
				db.Close();
				refreshRouteGoods();
			}

			break;
		}
		return false;
	}
}
