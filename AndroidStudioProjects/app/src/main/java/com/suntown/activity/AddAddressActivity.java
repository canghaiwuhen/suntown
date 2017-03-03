package com.suntown.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiClient;
import com.suntown.api.ApiService;
import com.suntown.bean.AddressBean;
import com.suntown.bean.BaseBean;
import com.suntown.bean.LoginBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;
import com.suntown.widget.OnWheelChangedListener;
import com.suntown.widget.WheelView;
import com.suntown.widget.adapters.ArrayWheelAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

import static com.suntown.utils.PhoneNumUtils.isPhoneNumberValid;


public class AddAddressActivity extends BaseWhellActivity implements OnWheelChangedListener, View.OnClickListener {
    private static final String TAG = "AddAddressActivity";
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView tv_cancel;
    private TextView tv_confirm;
    private TextView et_address;
    private TextView tv_back;
    private TextView tv_save;
    private LinearLayout ll_dialog_address;
    private LinearLayout llAddress;
    private EditText et_name;
    private EditText et_number;
    private EditText et_detail_address;
    private OkHttpClient client;
    boolean updateAddress = false;
    private String id;
    private String isdefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        client = new OkHttpClient();
        init();
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);

        et_address = ((TextView) findViewById(R.id.et_address));
        et_name = ((EditText) findViewById(R.id.et_name));
        et_number = ((EditText) findViewById(R.id.et_number));
        et_detail_address = ((EditText) findViewById(R.id.et_detail_address));

        tv_cancel = ((TextView) findViewById(R.id.tv_cancel));
        tv_confirm = ((TextView) findViewById(R.id.tv_confirm));
        tv_back = ((TextView) findViewById(R.id.tv_back));
        tv_save = ((TextView) findViewById(R.id.tv_save));
        ll_dialog_address = ((LinearLayout) findViewById(R.id.ll_dialog_address));
        ll_dialog_address.setVisibility(View.GONE);

        llAddress = ((LinearLayout) findViewById(R.id.ll_address));
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        et_address.setOnClickListener(this);

        AddressBean.RECORDBean recordBean = getIntent().getParcelableExtra(Constant.RECORD_BEAN);
        if (null!=recordBean){
//            陈＊＊/18037106523/河北省邯郸市临漳县1098号
            et_name.setText(recordBean.RECEIVER);
            String address = recordBean.ADDRESS;
            Log.i(TAG,"address:"+address);
            String[] split = address.split("/");
            et_address.setText(split[0]);
//            et_detail_address.setText(split[1]);
            et_number.setText(recordBean.PHONE);
            isdefault = recordBean.ISDEFAULT;
            id = recordBean.ID;
            updateAddress = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                ll_dialog_address.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                et_address.setText(mCurrentProviceName + mCurrentCityName  + mCurrentDistrictName);
                ll_dialog_address.setVisibility(View.GONE);
                break;
            case R.id.et_address:
                ll_dialog_address.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_address.getWindowToken(),0);
                break;
            case R.id.tv_back:
                //弹出dialog 尚未保存，是否退出
                showDialog();
                break;
            case R.id.tv_save:
                //TODO 提交服务器，判断号码格式
                commitAddress();
                break;
        }
    }

    private void commitAddress() {
        final String address = et_address.getText().toString().trim();
        final String etDetailAddress = et_detail_address.getText().toString().trim();
        final String etName = et_name.getText().toString().trim();
        final String etNumber = et_number.getText().toString().trim();
        Log.i("test1","number"+etNumber+"name"+etName+"address"+address+etDetailAddress);
        String memid = SPUtils.getString(this, Constant.MEMID);
        if (!isPhoneNumberValid(etNumber)) {
            Utils.showToast(this,"请填写正确的手机号码");
            return;
        }
        if ("".equals(address)||"".equals(etName)||"".equals(etDetailAddress)||"".equals(etNumber)){
            Utils.showToast(this,"请将收货地址填写完整");
            return;
        }
        Log.i(TAG, "id:"+id);
       if (updateAddress){
           //TODO 修改地址
           Map<String, String> params = new HashMap<>();
           params.put(Constant.ARG0, id);
           params.put(Constant.ARG1, memid);
           params.put(Constant.ARG2, address+etDetailAddress);
           params.put(Constant.ARG3, etName);
           params.put(Constant.ARG4, etNumber);
           params.put(Constant.ARG5, 0+"");
           String ip = Constant.BASE_HOST;
           Retrofit retrofit = new Retrofit.Builder().
                   addConverterFactory(ScalarsConverterFactory.create())
                   .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
           retrofit.create(ApiService.class).upDateAddress(params).compose(RxSchedulers.io_main()).subscribe(s -> {
               Log.i(TAG, "s:"+s);
               String result = s.replace("<ns:updateAddressResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
               result = result.replace("</ns:return></ns:updateAddressResponse>", "");
               Log.i(TAG,"result:"+result.toString());
               BaseBean baseBean = new Gson().fromJson(result, BaseBean.class);
               if (baseBean.RESULT.equals("0")) {
                   Utils.showToast(AddAddressActivity.this, "地址修改成功");
                   finish();
               }else{
                   Utils.showToast(AddAddressActivity.this, "地址修改失败");
               }
           }, throwable -> {
               Log.i(TAG, "throwable:"+throwable);
           });
       }else{
           RequestBody formBody = new FormBody.Builder().
                   add(Constant.ARG0, memid).add(Constant.ARG1,address+"/"+etDetailAddress).
                   add(Constant.ARG2,etName).add(Constant.ARG3,etNumber).add(Constant.ARG4,"0").build();
           final Request request = new Request.Builder()
                   .url(Constant.formatBASE_HOST("addAddress"))
                   .post(formBody)
                   .build();
           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(Call call, IOException e) {
                   Utils.showToast(AddAddressActivity.this, "联网失败，请检查网络");
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   InputStream is = response.body().byteStream();
                   String json;
                   try {
                       json = new Xml2Json(is).Pull2Xml();
                       LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                       if("0".equals(loginBean.getRESULT())){
                           Utils.showToast(AddAddressActivity.this,"地址添加成功");
                           finish();
//                           runOnUiThread(() -> {
//                               Intent intent = new Intent();
//                               Log.i("test","number"+etNumber+"name"+etName+"address"+etDetailAddress);
//                               intent.putExtra("address",mCurrentProviceName + mCurrentCityName  + mCurrentDistrictName+etDetailAddress);
//                               intent.putExtra("name",etName);
//                               intent.putExtra("number",etNumber);
//                               setResult(300, intent);
//                               finish();
//                           });
                       }else{
                           Utils.showToast(AddAddressActivity.this,"地址添加失败，请重试");
                       }
                   } catch (XmlPullParserException e) {
                       e.printStackTrace();
                   }
               }
           });
       }
    }



    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("地址尚未保存，确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", (dialog, which) -> {
            dialog.dismiss();
            AddAddressActivity.this.finish();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
    private void init() {
        setUpViews();
        setUpData();
        setUpListener();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    private void setUpListener() {
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(AddAddressActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(8);
        mViewCity.setVisibleItems(8);
        mViewDistrict.setVisibleItems(8);
        updateCities();
        updateAreas();
    }
}

