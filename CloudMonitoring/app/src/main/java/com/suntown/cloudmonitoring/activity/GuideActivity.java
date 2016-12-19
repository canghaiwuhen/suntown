package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.LoginBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

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
                    initNet();
                });
            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    private void initNet() {
        {
            HashMap<String, String> params = new HashMap<>();
            params.put(Constant.USER_NAME, SPUtils.getString(this, Constant.USER_NAME));
            params.put(Constant.PASS_WORD, SPUtils.getString(this, Constant.PASS_WORD));
            ApiClient.getInstance().mApiService.login(params).compose(RxSchedulers.io_main()).subscribe(loginBean -> {
                int resultCode = loginBean.getResultCode();
                LoginBean.UserBean user = loginBean.getUser();
                Log.i("WelcomeActivity","resultCode:"+resultCode+",user:"+user);
                if (1==resultCode) {
                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(this,LoginActivity.class));
                    finish();
                }
//            Utils.showToast(this,user.toString());
            }, throwable -> {
                Log.i("WelcomeActivity","throwable:"+throwable.toString());
//                Utils.showToast(this,throwable.toString());
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            });
        }
    }
}
