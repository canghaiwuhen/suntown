package com.suntown.smartscreen.shopCenter.detial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.DispMShopBean;
import com.suntown.smartscreen.data.ResultBean;
import com.suntown.smartscreen.data.ShopBoardBean;
import com.suntown.smartscreen.maintain.detial.MainTainDetialActivity;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class BoardDetialActivity extends BaseActivity {


    private static final String TAG = "BoardDetialActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    ExpandableListView llTitle;
    HashMap<String,List<ShopBoardBean.RECORDBean>> listHashMap = new HashMap<>();
    List<String> list = new ArrayList<>();
    private ExpandableItemAdapter adapter;
    private String modUrl;

    @Override
    protected int getContentView() {
        return R.layout.activity_board_detial;
    }

    @Override
    protected void initView() {
        String sid = getIntent().getStringExtra(Constant.USER_ID);
        List<ShopBoardBean.RECORDBean> recordBeen = getIntent().getParcelableArrayListExtra(Constant.RECORD);
//        Log.i(TAG, "recordBeen:" + recordBeen.toString());
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        tvTitle.setText(recordBeen.get(0).SPECNAME);
//        llTitle.setLayoutManager(new LinearLayoutManager(this));
        generateData(recordBeen);
        adapter = new ExpandableItemAdapter(this,list,listHashMap);
        llTitle.setAdapter(adapter);
        adapter.setOnAdapterCallBack(new ExpandableItemAdapter.OnAdapterCallBack() {
            @Override
            public void onSettingClick(int parentPosition, int childPosition) {
                //TODO 停用 ActiveGDM arg0=sid arg1 = dmcode arg2 = 1 启用  0停用
                List<ShopBoardBean.RECORDBean> recordBeanList= listHashMap.get(list.get(parentPosition));
                ShopBoardBean.RECORDBean recordBean = recordBeanList.get(childPosition);
                String activ = "0";
                if ("0".equals(recordBean.ISACTIVE)){
                    //启用
                    activ="1";
                }else if ("1".equals(recordBean.ISACTIVE)){
                    //停用
                    activ="0";
                }
                if (!Utils.isFastClick()) {
                    submitServer(sid,recordBean.DMCODE,activ,parentPosition,childPosition);
                }
            }

            @Override
            public void onItemClick(int parentPosition, int childPosition) {
                List<ShopBoardBean.RECORDBean> recordBeanList= listHashMap.get(list.get(parentPosition));
                ShopBoardBean.RECORDBean recordBean = recordBeanList.get(childPosition);
                Intent intent = new Intent(BoardDetialActivity.this, MainTainDetialActivity.class);
                intent.putExtra(Constant.DMCODE,recordBean.DMCODE);
                intent.putExtra(Constant.SPECNAME,recordBeen.get(0).SPECNAME);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置数据
     * @param recordBeen
     * @return
     */
    private void generateData(List<ShopBoardBean.RECORDBean> recordBeen) {
        for (ShopBoardBean.RECORDBean recordBean : recordBeen) {
            String typename = recordBean.TYPENAME;
            if (!listHashMap.containsKey(typename)) {
                ArrayList<ShopBoardBean.RECORDBean> arrayList = new ArrayList<>();
                arrayList.add(recordBean);
                listHashMap.put(typename,arrayList);
                list.add(typename);
            }else{
                List<ShopBoardBean.RECORDBean> been = listHashMap.get(typename);
                been.add(recordBean);
            }
        }

        }
        /**
         * 停用 启用模板
         * @param sid
         * @param dmCode
         * @param activ
         */
    private void submitServer(String sid, String dmCode, String activ,int parentPosition,int childPosition) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().
                //0不加载图片 1加载图片
                        create(ApiService.class).setActiveGDM(sid,dmCode, activ)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:" + s);
                String json = s.replace("<ns:ActiveGDMResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:ActiveGDMResponse>", "");
                Log.i(TAG, "json:" + json);
                ResultBean resultBean = new Gson().fromJson(json, ResultBean.class);
                if ("1".equals(resultBean.RESULT)) {
                    List<ShopBoardBean.RECORDBean> record = listHashMap.get(list.get(parentPosition));
                    if ("1".equals(activ)) {
                        record.get(childPosition).ISACTIVE="1";
                        Utils.showToast(BoardDetialActivity.this,"已启用");
                    }else if ("0".equals(activ)){
                        record.get(childPosition).ISACTIVE="0";
                        Utils.showToast(BoardDetialActivity.this,"已停用");
                    }
                    Log.i(TAG,"record:"+record.toString());
                    adapter.notifyDataSetChanged();
                }else{
                    Utils.showToast(BoardDetialActivity.this, "设置失败，请重试");
                }
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(BoardDetialActivity.this, "网络异常,请重试");
        });
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
