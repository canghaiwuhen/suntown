package com.suntown.suntownshop.widget;

import java.util.ArrayList;

import com.suntown.suntownshop.R;
import com.suntown.suntownshop.model.Path;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
/**
 * 物流路径容器，未使用
 *
 * @author 钱凯
 * @version 2015年9月21日 上午10:31:28
 *
 */
public class PathContainer extends LinearLayout {
	private ArrayList<Path> list;
	private LayoutInflater inflater;
	private LinearLayout view;
	public PathContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//view = (LinearLayout)inflater.inflate(R.layout.path_container, this);
	}
	
	public void setData(ArrayList<Path> list){
		this.list = list;
		refresh();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void refresh(){
		removeAllViews();
		int len = list.size();
		int green = getResources().getColor(R.color.green);
		for(int i =0;i<len;i++){
			Path p = list.get(i);
			View child = inflater.inflate(R.layout.path_item, null);
			TextView tvMsg = (TextView)child.findViewById(R.id.tv_msg);
			TextView tvTime = (TextView)child.findViewById(R.id.tv_time);
			View viewLT = child.findViewById(R.id.ll_lt);
			ImageView ivP = (ImageView)child.findViewById(R.id.iv_c);
			tvMsg.setText(p.getMsg());
			tvTime.setText(p.getTime());
			if(i==(len-1)){
				tvMsg.setTextColor(green);
				tvTime.setTextColor(green);
				ivP.setImageResource(R.drawable.circle_green);
				viewLT.setVisibility(View.GONE);
			}
			addView(child,0);
		}
		invalidate();
	}
}
