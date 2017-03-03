package com.suntown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.adapter.AddressAdapter;
import com.suntown.api.ApiService;
import com.suntown.bean.AddressBean;
import com.suntown.bean.BaseBean;
import com.suntown.bean.LoginBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class AddressCenterActivity extends BaseActivity {

    private static final String TAG = "AddressCenterActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv_address)
    RecyclerView lvAddress;
    private OkHttpClient client;
    private AddressAdapter adapter;
    private boolean isWaitPay;
    private List<AddressBean.RECORDBean> record;
    private String memid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_center);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        isWaitPay = getIntent().getBooleanExtra("isWaitPay", false);
        lvAddress.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAddress();
    }

    public void add_address(View view) {
        startActivity(new Intent(AddressCenterActivity.this, AddAddressActivity.class));
    }

    private void getAllAddress() {
        memid = SPUtils.getString(this, Constant.MEMID);
//        Log.i(String.valueOf(this), "MDMID:" + memid);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, memid);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getAllAddress(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG,"s:"+s.toString());
            String result = s.replace("<ns:getAllAddressResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:getAllAddressResponse>", "");
            AddressBean addressBean = new Gson().fromJson(result, AddressBean.class);
            Log.i(TAG,"addressBean:"+addressBean.toString());
            record = addressBean.getRECORD();
            int y = 0;
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).ISDEFAULT.equals("1")) {
                    y=i;
                }
            }
            AddressBean.RECORDBean recordBean = record.get(y);
            record.remove(recordBean);
            record.add(0,recordBean);
            adapter = new AddressAdapter(R.layout.address_item, record);
            lvAddress.setAdapter(adapter);
            adapter.setOnAddressAdapterCallBack(new AddressAdapter.OnAddressAdapterCallBack() {
                @Override
                public void deleteItemClick(int position) {
                    //TODO 向服务器发起删除请求
                    String id = record.get(position).ID;
                    String memid1 = SPUtils.getString(AddressCenterActivity.this, Constant.MEMID);
                    deleteAddress(id, memid1, position);
                }

                @Override
                public void onItemClick(int position) {
                    Log.i("test", "点击了");
                    AddressBean.RECORDBean recordBean = record.get(position);
                    boolean isClick = recordBean.isClick;
//                    String address = recordBean.RECEIVER + "/" + recordBean.PHONE + "/" + recordBean.ADDRESS;
//                    Log.i("test", "address:" + address);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.RECORD_BEAN, recordBean);
                    setResult(300, intent);
                    if (isWaitPay) {
                        finish();
                    }else{
                        if (isClick){
                            Intent intent1 = new Intent(AddressCenterActivity.this, AddAddressActivity.class);
                            intent1.putExtra(Constant.RECORD_BEAN, recordBean);
                            startActivity(intent1);
                        }
                    }
                }

                @Override
                public void onCheckClick(int position) {
                    Log.i(TAG,"position:"+position);
                    for (int i = 0; i < record.size(); i++) {
                        AddressBean.RECORDBean recordBean = record.get(i);
                        if (position==i){
                            recordBean.ISDEFAULT="1";
                        }else{
                            recordBean.ISDEFAULT="0";
                        }
                    }
                    sendService(position);
                    Log.i(TAG,"record:"+record.toString());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onEditClick(int position) {
                    AddressBean.RECORDBean recordBean = record.get(position);
                    Intent intent1 = new Intent(AddressCenterActivity.this, AddAddressActivity.class);
                    intent1.putExtra(Constant.RECORD_BEAN, recordBean);
                    startActivity(intent1);
                }
            });
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable.toString());
        });
    }

    private void sendService(int position) {
        AddressBean.RECORDBean recordBean = record.get(position);
        //TODO 修改地址
        Log.i(TAG,"recordBean:"+recordBean.toString());
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, recordBean.ID);
        params.put(Constant.ARG1, memid);
        params.put(Constant.ARG2, 1+"");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).updateAddressDefault(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:"+s);
            String result = s.replace("<ns:updateAddressDefaultResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:updateAddressDefaultResponse>", "");
            Log.i(TAG,"result:"+result.toString());
            BaseBean baseBean = new Gson().fromJson(result, BaseBean.class);
            if (baseBean.RESULT.equals("0")) {
                Utils.showToast(AddressCenterActivity.this, "设置默认地址成功");
//                record.remove(recordBean);
//                record.add(0,recordBean);
                adapter.notifyDataSetChanged();
            }else{
                Utils.showToast(AddressCenterActivity.this, "设置默认地址失败");
            }
        }, throwable -> {
            Log.i(TAG, "throwable:"+throwable);
        });
    }

    public void deleteAddress(String id, String memid, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, id);
        params.put(Constant.ARG1, memid);
        params.put(Constant.ARG2, "");
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).deleteAddress(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG,"s:"+s.toString());
            String result = s.replace("<ns:deleteAddressResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:deleteAddressResponse>", "");
            Log.i(TAG,"result:"+result.toString());
            BaseBean baseBean = new Gson().fromJson(result, BaseBean.class);
            if (baseBean.RESULT.equals("0")) {
                record.remove(position);
                adapter.notifyDataSetChanged();
                Utils.showToast(AddressCenterActivity.this, "删除成功");
            }else{
                Utils.showToast(AddressCenterActivity.this, "删除失败，请重试");
            }

        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable.toString());
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_manage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manage:
                //TODO 管理收货地址
                if (null!=record&&0!=record.size()) {
                    boolean isClick = record.get(0).isClick;
                    for (AddressBean.RECORDBean recordBean : record) {
                        recordBean.isClick=!isClick;
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
