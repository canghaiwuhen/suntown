package com.suntown.suntownshop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.model.Order;
import com.suntown.suntownshop.utils.FileManager;
import com.suntown.suntownshop.utils.ImageUtil;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.CircleImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 我的，个人中心
 *
 * @author 钱凯
 * @version 2014年12月21日 上午9:38:20
 *
 */
public class FragmentPage5 extends Fragment {
	private boolean isShowLogin = true;
	private TextView tvOrderNoPay;
	private ProgressBar pbOrderNoPay;
	private TextView tvPoints;
	private ProgressBar pbPoints;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_5, null);
		tvOrderNoPay = (TextView) view.findViewById(R.id.tv_ordernopay);
		pbOrderNoPay = (ProgressBar) view.findViewById(R.id.pb_ordernopay);
		tvPoints = (TextView) view.findViewById(R.id.tv_points);
		pbPoints = (ProgressBar) view.findViewById(R.id.pb_points);
		ivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
		initOptions();
		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		tvOrderNoPay.setVisibility(View.GONE);
		pbOrderNoPay.setVisibility(View.GONE);
		pbPoints.setVisibility(View.GONE);
		tvPoints.setVisibility(View.GONE);
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("suntownshop", 0);
		boolean isLogin = sharedPreferences.getBoolean("islogin", false);
		String userId = sharedPreferences.getString("userId", "");
		String nickname = sharedPreferences.getString("nickname", "");
		String showname = sharedPreferences.getString("showname", "");
		String username = sharedPreferences.getString("username", "");
		String password = sharedPreferences.getString("password", "");
		String strAvatar = sharedPreferences.getString("avatar", "");
		String token = sharedPreferences.getString("m_voucher", "");
		if (isLogin) {
			isShowLogin = false;
		}
		TextView tvUsername = (TextView) getActivity().findViewById(
				R.id.tv_myaccount_username);
		if (isLogin && !"".equalsIgnoreCase(userId)) {
			if (strAvatar != null && !"".equals(strAvatar)) {
				imageLoader.displayImage(strAvatar, ivAvatar, options);
			}
			tvUsername
					.setText("".equals(nickname) ? ("".equals(showname) ? ("ID:" + userId)
							: showname)
							: nickname);
			getOrdersById(userId);
			getMemPoints(userId, token);
		} else {
			ivAvatar.setImageResource(R.drawable.myaccount_default_avatar);
			tvUsername.setText(getString(R.string.myname_text));
			if (isShowLogin) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				isShowLogin = false;
			}
		}
		super.onResume();
	}

	private OnCompleteCallback callback = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			if(getActivity()==null||getActivity().isFinishing()){
				return;
			}
			pbOrderNoPay.setVisibility(View.GONE);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						// 取得订单数据，开始解析
						JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
						ArrayList<Order> list = JsonParser
								.ordersParse(jsonArray);
						if (list.size() > 0) {
							tvOrderNoPay.setVisibility(View.VISIBLE);
							tvOrderNoPay.setText("您有" + list.size() + "笔待付款订单");
						}
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(), "服务器返回错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();

					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};
	private final static String URL_ORDER_NOPAY = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getHistoryOrder";

	private void getOrdersById(String uId) {
		pbOrderNoPay.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", uId);
		params.put("formstatus", "0");
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_ORDER_NOPAY,
				callback);
		postAsyncTask.execute(params);
	}

	private final static String URL_GET_POINTS = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getMenMark";

	private void getMemPoints(String userId, String token) {
		pbPoints.setVisibility(View.VISIBLE);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memid", userId);
		params.put("logintoken", token);
		PostAsyncTask postAsyncTask = new PostAsyncTask(URL_GET_POINTS,
				callbackPoints);
		postAsyncTask.execute(params);
	}

	private OnCompleteCallback callbackPoints = new OnCompleteCallback() {

		@Override
		public void onComplete(boolean isOk, String msg) {
			// TODO Auto-generated method stub
			if(getActivity()==null||getActivity().isFinishing()){
				return;
			}
			pbPoints.setVisibility(View.GONE);
			if (isOk) {
				JSONObject jsonObj;
				try {
					msg = XmlParser.parse(msg, "UTF-8", "return");
					jsonObj = new JSONObject(msg);
					int sendState = jsonObj.getInt("RESULT");
					if (sendState == 0) {
						// 取得订单数据，开始解析
						String menmark = jsonObj.getString("MENMARK");
						int points = 0;
						if(menmark!=null&&!"".equals(menmark)){
							points = Integer.valueOf(menmark);
						}
						
						tvPoints.setVisibility(View.VISIBLE);
						tvPoints.setText(String.valueOf(points));
						
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(), "服务器返回错误，请稍后重试...",
							Toast.LENGTH_SHORT).show();
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}

		}
	};
}