package com.suntown.suntownshop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.model.Evaluate;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.widget.PullUpRefreshListView;
import com.suntown.suntownshop.widget.PullUpRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 商品评价
 *
 * @author 钱凯
 * @version 2015年3月21日 上午9:40:33
 *
 */
public class GoodsEvaluateActivity extends Activity {
	private PullUpRefreshListView listView;
	private ArrayList<Evaluate> list;
	private View loading;
	private int startIndex = 1;
	private final static int GET_COUNT_ONCE = 20;
	private String barcode;
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
		setContentView(R.layout.activity_goods_evaluate);
		initOptions();
		loading = findViewById(R.id.loading);
		listView = (PullUpRefreshListView)findViewById(R.id.lv_eva);
		listView.setonRefreshListener(refreshListener);
		
		list = new ArrayList<Evaluate>();
		loading.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		listView.setAdapter(adapter);
		Intent intent = getIntent();
		barcode = intent.getStringExtra("barcode");
		initData();
	}
	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getEvabyBarcode?Barcode=";
	private final static int MSG_GETEVA_COMPLETE = 1;
	private final static int MSG_ERROR = -1;
	private void initData(){

		GetJsonRunnable getJsonRunnable = new GetJsonRunnable(URL + barcode
				+ "&startIndex=" + startIndex + "&length=" + GET_COUNT_ONCE,
				MSG_GETEVA_COMPLETE, handler);
		new Thread(getJsonRunnable).start();

	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle = msg.getData();
			String strMsg;
			JSONObject jsonObj;
			switch(msg.what){
			case MSG_GETEVA_COMPLETE:
				strMsg = bundle.getString("MSG_JSON");
				System.out.println("JSON------>" + strMsg);
				try {
					jsonObj = new JSONObject(strMsg);
					int sendState = jsonObj.getInt("RESULT");
					if(sendState==0){
						JSONArray jsonArray = jsonObj.getJSONArray("RECORD");
						ArrayList<Evaluate> listOnce = JsonParser.evaluateParse(jsonArray);
						if(listOnce.size()<GET_COUNT_ONCE){
							listView.setRefreshable(false);
						}
						list.addAll(listOnce);
						adapter.notifyDataSetChanged();
						startIndex +=GET_COUNT_ONCE;
						loading.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						listView.onRefreshComplete();
					}else{
						Toast.makeText(getApplicationContext(),
								"该商品暂无评价" ,
								Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(),
							"服务器数据错误，请稍后重试..." ,
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
	static class ViewHolder{
		TextView tvEvaText;
		ImageView ivAvatar;
		RatingBar rbRate;
		TextView tvEvaDate;
		TextView tvNickname;
	}
	
	private BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null){
				convertView = LayoutInflater.from(GoodsEvaluateActivity.this).inflate(
						R.layout.goods_evaluate_item, null);
				holder = new ViewHolder();
				holder.tvEvaText = (TextView)convertView.findViewById(R.id.tv_evatext);
				holder.ivAvatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
				holder.rbRate = (RatingBar)convertView.findViewById(R.id.rb_evarate);
				holder.tvEvaDate = (TextView)convertView.findViewById(R.id.tv_evadate);
				holder.tvNickname = (TextView)convertView.findViewById(R.id.tv_nickname);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			Evaluate eva = list.get(position);
			String avatar = eva.getAvatar();
			if(avatar!=null&&!"".equals(avatar)){
				if(avatar.indexOf("http://")<0){
					avatar+="http://";
				}
				imageLoader.displayImage(avatar, holder.ivAvatar);
			}
			String nickname = eva.getNickname();
			String evaText = eva.getEvaText();
			if(nickname!=null&&!"".equals(nickname)){
				holder.tvNickname.setText(nickname);
			}else{
				holder.tvNickname.setText("昵称");
			}
			if(evaText==null||"".equals(evaText)){
				evaText = "该用户很懒，什么都没有留下";
			}
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date;
			try {
				date = formatDate.format(formatDate.parse(eva.getEvaDate()));
				holder.tvEvaDate.setText(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.tvEvaText.setText(evaText);
			holder.rbRate.setRating(eva.getRate());
			
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	};
	
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			initData();
			
		}
	};
	
	public void close(View v){
		finish();
	}
}
