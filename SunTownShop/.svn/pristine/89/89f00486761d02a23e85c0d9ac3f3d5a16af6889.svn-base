package com.suntown.suntownshop;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.HintOnEditText;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * 改版前的修改用户信息页面，发布时请删除
 *
 * @author 钱凯
 * @version 2015年9月21日 上午9:35:08
 *
 */
public class CopyOfModifyInfoActivity extends Activity {
	private HintOnEditText etName;
	private HintOnEditText etIdCard;
	private HintOnEditText etAge;
	private HintOnEditText etAddress;
	private TextView tvName;
	private TextView tvIdCard;
	private TextView tvAge;
	private TextView tvAddress;
	private TextView tvSex;
	private TextView tvErrMsg;
	private Button btnConfirm;
	private TextView tvEdit;
	String userId;
	String mName;
	String mIdCard;
	String mAge;
	String mAddress;
	String mVoucher;
	int mSex;
	PopupWindow pw;
	private View main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount_baseinfo);
		main = findViewById(R.id.main);
		tvName = (TextView)findViewById(R.id.tv_name);
		tvIdCard = (TextView)findViewById(R.id.tv_cardno);
		tvAge = (TextView)findViewById(R.id.tv_age);
		tvAddress = (TextView)findViewById(R.id.tv_address);
		//etName = (HintOnEditText) findViewById(R.id.et_name);	
		//etIdCard = (HintOnEditText) findViewById(R.id.et_cardno);
		//etAge = (HintOnEditText) findViewById(R.id.et_age);
		//etAddress = (HintOnEditText) findViewById(R.id.et_address);
		tvSex = (TextView) findViewById(R.id.tv_sex);
		tvErrMsg = (TextView) findViewById(R.id.tv_errmsg);
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		tvEdit = (TextView) findViewById(R.id.tv_modifyinfo_edit);
		initUserInfo();

	}

	private boolean isOnEdit = false;

	private void showEdit() {
		tvEdit.setText(isOnEdit ? "取消" : "编辑");
		// etName.setEnabled(isOnEdit);
		// etIdCard.setEnabled(isOnEdit);
		// etAge.setEnabled(isOnEdit);
		// etAddress.setEnabled(isOnEdit);
		// rgSex.getChildAt(0).setEnabled(isOnEdit);
		// rgSex.getChildAt(1).setEnabled(isOnEdit);
		tvErrMsg.setVisibility(View.GONE);
		// btnConfirm.setVisibility(isOnEdit ? View.VISIBLE : View.GONE);
		if (!isOnEdit) {
			etName.setText(mName);
			etIdCard.setText(mIdCard);
			etAge.setText(mAge);
			etAddress.setText(mAddress);
			tvSex.setText(mSex == 0 ? "男" : "女");
		}
	}

	private boolean checkAge() {
		mAge = etAge.getText().toString();
		if (!FormatValidation.isNumeric(mAge)) {
			tvErrMsg.setVisibility(View.VISIBLE);
			tvErrMsg.setText("请输入正确的年龄!");
			return false;
		}
		return true;
	}

	private boolean checkIdCard() {
		mIdCard = etIdCard.getText().toString();
		if (!FormatValidation.isIdCard(mIdCard)) {
			tvErrMsg.setVisibility(View.VISIBLE);
			tvErrMsg.setText("身份证号码不合法!");
			return false;
		}
		return true;
	}

	private void initUserInfo() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		userId = mSharedPreferences.getString("userId", "");
		if (userId == null || "".equals(userId)) {
			finish();
		}

		mName = mSharedPreferences.getString("m_name", "");
		mIdCard = mSharedPreferences.getString("m_cardno", "");
		mAge = mSharedPreferences.getString("m_age", "");
		mAddress = mSharedPreferences.getString("m_address", "");
		mSex = mSharedPreferences.getInt("m_sex", 0);
		mVoucher = mSharedPreferences.getString("m_voucher", "");
		if (mSex < 0 || mSex > 1) {
			mSex = 0;
		}
		tvName.setText(mName);
		tvIdCard.setText(mIdCard);
		tvAge.setText(mAge);
		tvAddress.setText(mAddress);
		tvSex.setText(mSex == 0 ? "男" : "女");
		//showEdit();
	}

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/addUserInfo";// Constants.DOMAIN_NAME

	public void confirm(View v) {
		if (checkIdCard() && checkAge()) {
			tvErrMsg.setVisibility(View.GONE);
			HashMap<String, String> params = new HashMap<String, String>();
			mAddress = etAddress.getText().toString();
			mName = etName.getText().toString();
			params.put("memid", userId);
			params.put("name", mName);
			params.put("cardno", mIdCard);
			params.put("age", mAge);
			params.put("address", mAddress);
			params.put("sex", "" + mSex);
			params.put("logintoken", mVoucher);
			params.put("loginname", "");
			PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
			postAsyncTask.execute(params);
		}
	}

	private OnCompleteCallback callback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			if (isOk) {
				JSONObject jsonObj;
				try {

					msg = XmlParser.parse(msg, "UTF-8", "return");
					System.out.println("msg------>" + msg);
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						Toast.makeText(getApplicationContext(), "信息修改成功!",
								Toast.LENGTH_SHORT).show();
						SharedPreferences mSharedPreferences = getSharedPreferences(
								"suntownshop", 0);
						SharedPreferences.Editor mEditor = mSharedPreferences
								.edit();
						mEditor.putString("m_name", mName);
						mEditor.putString("m_cardno", mIdCard);
						mEditor.putString("m_age", mAge);
						mEditor.putString("m_address", mAddress);
						mEditor.putInt("m_sex", mSex);
						mEditor.commit();
						isOnEdit = false;
						showEdit();
					} else {
						Toast.makeText(getApplicationContext(), "信息修改失败!",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getApplicationContext(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private ProgressDialog mPDialog;

	public void showProgress(final boolean show) {
		if (show) {
			mPDialog = new ProgressDialog(CopyOfModifyInfoActivity.this);
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

	public void modify(View v) {
		isOnEdit = !isOnEdit;
		initUserInfo();

	}

	public void close(View v) {
		finish();
	}
	
	public void selectSex(View v){
		LayoutInflater inflater = (LayoutInflater) CopyOfModifyInfoActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.select_sex_popup, null);
		View sexM = mView.findViewById(R.id.sex_m);
		View sexF = mView.findViewById(R.id.sex_f);
		if (pw == null) {
			// 生成PopupWindow对象
			pw = new PopupWindow(mView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			pw.setOutsideTouchable(true);
		} else {
			pw.setContentView(mView);
		}
		pw.setFocusable(true);
		pw.setBackgroundDrawable(new ColorDrawable(0x7f000000)); 
		sexM.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSex = 0;
				tvSex.setText("男");
				if(pw!=null&&pw.isShowing()){
					pw.dismiss();
				}
			}
		});
		sexF.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSex = 1;
				tvSex.setText("女");
				if(pw!=null&&pw.isShowing()){
					pw.dismiss();
				}
			}
		});
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
	}
}
