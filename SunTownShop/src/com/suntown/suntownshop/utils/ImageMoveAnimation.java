package com.suntown.suntownshop.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suntown.suntownshop.SpecialGoodsListActivity;
import com.suntown.suntownshop.db.RouteGoodsDb;
import com.suntown.suntownshop.listener.OnImageMoveListener;
/**
 * ͼƬ�ƶ���������Ҫ���ڼ��빺�ﳵЧ��
 *
 * @author Ǯ��
 * @version 2015��7��21�� ����10:04:22
 *
 */
public class ImageMoveAnimation {
	
	private Context context;
	private ViewGroup anim_mask_layout;
	/**
	 * ��������ʱ��
	 */
	private int AnimationDuration = 500;
	
	public ImageMoveAnimation(Context context){
		this.context = context;
		anim_mask_layout = createAnimLayout();
	}
	
	/**
	 * @Description: ����������
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) ((Activity)context).getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		animLayout.setLayoutParams(lp);
		// animLayout.setId(R.id.age);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private ImageView animIv;

	/**
	 * @Description: ������ͼ��������
	 * @param @param vg
	 * @param @param view
	 * @param @param location
	 * @param @return
	 * @return View
	 * @throws
	 */
	private View addViewToAnimLayout(final ViewGroup vg, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		if (animIv == null) {
			animIv = new ImageView(context);
			vg.addView(animIv);
		}
		animIv.setVisibility(View.VISIBLE);
		animIv.setImageDrawable(((ImageView) view).getDrawable());

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		animIv.setLayoutParams(lp);
		return animIv;
	}

	public void setAnim(View source,View dest,final OnImageMoveListener listener) {
		Animation mScaleAnimation = new ScaleAnimation(1.5f, 0.1f, 1.5f, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
				0.1f);
		mScaleAnimation.setDuration(AnimationDuration);
		mScaleAnimation.setFillAfter(true);

		int[] start_location = new int[2];
		source.getLocationInWindow(start_location);
		// ViewGroup vg = (ViewGroup) v.getParent();
		// vg.removeView(v);
		// ��������ӵ����ǵĶ�������
		View view = addViewToAnimLayout(anim_mask_layout, source, start_location);
		int[] end_location = new int[2];
		dest.getLocationInWindow(end_location);
		// ����λ��
		int endX = end_location[0] - start_location[0];
		int endY = end_location[1] - start_location[1];

		Animation mTranslateAnimation = new TranslateAnimation(0, endX, 0, endY);// �ƶ�

		mTranslateAnimation.setDuration(AnimationDuration);

		AnimationSet mAnimationSet = new AnimationSet(false);
		// ���Ҫע�⣬������Ϊfalse,��Ȼ������������󣬲����λ��
		mAnimationSet.setFillAfter(false);
		mAnimationSet.addAnimation(mScaleAnimation);
		mAnimationSet.addAnimation(mTranslateAnimation);
		view.startAnimation(mAnimationSet);

		mTranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// tvNumber.setText(goodsNumber+"");
				if (animIv != null) {
					animIv.setVisibility(View.GONE);
				}
				listener.onMoveEnd();
			}
		});
	}
	
}