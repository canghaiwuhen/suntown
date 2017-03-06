package com.suntown.suntownshop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.suntown.suntownshop.asynctask.PostAsyncTask;
import com.suntown.suntownshop.asynctask.PostAsyncTask.OnCompleteCallback;
import com.suntown.suntownshop.imageshow.ImagePagerActivity;
import com.suntown.suntownshop.utils.FileManager;
import com.suntown.suntownshop.utils.FormatValidation;
import com.suntown.suntownshop.utils.ImageUtil;
import com.suntown.suntownshop.utils.JsonBuilder;
import com.suntown.suntownshop.utils.XmlParser;
import com.suntown.suntownshop.widget.ClearableEditText;
import com.suntown.suntownshop.widget.ConfirmDialog;
import com.suntown.suntownshop.widget.SelectPicPopupWindow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发起退货页面
 *
 * @author 钱凯
 * @version 2015年4月24日 下午2:32:21
 *
 */
public class RefundActivity extends Activity implements OnClickListener {
	private Gallery gallery;
	private ArrayList<String> list;
	private boolean isFinish = true;
	private File tempFile;
	private String voiceDataPath;
	private String imageDataPath;
	private String tempFileName;
	private String imageFileName;
	private String mUserId;
	private ImageView mVoiceBtn;
	private TextView mPhotoPrompt;
	private String mStrPhotoPrompt;
	private int photoCount = 0;

	// 自定义的弹出菜单
	private SelectPicPopupWindow menuWindow;
	private final static int REQUEST_CODE = 1;
	private final static int SELECT_PIC_KITKAT = 2;
	private final static int SELECT_PIC = 3;
	private final static int PIC_CUT_RESULT = 4;
	// 录音最大时长
	public static final int MAX_LENGTH = 1000 * 60;
	private long startTime;
	private long endTime;

	private View viewPlayVoice;
	private ImageView ivDeleteVoice;
	private ImageView ivPlayA;
	private TextView tvTimeTips;
	private View voiceLinear;
	private ClearableEditText remarkEdit;
	private String mLoginToken;
	private String mOrderNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_refund);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"suntownshop", 0);
		mUserId = mSharedPreferences.getString("userId", "");
		mLoginToken = mSharedPreferences.getString("m_voucher", "");
		boolean isLogin = mSharedPreferences.getBoolean("islogin", false);
		Intent intent = getIntent();
		if (intent.hasExtra("orderno")) {
			mOrderNo = intent.getStringExtra("orderno");
		}
		if (mOrderNo == null || "".equals(mOrderNo)) {
			Toast.makeText(this, "订单错误", Toast.LENGTH_SHORT).show();
			finish();
		}
		if (!isLogin) {
			intent = new Intent(RefundActivity.this, LoginActivity.class);
			startActivity(intent);
			isFinish = false;
			finish();
		} else {

			imageDataPath = FileManager.getDataPath(this) + "/imagedata/";

			File dir = new File(imageDataPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			gallery = (Gallery) findViewById(R.id.photo_gallery);
			remarkEdit = (ClearableEditText) findViewById(R.id.remarkEdit);

			mPhotoPrompt = (TextView) findViewById(R.id.photoPrompt);

			mStrPhotoPrompt = getString(R.string.photo_prompt);
			mPhotoPrompt
					.setText(String.format(mStrPhotoPrompt, photoCount, 12));
			list = new ArrayList<String>();
			list.add("");
			gallery.setAdapter(photoAdapter);
			gallery.setOnItemClickListener(onImageClick);
			alignGalleryToLeft(gallery);
		}

	}

	private OnItemClickListener onImageClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			hideInput();
			String path = list.get(position);
			if (path != null && !"".equals(path)) {
				String[] urls = new String[] { "file:///" + path };
				Intent intent = new Intent(RefundActivity.this,
						ImagePagerActivity.class);

				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				isFinish = false;
				startActivity(intent);
			} else {
				showSelectPicMenu();
			}
		}
	};

	private void hideInput() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法

		imm.hideSoftInputFromWindow(RefundActivity.this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void showSelectPicMenu() {
		// 实例化SelectPicPopupWindow
		menuWindow = new SelectPicPopupWindow(RefundActivity.this, itemsOnClick);
		// 显示窗口
		menuWindow.showAtLocation(RefundActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
			imageFileName = mUserId + format.format(new Date()) + ".jpg";
			switch (v.getId()) {
			case R.id.btn_take_photo:
				openCamera();
				break;
			case R.id.btn_pick_photo:
				openGallery();
				break;
			default:
				break;
			}
		}
	};

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
			File file = new File(imageDataPath + imageFileName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, REQUEST_CODE);
		} else {
			Toast.makeText(RefundActivity.this, "没有SD卡，无法保存相片",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Bitmap photo = null;
		switch (requestCode) {
		case REQUEST_CODE: // 拍照获得照片
			File temp = new File(imageDataPath + imageFileName);
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
				photo = ImageUtil.getimage(imageDataPath + imageFileName); // extras.getParcelable("data");
				if (photo != null) {
					if (ImageUtil.compressBmpToFile(photo, imageDataPath
							+ imageFileName)) {
						list.add(list.size() - 1, imageDataPath + imageFileName);
						mPhotoPrompt.setText(String.format(mStrPhotoPrompt,
								++photoCount, 12 - photoCount));
						if (photoCount == 12) {
							list.remove(12);
						}
						photoAdapter.notifyDataSetChanged();
						alignGalleryToLeft(gallery);
					}
				}
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private class ViewHolder {
		View addFrame;
		ImageView addImage;
		View showFrame;
		ImageView itemImage;
		ImageView closeImg;
	}

	private BaseAdapter photoAdapter = new BaseAdapter() {

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(RefundActivity.this).inflate(
						R.layout.photo_grid_item, null);
				viewHolder = new ViewHolder();
				viewHolder.addFrame = convertView.findViewById(R.id.addFrame);
				viewHolder.addImage = (ImageView) convertView
						.findViewById(R.id.addImage);
				viewHolder.showFrame = convertView.findViewById(R.id.showFrame);
				viewHolder.itemImage = (ImageView) convertView
						.findViewById(R.id.itemImage);
				viewHolder.closeImg = (ImageView) convertView
						.findViewById(R.id.closeImg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if ((list.size() - 1) == position && "".equals(list.get(position))) {
				viewHolder.addFrame.setVisibility(View.VISIBLE);
				viewHolder.showFrame.setVisibility(View.GONE);
				viewHolder.addImage.setImageResource(R.drawable.bg_photo_add);
				/*
				 * if (position == 0) { viewHolder.addImage
				 * .setImageResource(R.drawable.bg_photo_add2); } else {
				 * viewHolder.addImage
				 * .setImageResource(R.drawable.bg_photo_add); }
				 */

			} else {
				viewHolder.addFrame.setVisibility(View.GONE);
				viewHolder.showFrame.setVisibility(View.VISIBLE);
				Bitmap bm = ImageUtil.loadBitmap(list.get(position));
				if (bm != null) {
					viewHolder.itemImage.setImageBitmap(bm);
				}
				viewHolder.closeImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ConfirmDialog dialog = new ConfirmDialog(
								RefundActivity.this,
								getString(R.string.delete_img_confirm),
								getString(R.string.tips_text),
								getString(R.string.confirm_text),
								getString(R.string.cancel_text));
						if (dialog.ShowDialog()) {
							list.remove(position);
							mPhotoPrompt.setText(String.format(mStrPhotoPrompt,
									--photoCount, 12 - photoCount));
							if (photoCount == 11) {
								list.add("");
							}
							photoAdapter.notifyDataSetChanged();
							alignGalleryToLeft(gallery);
						}
					}
				});
			}
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

	public void close(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", 150);
		// intent.putExtra("outputY", 150);
		// 部分手机内存无法支持返回大图片，以文件形式返回
		intent.putExtra("return-data", false);
		intent.putExtra("noFaceDetection", true);
		// 设置裁剪后输出文件位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(imageDataPath + imageFileName)));
		// 输出文件格式
		intent.putExtra("outputFormat", "JPEG");
		startActivityForResult(intent, PIC_CUT_RESULT);
	}

	public void confirm(View v) {
		Bundle b = new Bundle();
		String remarkMsg = remarkEdit.getText().toString();
		int msgLen = FormatValidation.getWordCount(remarkMsg);
		if (msgLen > 500) {
			Toast.makeText(this, "退货原因最多输入500个汉字", Toast.LENGTH_SHORT).show();
			return;
		}
		if (msgLen == 0) {
			Toast.makeText(this, "请输入退货原因", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			showProgress(true);
			String strJson = JsonBuilder.makeRefundJson(mOrderNo, mUserId,
					mLoginToken, remarkMsg, list);
			System.out.println(strJson);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("strMsg", strJson);
			PostAsyncTask postAsyncTask = new PostAsyncTask(URL, callback);
			postAsyncTask.execute(params);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			showProgress(false);
			e.printStackTrace();
		}
	}

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

	private final static String URL = Constants.DOMAIN_NAME
			+ "axis2/services/sunteslwebservice/rebackGoods";
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
						Toast.makeText(RefundActivity.this,
								"退货申请已提交，请携带所有商品至超市服务台退货", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(RefundActivity.this,
								"提交退货申请失败,请稍后重试...", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(RefundActivity.this, "服务器返回信息错误:" + msg,
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			} else {
				Toast.makeText(RefundActivity.this, "网络连接错误,请稍后重试...",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	private void alignGalleryToLeft(Gallery gallery) {
		int galleryWidth = getWindowManager().getDefaultDisplay().getWidth();
		int itemWidth = getResources().getDimensionPixelSize(
				R.dimen.gallery_item_width);
		int spacing = getResources().getDimensionPixelSize(
				R.dimen.gallery_spacing);
		int offset;
		if (galleryWidth <= itemWidth) {
			offset = galleryWidth / 2 - itemWidth / 2 - spacing;
		} else {
			offset = galleryWidth - itemWidth - 2 * spacing;
		}
		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
				mlp.bottomMargin);
	}
}
