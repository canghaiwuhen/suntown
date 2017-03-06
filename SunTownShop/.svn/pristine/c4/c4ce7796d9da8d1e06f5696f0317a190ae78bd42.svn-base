package com.suntown.suntownshop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
/**
 * 自带滑动通知接口的HorizontalScrollView
 *
 * @author 钱凯
 * @version 2015年5月21日 上午10:33:20
 *
 */
public class SynHorizontalScrollView extends HorizontalScrollView {
	private OnScrollChangedListener listener;
	public SynHorizontalScrollView(Context context){
		super(context);
	}
	
	public SynHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if(listener!=null){
			listener.onScroll(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public interface OnScrollChangedListener{
		public void onScroll(int left,int top,int oldl,int oldt);
	}
}
