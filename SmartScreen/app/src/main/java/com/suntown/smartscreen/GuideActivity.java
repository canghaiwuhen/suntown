package com.suntown.smartscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.login.LoginActivity;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GuideActivity extends Activity {

    @BindView(R.id.guide_vp)
    ViewPager guideVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        guideVp.setAdapter(pagerAdapter);
    }
    int[] picRes = new int[]{R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3};
    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return picRes == null ? 0 : picRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setImageResource(picRes[position]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            if(position==picRes.length-1){
                iv.setOnClickListener(v -> {
                    SPUtils.put(GuideActivity.this, Constant.GUIDED,true);
                    startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                    finish();
                });
            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

}
