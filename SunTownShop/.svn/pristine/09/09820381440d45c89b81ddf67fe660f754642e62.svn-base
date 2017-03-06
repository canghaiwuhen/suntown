package com.suntown.suntownshop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 不滚动的GridView
 *
 * @author 钱凯
 * @version 2015年7月17日 上午9:14:45
 *
 */
public class UnScrollGridView extends GridView {

	public UnScrollGridView(Context context) {
		super(context);
	}

	public UnScrollGridView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public UnScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// 设置不滚动
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
