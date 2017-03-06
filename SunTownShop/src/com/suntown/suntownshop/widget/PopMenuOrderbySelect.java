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
 * ����ͷ�ĵ����˵�-��Ŀ ����
 *
 * @author Ǯ��
 * @version 2015��7��14�� ����5:33:53
 *
 */
public class PopMenuOrderbySelect implements OnDismissListener {

	// �洢ÿ����ť������Ӧ���ɵ�drawable
	private HashMap<View, Drawable> mapDrawable = new HashMap<View, Drawable>();

	private Context context;
	private PopupWindow popupWindow;
	private PopupWindow popupMask;

	private int screenwidth;// ��Ļ�Ŀ��

	private int popupWindowHeight = 0;// popupWindow�ĸ߶�
	private int parentLeft = 0;// ���ؼ�����߾�
	private int parentWidth = 0;// ���ؼ��Ŀ��
	private Drawable dleft; // ����ͼƬ
	private Drawable dmid;// ����ͼƬ
	private Drawable dright;// �ұߵ�ͼƬ

	private View viewMask;
	private Drawable bg;// ���ɵı���ͼƬ

	private OnOrderbySelectListener selectListener;
	private LinkedHashMap<Integer, String> orderbys; // ��������ʽ
	
	private int curIndex = -1;


	/**
	 * 
	 * @param context
	 *            ������
	 * @param leftDrawableID
	 *            ����ͼƬ����ԴID
	 * @param midDrawableID
	 *            ����ͼƬ����ԴID
	 * @param righrDrawableID
	 *            �Ұ��ͼƬ����ԴID
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

		// ����PopupWindow����
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
		
		// ��ָ��λ�õ�������
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});

		// ����drawable
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

		bg = mapDrawable.get(parent);// Ϊ��ʡ��Դ��map�лᱣ����ǰ���ɵı��������ݸ��ؼ������
		popupWindowHeight = popupWindow.getHeight();// �õ�popupWindow�ĸ߶ȣ���popupWindow���������ܻ�ȡ
		this.parentLeft = parent.getLeft();// ���ؼ�����߾�
		this.parentWidth = parent.getWidth();// ���ؼ��Ŀ��
		if (bg == null)// ����Ϊ��
		{
			createDrawable(context);// �������ɱ���ͼ
			mapDrawable.put(parent, bg);// ���浽map��
		}
		popupWindow.setBackgroundDrawable(bg);// ��popupWindow���ñ���
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
	 * ��һ��BitmapΪ����������һ��Bitmap
	 * 
	 * @param canvasBitmap
	 *            ��Ϊ������Bitmap
	 * @param drawBitmap
	 *            Ҫ�����Ƶ�Bitmap
	 * @param top
	 *            �ӻ����ľ��붥����topλ�ÿ�ʼ
	 * @param left
	 *            �ӻ����ľ�����ߵ�leftλ�ÿ�ʼ
	 */
	private void drawbitMap(Bitmap canvasBitmap, Bitmap drawBitmap, int top,
			int left) {
		Canvas localCanvas = new Canvas(canvasBitmap);// ��canvasBitmap���ɻ���
		localCanvas.drawBitmap(drawBitmap, left, top, null);// �ڻ�������left��top��꿪ʼ����drawBitmap
		localCanvas.save(Canvas.ALL_SAVE_FLAG);// ����
		localCanvas.restore();
		drawBitmap.recycle();// �ͷŵ�drawBitmap����ֹ�ڴ�й©
	}

	/**
	 * ��Drawable���ɶ�Ӧ��Bitmap
	 * 
	 * @param paramRect
	 *            ���ɵ�Bitmap��С��һЩ����
	 * @param drawable
	 *            Ҫ���Ƶ�drawable
	 * @param canvasBitmap
	 *            ��drawable���Ƶ�canvasBitmap��
	 */
	private void getBitMap(Rect paramRect, Drawable drawable,
			Bitmap canvasBitmap) {
		// �������������˼�壬�������ñ߽磬
		// �õ�����.9ͼ����������ͼƬ����ʧ��,��drawable����һ��left��top�㿪ʼ��һ��paramRect.right��
		// paramRect.bottom�ߵľ������򣬰��ҵ�������Ū��������򣬾��ǰ�ͼƬ��.9ͼ������������Ӧ��
		// ��С��䵽����������ȥ�������׿�http://dyh7077063.iteye.com/blog/970672�Ĳ���
		drawable.setBounds(0, 0, paramRect.right, paramRect.bottom);
		// ��canvasBitmap����һ������
		Canvas localCanvas = new Canvas(canvasBitmap);
		drawable.draw(localCanvas);// ʹ��drawable��draw��������������
		localCanvas.save(Canvas.ALL_SAVE_FLAG);// ����
		localCanvas.restore();
	}

	private void createDrawable(Context context) {
		// ����3��ͼƬ�Ĵ�С��һЩ����
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
		// ���ɱ���
		bg = getDrawable(context, arrayOfRect, arrayOfDrawable);
	}

	/**
	 * ���ɱ���ͼ
	 * 
	 * @param context
	 *            �����ģ�Ϊ����BitmapDrawable�����
	 * @param ArrayOfRect
	 * @param ArrayOfDrawable
	 * @return
	 */
	private Drawable getDrawable(Context context, Rect[] ArrayOfRect,
			Drawable[] ArrayOfDrawable) {
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		// �ȸ���popupWindow�Ĵ�С����һ��Bitmap
		Bitmap paramBitmap = Bitmap.createBitmap(screenwidth,
				popupWindowHeight, localConfig);

		// ����ѭ����3��ͼƬ���Ƶ�paramBitmap��
		for (int i = 0; i < ArrayOfDrawable.length; i++) {
			Rect localRect = ArrayOfRect[i];
			Bitmap localBitmap = Bitmap.createBitmap(localRect.right,
					localRect.bottom, localConfig);
			Drawable localDrawable = ArrayOfDrawable[i];
			getBitMap(localRect, localDrawable, localBitmap);// �õ���Ӧ��drawable��BitMap
			drawbitMap(paramBitmap, localBitmap, localRect.top, localRect.left);// ��paramBitmap�л���localBitmap
			localBitmap.recycle();// �ͷŵ���Ҫ����������п��ܻ��ڴ�й©
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
