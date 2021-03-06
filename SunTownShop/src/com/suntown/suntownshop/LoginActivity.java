package com.suntown.suntownshop;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.Md5Manager;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;
/**
 * 登录页面
 *
 * @author 钱凯
 * @version 2015年9月21日 上午9:40:48
 *
 */
public class LoginActivity extends Activity {
	EditText mViewUsername;
	EditText mViewPassword;
	private TextView tvUserErr;
	private TextView tvPassErr;
	private CheckBox cbShowPass;
	private ImageView ivUserCheck;
	LinearLayout viewRemember;
	ImageView ivRemember;
	boolean isRemember = false;
	private int mode;
	private final static int MODE_PHONE = 1;
	private final static int MODE_EMAIL = 2;

	private String username = "";
	private String password = "";
	private boolean isEncrypt = true;
	private boolean isUsernameOk = false;
	private int loginType = 0;
	/**
	 * QQ登录相关
	 */
	private Tencent mTencent;
	private final static String APP_ID = "1104412605";
	private static final String SCOPE = "all";// 读取用户信息

	/**
	 * 微信登录相关
	 */
	private IWXAPI wxApi;
	private final static String URL_WX_GETTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			+ Constants.wx.APP_ID
			+ "&secret="
			+ Constants.wx.APP_SECRET
			+ "&grant_type=authorization_code&code=";
	private final static String URL_WX_GETINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
	private final static int MSG_WX_GETTOKEN = 2;
	private final static int MSG_WX_GETINFO = 3;
	/**
	 * 新浪微博相关
	 */
	private AuthInfo mAuthInfo;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount_login);
		// 新浪微博相关
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(this,
				com.suntown.suntownshop.weibo.Constants.APP_KEY,
				com.suntown.suntownshop.weibo.Constants.REDIRECT_URL,
				com.suntown.suntownshop.weibo.Constants.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
		// QQ相关
		mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
		// 微信相关
		regToWx();
		mViewUsername = (EditText) findViewById(R.id.myaccount_login_editext_username);
		mViewPassword = (EditText) findViewById(R.id.myaccount_login_editext_userpassword);
		tvUserErr = (TextView) findViewById(R.id.tv_username_err);
		tvPassErr = (TextView) findViewById(R.id.tv_pass_err);
		cbShowPass = (CheckBox) findViewById(R.id.cb_showpass);
		ivUserCheck = (ImageView) findViewById(R.id.iv_checkusername);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		String name = sharedPreferences.getString("username", "");
		String pass = sharedPreferences.getString("password", "");
		isRemember = sharedPreferences.getBoolean("isremember", false);
		//2015-07-31 新版UI调整后不再保存登录信息
		/*if (!"".equals(username)) {
			mViewUsername.setText(name);
			username = name;
		}
		if (!"".equals(password) && isRemember) {
			mViewPassword.setText(pass);
			password = pass;
			isEncrypt = false;
		}*/
		ivUserCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isUsernameOk) {
					mViewUsername.setText("");
					mViewUsername.setFocusable(true);
				}
			}
		});
		cbShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				int sel = mViewPassword.getSelectionStart();
				if (isChecked) {
					if (!isEncrypt) {
						password = "";
						isEncrypt = true;
						mViewPassword.setText("");
					}
					mViewPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

				} else {
					mViewPassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				mViewPassword.setSelection(sel);
			}
		});

		mViewUsername.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				ivUserCheck.setVisibility(View.VISIBLE);
				if (hasFocus) {
					ivUserCheck.setImageResource(R.drawable.icon_clean);
					tvUserErr.setVisibility(View.GONE);
				} else {
					checkUsername();
				}
			}
		});

		mViewPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					tvPassErr.setVisibility(View.GONE);
					if (!isEncrypt) {
						password = "";
						isEncrypt = true;
						mViewPassword.setText("");
					}
				} else {
					checkPassword();
				}
			}
		});
		ivRemember = (ImageView) findViewById(R.id.myaccount_login_checkbox_remember);
		ivRemember.setImageResource(isRemember ? R.drawable.icon_item_selected
				: R.drawable.icon_item_select);
		viewRemember = (LinearLayout) findViewById(R.id.myaccount_login_action_remember);
		viewRemember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRemember = !isRemember;
				ivRemember
						.setImageResource(isRemember ? R.drawable.icon_item_selected
								: R.drawable.icon_item_select);
				SharedPreferences mSharedPreferences = getSharedPreferences(
						"suntownshop", 0);
				SharedPreferences.Editor mEditor = mSharedPreferences.edit();
				mEditor.putBoolean("isremember", isRemember);
				mEditor.commit();
			}
		});
		Button btnLogin = (Button) findViewById(R.id.myaccount_login_button_login);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptLogin();
			}
		});
		// 注册微信登录回调广播接收者
		IntentFilter filter = new IntentFilter(
				"com.suntown.suntownshop.ACTION_WX_REBACK");
		registerReceiver(wxRebackReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTencent.logout(this);
		unregisterReceiver(wxRebackReceiver);
	}

	public void close(View v) {
		hideInput();
		finish();
	}

	private void logout() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean("islogin", false);
		mEditor.commit();
	}

	private boolean isUsernameValid(String email) {
		// TODO: Replace this with your own logic
		return email.length() > 5;
		// return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 5;
	}

	public void attemptLogin() {
		loginType = 0;
		if (checkUsername() && checkPassword()) {
			if (isEncrypt) {
				try {
					password = Md5Manager.md5(password);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("err------>" + e.getMessage());
					e.printStackTrace();
				}
				System.out.println("password------>" + password);
				if (password == null || "".equals(password)) {
					Toast.makeText(getApplicationContext(), "登录失败，加密出错",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			showProgress(true);
			GetJsonRunnable getJsonRunnable;
			try {
				getJsonRunnable = new GetJsonRunnable(URL_LOGIN + (mode - 1)
						+ "&lgn=" + username + "&pwd="
						+ URLEncoder.encode(password, "UTF-8"), MSG_LOGIN,
						handler);
				new Thread(getJsonRunnable).start();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private final static String URL_THIRDPART_LOGIN = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/thirdpartLogin?logintype=";

	private void otherLogin(int type, String token) {
		loginType = 1;
		showProgress(true);
		GetJsonRunnable getJsonRunnable = new GetJsonRunnable(
				URL_THIRDPART_LOGIN + type + "&token=" + token, MSG_LOGIN,
				handler);
		System.out.println(URL_THIRDPART_LOGIN + type + "&token=" + token);
		new Thread(getJsonRunnable).start();
	}

	private void login(String userId, String name, String cardNo, String age,
			String address, int sex, String phone, String email, String memNo,
			String voucher, String avatar, String nickname) {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putString("userId", userId);	
		mEditor.putString("m_name", name);
		mEditor.putString("m_cardno", cardNo);
		mEditor.putString("m_age", age);
		mEditor.putString("m_address", address);
		mEditor.putInt("m_sex", sex);
		if (!"".equals(avatar) && avatar.indexOf("http://") < 0) {
			avatar = "http://" + avatar;
		}
		mEditor.putString("avatar", avatar);
		mEditor.putString("nickname", nickname);
		// 用户写操作凭证，类似session
		mEditor.putString("m_voucher", voucher);
		// mEditor.putString("m_voucher", password);
		mEditor.putString("username", ""); //2015-07-31新版UI后不再保存登录信息
		mEditor.putString("phone", phone);
		mEditor.putString("mobile", phone);
		mEditor.putString("email", email);
		mEditor.putInt("logintype", loginType);
		if (loginType == 0) {
			mEditor.putString("showname",
					(phone == null || "".equals(phone)) ? email : phone);
		}
		mEditor.putString("menno", memNo);
		mEditor.putBoolean("isvip", memNo == null || "".equals(memNo) ? false
				: true);
		mEditor.putString("password",  ""); //2015-07-31新版UI后不再保存登录信息
		mEditor.putBoolean("islogin", true);
		mEditor.commit();
		// 刷新购物车
		sendBroadcast(new Intent(Constants.ACTION_SHOPCART_CHANGED));
		
		hideInput();
		Intent intent = new Intent(LoginActivity.this,
				MainTabActivity.class);
		Bundle b = new Bundle();
		b.putInt("gototab", 3);
		b.putBoolean("login", true);
		intent.putExtras(b);
		startActivity(intent);
		finish();
		
	}

	private final static String URL_LOGIN = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/login?type=";// Constants.DOMAIN_NAME+
	// "axis2/services/sunteslwebservice/login?type=";
	private final static int MSG_LOGIN = 1;
	private final static int MSG_ERROR = -1;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle = msg.getData();
			String strMsg;
			JSONObject jsonObj;
			showProgress(false);
			switch (msg.what) {
			case MSG_LOGIN:
				strMsg = bundle.getString("MSG_JSON");
				System.out.println("JSON------>" + strMsg);
				try {
					jsonObj = new JSONObject(strMsg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						jsonObj = jsonObj.getJSONObject("USERINFO");
						String userId = jsonObj.getString("MEMID");
						String name = jsonObj.getString("NAME");
						String cardno = jsonObj.getString("CARDNO"); // 身份证号暂时未暴露
						String age = jsonObj.getString("AGE");
						String address = jsonObj.getString("ADDRESS");
						String strSex = jsonObj.getString("SEX");
						String memNo = jsonObj.getString("MEMNO");
						String voucher = jsonObj.getString("LOGINTOKEN");
						String avatar = jsonObj.getString("AVATAR");
						String nickname = jsonObj.getString("NICKNAME");
						int sex = 0;
						if (strSex != null && !"".equals(strSex)) {
							sex = jsonObj.getInt("SEX");
						}

						String telPhone = jsonObj.getString("TEL");
						String email = jsonObj.getString("EMAIL");
						if (userId != null && !"".equals(userId)) {
							Toast.makeText(getApplicationContext(), "登录成功",
									Toast.LENGTH_LONG).show();
							login(userId, name, cardno, age, address, sex,
									telPhone, email, memNo, voucher, avatar,
									nickname);

							
						}
					} else if (sendState == 1) {
						tvUserErr.setVisibility(View.VISIBLE);
						tvUserErr.setText("该手机号尚未注册!");
					} else {
						tvPassErr.setVisibility(View.VISIBLE);
						tvPassErr.setText("密码错误!");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:登录结果解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
			case MSG_WX_GETTOKEN:
				strMsg = bundle.getString("MSG_JSON");
				try {
					jsonObj = new JSONObject(strMsg);
					if (jsonObj.has("errcode")) {
						Toast.makeText(getApplicationContext(), "ERROR:微信授权失败",
								Toast.LENGTH_SHORT).show();
					} else {
						String token = jsonObj.getString("access_token");
						String openid = jsonObj.getString("openid");
						showProgress(true);
						GetJsonRunnable getJsonRunnable = new GetJsonRunnable(
								String.format(URL_WX_GETINFO, token, openid),
								MSG_WX_GETINFO, false, handler);
						new Thread(getJsonRunnable).start();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:微信信息解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			case MSG_WX_GETINFO:
				strMsg = bundle.getString("MSG_JSON");
				System.out.println(strMsg);
				try {
					jsonObj = new JSONObject(strMsg);
					if (jsonObj.has("errcode")) {
						Toast.makeText(getApplicationContext(), "ERROR:微信授权失败",
								Toast.LENGTH_SHORT).show();
					} else {
						String nickname = jsonObj.getString("nickname");
						String unionid = jsonObj.getString("unionid");
						// 微信方式登录
						otherLogin(3, unionid);
						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putString("showname", nickname);
						mEditor.commit();
						Toast.makeText(getApplicationContext(), "微信授权成功",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:微信信息解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
			case MSG_ERROR:
				strMsg = bundle.getString("MSG_ERR");
				System.out.println(strMsg);
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

		imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(LoginActivity.this);
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
			}
		}
	}

	public void register(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean checkPassword() {
		String pass = mViewPassword.getText().toString();
		if (pass == null || "".equals(pass)) {
			tvPassErr.setText("密码不能为空!");
		} else if (pass.length() < 6 || pass.length() > 20) {
			tvPassErr.setText("密码必须为6-20位!");
		} else if (FormatValidation.isCharacter(pass)) {
			tvPassErr.setText("密码不能全为字母!");
		} else if (FormatValidation.isNumeric(pass)) {
			tvPassErr.setText("密码不能全为数字!");
		} else if (FormatValidation.isSymbol(pass)) {
			tvPassErr.setText("密码不能全为符号!");
		} else {
			if (isEncrypt) {
				password = pass;
			}
			return true;
		}
		tvPassErr.setVisibility(View.VISIBLE);
		return false;
	}

	private boolean checkUsername() {
		String uname = mViewUsername.getText().toString();
		isUsernameOk = false;
		if (uname == null || "".equals(uname)) {
			tvUserErr.setText("手机号不能为空!");
		} else if (FormatValidation.isMobileNO(uname)) {
			mode = MODE_PHONE;
			ivUserCheck.setImageResource(R.drawable.icon_ok);
			isUsernameOk = true;
			username = uname;
			return true;
		} /*else if (FormatValidation.isEmail(uname)) {
			mode = MODE_EMAIL;
			ivUserCheck.setImageResource(R.drawable.icon_ok);
			isUsernameOk = true;
			username = uname;
			return true;
		}*/ else {
			tvUserErr.setText("手机号不正确!");
		}
		ivUserCheck.setImageResource(R.drawable.icon_no);
		tvUserErr.setVisibility(View.VISIBLE);
		return false;
	}

	public void otherLogin(View v) {

		switch (v.getId()) {
		case R.id.login_union_qq:
			if (!mTencent.isSessionValid()) {
				mTencent.login(LoginActivity.this, SCOPE, new BaseUiListener());
			} else {
				// QQ方式登录
				otherLogin(0, mTencent.getOpenId());
				UserInfo mInfo = new UserInfo(LoginActivity.this,
						mTencent.getQQToken());
				mInfo.getUserInfo(qqinfoListener);
			}
			break;
		case R.id.login_union_weixin:
			wxLogin();
			break;
		case R.id.login_union_weibo:
			mSsoHandler.authorize(new AuthListener());
			break;
		}
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token

				// 保存 Token 到 SharedPreferences
				long uid = Long.parseLong(mAccessToken.getUid());
				/** 用户信息接口 */
				UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this,
						com.suntown.suntownshop.weibo.Constants.APP_KEY,
						mAccessToken);
				mUsersAPI.show(uid, mListener);
				// 微博方式登录
				otherLogin(2, mAccessToken.getUid());
				System.out.println("uid:" + mAccessToken.getUid() + "token:"
						+ mAccessToken.getToken());
				Toast.makeText(LoginActivity.this, "微博授权成功", Toast.LENGTH_SHORT)
						.show();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "微博授权失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(LoginActivity.this, "微博授权取消", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(LoginActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 微博 OpenAPI 回调接口。 获取用户信息
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					System.out.println("头像:" + user.avatar_large);
					System.out.println("获取User信息成功，用户昵称：" + user.screen_name);
					SharedPreferences mSharedPreferences = getSharedPreferences(
							"suntownshop", 0);
					SharedPreferences.Editor mEditor = mSharedPreferences
							.edit();
					mEditor.putString("showname", user.screen_name);
					mEditor.commit();
				} else {
					Toast.makeText(LoginActivity.this, response,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(LoginActivity.this, info.toString(),
					Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * QQ 登录回调接口
	 *
	 * @author Ken
	 * @version 2015年3月5日 上午10:04:14
	 *
	 */
	private class BaseUiListener implements IUiListener {

		@Override
		public void onError(UiError e) {
			Toast.makeText(LoginActivity.this, "授权出错，您的QQ版本太低，请升级QQ后重试...",
					Toast.LENGTH_SHORT).show();
			System.out.println("QQ登录错误：code-->"+e.errorCode+" detail-->"+e.errorDetail+" mes-->"+e.errorMessage);
		}

		@Override
		public void onCancel() {
			Toast.makeText(LoginActivity.this, "取消授权",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Object obj) {
			// TODO Auto-generated method stub

			try {
				if (null == obj) {
					Toast.makeText(LoginActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject jsonObj = (JSONObject) obj;
				if (null != jsonObj && jsonObj.length() == 0) {
					Toast.makeText(LoginActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				System.out.println(jsonObj.toString());
				int ret = jsonObj.getInt("ret");
				if (ret == 0) {
					UserInfo mInfo = new UserInfo(LoginActivity.this,
							mTencent.getQQToken());
					mInfo.getUserInfo(qqinfoListener);
					String openid = jsonObj.getString("openid");
					// QQ方式登录
					otherLogin(0, openid);
					Toast.makeText(LoginActivity.this, "QQ授权成功",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("json数据错误在QQ登录返回时");
			}

		}
	}

	private IUiListener qqinfoListener = new IUiListener() {

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Object obj) {
			// TODO Auto-generated method stub
			JSONObject jsonObj = (JSONObject) obj;
			System.out.println(jsonObj.toString());
			try {
				if (null == obj) {
					Toast.makeText(LoginActivity.this, "获取用户信息失败",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (null != jsonObj && jsonObj.length() == 0) {
					Toast.makeText(LoginActivity.this, "获取用户信息失败",
							Toast.LENGTH_SHORT).show();
					return;
				}

				int ret = jsonObj.getInt("ret");
				if (ret == 0) {
					SharedPreferences mSharedPreferences = getSharedPreferences(
							"suntownshop", 0);
					SharedPreferences.Editor mEditor = mSharedPreferences
							.edit();
					mEditor.putString("showname", jsonObj.getString("nickname"));
					mEditor.commit();
					System.out.println("获取用户信息成功，用户昵称为:"
							+ jsonObj.getString("nickname"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("json数据错误在QQ获取信息返回时");
			}
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 微信登录相关
	 */
	/**
	 * 注册到微信
	 */
	private void regToWx() {
		wxApi = WXAPIFactory.createWXAPI(this, Constants.wx.APP_ID);
		wxApi.registerApp(Constants.wx.APP_ID);
	}

	/**
	 * 微信登录
	 */
	private void wxLogin() {
		// send oauth request
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "weixin_login";
		wxApi.sendReq(req);
	}

	/**
	 * 微信登录回调
	 */
	private void wxLoginReback(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (intent.hasExtra("wx_auth_errcode")) {
			int errCode = intent.getIntExtra("wx_auth_errcode", -1);
			switch (errCode) {
			case BaseResp.ErrCode.ERR_OK:
				String code = intent.getStringExtra("wx_auth_code");
				showProgress(true);
				GetJsonRunnable getJsonRunnable = new GetJsonRunnable(
						URL_WX_GETTOKEN + code, MSG_WX_GETTOKEN, false, handler);
				new Thread(getJsonRunnable).start();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Toast.makeText(this, "拒绝登录", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	/**
	 * 微信登录回调广播接收
	 */
	private BroadcastReceiver wxRebackReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			wxLoginReback(intent);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	public void RtnPwdClick(View v){
		Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
		startActivity(intent);
	}
}
