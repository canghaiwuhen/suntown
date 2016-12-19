package com.suntown.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.adapter.AddressAdapter;
import com.suntown.bean.AddressBean;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

public class AddressCenterActivity extends Activity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv_address)
    ListView lvAddress;
    private OkHttpClient client;
    List<AddressBean.RECORDBean> record;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj!=null){
                record = (List<AddressBean.RECORDBean>) msg.obj;
                adapter = new AddressAdapter(AddressCenterActivity.this,record);
                lvAddress.setAdapter(adapter);
                adapter.setOnAddressAdapterCallBack(new AddressAdapter.OnAddressAdapterCallBack() {
                    @Override
                    public void deleteItemClick(int position) {
                        //TODO 向服务器发起删除请求
                        String id = record.get(position).getID();
                        String memid = SPUtils.getString(AddressCenterActivity.this, Constant.MEMID);
                        deleteAddress(id,memid,position);
//                        record.remove(position);
//                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onItemClick(int position) {
                        Log.i("test","点击了");
                        AddressBean.RECORDBean recordBean = record.get(position);
                        String address = recordBean.getRECEIVER()+"/"+recordBean.getPHONE()+"/"+recordBean.getADDRESS();
                        Log.i("test","address:"+address);
                        Intent intent = new Intent();
                        intent.putExtra(Constant.ADDRESS,address);
                        setResult(300, intent);
                        if (isWaitPay){
                            finish();
                        }
                    }
                });
            }
        }
    };
    private AddressAdapter adapter;
    private boolean isWaitPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_center);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        isWaitPay = getIntent().getBooleanExtra("isWaitPay", false);
        getAllAddress();
    }

    public void add_address(View view) {
        startActivityForResult(new Intent(AddressCenterActivity.this, AddAddressActivity.class),200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200&&resultCode==300){
            String address = data.getStringExtra("address");
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("number");
            Utils.showToast(this,"number"+number+"name"+name+"address"+address);
            if (number==null){
                return;
            }
            record.add(new AddressBean.RECORDBean("","","",address,"1",number,name));
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    private void getAllAddress() {
        final String memid = SPUtils.getString(this, Constant.MEMID);
        Log.i(String.valueOf(this),"MDMID:"+memid);
        RequestBody formBody = new FormBody.Builder().add("arg0", memid).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("getAllAddress"))
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Utils.showToast(AddressCenterActivity.this, "联网失败，请检查网络");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream is = response.body().byteStream();
                            String json;
                            try {
                                json = new Xml2Json(is).Pull2Xml();
                                AddressBean addressBean = new Gson().fromJson(json, AddressBean.class);
                                if (!"0".equals(addressBean.getRESULT())){
                                    return;
                                }
                                List<AddressBean.RECORDBean> record1 = addressBean.getRECORD();
                                Message msg = new Message();
                                msg.obj= record1;
                                handler.sendMessage(msg);
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    public void deleteAddress(String id, String memid, final int position) {
        RequestBody formBody = new FormBody.Builder().add("arg0", id).add("arg1",memid).build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("deleteAddress"))
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Utils.showToast(AddressCenterActivity.this, "联网失败，请检查网络");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream is = response.body().byteStream();
                            String json;
                            try {
                                json = new Xml2Json(is).Pull2Xml();
                                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                if ("0".equals(loginBean.getRESULT())){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            record.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
//                            Utils.showToast(AddressCenterActivity.this, "删除成功");
                                }else{
                                    return;
                                }
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
