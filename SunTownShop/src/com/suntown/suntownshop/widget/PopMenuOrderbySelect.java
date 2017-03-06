package com.suntown.suntownshop.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.suntown.suntownshop.R;
import com.suntown.suntownshop.listener.OnClassSelectListener;
import com.suntown.suntownshop.listener.OnOrderbySelectListener;
import com.suntown.suntownshop.listener.OnShopSelectListener;
import com.suntown.suntownshop.model.Category;
import com.suntown.suntownshop.model.Store;
import com.suntown.suntownshop.utils.MyMath;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 带箭头的弹出菜单-类目 排序
 *
 * @author 钱凯
 * @version 2015年7月14日 下午5:33:53
 *
 */
public class PopMenuOrderbySelect implements OnDismissListener {

	// 存储每个按钮点击后对应生成的drawable
	private HashMap<View, Drawable> mapDrawable = new HashMap<View, Drawable>();

	private Context context;
	private PopupWindow popupWindow;
	private PopupWindow popupMask;

	private int screenwidth;// 屏幕的宽度

	private int popupWindowHeight = 0;// popupWindow的高度
	private int parentLeft = 0;// 父控件的左边距
	private int parentWidth = 0;// 父控件的宽度
	private Drawable dleft; // 左半边图片
	private Drawable dmid;// 当中图片
	private Drawable dright;// 右边的图片

	private View viewMask;
	private Drawable bg;// 生成的背景图片

	private OnOrderbySelectListener selectListener;
	private LinkedHashMap<Integer, String> orderbys; // 所有排序方式
	
	private int curIndex = -1;


	/**
	 * 
	 * @param context
	 *            上下文
	 * @param leftDrawableID
	 *            左半边图片的资源ID
	 * @param midDrawableID
	 *            当中图片的资源ID
	 * @param righrDrawableID
	 *            右半边图片的资源ID
	 */
	public PopMenuOrderbySelect(Context context, int leftDrawableID,
			int midDrawableID, int righrDrawableID,
			LinkedHashMap<Integer, String> orderby,
			OnOrderbySelectListener listener) {

		// TODO Auto-generated constructor stub
		this.context = context;
		this.orderbys = orderby;
		
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View view = inflater.inflate(R.layout.select_orderby_popup, null);
		this.viewMask = inflater.inflate(R.layout.mask_layout, null);

		this.selectListener = listener;

		// 生成PopupWindow对象
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, context
				.getResources().getDimensionPixelSize(R.dimen.popmenu_h));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(this);
		screenwidth = getScreenWidth();
		
		final ListView lv = (ListView) view.findViewById(R.id.lv_orderby);

		lv.setAdapter(adapterOrderby);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				if (curIndex != position) {
					List<Entry<Integer, String>> list = new ArrayList<Entry<Integer, String>>(
							orderbys.entrySet());
					String name = list.get(position).getValue();
					int id = list.get(position).getKey();
					TextView tv = (TextView) v.findViewById(R.id.tv_class);
					ImageView iv = (ImageView) v.findViewById(R.id.iv_check);
					tv.setTextColor(PopMenuOrderbySelect.this.context
							.getResources().getColor(R.color.item_text_press));
					iv.setVisibility(View.VISIBLE);
					if (curIndex > -1) {
						View oldView = getViewByPosition(curIndex, lv);
						tv = (TextView) oldView.findViewById(R.id.tv_class);
						iv = (ImageView) oldView.findViewById(R.id.iv_check);

						try {
							XmlPullParser xrp = PopMenuOrderbySelect.this.context
									.getResources().getXml(
											R.drawable.selector_item_menu_text);
							ColorStateList csl = ColorStateList.createFromXml(
									PopMenuOrderbySelect.this.context
											.getResources(), xrp);
							tv.setTextColor(csl);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						iv.setVisibility(View.GONE);
					}
					curIndex = position;

					if (selectListener != null) {
						selectListener.onOrderbySelect(id, name);
					}
					
				}
			}
		});
		
		// 在指定位置弹出窗口
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

		// 生成drawable
		this.dleft = context.getResources().getDrawable(leftDrawableID);
		this.dmid = context.getResources().getDrawable(midDrawableID);
		this.dright = context.getResources().getDrawable(righrDrawableID);
	}

	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenW = dm.widthPixels;
		return screenW;

	}

	public void show(View parent, Context context) {

		bg = mapDrawable.get(parent);// 为节省资源，map中会保存以前生成的背景，根据父控件来获得
		popupWindowHeight = popupWindow.getHeight();// 得到popupWindow的高度，在popupWindow构造完后才能获取
		this.parentLeft = parent.getLeft();// 父控件的左边距
		this.parentWidth = parent.getWidth();// 父控件的宽度
		if (bg == null)// 背景为空
		{
			createDrawable(context);// 重新生成背景图
			mapDrawable.put(parent, bg);// 保存到map中
		}
		popupWindow.setBackgroundDrawable(bg);// 给popupWindow设置背景
		// WindowManager
		// winManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

		Rect frame = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		if (popupMask == null) {
			popupMask = new PopupWindow(viewMask, LayoutParams.MATCH_PARENT,
					frame.height() - parent.getHeight());
			popupMask.setBackgroundDrawable(new ColorDrawable(0x7f000000));
		}
		popupMask.showAsDropDown(parent, 0, 0);
		popupWindow.showAsDropDown(parent, 0, -12);
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		// viewMask.setVisibility(View.VISIBLE);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	/**
	 * 以一个Bitmap为画布，画上一个Bitmap
	 * 
	 * @param canvasBitmap
	 *            作为画布的Bitmap
	 * @param drawBitmap
	 *            要被绘制的Bitmap
	 * @param top
	 *            从画布的距离顶部的top位置开始
	 * @param left
	 *            从画布的距离左边的left位置开始
	 */
	private void drawbitMap(Bitmap canvasBitmap, Bitmap drawBitmap, int top,
			int left) {
		Canvas localCanvas = new Canvas(canvasBitmap);// 以canvasBitmap生成画布
		localCanvas.drawBitmap(drawBitmap, left, top, null);// 在画布上移left和top左标开始绘制drawBitmap
		localCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		localCanvas.restore();
		drawBitmap.recycle();// 释放掉drawBitmap，防止内存泄漏
	}

	/**
	 * 把Drawable生成对应的Bitmap
	 * 
	 * @param paramRect
	 *            生成的Bitmap大小等一些参数
	 * @param drawable
	 *            要绘制的drawable
	 * @param canvasBitmap
	 *            将drawable绘制到canvasBitmap中
	 */
	private void getBitMap(Rect paramRect, Drawable drawable,
			Bitmap canvasBitmap) {
		// 中这个方法顾名思义，就是设置边界，
		// 用到的是.9图，所以拉伸图片不会失真,把drawable设置一个left、top点开始拉一个paramRect.right宽
		// paramRect.bottom高的矩形区域，按我的理解就是弄了这个区域，就是把图片按.9图的设置拉倒相应的
		// 大小填充到矩形区域里去，不明白看http://dyh7077063.iteye.com/blog/970672的博客
		drawable.setBounds(0, 0, paramRect.right, paramRect.bottom);
		// 用canvasBitmap生成一个画布
		Canvas localCanvas = new Canvas(canvasBitmap);
		drawable.draw(localCanvas);// 使用drawable的draw方法画到画布上
		localCanvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		localCanvas.restore();
	}

	private void createDrawable(Context context) {
		// 定义3个图片的大小等一些参数
		Rect[] arrayOfRect = new Rect[3];
		arrayOfRect[0] = new Rect();
		arrayOfRect[0].top = 0;
		arrayOfRect[0].left = 0;
		arrayOfRect[0].right = this.parentLeft + this.parentWidth / 4;
		arrayOfRect[0].bottom = this.popupWindowHeight;

		arrayOfRect[1] = new Rect();
		arrayOfRect[1].top = 0;
		arrayOfRect[1].left = arrayOfRect[0].right;
		arrayOfRect[1].right = this.parentWidth / 2;
		arrayOfRect[1].bottom = this.popupWindowHeight;

		arrayOfRect[2] = new Rect();
		arrayOfRect[2].top = 0;
		arrayOfRect[2].left = this.parentLeft + this.parentWidth * 3 / 4;
		arrayOfRect[2].right = screenwidth - arrayOfRect[2].left;
		arrayOfRect[2].bottom = this.popupWindowHeight;

		Drawable[] arrayOfDrawable = new Drawable[3];
		arrayOfDrawable[0] = dleft;
		arrayOfDrawable[1] = dmid;
		arrayOfDrawable[2] = dright;
		// 生成背景
		bg = getDrawable(context, arrayOfRect, arrayOfDrawable);
	}

	/**
	 * 生成背景图
	 * 
	 * @param context
	 *            上下文，为生成BitmapDrawable所需的
	 * @param ArrayOfRect
	 * @param ArrayOfDrawable
	 * @return
	 */
	private Drawable getDrawable(Context context, Rect[] ArrayOfRect,
			Drawable[] ArrayOfDrawable) {
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		// 先更具popupWindow的大小生成一个Bitmap
		Bitmap paramBitmap = Bitmap.createBitmap(screenwidth,
				popupWindowHeight, localConfig);

		// 这里循环把3个图片绘制到paramBitmap中
		for (int i = 0; i < ArrayOfDrawable.length; i++) {
			Rect localRect = ArrayOfRect[i];
			Bitmap localBitmap = Bitmap.createBitmap(localRect.right,
					localRect.bottom, localConfig);
			Drawable localDrawable = ArrayOfDrawable[i];
			getBitMap(localRect, localDrawable, localBitmap);// 得到相应的drawable的BitMap
			drawbitMap(paramBitmap, localBitmap, localRect.top, localRect.left);// 在paramBitmap中绘制localBitmap
			localBitmap.recycle();// 释放掉，要不多次运行有可能会内存泄漏
		}
		return new BitmapDrawable(context.getResources(), paramBitmap);
	}

	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition
				+ listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	static class ViewHolder {
		TextView tvClassName;
		ImageView ivCheck;
	}


	private BaseAdapter adapterOrderby = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				convertView = inflater.inflate(R.layout.orderby_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvClassName = (TextView) convertView
						.findViewById(R.id.tv_class);
				viewHolder.ivCheck = (ImageView) convertView
						.findViewById(R.id.iv_check);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (curIndex != position) {
				viewHolder.tvClassName.setTextColor(context.getResources()
						.getColor(R.color.item_text_normal));
				viewHolder.ivCheck.setVisibility(View.GONE);
			} else {
				viewHolder.tvClassName.setTextColor(context.getResources()
						.getColor(R.color.item_text_press));
				viewHolder.ivCheck.setVisibility(View.VISIBLE);
			}
			List<Entry<Integer, String>> list = new ArrayList<Entry<Integer, String>>(
					orderbys.entrySet());
			String name = list.get(position).getValue();
			viewHolder.tvClassName.setText(name);
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
			List<Entry<Integer, String>> list = new ArrayList<Entry<Integer, String>>(
					orderbys.entrySet());
			return list.get(position).getValue();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderbys.size();
		}
	};

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		if (popupMask != null) {
			popupMask.dismiss();
		}
		if(selectListener!=null){
			selectListener.onCancel();
		}
	}

}
