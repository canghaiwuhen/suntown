package com.suntown.suntownshop;

import java.util.HashMap;

import org.json.JSONObject;

import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ConfirmDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * �޸��ǳ�
 *
 * @author Ǯ��
 * @version 2015��5��21�� ����9:44:45
 *
 */
public class NicknameModifyActivity extends Activity {
	private EditText etNickname;
	private TextView tvErr;
	private String mNickname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nicknamemodify);
		etNickname = (EditText) findViewById(R.id.et_nickname);
		tvErr = (TextView) findViewById(R.id.tv_errmsg);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mNickname = mSharedPreferences.getString("nickname", "");

		if (mNickname != null && !"".equals(mNickname)) {
			etNickname.setText(mNickname);
			etNickname.setSelection(mNickname.length());
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInputFromInputMethod(etNickname.getWindowToken(), 0);
	}

	public void close(View v) {
		finish();
	}

	public void save(View v) {
		mNickname = etNickname.getText().toString();
		if (mNickname != null && !"".equals(mNickname)) {
			mNickname = mNickname.replaceAll("\\s*", "");
			if (FormatValidation.getWordCount(mNickname) > 20) {
				Toast.makeText(NicknameModifyActivity.this, "�ǳ����Ϊ10�����ֻ�20���ַ�",
						Toast.LENGTH_SHORT).show();
			} else {
				updateNickname(mNickname);
			}
		} else {
			mNickname = "";
			ConfirmDialog dialog = new ConfirmDialog(
					NicknameModifyActivity.this, "ȷ��Ҫ����ǳ���?",
					getString(R.string.tips_text),
					getString(R.string.confirm_text),
					getString(R.string.cancel_text));
			if (dialog.ShowDialog()) {
				updateNickname(mNickname);
			}
		}
	}

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(NicknameModifyActivity.this);
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
			}
		}
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/updatenikename";;

	/**
	 * �޸��ǳ�
	 * 
	 * @param nickname
	 * @return
	 */
	private void updateNickname(String nickname) {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		String mVoucher = mSharedPreferences.getString("m_voucher", "");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", mVoucher);
		params.put("nickname", nickname);
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
						Toast.makeText(NicknameModifyActivity.this, "�ǳ��޸ĳɹ�",
								Toast.LENGTH_SHORT);

						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putString("nickname", mNickname);
						mEditor.commit();
						finish();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(NicknameModifyActivity.this,
							"�ǳ��޸�ʧ��:" + msg, Toast.LENGTH_SHORT);
					e.printStackTrace();
				}

			} else {
				Toast.makeText(NicknameModifyActivity.this, "�������Ӵ���,���Ժ�����...",
						Toast.LENGTH_SHORT);
			}
		}

	};
}