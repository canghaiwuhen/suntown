package com.suntown.smartscreen.shopCenter.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.data.ApListBean;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.shopCenter.detial.ApDetialActivity;
import com.suntown.smartscreen.shopCenter.detial.ShopDetialActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.Utils;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

/**
 *
 */
public class APFragment extends Fragment {
    private static final String TAG = "APFragment";
    private View inflate;
    private RecyclerView rlMain;
    private LinearLayout llNormal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_a, container, false);
        rlMain = (RecyclerView) inflate.findViewById(R.id.rl_main);
        llNormal = (LinearLayout) inflate.findViewById(R.id.normal);
        initData();
        return inflate;
    }

    private void initData() {
        // getAPList 获取ap列表 userid sid
        ShopDetialActivity activity = (ShopDetialActivity) getActivity();
        String modUrl = activity.modUrl;
        String sid = activity.sid;
        String userId = activity.userId;
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                        create(ApiService.class).getAPList(userId,sid)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:getAPListResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getAPListResponse>", "");
                Log.i(TAG, "json:" + json);
                ApListBean apListBean = new Gson().fromJson(json, ApListBean.class);
                if (0<apListBean.ROWS) {
                    //设置adapter
                    List<ApListBean.RECORDBean> record = apListBean.RECORD;
                    rlMain.setLayoutManager(new LinearLayoutManager(getActivity()));
                    ApListAdapter apListAdapter = new ApListAdapter(record);
                    apListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                    rlMain.setAdapter(apListAdapter);
                    rlMain.addOnItemTouchListener(new OnItemClickListener() {
                        @Override
                        public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                            Intent intent = new Intent(getActivity(), ApDetialActivity.class);
                            intent.putExtra(Constant.RECORD,record.get(i));
                            startActivity(intent);
                        }
                    });
                }else{
                    llNormal.setVisibility(View.VISIBLE);
//                    Utils.showToast(getActivity(),"暂无数据");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            llNormal.setVisibility(View.VISIBLE);
            Utils.showToast(getActivity(), "网络异常,请重试");
        });
    }
}
