package com.suntown.suntownshop.widget;

import com.suntown.suntownshop.Constants;
import com.suntown.suntownshop.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 推荐商品容器类，2行3列
 * @author Administrator
 *
 */
public class GoodsViewGroup extends ViewGroup {
	private Context context;

	public GoodsViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++) {
			int row = i / 3;
			int col = i % 3;
			View view = getChildAt(i);
			view.setVisibility(View.VISIBLE);
			int width = Constants.displayWidth/3;
			view.layout(col * width, row * width, col * width + width, row * width + width);
		}
	}

}
