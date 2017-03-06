package com.suntown.suntownshop.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.suntown.suntownshop.R;
import com.suntown.suntownshop.model.Coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 优惠券列表适配器
 *
 * @author Ken
 * @version 2015年3月10日 下午1:21:07
 *
 */
public class CouponUseAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Coupon> list;
	private LinkedHashMap<String, Coupon> useCoupons;
	private CouponChangeListener listener;

	public CouponUseAdapter(Context context, ArrayList<Coupon> list,
			LinkedHashMap<String, Coupon> useCoupons,
			CouponChangeListener listener) {
		this.mContext = context;
		this.list = list;
		this.useCoupons = useCoupons;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.coupon_item_use, null);
			viewHolder.tvAmount = (TextView) convertView
					.findViewById(R.id.tv_amount);
			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tv_date);
			viewHolder.tvExtent = (TextView)convertView.findViewById(R.id.tv_extent);
			viewHolder.cbUse = (CheckBox) convertView.findViewById(R.id.cb_use);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Coupon coupon = list.get(position);
		double denomination = coupon.getDenomination();
		String type = "";
		if (coupon.getType() == Coupon.TYPE_FROM_REGISTER) {
			type = "全场满5.01元";
		}
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");
		
		viewHolder.tvExtent.setText(type);
		viewHolder.tvAmount.setText(String.format("%.0f", coupon.getDenomination()));
		try {
			viewHolder.tvDate.setText(formatDate.format(formatDate.parse(coupon.getUseDate().replace("-", "."))) + "-"
					+ formatDate.format(formatDate.parse(coupon.getEndDate().replace("-", "."))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (useCoupons.containsKey(coupon.getId())) {
			viewHolder.cbUse.setChecked(true);
		} else {
			viewHolder.cbUse.setChecked(false);
		}
		viewHolder.cbUse
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							useCoupons.put(coupon.getId(), coupon);
						} else {
							useCoupons.remove(coupon.getId());
						}
						listener.onChange();
					}
				});
		return convertView;
	}

	static class ViewHolder {
		TextView tvAmount;
		TextView tvExtent;
		TextView tvDate;
		CheckBox cbUse;
	}

	public interface CouponChangeListener {
		public void onChange();
	}
}
