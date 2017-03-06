package com.suntown.suntownshop;

import java.util.HashMap;

import org.json.JSONObject;

import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.model.Receiver;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.XmlParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �ջ���ַ�޸�ҳ��
 *
 * @author Ǯ��
 * @version 2015��6��21�� ����9:12:18
 *
 */
public class AddressModifyActivity extends Activity {
	private TextView tvTitle;
	private EditText etName;
	private EditText etPhone;
	private EditText etAddress;
	private CheckBox cbIsDefault;
	private int id = -1;
	private String name;
	private String phone;
	private String address;
	private boolean isDefault;
	private String mUserId;
	private String mLoginToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addressmodify);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mUserId = mSharedPreferences.getString("userId", "");
		mLoginToken = mSharedPreferences.getString("m_voucher", "");
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		etName = (EditText) findViewById(R.id.et_receiver_name);
		etPhone = (EditText) findViewById(R.id.et_receiver_phone);
		etAddress = (EditText) findViewById(R.id.et_receiver_address);
		cbIsDefault = (CheckBox) findViewById(R.id.cb_isdefault);
		Intent intent = getIntent();
		if (intent.hasExtra("id")) {
			id = intent.getIntExtra("id", -1);
			name = intent.getStringExtra("name");
			phone = intent.getStringExtra("phone");
			address = intent.getStringExtra("address");
			isDefault = intent.getBooleanExtra("isdefault", false);
			etName.setText(name);
			etPhone.setText(phone);
			etAddress.setText(address);
			cbIsDefault.setChecked(isDefault);
			tvTitle.setText("�޸��ջ���ַ");
		} else {
			tvTitle.setText("�����ջ���ַ");
		}

	}

	public void close(View v) {
		finish();
	}
	/**
	 * ����У��������Ϣ
	 * @return
	 */
	private boolean checkInput() {
		name = etName.getText().toString();
		phone = etPhone.getText().toString();
		address = etAddress.getText().toString();
		isDefault = cbIsDefault.isChecked();
		if (name == null || phone == null || address == null || "".equals(name)
				|| "".equals(phone) || "".equals(address)) {
			Toast.makeText(AddressModifyActivity.this, "�ջ��ˡ��绰�͵�ַ���Ǳ�����!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(FormatValidation.getWordCount(name)>20){
			Toast.makeText(AddressModifyActivity.this, "�ջ����������10�������ַ�!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(FormatValidation.getWordCount(phone)>20){
			Toast.makeText(AddressModifyActivity.this, "�绰���벻�ܳ���20λ!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!FormatValidation.isPhoneNo(phone)){
			Toast.makeText(AddressModifyActivity.this, "�绰�����ʽ����ȷ!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if(FormatValidation.getWordCount(address)>50){
			Toast.makeText(AddressModifyActivity.this, "��ַ�������50�������ַ�!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	/**
	 * �����ַ
	 * @param v
	 */
	public void save(View v) {
		if (checkInput()) {
			showProgress(true);
			HashMap<String, String> params = new HashMap<String, String>();
			if (id > 0) {			
				params.put("memid", mUserId);
				params.put("logintoken", mLoginToken);
				params.put("addressId", String.valueOf(id));
				params.put("address", address);
				params.put("receiver", name);
				params.put("phone", phone);
				params.put("isdefault", String.valueOf(isDefault?1:0));
				PostAsyncTask postAsyncTask = new PostAsyncTask(URL_MODIFY,
						callback);
				postAsyncTask.execute(params);
			} else {
				params.put("memid", mUserId);
				params.put("logintoken", mLoginToken);
				params.put("address", address);
				params.put("receiver", name);
				params.put("phone", phone);
				params.put("isdefault", String.valueOf(isDefault?1:0));
				PostAsyncTask postAsyncTask = new PostAsyncTask(URL_ADD,
						callback);
				postAsyncTask.execute(params);
			}
		}
	}
	/**
	 * �����ջ���ַ�ӿڵ�ַ
	 */
	private final static String URL_ADD = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/addAddress";
	/**
	 * �޸��ջ���ַ�ӿڵ�ַ
	 */
	private final static String URL_MODIFY = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/updateAddress";
	/**
	 * �������޸ĵ�ַ�ص��ӿ�
	 */
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
						Toast.makeText(getApplicationContext(), "��ַ����ɹ�",
								Toast.LENGTH_SHORT).show();
						finish();
					} else if(sendState==2){
						Toast.makeText(getApplicationContext(), "���ܱ�����ͬ�ĵ�ַ��Ϣ",
								Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(getApplicationContext(), "��ַ����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ش������Ժ�����...",
							Toast.LENGTH_SHORT).show();

					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "���ӳ�ʱ�����Ժ�����...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	
	private ProgressDialog mPDialog;
	/**
	 * ������Dialog
	 * @param show
	 */
	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(AddressModifyActivity.this);
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
}