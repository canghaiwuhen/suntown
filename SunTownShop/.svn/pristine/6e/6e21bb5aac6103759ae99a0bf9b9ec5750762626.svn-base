package com.suntown.suntownshop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.adapter.GridGoodsListAdapter;
import com.suntown.suntownshop.db.RouteGoodsDb;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.listener.OnImageMoveListener;
import com.suntown.suntownshop.listener.OnMoveViewListener;
import com.suntown.suntownshop.listener.ShakeListener;
import com.suntown.suntownshop.listener.ShakeListener.OnShakeListener;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Category;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.ParcelableGoods;
import com.suntown.suntownshop.runnable.GetJsonRunnable;
import com.suntown.suntownshop.service.LocalService;
import com.suntown.suntownshop.service.LocalService.OnBeaconFoundListener;
import com.suntown.suntownshop.utils.FileManager;
import com.suntown.suntownshop.utils.ImageMoveAnimation;
import com.suntown.suntownshop.utils.IsChineseOrNot;
import com.suntown.suntownshop.utils.JsonParser;
import com.suntown.suntownshop.utils.MyMath;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.PullUpRefreshListView;
import com.suntown.suntownshop.widget.UnScrollGridView;
import com.suntown.suntownshop.widget.PullUpRefreshListView.OnRefreshListener;
import com.suntown.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 改版前的摇一摇，发布时删除
 *
 * @author 钱凯
 * @version 2015年9月21日 上午9:54:08
 *
 */
public class ShakeActivitybak extends Activity implements OnShakeListener {
	private PullToRefreshGridView gridView;
	private ArrayList<Goods> list;
	private GridGoodsListAdapter adapter;
	private TextView tvTitle;
	private View mLoading;
	private View mShaking;
	private final static int MSG_GETGOODSLIST_COMPLETE = 1;
	private final static int MSG_ERR_NETWORKERR = -1;
	private final static int LOAD_ONCE_LEN = 30;
	private int mLoadTimes = 0;
	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/getgoods_upt_day?length="
			+ LOAD_ONCE_LEN + "&startIndex=";

	private ImageView ivBeacon;
	private LocalService mService;
	private boolean isOnShaking = false;
	private HashMap<String, Boolean> beaconMap;
	// 摇一摇 相关
	private ShakeListener mShakeListener;
	private SoundPool sndPool;
	private HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private PopupWindow pw;

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

	private void initViews() {
		list = new ArrayList<Goods>();
		adapter = new GridGoodsListAdapter(this, list);
		gridView.setAdapter(adapter);
		gridView.setVisibility(View.GONE);
		loadGoodsMore();
	}

	private void loadGoodsMore() {
		GetJsonRunnable getJsonRunnable = new GetJsonRunnable(URL
				+ (mLoadTimes * LOAD_ONCE_LEN + 1), MSG_GETGOODSLIST_COMPLETE,
				handler);
		new Thread(getJsonRunnable).start();
		mLoadTimes++;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle;
			String strMsg;
			JSONObject jsonObj;
			JSONArray jsonArray;
			mLoading.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			switch (msg.what) {
			case MSG_GETGOODSLIST_COMPLETE:
				gridView.onRefreshComplete();
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_JSON");
				int count = 0;
				System.out.println(strMsg);
				try {
					jsonObj = new JSONObject(strMsg);
					jsonArray = jsonObj.getJSONArray("INFO");
					count = jsonArray.length();
					if (count > 0) {
						for (int i = 0; i < count; i++) {
							jsonObj = (JSONObject) jsonArray.opt(i);
							String barCode = jsonObj.getString("BARCODE");
							String gCode = jsonObj.getString("GCODE");
							String gName = jsonObj.getString("GNAME");
							String gKind = jsonObj.getString("KIND");
							String gUnit = jsonObj.getString("UNIT");
							String gOriPrice = jsonObj.getString("ORIPRICE");
							String gMemPrice = jsonObj.getString("MEMPRICE");
							String gUptPrice = jsonObj.getString("UPTPRICE");
							String gSpec = jsonObj.getString("SPEC");
							String gClass = jsonObj.getString("GCLASS");
							String gProvider = jsonObj.getString("PROVIDER");
							String gBrand = jsonObj.getString("BRAND");
							String gOrigin = jsonObj.getString("ORIGIN");
							String gImgPath = jsonObj.getString("IMGPATH");
							int priceType = jsonObj.getInt("PRICETYPE");
							int deliverType = jsonObj.getInt("DELIVERYMODE");
							Goods goods = new Goods(barCode, gCode, gName,
									gKind, gUnit, gOriPrice, gMemPrice,
									gUptPrice, gSpec, gClass, gProvider,
									gBrand, gOrigin, gImgPath, priceType,
									deliverType);
							adapter.goodsList.add(goods);
						}
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(ShakeActivitybak.this, "找不到商品",
								Toast.LENGTH_SHORT).show();
					}
					if (count < LOAD_ONCE_LEN) {
						gridView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(ShakeActivitybak.this,
							"ERROR:分类商品解析错误:" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				break;

			case MSG_ERR_NETWORKERR:
				bundle = msg.getData();
				strMsg = bundle.getString("MSG_ERR");
				Toast.makeText(ShakeActivitybak.this, "连接超时，请稍后重试...",
						Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	private void initIndicator() {
		ILoadingLayout startLabels = gridView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("松开刷新");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在加载...");// 刷新时
		endLabels.setReleaseLabel("松开加载更多");// 下来达到一定距离时，显示的提示
	}

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = ((LocalService.LocalBinder) service).getService();
			mService.setOnBeaconFoundListener(foundListener);

		}
	};

	private OnBeaconFoundListener foundListener = new OnBeaconFoundListener() {

		@Override
		public void OnFound(String name) {
			// 检查是否是新发现的设备,如果是新发现的且动画没有播放则进入动画播放
			if (!beaconMap.containsKey(name)) {
				beaconMap.put(name, false);
			}
			mLastFoundTime = System.currentTimeMillis();
			if (!isOnShaking) {
				// 需在主线程中更新UI，否则可能会导致Fragment切换时显示异常
				ShakeActivitybak.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						shaking();
					}
				});
				isOnShaking = true;
			}

		}
	};
	private long mLastFoundTime = 0;
	private int BEACON_FIND_INTTERVAL = 10000;
	// 检查是否离开IBeacon区域
	private Runnable mCheckShakingRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			long now = System.currentTimeMillis();
			if (now - mLastFoundTime > BEACON_FIND_INTTERVAL) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						unShaking();
					}
				});
			} else {
				handler.postDelayed(mCheckShakingRunnable, 1000);
			}
		}
	};

	private void shaking() {
		handler.postDelayed(mCheckShakingRunnable, 200);
		ivBeacon.setImageResource(R.drawable.icon_shake_enable);
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.beacon_shake);
		ivBeacon.startAnimation(animation);
		// 加速度传感器
		mShakeListener.start();
	}

	private void unShaking() {
		isOnShaking = false;
		ivBeacon.clearAnimation();
		ivBeacon.setImageResource(R.drawable.icon_shake_disable);
		mShakeListener.stop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		initOptions();
		gridView = (PullToRefreshGridView) findViewById(R.id.gv_goodslist);
		initIndicator();

		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		mLoading = findViewById(R.id.loading);
		mShaking = findViewById(R.id.shaking);
		mShaking.setVisibility(View.GONE);
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
		loadSound();
		ivBeacon = (ImageView) findViewById(R.id.iv_beacon);
		// 获取传感器管理服务
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(this);
		beaconMap = new HashMap<String, Boolean>();
		initViews();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		if (isOnShaking) {
			shaking();
		}
		Intent intent = new Intent("com.suntown.suntownshop.SERVICE");
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		// 注销监听
		if (mService != null) {
			mService.unSetOnBeaconFoundListener();
			unbindService(conn);
			mShakeListener.stop();
		}
		super.onPause();
	}

	@Override
	public void onShake() {
		// TODO Auto-generated method stub
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}
		mShaking.setVisibility(View.VISIBLE);
		startAnim(); // 开始 摇一摇手掌动画
		mShakeListener.stop();
		sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 0,
				(float) 1.2);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0,
						(float) 1.0);
				showAward();
				mShakeListener.start();
				mShaking.setVisibility(View.GONE);
			}
		}, 2000);
	}

	private void loadSound() {

		sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		new Thread() {
			public void run() {
				try {
					soundPoolMap.put(
							0,
							sndPool.load(
									getAssets().openFd(
											"sound/shake_sound_male.mp3"), 1));

					soundPoolMap.put(1, sndPool.load(
							getAssets().openFd("sound/shake_match.mp3"), 1));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	private void showAward() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.get_award_popup, null);

		if (pw == null) {
			// 生成PopupWindow对象
			pw = new PopupWindow(mView, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			pw.setOutsideTouchable(true);
		} else {
			pw.setContentView(mView);
		}
		ImageView iv = (ImageView) mView.findViewById(R.id.iv_award);
		TextView tvMsg = (TextView) mView.findViewById(R.id.tv_award_msg);
		TextView tvNum = (TextView) mView.findViewById(R.id.tv_award_num);
		int index = MyMath.getRandom(1, 100);
		if (index <= 50) {
			imageLoader.displayImage("assets://image/award.jpg", iv);
			tvMsg.setText("恭喜您获得\n【清风2层抽取式面巾纸】1包");
			tvNum.setText("共50份奖品，您是第" + index + "位获奖者");

		} else {
			imageLoader.displayImage("assets://image/girl_shock.png", iv);
			tvMsg.setText("真遗憾\n差一点就拿到大奖了");
			tvNum.setText("别灰心\n下次再努力哦");

		}
		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
			}
		});
		pw.setAnimationStyle(R.style.popwindow_anim_style);
		pw.setFocusable(true);

		pw.setBackgroundDrawable(new ColorDrawable(0x7f000000));

		pw.showAtLocation(gridView, Gravity.CENTER, 0, 0);
	}
}