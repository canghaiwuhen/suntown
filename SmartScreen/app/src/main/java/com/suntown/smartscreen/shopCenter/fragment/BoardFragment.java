package com.suntown.smartscreen.shopCenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.data.ShopBoardBean;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.shopCenter.ShopCenterActivity;
import com.suntown.smartscreen.shopCenter.detial.BoardDetialActivity;
import com.suntown.smartscreen.shopCenter.detial.ShopDetialActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/2/15.
 */

public class BoardFragment extends Fragment{
    private static final String TAG = "BoardFragment";
    private View inflate;
    private ListView rlMain;
    private List<ShopBoardBean.RECORDBean> record = new ArrayList<>();
    private BoardListAdapter boardListAdapter;
    private LinearLayout llNormal;
    HashMap<String,List<ShopBoardBean.RECORDBean>> hashMap = new HashMap<>();
    List<String> name = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_board, container, false);
        rlMain = (ListView) inflate.findViewById(R.id.rl_main);
        llNormal = (LinearLayout) inflate.findViewById(R.id.ll_normal);
        init();
        initData();
        return inflate;
    }

    private void init() {
        //设置adapter
//        rlMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        boardListAdapter = new BoardListAdapter(getActivity(),hashMap,name);
        rlMain.setAdapter(boardListAdapter);
        rlMain.setOnItemClickListener((adapterView, view, i, l) -> {
            ArrayList<ShopBoardBean.RECORDBean> recordBeen = (ArrayList<ShopBoardBean.RECORDBean>) hashMap.get(name.get(i));
            Intent intent = new Intent(getActivity(), BoardDetialActivity.class);
//            Log.i(TAG,"recordBeen:"+recordBeen.toString());
            intent.putParcelableArrayListExtra(Constant.RECORD,recordBeen);
            intent.putExtra(Constant.USER_ID, ((ShopDetialActivity) getActivity()).sid);
            startActivity(intent);
        });
    }

    private void initData() {
        //获取门店分配的模板  public String getGoodsDispMs(String arg0,String arg1) sid userid
        ShopDetialActivity activity = (ShopDetialActivity) getActivity();
        String modUrl = activity.modUrl;
        String sid = activity.sid;
        String userId = activity.userId;
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                //0不加载图片 1加载图片
                        create(ApiService.class).getGoodsDispMs(sid,userId)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:getGoodsDispMsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getGoodsDispMsResponse>", "");
                Log.i(TAG, "json:" + json);
                ShopBoardBean shopBoardBean = new Gson().fromJson(json, ShopBoardBean.class);
                if (0<shopBoardBean.ROWS) {
                    record = shopBoardBean.RECORD;
                    initList(record);
                    boardListAdapter.notifyDataSetChanged();
                }else{
                    llNormal.setVisibility(View.VISIBLE);
                }
            }
        }, throwable -> {
            llNormal.setVisibility(View.VISIBLE);
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(getActivity(), "网络异常,请重试");
        });
    }

    /**
     * 初始化集合
     * @param record
     */
    private void initList(List<ShopBoardBean.RECORDBean> record) {
        for (ShopBoardBean.RECORDBean recordBean : record) {
            String specname = recordBean.SPECNAME;
            if (!hashMap.containsKey(specname)) {
                ArrayList<ShopBoardBean.RECORDBean> recordBeen = new ArrayList<>();
                recordBeen.add(recordBean);
                hashMap.put(specname,recordBeen);
            }else{
                List<ShopBoardBean.RECORDBean> recordBeen = hashMap.get(specname);
                recordBeen.add(recordBean);
            }

        }
        Log.i(TAG,hashMap.toString());
        Set<String> keySet = hashMap.keySet();
        for (String s : keySet) {
            name.add(s);
        }
    }
}
