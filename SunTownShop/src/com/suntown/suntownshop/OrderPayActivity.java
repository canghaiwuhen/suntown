package com.suntown.suntownshop;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import com.alipay.sdk.app.PayTask;
import com.suntown.suntownshop.adapter.CouponUseAdapter;
import com.suntown.suntownshop.alipay.Result;
import com.suntown.suntownshop.alipay.SignUtils;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Coupon;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.MD5;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ֧��ҳ��
 *
 * @author Ǯ��
 * @version 2015��2��21�� ����9:45:16
 *
 */
public class OrderPayActivity extends Activity {
	private final static int PAYWAY_ALIPAY = 0;
	private final static int PAYWAY_TENPAY = 1;
	private final static int PAYWAY_UNIONPAY = 2;
	private final static int PAYWAY_WXPAY = 3;
	private final static int MSG_SET_MOBILE = 4;
	/**
	 * ֧������ز���
	 */
	// ֧���������̻�ID
	public static String PARTNER = "";
	// ֧�����˻�
	public static String SELLER = "";
	// ˽Կ
	public static String RSA_PRIVATE = "";
	// ��Կ
	public static final String RSA_PUBLIC = "";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	// ģ��֧��
	private static final int IMITATE_PAY_FLAG = 3;

	// ����֧�����TN
	private static final int MSG_GET_UPTN = 5;

	private static final int NETWORK_ERR = -1;

	private static String CALLBACK_URL = "";

	// ����֧����ز���
	/*****************************************************************
	 * mMode�������ͣ� "00" - ����������ʽ���� "01" - �����������Ի���
	 *****************************************************************/
	private final String mMode = "00";
	private static final String TN_URL_01 = "http://202.101.25.178:8080/sim/gettn";
	private String URL_UNIONPAY_TN = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/unionpay";
	/**
	 * 
	 */
	private String URL_PAYWAY_INFO = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getAlipay";
	private static final int MSG_GET_PAYWAY_INFO = 3;

	private ArrayList<Coupon> coupons;
	private Spinner spCoupons;
	private View viewCoupons;
	private CouponUseAdapter adapter;
	LinearLayout viewAlipay;
	LinearLayout viewTenpay;
	LinearLayout viewUnionpay;
	LinearLayout viewWeipay;
	CheckBox[] cbPayWay;

	int[] imgSource = new int[] { R.drawable.alipay, R.drawable.tenpay,
			R.drawable.union, R.drawable.wei };
	TextView tvPayWay;
	TextView tvAmount;
	String strAmount;
	String mOrderNo;
	private String mSubject;
	private String mBody = "";
	int mPayWay;
	double mAmount = 0.0;
	int couponIndex = 0;
	boolean isFirstClick = true;
	private String mUserId;
	private String mLoginToken;
	private boolean mIsLogin;

	int[] strPayWay = new int[] { R.string.pay_way_alipay_text,
			R.string.pay_way_tenpay_text, R.string.pay_way_unionpay_text,
			R.string.pay_way_weipay_text };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_pay);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mUserId = mSharedPreferences.getString("userId", "");
		mLoginToken = mSharedPreferences.getString("m_voucher", "");
		mIsLogin = mSharedPreferences.getBoolean("islogin", false);
		initViews();
		initPayWay();

		spCoupons = (Spinner) findViewById(R.id.sp_coupon);

		// spCoupons.setAdapter(adapter);
		spCoupons.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String msg;
				double denomination = coupons.get(position).getDenomination();
				if (denomination == 0) {
					msg = "��ʹ���Ż�ȯ";
				} else {
					msg = "ʹ��" + denomination + "Ԫ�Ż�ȯ";
				}
				couponIndex = position;
				TextView tvPayInfoCount = (TextView) findViewById(R.id.tv_pay_info_count);
				strAmount = "��" + String.format("%.2f", mAmount - denomination);
				tvAmount.setText(strAmount);
				tvPayInfoCount.setText(strAmount);
				Toast.makeText(OrderPayActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);

		String mobile = mSharedPreferences.getString("mobile", "");
		if (mobile == null || "".equals(mobile)) {
			mHandler.sendEmptyMessage(MSG_SET_MOBILE);
		}
	}

	private void initPayWay() {
		showProgress(true);
		HashMap<String, String> params = new HashMap<String, String>();
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_PAYWAY_INFO,
				getPayWayCallback);
		postAsyncTask.execute(params);
	}

	private OnCompleteCallback getPayWayCallback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			showProgress(false);

			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					PARTNER = jsonObj.getString("ALIPAYSFID");
					SELLER = jsonObj.getString("ALIPAYSFZH");
					RSA_PRIVATE = jsonObj.getString("ALIPAYSFRSA");
					CALLBACK_URL = jsonObj.getString("ALIPAYRETURNWAY");
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"���������ش�������δ��ɶ���������...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					finish();
				}

			} else {
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ������δ��ɶ���������...",
						Toast.LENGTH_SHORT).show();
				finish();

			}
		}
	};

	public void slideview(final View view, final float p1, final float p2,
			long durationMillis, long delayMillis) {
		TranslateAnimation animation = new TranslateAnimation(0, 0, p1, p2);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int left = view.getLeft();
				int top = view.getTop() + (int) (p2 - p1);
				int width = view.getWidth();
				int height = view.getHeight();
				System.out.println(left + "|" + top + "|" + width + "|"
						+ height);
				view.clearAnimation();
				// view.layout(left, top, left + width, top + height);
				view.setY(top);
			}
		});
		view.startAnimation(animation);
	}

	private void checkPayStatus() {
		showProgress(true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("formno", mOrderNo);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_CHECKPAY,
				checkPayCallback);
		postAsyncTask.execute(params);
	}

	private final static String URL_CHECKPAY = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/checkPayResult";

	private OnCompleteCallback checkPayCallback = new OnCompleteCallback() {

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
					// sendState״̬��Ϊ֧���ӿ�δ���룬ֻ�ܷ���ʧ�ܣ�������ʱģ��ɹ�
					if (sendState == 0) {
						Toast.makeText(getApplicationContext(), "֧�����!",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(OrderPayActivity.this,
								OrderDetailActivity.class);
						Bundle b = new Bundle();
						b.putString("orderno", mOrderNo);
						intent.putExtras(b);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"֧�����ȷ���У��Ժ������ҵĶ����в鿴!", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ش������Ժ�����...",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			} else {
				System.out.println(msg);
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * ģ��֧��
	 */
	private void imitatePay() {
		showProgress(true);
		/*
		 * GetJsonRunnable mGetRecomdRunnable; String params = "?out_trade_no="
		 * + mOrderNo + "&subject=smartshopping&seller_email=" + SELLER +
		 * "&buyer_email=test_android@test.com" + "&total_fee=" +
		 * String.valueOf(mAmount) + "&trade_status=TRADE_SUCCESS" +
		 * "&trade_no=" + mOrderNo;
		 * 
		 * mGetRecomdRunnable = new GetJsonRunnable(URL_IMITATEPAY + params,
		 * IMITATE_PAY_FLAG, mHandler); System.out.println(URL_IMITATEPAY +
		 * params); new Thread(mGetRecomdRunnable).start();
		 */

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("out_trade_no", mOrderNo);
		params.put("subject", "smartshopping");
		params.put("seller_email", SELLER);
		params.put("buyer_email", "test_android@test.com");
		params.put("total_fee", String.valueOf(mAmount));
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("trade_no", mOrderNo);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_IMITATEPAY,
				callbackImitatePay);
		postAsyncTask.execute(params);

	}

	private final static String URL_IMITATEPAY = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/Alipayrebackcall";

	private OnCompleteCallback callbackImitatePay = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					if ("success".equals(msg)) {
						checkPayStatus();
					} else {

						Toast.makeText(getApplicationContext(),
								"����֧���ӿ�ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ش������Ժ�����...",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				System.out.println("֧��ʧ��");
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	private final static String URL_USE_COUPON = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/useTicket";

	private OnCompleteCallback callbackUseCoupon = new OnCompleteCallback() {

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
						// 4.1չ������ģ��֧��
						imitatePay();
						// �Ǽ�֧����ʽ�ɹ�������֧���ӿ�
						/*
						 * switch (mPayWay) { case PAYWAY_ALIPAY: payByAlipay();
						 * break; case PAYWAY_TENPAY:
						 * Toast.makeText(OrderPayActivity.this,
						 * "�ö����ݲ�֧�ֲƸ�֧ͨ��!", Toast.LENGTH_SHORT).show(); break;
						 * case PAYWAY_UNIONPAY:
						 * Toast.makeText(OrderPayActivity.this, "�ö����ݲ�֧������֧��!",
						 * Toast.LENGTH_SHORT).show(); break; case PAYWAY_WXPAY:
						 * Toast.makeText(OrderPayActivity.this, "�ö����ݲ�֧��΢��֧��!",
						 * Toast.LENGTH_SHORT).show(); break; }
						 */

					} else if (sendState == 3) {
						Toast.makeText(getApplicationContext(),
								"������֧���ɹ��������ظ�����!", Toast.LENGTH_SHORT).show();
					} else if (sendState == 2) {
						Toast.makeText(getApplicationContext(),
								"�ö�����ʧЧ���������µ�!", Toast.LENGTH_SHORT).show();
					} else if (sendState == 4) {
						Toast.makeText(getApplicationContext(), "��¼��ʧЧ�������µ�¼!",
								Toast.LENGTH_SHORT).show();

						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putBoolean("islogin", false);
						mEditor.putString("userId", "");
						mEditor.putString("showname", "");
						mEditor.putString("avatar", "");
						mEditor.putBoolean("isvip", false);
						mEditor.commit();
						// ˢ�¹��ﳵ
						sendBroadcast(new Intent(
								Constants.ACTION_SHOPCART_CHANGED));
						Intent intent = new Intent(OrderPayActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					} else {

						Toast.makeText(getApplicationContext(),
								"����֧���ӿ�ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ش������Ժ�����...",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				System.out.println(msg);
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/setPayWay";

	private OnCompleteCallback callback = new OnCompleteCallback() {

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
						// ģ��֧��
						// imitatePay();
						// �Ǽ�֧����ʽ�ɹ�������֧���ӿ�
						switch (mPayWay) {
						case PAYWAY_ALIPAY:
							payByAlipay();
							break;
						case PAYWAY_TENPAY:
							Toast.makeText(OrderPayActivity.this,
									"�ö����ݲ�֧�ֲƸ�֧ͨ��!", Toast.LENGTH_SHORT).show();
							break;
						case PAYWAY_UNIONPAY:
							unionPay();
							break;
						case PAYWAY_WXPAY:
							wxPay();
							break;
						}

					} else if (sendState == 3) {
						Toast.makeText(getApplicationContext(),
								"������֧���ɹ��������ظ�����!", Toast.LENGTH_SHORT).show();
					} else if (sendState == 2) {
						Toast.makeText(getApplicationContext(),
								"�ö�����ʧЧ���������µ�!", Toast.LENGTH_SHORT).show();
					} else if (sendState == 4) {
						Toast.makeText(getApplicationContext(), "��¼��ʧЧ�������µ�¼!",
								Toast.LENGTH_SHORT).show();

						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putBoolean("islogin", false);
						mEditor.putString("userId", "");
						mEditor.putString("showname", "");
						mEditor.putString("avatar", "");
						mEditor.putBoolean("isvip", false);
						mEditor.commit();
						// ˢ�¹��ﳵ
						sendBroadcast(new Intent(
								Constants.ACTION_SHOPCART_CHANGED));
						Intent intent = new Intent(OrderPayActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					} else {

						Toast.makeText(getApplicationContext(),
								"����֧���ӿ�ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ش������Ժ�����...",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				System.out.println(msg);
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(OrderPayActivity.this);
			// ʵ����
			mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// ���ý�������񣬷��ΪԲ�Σ���ת��
			// pDialog.setTitle("Google");
			// ����ProgressDialog ����
			mPDialog.setMessage(getString(R.string.wait_a_minute));
			// ����ProgressDialog ��ʾ��Ϣ
			// pDialog.setIcon(R.drawable.ic_launcher);
			// ����ProgressDialog ����ͼ��
			// mypDialog.setButton();
			// ����ProgressDialog ��һ��Button
			mPDialog.setIndeterminate(false);
			// ����ProgressDialog �Ľ������Ƿ���ȷ
			mPDialog.setCancelable(false);
			// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
			mPDialog.show();
		} else {
			if (mPDialog != null && mPDialog.isShowing()) {
				mPDialog.dismiss();
				mPDialog = null;
			}
		}
	}

	private void initViews() {
		viewCoupons = findViewById(R.id.view_coupons);
		cbPayWay = new CheckBox[4];

		Button btnPay = (Button) findViewById(R.id.btn_order_pay_checkout);
		btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFirstClick) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.check_payway_tips),
							Toast.LENGTH_SHORT).show();
				} else {
					if (!mIsLogin || "".equalsIgnoreCase(mUserId)) {
						Intent intent = new Intent(OrderPayActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					} else {
						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);

						String mobile = mSharedPreferences.getString("mobile",
								"");
						if (mobile == null || "".equals(mobile)) {
							mHandler.sendEmptyMessage(MSG_SET_MOBILE);
						} else {
							showProgress(true);
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("memid", mUserId);
							params.put("logintoken", mLoginToken);
							params.put("formno", mOrderNo);
							params.put("type", String.valueOf(mPayWay));
							PostAsyncTask postAsyncTask = new PostAsyncTask(
									URL, callback);
							postAsyncTask.execute(params);
						}
					}
				}
			}
		});

		cbPayWay[0] = (CheckBox) findViewById(R.id.cb_order_alipay);
		// cbPayWay[0].setChecked(true);
		cbPayWay[0].setOnCheckedChangeListener(new CheckBoxClick(0));
		cbPayWay[1] = (CheckBox) findViewById(R.id.cb_order_tenpay);
		cbPayWay[1].setOnCheckedChangeListener(new CheckBoxClick(1));
		cbPayWay[2] = (CheckBox) findViewById(R.id.cb_order_union);
		cbPayWay[2].setOnCheckedChangeListener(new CheckBoxClick(2));
		cbPayWay[3] = (CheckBox) findViewById(R.id.cb_order_wei);
		cbPayWay[3].setOnCheckedChangeListener(new CheckBoxClick(3));
		tvPayWay = (TextView) findViewById(R.id.tv_order_pay_way);
		tvPayWay.setText(strPayWay[0]);
		tvAmount = (TextView) findViewById(R.id.tv_order_pay_count);

		Intent intent = getIntent();
		if (intent.hasExtra("amount")) {
			Bundle b = intent.getExtras();
			mAmount = b.getDouble("amount");
			strAmount = "��" + String.format("%.2f", mAmount);
			tvAmount.setText(strAmount);
		}
		if (intent.hasExtra("orderno")) {
			mOrderNo = intent.getStringExtra("orderno");
		}
		viewAlipay = (LinearLayout) findViewById(R.id.view_order_pay_alipay);
		viewTenpay = (LinearLayout) findViewById(R.id.view_order_pay_tenpay);
		viewUnionpay = (LinearLayout) findViewById(R.id.view_order_pay_unionpay);
		viewWeipay = (LinearLayout) findViewById(R.id.view_order_pay_weipay);
		viewAlipay.setOnClickListener(new PayWayClick(0));
		viewTenpay.setOnClickListener(new PayWayClick(1));
		viewUnionpay.setOnClickListener(new PayWayClick(2));
		viewWeipay.setOnClickListener(new PayWayClick(3));

	}

	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void payByAlipay() {
		showProgress(true);
		String orderInfo = null;
		String sign = null;
		try {
			orderInfo = getOrderInfo("smartshopping", "orderno" + mOrderNo,
					String.valueOf(mAmount)); // String.valueOf(mAmount-
			// coupons.get(couponIndex).getDenomination())
			sign = sign(orderInfo);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		if (sign == null || sign.length() == 0) {
			Toast.makeText(this, "ǩ������", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			// �����sign ��URL����
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (Exception e) {
			Toast.makeText(this, sign, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		System.out.println(payInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(OrderPayActivity.this);
				// ����֧���ӿ�
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	/**
	 * create the order info. ����������Ϣ
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// ���������ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + mOrderNo + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + CALLBACK_URL + "\"";

		// �ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// ֧�����ͣ� �̶�ֵ
		// orderInfo += "&payment_type=\"1\"";

		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";

		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		orderInfo += "&return_url=\"m.alipay.com\"";

		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ֧�����ͷ���ʾ��Ȩ���ѹرգ��޷�ʹ�á�֧������ֻ�а��˿��֧�������п�����ʹ��
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	private class ShowDialogAsync extends AsyncTask<Void, Void, String> {
		private Context context;
		private ProgressDialog pDialog;

		public ShowDialogAsync(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(context);
			// ʵ����
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// ���ý�������񣬷��ΪԲ�Σ���ת��
			// pDialog.setTitle("Google");
			// ����ProgressDialog ����
			pDialog.setMessage(getString(R.string.wait_a_minute));
			// ����ProgressDialog ��ʾ��Ϣ
			// pDialog.setIcon(R.drawable.ic_launcher);
			// ����ProgressDialog ����ͼ��
			// mypDialog.setButton();
			// ����ProgressDialog ��һ��Button
			pDialog.setIndeterminate(false);
			// ����ProgressDialog �Ľ������Ƿ���ȷ
			pDialog.setCancelable(false);
			// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				ShopCartDb scdb = new ShopCartDb(OrderPayActivity.this, mUserId);
				ArrayList<CartGoods> list = (ArrayList<CartGoods>) scdb
						.getAllChecked();
				scdb.deleteAllChecked();
				scdb.Close();

				int count = 0;
				String orderNo = mOrderNo;

				for (CartGoods goods : list) {
					count += goods.getQuantity();
				}
				// odb.close();
				mSubject = "orderno=" + mOrderNo + "&money=" + mAmount
						+ "&count=" + count + "&kinds=" + list.size();
				mBody = mSubject;
				return orderNo;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			initPayWay();
			super.onPostExecute(result);
		}

	}

	private class CheckBoxClick implements OnCheckedChangeListener {
		int index;

		public CheckBoxClick(int index) {
			// TODO Auto-generated constructor stub
			this.index = index;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			for (CheckBox cb : cbPayWay) {
				cb.setChecked(false);
			}
			mPayWay = index;
			cbPayWay[index].setChecked(isChecked);
			tvPayWay.setText(getString(strPayWay[index]));
			isFirstClick = false;
			/*TextView tvPayInfo = (TextView) findViewById(R.id.tv_pay_info);
			TextView tvPayInfoCount = (TextView) findViewById(R.id.tv_pay_info_count);
			ImageView ivPayInfo = (ImageView) findViewById(R.id.iv_pay_info);
			ivPayInfo.setImageResource(imgSource[index]);
			tvPayInfo.setText(getString(strPayWay[index]));
			tvPayInfoCount.setText(strAmount);
			if (isFirstClick) {
				RelativeLayout layoutSelect = (RelativeLayout) findViewById(R.id.order_pay_select);
				RelativeLayout layoutFooter = (RelativeLayout) findViewById(R.id.order_pay_footer);
				LayoutParams params = layoutFooter.getLayoutParams();
				slideview(layoutSelect, 0, 0 - params.height, 2000, 100);
				isFirstClick = false;
			}*/
		}

	}

	private class PayWayClick implements OnClickListener {
		int index;

		public PayWayClick(int index) {
			// TODO Auto-generated constructor stub
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (CheckBox cb : cbPayWay) {
				cb.setChecked(false);
			}
			mPayWay = index;
			cbPayWay[index].setChecked(true);
			tvPayWay.setText(getString(strPayWay[index]));
			isFirstClick = false;
			/*TextView tvPayInfo = (TextView) findViewById(R.id.tv_pay_info);
			TextView tvPayInfoCount = (TextView) findViewById(R.id.tv_pay_info_count);
			ImageView ivPayInfo = (ImageView) findViewById(R.id.iv_pay_info);
			ivPayInfo.setImageResource(imgSource[index]);
			tvPayInfo.setText(getString(strPayWay[index]));
			tvPayInfoCount.setText(strAmount);
			if (isFirstClick) {
				RelativeLayout layoutSelect = (RelativeLayout) findViewById(R.id.order_pay_select);
				RelativeLayout layoutFooter = (RelativeLayout) findViewById(R.id.order_pay_footer);
				LayoutParams params = layoutFooter.getLayoutParams();
				slideview(layoutSelect, 0, 0 - params.height, 2000, 100);
				isFirstClick = false;
			}*/
		}

	}

	public void close(View v) {
		finish();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			showProgress(false);
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					// Toast.makeText(OrderPayActivity.this, "֧���ɹ�",
					// Toast.LENGTH_SHORT).show();
					checkPayStatus();

				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000��
					// ����֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderPayActivity.this,
								"֧�����ȷ���У��Ժ��뵽�����б��в鿴֧�����", Toast.LENGTH_SHORT)
								.show();
					} else if (TextUtils.equals(resultStatus, "6001")) {
						Toast.makeText(OrderPayActivity.this, "ȡ��֧����֧��",
								Toast.LENGTH_SHORT).show();
					} else if (TextUtils.equals(resultStatus, "6002")) {
						Toast.makeText(OrderPayActivity.this,
								"�������Ӵ����Ժ��뵽�����б��в鿴֧�����", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(OrderPayActivity.this, "֧��ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(OrderPayActivity.this, "�����Ϊ��" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case IMITATE_PAY_FLAG:

				break;
			case MSG_SET_MOBILE:

				ConfirmDialog dialog = new ConfirmDialog(OrderPayActivity.this,
						"֧��ǰ�����������ֻ����룬����������?", getString(R.string.tips_text),
						getString(R.string.confirm_text),
						getString(R.string.cancel_text));
				if (dialog.ShowDialog()) {
					Intent intent = new Intent(OrderPayActivity.this,
							ModifyMobileActivity.class);
					startActivity(intent);
				}
				finish();

				break;
			case MSG_GET_UPTN:
				String tn = "";
				if (msg.obj == null || ((String) msg.obj).length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							OrderPayActivity.this);
					builder.setTitle("������ʾ");
					builder.setMessage("��������ʧ��,������!");
					builder.setNegativeButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				} else {
					tn = (String) msg.obj;
					/*************************************************
					 * ����2��ͨ����������������֧�����
					 ************************************************/
					UPPayAssistEx.startPayByJAR(OrderPayActivity.this,
							PayActivity.class, null, null, tn, mMode);
				}
				break;
			default:
				break;
			}
		};
	};

	private void unionPay() {
		showProgress(true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("formno", mOrderNo);
		params.put("memid", mUserId);
		params.put("logintoken", mLoginToken);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_UNIONPAY_TN,
				getUnionpayTNCallback);
		postAsyncTask.execute(params);
	}

	private OnCompleteCallback getUnionpayTNCallback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			System.out.println(msg);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int state = jsonObj.getInt("RESULT");
					String tn = jsonObj.getString("TN");
					if (state == 0) {
						/*************************************************
						 * ����2��ͨ����������������֧�����
						 ************************************************/
						UPPayAssistEx.startPayByJAR(OrderPayActivity.this,
								PayActivity.class, null, null, tn, mMode);
					} else if (state == 1) {
						Toast.makeText(getApplicationContext(),
								"��¼״̬�ѹ��ڣ������µ�¼!", Toast.LENGTH_SHORT).show();
						Constants.reLogin(OrderPayActivity.this);
						finish();
					} else if (state == 2) {
						Toast.makeText(getApplicationContext(),
								"����״̬��������δ��ɶ����б�������...", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getApplicationContext(),
								"������֧����ɣ������ظ�֧��!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"���������ش�������δ��ɶ���������...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					finish();
				}

			} else {
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();

			}
		}
	};

	/**
	 * ������ȡTN�߳� ��ִ̨�е������ǽ�������Ϣ���͸��������������TN(�����������Ľ��׺�,trade no) ��ʱΪģ��
	 */
	private Runnable getUPTNRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String tn = null;
			InputStream is;
			try {

				String url = TN_URL_01;

				URL myURL = new URL(url);
				URLConnection ucon = myURL.openConnection();
				ucon.setConnectTimeout(120000);
				is = ucon.getInputStream();
				int i = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((i = is.read()) != -1) {
					baos.write(i);
				}

				tn = baos.toString();
				is.close();
				baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Message msg = mHandler.obtainMessage();
			msg.what = MSG_GET_UPTN;
			msg.obj = tn;
			mHandler.sendMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * ����3�����������ֻ�֧���ؼ����ص�֧�����
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * ֧���ؼ������ַ���:success��fail��cancel �ֱ����֧���ɹ���֧��ʧ�ܣ�֧��ȡ��
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "֧���ɹ���";
			checkPayStatus();
			return;
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "֧��ʧ�ܣ�";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "�û�ȡ����֧��";
		}

		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}

	// ΢��֧��
	private void wxPay() {

		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
		sb = new StringBuffer();
		req = new PayReq();
	}

	Map<String, String> resultunifiedorder;
	StringBuffer sb;
	PayReq req;
	private boolean isOnWxPay = false;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	private BroadcastReceiver wxPayResultReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.hasExtra("errCode")) {
				int errCode = intent.getIntExtra("errCode", -1);
				if (errCode == -2) {
					Toast.makeText(OrderPayActivity.this, "ȡ��΢��֧��",
							Toast.LENGTH_SHORT).show();
				} else if (errCode == 0) {
					checkPayStatus();
				} else {
					Toast.makeText(OrderPayActivity.this, "΢��֧��ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (isOnWxPay) {
				unregisterReceiver(wxPayResultReceiver);
				isOnWxPay = false;
			}
		}
	};

	private void sendPayReq() {

		msgApi.registerApp(Constants.wx.APP_ID);
		msgApi.sendReq(req);
		IntentFilter filter = new IntentFilter(
				"com.suntown.suntownshop.Action.PAYRESULT_WX");
		registerReceiver(wxPayResultReceiver, filter);
		isOnWxPay = true;
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.wx.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = null;

		appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

		Log.e("orion", appSign);
		return appSign;
	}

	private void genPayReq() {

		req.appId = Constants.wx.APP_ID;
		req.partnerId = Constants.wx.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		sendPayReq();

		Log.e("orion", signParams.toString());

	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(OrderPayActivity.this, "΢��֧��",
					"���ڻ�ȡԤ֧������...");
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			// show.setText(sb.toString());
			System.out.println("��ȡԤ֧��������:" + sb.toString());
			if (result.get("return_code") != null
					&& "SUCCESS".equals(result.get("return_code"))) {

				if (result.get("result_code") != null
						&& "SUCCESS".equals(result.get("result_code"))) {
					resultunifiedorder = result;
					genPayReq();
				} else {
					if (result.containsKey("err_code_des")) {
						Toast.makeText(OrderPayActivity.this,
								result.get("err_code_des"), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(OrderPayActivity.this, "��ȡԤ֧������ʧ��!",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(OrderPayActivity.this, "��ȡԤ֧������ʧ��!",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = com.suntown.suntownshop.utils.Util.httpPost(url,
					entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// ʵ����student����
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();

		return com.suntown.suntownshop.utils.MD5.getMessageDigest(String
				.valueOf(random.nextInt(10000)).getBytes());
	}

	/**
	 * ����ǩ��
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.wx.API_KEY);

		String packageSign = null;
		packageSign = com.suntown.suntownshop.utils.MD5.getMessageDigest(
				sb.toString().getBytes()).toUpperCase();

		Log.e("orion", packageSign);
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		try {
			return new String(sb.toString().getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid",
					Constants.wx.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "��������"));
			packageParams.add(new BasicNameValuePair("mch_id",
					Constants.wx.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://www.suntowngis.com:8080/suntesl/order/weixinpay"));
			packageParams.add(new BasicNameValuePair("out_trade_no", mOrderNo));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {

			return null;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (isOnWxPay) {
			unregisterReceiver(wxPayResultReceiver);
		}
		super.onDestroy();
	}
}
