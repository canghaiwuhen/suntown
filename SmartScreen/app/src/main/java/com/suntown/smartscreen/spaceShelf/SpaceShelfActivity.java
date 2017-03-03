package com.suntown.smartscreen.spaceShelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.data.ShelfListBean;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.UpdatePriceActivity;
import com.suntown.smartscreen.price.chooseShop.ChooseShopActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.weight.LoadingDialog;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SpaceShelfActivity extends BaseActivity {


    private static final int RESULT = 200;
    private static final String TAG = "SpaceShelfActivity";
    @BindView(R.id.iv_back)
    TextView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_serach)
    EditText etSerach;
    @BindView(R.id.elv_main)
    ExpandableListView elvList;
    private String shopId;
    private String shopName;
    private String userid;
    private String modUrl;
    private LoadingDialog dialog;
    private ShelfExpandableAdapter adapter;
    List<ShelfListBean.RECORDBeanX> list = new ArrayList<>();
    private List<ShelfListBean.RECORDBeanX> record;

    @Override
    protected int getContentView() {
        return R.layout.activity_space_shelf;
    }

    @Override
    protected void initView() {
        etSerach.addTextChangedListener(watcher);
        etSerach.setOnEditorActionListener((textView, i, keyEvent) -> true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopId = SPUtils.getString(this, Constant.SHOP_ID);
        shopName = SPUtils.getString(this, Constant.SHOP_NAME);
        userid = SPUtils.getString(this, Constant.USER_ID);
        tvTitle.setText("".equals(shopName) ? "选择门店" : shopName);
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        if (!"".equals(shopId)) {
            dialog = new LoadingDialog(this);
            dialog.show();
            getShelfList(userid,shopId);
        }
        adapter = new ShelfExpandableAdapter(SpaceShelfActivity.this,list);
        elvList.setAdapter(adapter);
        elvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                List<ShelfListBean.RECORDBeanX.RECORDBean> record = SpaceShelfActivity.this.record.get(i).RECORD;
                ShelfListBean.RECORDBeanX.RECORDBean recordBean = record.get(i1);
                Intent intent = new Intent(SpaceShelfActivity.this, ShelfDetialActivity.class);
                intent.putExtra(Constant.SHOP_ID,shopId);
                intent.putExtra(Constant.SHELF,recordBean.SFID);
                startActivity(intent);
                return true;
            }
        });

    }

    /**
     * 查找门店货架列表
     * @param userid
     * @param shopId
     */
    private void getShelfList(String userid, String shopId) {
        new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(modUrl).build().create(ApiService.class)
                .getShelfList(userid,shopId).compose(RxSchedulers.io_main()).
                subscribe(s -> {
                    Log.i(TAG, "s:" + s);
                    String json = s.replace("<ns:getShelfListResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    json = json.replace("</ns:return></ns:getShelfListResponse>", "");
                    Log.i(TAG, "json:" + json);
                    ShelfListBean shelfListBean = new Gson().fromJson(json, ShelfListBean.class);
                    if (0<shelfListBean.ROWS) {
                        record = shelfListBean.RECORD;
                        list.clear();
                        list.addAll(record);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }else{
                        Utils.showToast(SpaceShelfActivity.this, "暂无数据");
                    }
                }, throwable -> {
            Log.i(TAG, "throwable:" + throwable);
            Utils.showToast(SpaceShelfActivity.this, "网络异常,请重试");
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title:
                //TODO 选择门店
                startActivity(new Intent(SpaceShelfActivity.this, ChooseShopActivity.class));
                break;
        }
    }

    /**
     * editText监听
     */
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

        }
        //查询标签
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
           if (!"".equals(shopId)){
               String tag = s.toString();
//               String tag = str.toUpperCase();
               //查询标签  并显示
               list.clear();
               List<ShelfListBean.RECORDBeanX> recordBeanXList = new ArrayList<>();
               ArrayList<ShelfListBean.RECORDBeanX.RECORDBean> recordBeen = null;
               for (ShelfListBean.RECORDBeanX recordBeanX : record) {
                   for (ShelfListBean.RECORDBeanX.RECORDBean recordBean : recordBeanX.RECORD) {
                       if (recordBean.SFID.equals(tag)) {
                           ShelfListBean.RECORDBeanX.RECORDBean bean1 = new ShelfListBean.RECORDBeanX.RECORDBean(recordBean.FLOORNO, recordBean.ISSTOP,
                                   recordBean.LASTMODIFY, recordBean.LASTOP, recordBean.REMARKS, recordBean.RID,
                                   recordBean.SFID, recordBean.SFNAME, recordBean.SNO);
                           recordBeen =  new ArrayList<>();
                           recordBeen.add(bean1);
                           recordBeanXList.add(new ShelfListBean.RECORDBeanX(recordBeanX.ORD,recordBeanX.RID,recordBeanX.RNAME,1,recordBeanX.SID,recordBeen));
                       }
                   }
               }
               if (null==recordBeen||"".equals(tag)) {
                   list.addAll(record);
               }
               list.addAll(recordBeanXList);
               adapter.notifyDataSetChanged();
           }else{
               Utils.showToast(SpaceShelfActivity.this,"请选择门店");
           }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
