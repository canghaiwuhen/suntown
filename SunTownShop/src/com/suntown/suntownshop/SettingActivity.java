package com.suntown.suntownshop;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.widget.CircleImageView;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.SlipButton;
import com.suntown.suntownshop.widget.SlipButton.OnChangedListener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 设置
 *
 * @author 钱凯
 * @version 2015年4月21日 上午9:53:55
 *
 */
public class SettingActivity extends Activity implements OnChangedListener {

	private boolean isOnLogin = false;
	private Button btnLoginLogout;
	private SlipButton sbMsgPush;
	private CircleImageView ivAvatar;
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
		setContentView(R.layout.activity_setting);
		initOptions();
		sbMsgPush = (SlipButton) findViewById(R.id.sb_msgpush);
		ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		isOnLogin = mSharedPreferences.getBoolean("islogin", false);
		boolean isMsgPush = mSharedPreferences.getBoolean("msgpush", true);
		int loginType = mSharedPreferences.getInt("logintype", 0);
		String nickname = mSharedPreferences.getString("nickname", "");
		String strAvatar = mSharedPreferences.getString("avatar", "");
		if (loginType == 1 || !isOnLogin) {
			View ViewMD = findViewById(R.id.view_modifypwd);
			ViewMD.setVisibility(View.GONE);
		}
		if (!isOnLogin) {
			View viewMNN = findViewById(R.id.view_modifynickname);
			viewMNN.setVisibility(View.GONE);
			ivAvatar.setImageResource(R.drawable.myaccount_default_avatar);
		} else {
			if (strAvatar != null && !"".equals(strAvatar)) {
				imageLoader.displayImage(strAvatar, ivAvatar, options);
			} else {
				ivAvatar.setImageResource(R.drawable.myaccount_default_avatar);
			}
			TextView tvNickname = (TextView) findViewById(R.id.tv_nickname);
			tvNickname.setText("".equals(nickname) ? "未设置" : nickname);
		}
		sbMsgPush.setChecked(isMsgPush);
		sbMsgPush.setOnChangedListener(this);
		btnLoginLogout = (Button) findViewById(R.id.btn_login_logout);
		if (isOnLogin) {
			btnLoginLogout.setText(getString(R.string.logout_curuser));
		} else {
			btnLoginLogout.setText("点此登录");
		}

	}

	public void modifyNickname(View v) {
		Intent intent = new Intent(SettingActivity.this,
				NicknameModifyActivity.class);
		startActivity(intent);
	}

	public void loginLogout(View v) {
		if (isOnLogin) {
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"suntownshop", 0);
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putBoolean("islogin", false);
			mEditor.putString("userId", "");
			mEditor.putString("showname", "");
			mEditor.putString("nickname", "");
			mEditor.putString("mobile", "");
			mEditor.putString("avatar", "");
			mEditor.putString("m_voucher", "");
			mEditor.putBoolean("isvip", false);
			mEditor.commit();
			// 刷新购物车
			sendBroadcast(new Intent(Constants.ACTION_SHOPCART_CHANGED));
			finish();
		} else {
			Intent intent = new Intent(SettingActivity.this,
					LoginActivity.class);
			startActivity(intent);
		}
	}

	public void close(View v) {
		finish();
	}

	public void modifypwd(View v) {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		String username = sharedPreferences.getString("username", "");
		String password = sharedPreferences.getString("password", "");
		if (!isLogin || "".equalsIgnoreCase(userId)) {
			Intent intent = new Intent(SettingActivity.this,
					LoginActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(SettingActivity.this,
					ModifyPWDActivity.class);
			startActivity(intent);
		}

	}

	public void clearImageCache(View v) {
		ConfirmDialog dialog = new ConfirmDialog(SettingActivity.this,
				"确定要清除图片缓存吗?", getString(R.string.tips_text),
				getString(R.string.confirm_text),
				getString(R.string.cancel_text));
		if (dialog.ShowDialog()) {
			imageLoader.clearDiskCache();
			imageLoader.clearMemoryCache();
			Toast.makeText(getApplicationContext(),
					getString(R.string.clear_imagecache_done),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void bindCard(View v) {
		Intent intent = new Intent(SettingActivity.this, BindCardActivity.class);
		startActivity(intent);
	}

	// 初始化BLE
	private boolean initBLE() {
		/*
		 * IntentFilter filter = new IntentFilter(
		 * BluetoothDevice.ACTION_ACL_CONNECTED);
		 * this.registerReceiver(mReceiver, filter);
		 */

		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "该设备不支持BLE", Toast.LENGTH_SHORT).show();
			System.out.println("该设备不支持BLE");

			return false;
		}

		// 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

		// 检查设备上是否支持蓝牙
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
			System.out.println("该设备不支持蓝牙");
			return false;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			ConfirmDialog dialog = new ConfirmDialog(SettingActivity.this,
					"推送消息必须开启蓝牙,是否开启蓝牙?", getString(R.string.tips_text),
					getString(R.string.confirm_text),
					getString(R.string.cancel_text));
			if (dialog.ShowDialog()) {
				mBluetoothAdapter.enable();
			}
			return false;
		}

		return true;
	}

	@Override
	public void OnChanged(SlipButton slipButton, boolean checkState) {
		// TODO Auto-generated method stub
		if (checkState) {
			initBLE();
		}
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean("msgpush", checkState);
		mEditor.commit();
	}

	public void about(View v) {
		Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
		startActivity(intent);
	}

	public void modifyInfo(View v) {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		String username = sharedPreferences.getString("username", "");
		String password = sharedPreferences.getString("password", "");
		if (!isLogin || "".equalsIgnoreCase(userId)) {
			Intent intent = new Intent(SettingActivity.this,
					LoginActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(SettingActivity.this,
					ModifyInfoActivity.class);
			startActivity(intent);
		}

	}

	public void setMobile(View v) {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);

		String mobile = mSharedPreferences.getString("mobile", "");
		if (mobile == null || "".equals(mobile)) {
			Intent i = new Intent(this, ModifyMobileActivity.class);
			startActivity(i);
		}
	}

	public void modifyAvatar(View v) {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		if (!isLogin) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, AvatarModifyActivity.class);
			startActivity(intent);
		}
	}
}
