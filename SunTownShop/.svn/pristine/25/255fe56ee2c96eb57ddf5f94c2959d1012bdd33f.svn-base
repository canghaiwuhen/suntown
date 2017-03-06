package com.suntown.suntownshop.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.R;
import com.suntown.suntownshop.model.CartGoods;
import com.suntown.suntownshop.model.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 购物车适配器 
 *
 * @author 钱凯
 * @version 2015年2月21日 上午9:56:35
 *
 */
public class ShopCartAdapter extends BaseAdapter {
	/**
	 * 上下文对象
	 */
	private Context mContext = null;
	private LinkedHashMap<String,CartGoods> data;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;

	private int mRightWidth = 0;

	/**
	 * @param mainActivity
	 */
	public ShopCartAdapter(Context ctx, LinkedHashMap<String,CartGoods> data, int rightWidth,
			ImageLoader imageLoader, DisplayImageOptions options) {
		mContext = ctx;
		this.data = data;
		mRightWidth = rightWidth;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		List<Entry<String,CartGoods>> list = new ArrayList<Entry<String,CartGoods>>(
				data.entrySet());
		return list.get(position).getValue();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.shopcart_item, parent, false);
			holder = new ViewHolder();
			holder.item_left = (RelativeLayout) convertView
					.findViewById(R.id.item_left);
			holder.item_right = (RelativeLayout) convertView
					.findViewById(R.id.item_right);

			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name_shopcart_item);
			holder.tv_spec = (TextView) convertView
					.findViewById(R.id.tv_spec_shopcart_item);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price_shopcart_item);
			holder.tv_quantity = (TextView) convertView
					.findViewById(R.id.tv_quantity_shopcart_item);

			holder.imgView = (ImageView) convertView
					.findViewById(R.id.iv_shopcart_item);
			holder.cb = (CheckBox) convertView
					.findViewById(R.id.cb_chopcart_item);
			holder.editForm = (RelativeLayout) convertView
					.findViewById(R.id.shopcart_item_editform);
			holder.showForm = (RelativeLayout) convertView
					.findViewById(R.id.shopcart_item_quantityform);

			holder.etQuantity = (EditText) convertView
					.findViewById(R.id.et_quantity_chopcart_item);

			holder.btnAdd = (Button) convertView
					.findViewById(R.id.btn_add_shopcart_item);
			holder.btnSub = (Button) convertView
					.findViewById(R.id.btn_sub_shopcart_item);
			holder.btnDel = (Button) convertView
					.findViewById(R.id.btn_del_shopcart_item);

			holder.item_right_txt = (TextView) convertView
					.findViewById(R.id.item_right_txt);
			convertView.setTag(holder);
		} else {// 有直接获得ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}

		LinearLayout.LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.item_left.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		holder.item_right.setLayoutParams(lp2);
		List<Entry<String,CartGoods>> list = new ArrayList<Entry<String,CartGoods>>(
				data.entrySet());

		CartGoods goods = list.get(position).getValue();

		String imgPath = goods.getImagePath();
		if (imgPath != null && imgPath.length() > 0) {
			imageLoader.displayImage("http://" + imgPath, holder.imgView,
					options);
		} else {
			holder.imgView.setImageResource(R.drawable.picture_noimg_200x200);
		}
		
		holder.tv_name.setText(goods.getName());
		holder.tv_spec.setText(mContext.getString(R.string.goods_spec_text)
				+ goods.getSpec());
		holder.tv_price.setText("￥" + goods.getPrice());

		holder.tv_quantity.setText("x" + goods.getQuantity());
		holder.etQuantity.setText(goods.getQuantity() + "");
		
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(mCbListener!=null){
					mCbListener.onCheckedChanged(buttonView, isChecked, position);
				}
			}
		});
		
		holder.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		RelativeLayout item_left;
		RelativeLayout item_right;
		RelativeLayout editForm;
		RelativeLayout showForm;
		EditText etQuantity;

		Button btnAdd;
		Button btnSub;
		Button btnDel;

		TextView tv_name;
		TextView tv_spec;
		TextView tv_price;
		TextView tv_quantity;
		ImageView imgView;

		CheckBox cb;

		TextView item_right_txt;
	}

	/**
	 * 单击事件监听器
	 */
	private onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}
	
	/**
	 * CheckBox 状态改变监听器
	 */
	private onCheckBoxCheckedChangeListener mCbListener = null;
	public void setOnCheckBoxCheckedChangeListener(onCheckBoxCheckedChangeListener listener){
		mCbListener = listener;
	}
	public interface onCheckBoxCheckedChangeListener{
		void onCheckedChanged(CompoundButton buttonView, boolean isChecked,int position);
	}
	
}
