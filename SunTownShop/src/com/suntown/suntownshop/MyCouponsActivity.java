package com.suntown.suntownshop;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.zxing.WriterException;
import com.suntown.suntownshop.adapter.CouponAdapter;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.model.Coupon;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 我的礼券页面
 *
 * @author 钱凯
 * @version 2015年4月8日 下午3:31:28
 *
 */
public class MyCouponsActivity extends Activity {
	private View loading;
	private ListView listView;
	private View main;
	private ArrayList<Coupon> list;
	private CouponAdapter adapter;
	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getMemticket";
	PopupWindow pw;
	private int tickettype =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupons);
		loading = findViewById(R.id.loading);
		main = findViewById(R.id.main);
		listView = (ListView) findViewById(R.id.lv_coupons);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				showCoupon(position);
			}
		});
		Intent intent = getIntent();
		if(intent.hasExtra("tickettype")){
			tickettype = intent.getIntExtra("tickettype", 0);
		}
		initCoupon();
	}

	private void showCoupon(int index) {
		Coupon coupon = list.get(index);
		String StrQrCode = "TNO:" + coupon.getId() + ";MONEY:"
				+ coupon.getDenomination() + ";SENDCAUSE:" + coupon.getType()
				+ ";ENDDATE:" + coupon.getEndDate() + ";";
		Bitmap qrCodeBitmap;
		try {
			qrCodeBitmap = EncodingHandler.createQRCode(StrQrCode, 500);

			LayoutInflater inflater = (LayoutInflater) MyCouponsActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View mView = inflater.inflate(R.layout.coupon_popup, null);
			ImageView mImageView = (ImageView) mView
					.findViewById(R.id.iv_coupon_qrcode);
			mImageView.setImageBitmap(qrCodeBitmap);
			if (pw == null) {
				// 生成PopupWindow对象
				pw = new PopupWindow(mView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				pw.setOutsideTouchable(true);
			} else {
				pw.setContentView(mView);
			}
			// 在指定位置弹出窗口
			mView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(pw!=null&&pw.isShowing()){
						pw.dismiss();
					}
				}
			});
			pw.showAtLocation(main, Gravity.CENTER, 0, 0);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyCouponsActivity.this, "二维码生成失败，请重试...", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private void initCoupon() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		String mUId = mSharedPreferences.getString("userId", "");
		String mVoucher = mSharedPreferences.getString("m_voucher", "");
		listView.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		list = new ArrayList<Coupon>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", mUId);
		params.put("logintoken", mVoucher);
		params.put("type", "0");
		params.put("startIndex", "1");
		params.put("length", "1000");
		params.put("tickettype", String.valueOf(tickettype));
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
		postAsyncTask.execute(params);
	}

	private OnCompleteCallback callback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			System.out.println(msg);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						// 取得订单数据，开始解析
						JSONArray jsonArray = jsonObj.getJSONArray("TICKET");
						list = JsonParser.couponsParse(jsonArray);
					} else {
						list.clear();
					}
					loading.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					adapter = new CouponAdapter(MyCouponsActivity.this, list);
					listView.setAdapter(adapter);
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

	public void close(View v) {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(pw!=null&&pw.isShowing()){
				pw.dismiss();
				return true;
			}		
		}

		return super.onKeyDown(keyCode, event);
	}
}
