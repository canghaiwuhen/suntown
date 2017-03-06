package com.suntown.suntownshop.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.suntown.suntownshop.R;
import com.suntown.suntownshop.model.Coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 优惠券列表适配器
 *
 * @author Ken
 * @version 2015年3月10日 下午1:21:07
 *
 */
public class CouponAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Coupon> list;

	public CouponAdapter(Context context, ArrayList<Coupon> list) {
		this.mContext = context;
		this.list = list;
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
					R.layout.coupon_item, null);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.tvUseDate = (TextView) convertView.findViewById(R.id.tv_usedate);
			viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
			viewHolder.tvEndDate = (TextView) convertView.findViewById(R.id.tv_enddate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Coupon coupon = list.get(position);
		double denomination = coupon.getDenomination();
		if(coupon.getType()==Coupon.TYPE_FROM_REGISTER){
			viewHolder.tvTitle.setText("用户注册发放");
		}else if(coupon.getCouponType()!=1&&!"".equals(coupon.getTypeName())){
			viewHolder.tvTitle.setText(coupon.getTypeName());
		}else{
			viewHolder.tvTitle.setText("未知");
		}
		viewHolder.tvAmount.setText("￥"+String.format("%.2f",coupon.getDenomination())+"元");
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			viewHolder.tvUseDate.setText("领取日期:"+format.format(format2.parse(coupon.getUseDate())));
			viewHolder.tvEndDate.setText("有效期至:"+format.format(format2.parse(coupon.getEndDate())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期解析错误:"+e.getMessage());
			e.printStackTrace();
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvUseDate;
		TextView tvAmount;
		TextView tvEndDate;
	}
}
