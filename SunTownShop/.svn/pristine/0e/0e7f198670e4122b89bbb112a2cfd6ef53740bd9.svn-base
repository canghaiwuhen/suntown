package com.suntown.suntownshop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.suntown.suntownshop.LoginActivity.AuthListener;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.FileManager;
import com.suntown.suntownshop.utils.ImageUtil;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.CircleImageView;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

/**
 * 修改头像页面
 *
 * @author 钱凯
 * @version 2015年4月10日 上午10:10:30
 *
 */
public class AvatarModifyActivity extends Activity {
	private final static int REQUEST_CODE = 1;
	private final static int SELECT_PIC_KITKAT = 2;
	private final static int SELECT_PIC = 3;
	private final static int PIC_CUT_RESULT = 4;
	private String imageDataPath;
	private CircleImageView ivAvatar;
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
	/**
	 * imageloader相关
	 */
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	/**
	 * 初始化ImageLoader Options
	 */
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
		setContentView(R.layout.activity_avatarmodify);
		initOptions();
		ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		String strAvatar = sharedPreferences.getString("avatar", "");
		imageDataPath = FileManager.getDataPath(this); //+ "/" + userId + "/";
		File dir = new File(imageDataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (strAvatar != null && !"".equals(strAvatar)) {
			imageLoader.displayImage(strAvatar, ivAvatar, options);
		}

		// 微信相关
		regToWx();
		// 注册微信登录回调广播接收者
		IntentFilter filter = new IntentFilter(
				"com.suntown.suntownshop.ACTION_WX_REBACK");
		registerReceiver(wxRebackReceiver, filter);
		// QQ相关
		mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
	}

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
						String headimgurl = jsonObj.getString("headimgurl");
						headimgurl = headimgurl.replaceAll("\\\\", "");
						// 上传头像地址
						uploadAvatar(headimgurl, AVATAR_TYPE_URL);

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

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(AvatarModifyActivity.this);
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

	/**
	 * 调用手机相册
	 */
	public void openGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/jpeg");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			startActivityForResult(intent, SELECT_PIC_KITKAT);
		} else {
			startActivityForResult(intent, SELECT_PIC);
		}
	}

	/**
	 * 调用手机拍照方法
	 */
	private void openCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File file = new File(imageDataPath + "/myavatar.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, REQUEST_CODE);
		} else {
			Toast.makeText(AvatarModifyActivity.this, "没有SD卡，无法保存相片",
					Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 发起微博授权登录并获得微博头像
	 * @param v
	 */
	public void fromWeibo(View v) {
		// 新浪微博相关
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(AvatarModifyActivity.this,
				com.suntown.suntownshop.weibo.Constants.APP_KEY,
				com.suntown.suntownshop.weibo.Constants.REDIRECT_URL,
				com.suntown.suntownshop.weibo.Constants.SCOPE);
		mSsoHandler = new SsoHandler(AvatarModifyActivity.this, mAuthInfo);
		mSsoHandler.authorize(new AuthListener());
	}
	/**
	 * 发起微信授权登录并获得微信头像
	 * @param v
	 */
	public void fromWX(View v) {

		// send oauth request
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "weixin_login";
		wxApi.sendReq(req);
	}

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
				Toast.makeText(this, "取消授权", Toast.LENGTH_SHORT).show();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Toast.makeText(this, "拒绝授权", Toast.LENGTH_SHORT).show();
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
	/**
	 * 发起QQ登录授权并获得QQ头像
	 * @param v
	 */
	public void fromQQ(View v) {
		if (!mTencent.isSessionValid()) {
			mTencent.login(AvatarModifyActivity.this, SCOPE,
					new BaseUiListener());
		} else {
			showProgress(true);
			UserInfo mInfo = new UserInfo(AvatarModifyActivity.this,
					mTencent.getQQToken());
			mInfo.getUserInfo(qqinfoListener);
		}
	}

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

		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onComplete(Object obj) {
			// TODO Auto-generated method stub

			try {
				if (null == obj) {
					Toast.makeText(AvatarModifyActivity.this, "授权失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject jsonObj = (JSONObject) obj;
				if (null != jsonObj && jsonObj.length() == 0) {
					Toast.makeText(AvatarModifyActivity.this, "授权失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				System.out.println(jsonObj.toString());
				int ret = jsonObj.getInt("ret");
				if (ret == 0) {
					showProgress(true);
					UserInfo mInfo = new UserInfo(AvatarModifyActivity.this,
							mTencent.getQQToken());
					mInfo.getUserInfo(qqinfoListener);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("json数据错误在QQ登录返回时");
			}

		}
	}
	/**
	 * QQ用户信息获取回调接口
	 */
	private IUiListener qqinfoListener = new IUiListener() {

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			showProgress(false);
		}

		@Override
		public void onComplete(Object obj) {
			// TODO Auto-generated method stub
			showProgress(false);
			JSONObject jsonObj = (JSONObject) obj;
			System.out.println(jsonObj.toString());
			try {
				if (null == obj) {
					Toast.makeText(AvatarModifyActivity.this, "获取用户信息失败",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (null != jsonObj && jsonObj.length() == 0) {
					Toast.makeText(AvatarModifyActivity.this, "获取用户信息失败",
							Toast.LENGTH_SHORT).show();
					return;
				}

				int ret = jsonObj.getInt("ret");
				if (ret == 0) {
					String figureurl = jsonObj.getString("figureurl_qq_2");
					figureurl = figureurl.replaceAll("\\\\", "");
					// 上传头像地址
					uploadAvatar(figureurl, AVATAR_TYPE_URL);

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
			showProgress(false);
		}
	};
	/**
	 * 从相册选择头像
	 * @param v
	 */
	public void fromGallery(View v) {
		openGallery();
	}
	/**
	 * 拍照获得头像
	 */
	public void fromCamera(View v) {
		openCamera();
	}

	public void close(View v) {
		finish();
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 *            图片Uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", 150);
		// intent.putExtra("outputY", 150);
		//部分手机内存无法支持返回大图片，以文件形式返回
		intent.putExtra("return-data", false);
		intent.putExtra("noFaceDetection", true);
		// 设置裁剪后输出文件位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.parse("file://"+imageDataPath + "/myavatar.jpg"));
		// 输出文件格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PIC_CUT_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Bitmap photo = null;
		switch (requestCode) {
		case REQUEST_CODE: // 拍照获得照片
			File temp = new File(imageDataPath + "/myavatar.jpg");
			System.out.println(imageDataPath + "/myavatar.jpg");
			if (temp.exists()) {
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		case SELECT_PIC: // 从相册获得照片
		case SELECT_PIC_KITKAT:
			if (data != null) {
				startPhotoZoom(data.getData());
			}
			break;
		case PIC_CUT_RESULT: // 裁剪结果
			if (data != null) {
				Bundle extras = data.getExtras();
				photo = ImageUtil.getimage(imageDataPath + "/myavatar.jpg"); // extras.getParcelable("data");
				if (photo != null) {
					if (ImageUtil.compressBmpToFile(photo, imageDataPath
							+ "/myavatar.jpg")) {
						photo = ImageUtil.loadBitmap(imageDataPath
								+ "/myavatar.jpg");
					}
					String context = getFileBase64(imageDataPath
							+ "/myavatar.jpg");
					if (context != null && !"".equals(context)) {
						// 上传头像地址
						uploadAvatar(context, AVATAR_TYPE_FILE);
					} else {
						Toast.makeText(AvatarModifyActivity.this, "图像处理错误",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
		}
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);

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
			System.out.println("授权完成");
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				showProgress(true);
				// 保存 Token 到 SharedPreferences
				long uid = Long.parseLong(mAccessToken.getUid());
				/** 用户信息接口 */
				UsersAPI mUsersAPI = new UsersAPI(AvatarModifyActivity.this,
						com.suntown.suntownshop.weibo.Constants.APP_KEY,
						mAccessToken);
				mUsersAPI.show(uid, mListener);
				// 微博方式登录
				System.out.println("uid:" + mAccessToken.getUid() + "token:"
						+ mAccessToken.getToken());

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
				Toast.makeText(AvatarModifyActivity.this, message,
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(AvatarModifyActivity.this, "微博授权取消",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(AvatarModifyActivity.this,
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
			showProgress(false);
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					uploadAvatar(user.avatar_large, AVATAR_TYPE_URL);

				} else {
					Toast.makeText(AvatarModifyActivity.this, response,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			showProgress(false);
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(AvatarModifyActivity.this, info.toString(),
					Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 获取BASE64编码的文件数据流
	 * 
	 * @param filename
	 * @return
	 */
	private String getFileBase64(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT)); // 进行Base64编码
			return uploadBuffer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/uploadAvatar";
	private final static int AVATAR_TYPE_FILE = 1;
	private final static int AVATAR_TYPE_URL = 2;

	/**
	 * 上传文件
	 * 
	 * @param filename
	 * @param type
	 * @return
	 */
	private void uploadAvatar(String context, int type) {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		String userId = mSharedPreferences.getString("userId", "");
		String mVoucher = mSharedPreferences.getString("m_voucher", "");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", userId);
		params.put("token", mVoucher);
		params.put("context", context);
		params.put("type", String.valueOf(type));
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
		showProgress(true);
		postAsyncTask.execute(params);
	}

	private OnCompleteCallback callback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			showProgress(false);
			// TODO Auto-generated method stub
			System.out.println(msg);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						Toast.makeText(AvatarModifyActivity.this, "头像上传成功",
								Toast.LENGTH_SHORT).show();
						String avatar = jsonObj.getString("AVATAR");
						if (avatar.indexOf("http://") < 0) {
							avatar = "http://" + avatar;
						}
						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putString("avatar", avatar);
						mEditor.commit();
						showAvatar(avatar);
					} else {
						Toast.makeText(AvatarModifyActivity.this,
								"登录状态已失效，请重新登录!", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(AvatarModifyActivity.this,
								LoginActivity.class);
						startActivity(i);
						finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(AvatarModifyActivity.this, "头像上传失败:" + msg,
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			} else {
				Toast.makeText(AvatarModifyActivity.this, "网络连接错误,请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	// 重新拉取头像
	private void showAvatar(String avatar) {
		MemoryCacheUtils.removeFromCache(avatar, imageLoader.getMemoryCache());
		DiskCacheUtils.removeFromCache(avatar, imageLoader.getDiskCache());
		imageLoader.displayImage(avatar, ivAvatar, options);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTencent.logout(this);
		unregisterReceiver(wxRebackReceiver);
	}
}
