package com.suntown.suntownshop;

import org.json.JSONObject;

import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.SmsContent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 修改手机号码，由于接口尚未调整，修改手机号码的2次验证功能尚未开放，后期需调整。
 *
 * @author 钱凯
 * @version 2015年7月21日 上午9:41:31
 *
 */
public class ModifyMobileActivity extends Activity {
	private EditText etCheckCode;
	private Button btnCheckCode;
	private SmsContent smsContent;
	private EditText etMobile;
	private String mMobile;
	private String mCheckcode;
	private String mUserId;
	private String mLoginToken;
	private final static int MSG_GETCHECKCODE = 1;
	private final static int MSG_MODIFY = 2;
	private final static int MSG_ERROR = -1;
	private final static String URL_GETCHECKCODE = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/checkCodeSend?moblie=";
	private final static String URL_MODIFY_MOBILE = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/updateMobile?moblie=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifymobile);
		SharedPreferences sharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		mUserId = sharedPreferences.getString("userId", "");
		mLoginToken = sharedPreferences.getString("m_voucher", "");
		if (!isLogin || "".equalsIgnoreCase(mUserId)) {
			Intent intent = new Intent(this,
					LoginActivity.class);
			startActivity(intent);
			finish();
		} 
		etMobile = (EditText)findViewById(R.id.et_mobile);
		btnCheckCode = (Button) findViewById(R.id.btn_checkcode);
		etCheckCode = (EditText) findViewById(R.id.et_checkcode);
		smsContent = new SmsContent(this, new Handler(),
				etCheckCode);
		btnCheckCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getCheckCode();
			}
		});
	}
	
	private void getCheckCode() {

		if (checkMobile()){

			// 注册短信变化监听
			this.getContentResolver().registerContentObserver(
					Uri.parse("content://sms/"), true, smsContent);

			showProgress(true);
			GetJsonRunnable getJsonRunnable = new GetJsonRunnable(
					URL_GETCHECKCODE + mMobile, MSG_GETCHECKCODE, handler);
			new Thread(getJsonRunnable).start();
		}
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			showProgress(false);
			Bundle bundle = msg.getData();
			String strMsg;
			JSONObject jsonObj;
			switch (msg.what) {
			case MSG_MODIFY:
				strMsg = bundle.getString("MSG_JSON");
				try {
					jsonObj = new JSONObject(strMsg);
					int sendState = jsonObj.getInt("RELSUT");
					if (sendState == 0) {
						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putString("mobile", mMobile);
						mEditor.commit();
						Toast.makeText(getApplicationContext(),
								"手机号码设置成功!",
								Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(),
								"手机号码设置失败，请稍后重试...",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"服务器返回错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;
			case MSG_GETCHECKCODE:
				strMsg = bundle.getString("MSG_JSON");
				try {
					jsonObj = new JSONObject(strMsg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						Toast.makeText(getApplicationContext(),
								"已发送验证码到手机号码" + mMobile, Toast.LENGTH_SHORT)
								.show();
						btnCheckCode.setEnabled(false);
						etCheckCode.requestFocus();
						countdown = 60;
						btnCheckCode.setText(countdown + "秒后重发");
						handler.postDelayed(runnableTimer, 1000);
					} else if (sendState == 1) {
						Toast.makeText(getApplicationContext(), "该手机号码已经被其他用户绑定，请更换手机号码!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"验证码发送失败，请稍后重试", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:验证码解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			case MSG_ERROR:
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_LONG).show();
				break;
			}
			showProgress(false);
			super.handleMessage(msg);
		}

	};
	private int countdown = 60;

	Runnable runnableTimer = new Runnable() {
		@Override
		public void run() {
			countdown--;
			if (countdown > 0) {
				btnCheckCode.setText(countdown + "秒后重发");
				handler.postDelayed(this, 1000);
			} else {
				btnCheckCode.setText(getString(R.string.getcheckcode));
				btnCheckCode.setEnabled(true);
			}
		}
	};
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
	
	private boolean checkMobile() {
		String phone = etMobile.getText().toString();
		if (phone == null || "".equals(phone)) {
			Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
		}else if (!FormatValidation.isMobileNO(phone)) {
			Toast.makeText(this, "手机号码不合法!", Toast.LENGTH_SHORT).show();
		}else{
			mMobile = phone;
			return true;
		}
		return false;
	}
	
	private boolean checkCheckcode() {
		String checkcode = etCheckCode.getText().toString();
		if (checkcode == null || "".equals(checkcode)) {
			Toast.makeText(this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
		} else if (checkcode.length() != 6
				|| !FormatValidation.isNumeric(checkcode)) {
			Toast.makeText(this, "请输入6位数字验证码!", Toast.LENGTH_SHORT).show();
		} else {
			mCheckcode = checkcode;
			return true;
		}
		return false;
	}
	
	public void confirm(View v){
		if(checkMobile()&&checkCheckcode()){
			showProgress(true);
			GetJsonRunnable getJsonRunnable = new GetJsonRunnable(
					URL_MODIFY_MOBILE + mMobile +"&code="+mCheckcode+"&memid="+mUserId+"&logintoken="+mLoginToken, MSG_MODIFY, handler);
			new Thread(getJsonRunnable).start();
		}
	}
	
	public void close(View v){
		finish();
	}
}
