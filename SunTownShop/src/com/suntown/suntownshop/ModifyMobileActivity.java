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
 * �޸��ֻ����룬���ڽӿ���δ�������޸��ֻ������2����֤������δ���ţ������������
 *
 * @author Ǯ��
 * @version 2015��7��21�� ����9:41:31
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

			// ע����ű仯����
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
								"�ֻ��������óɹ�!",
								Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(),
								"�ֻ���������ʧ�ܣ����Ժ�����...",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"���������ش������Ժ�����...",
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
								"�ѷ�����֤�뵽�ֻ�����" + mMobile, Toast.LENGTH_SHORT)
								.show();
						btnCheckCode.setEnabled(false);
						etCheckCode.requestFocus();
						countdown = 60;
						btnCheckCode.setText(countdown + "����ط�");
						handler.postDelayed(runnableTimer, 1000);
					} else if (sendState == 1) {
						Toast.makeText(getApplicationContext(), "���ֻ������Ѿ��������û��󶨣�������ֻ�����!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"��֤�뷢��ʧ�ܣ����Ժ�����", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"ERROR:��֤���������:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

				break;
			case MSG_ERROR:
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
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
				btnCheckCode.setText(countdown + "����ط�");
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
	
	private boolean checkMobile() {
		String phone = etMobile.getText().toString();
		if (phone == null || "".equals(phone)) {
			Toast.makeText(this, "�ֻ����벻��Ϊ��!", Toast.LENGTH_SHORT).show();
		}else if (!FormatValidation.isMobileNO(phone)) {
			Toast.makeText(this, "�ֻ����벻�Ϸ�!", Toast.LENGTH_SHORT).show();
		}else{
			mMobile = phone;
			return true;
		}
		return false;
	}
	
	private boolean checkCheckcode() {
		String checkcode = etCheckCode.getText().toString();
		if (checkcode == null || "".equals(checkcode)) {
			Toast.makeText(this, "��֤�벻��Ϊ��!", Toast.LENGTH_SHORT).show();
		} else if (checkcode.length() != 6
				|| !FormatValidation.isNumeric(checkcode)) {
			Toast.makeText(this, "������6λ������֤��!", Toast.LENGTH_SHORT).show();
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
