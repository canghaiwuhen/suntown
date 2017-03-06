package com.suntown.suntownshop.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.suntown.suntownshop.GoodsDetailActivity;
import com.suntown.suntownshop.R;
import com.suntown.suntownshop.db.ShopCartDb;
import com.suntown.suntownshop.model.Goods;
import com.suntown.suntownshop.model.OrderGoods;
import com.suntown.suntownshop.utils.FormatValidation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Order Evaluate Goods List Adapter
 *
 * @author 钱凯
 * @version 2015年4月17日 上午9:07:04
 *
 */
public class GoodsEvaluateAdapter extends BaseAdapter {
	public ArrayList<OrderGoods> goodsList;
	private Context context;
	private boolean isEditable;
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

	static class GoodsViewHolder {
		ImageView ivMain;
		TextView tvName;
		TextView tvPrice;
		RatingBar rbEvaRate;
		EditText etEvaText;
		TextView tvEvaText;
		TextView tvEvaHint;
		TextView tvEvaDate;
	}

	public GoodsEvaluateAdapter(Context context, ArrayList<OrderGoods> goodsList,boolean isEditable) {
		this.context = context;
		this.goodsList = goodsList;
		this.isEditable = isEditable;
		initOptions();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GoodsViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.evaluate_item, null);
			holder = new GoodsViewHolder();

			holder.ivMain = (ImageView) convertView.findViewById(R.id.iv_main);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			holder.rbEvaRate = (RatingBar) convertView
					.findViewById(R.id.rb_evarate);
			holder.etEvaText = (EditText) convertView
					.findViewById(R.id.et_evatext);
			holder.tvEvaText = (TextView)convertView.findViewById(R.id.tv_evatext);
			holder.tvEvaHint = (TextView)convertView.findViewById(R.id.tv_evahint);
			holder.tvEvaDate = (TextView)convertView.findViewById(R.id.tv_evadate);
			convertView.setTag(holder);
		} else {
			holder = (GoodsViewHolder) convertView.getTag();
		}
		

		OnItemClick onItemClick = new OnItemClick(position);
		convertView.setOnClickListener(onItemClick);
		OnRateChangeClick onRateChangeClick = new OnRateChangeClick(position);
		holder.rbEvaRate.setOnRatingBarChangeListener(onRateChangeClick);
		EvaTextWatcher evaTextWatcher = new EvaTextWatcher(position, holder.etEvaText);
		holder.etEvaText.addTextChangedListener(evaTextWatcher);
		OrderGoods goods = goodsList.get(position);
		holder.tvName.setText(goods.getName());

		holder.tvPrice.setText(context.getString(R.string.currency_symbol_text)
				+ String.format("%.2f", goods.getPrice()));

		String imgPath = goods.getImagePath();
		if (imgPath != null && imgPath.length() > 0) {
			imageLoader.displayImage("http://" + imgPath, holder.ivMain,
					options);
		} else {
			holder.ivMain.setImageResource(R.drawable.picture_noimg_200x200);
		}
		holder.rbEvaRate.setRating(goods.getEvaRate());
		if(isEditable){
			holder.etEvaText.setText(goods.getEvaText());
			holder.etEvaText.setVisibility(View.VISIBLE);
			holder.tvEvaText.setVisibility(View.GONE);
			holder.tvEvaHint.setVisibility(View.VISIBLE);
			holder.tvEvaDate.setVisibility(View.GONE);
		}else{
			holder.tvEvaText.setVisibility(View.VISIBLE);
			holder.tvEvaText.setText(goods.getEvaText());
			holder.etEvaText.setVisibility(View.GONE);
			holder.tvEvaHint.setVisibility(View.GONE);
			holder.tvEvaDate.setVisibility(View.VISIBLE);
			if(goods.getEvaDate()!=null&&!"".equals(goods.getEvaDate())){
				holder.tvEvaDate.setText(goods.getEvaDate());
			}
		}
		holder.rbEvaRate.setIsIndicator(!isEditable);
		
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
		return goodsList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goodsList.size();
	}

	private class OnItemClick implements OnClickListener {
		private int index;

		public OnItemClick(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String barCode = goodsList.get(index).getBarCode();
			showGoodsDetail(barCode);
		}

	}

	private class OnRateChangeClick implements OnRatingBarChangeListener {

		private int index;

		public OnRateChangeClick(int index) {
			this.index = index;
		}

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (fromUser) {
				if(rating<1){
					ratingBar.setRating(1);
					rating = 1;
				}
				OrderGoods goods = goodsList.get(index);
				goods.setEvaRate(rating);
			}
		}
	}

	private class EvaTextWatcher implements TextWatcher {
		private int index;
		private EditText editText;

		public EvaTextWatcher(int index, EditText editText) {
			this.index = index;
			this.editText = editText;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			OrderGoods goods = goodsList.get(index);
			if (FormatValidation.getWordCount(s.toString()) > 500) {
				editText.setText(goods.getEvaText());
				Toast.makeText(context, "评语最多输入500个中文字符!", Toast.LENGTH_SHORT)
						.show();
			} else {
				goods.setEvaText(s.toString());
			}
		}
	}

	/**
	 * 根据条形码跳转到商品详情页
	 * 
	 * @param barCode
	 */
	private void showGoodsDetail(String barCode) {
		Intent intent = new Intent(context, GoodsDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("barCode", barCode);
		intent.putExtras(b);
		context.startActivity(intent);
	}

}
