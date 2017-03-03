package com.suntown.scannerproject.query;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.adapter.ShopAdapter;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.ShopListBean;
import com.suntown.scannerproject.bean.ShopXmlBean;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.query.bean.Person;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GoodsDetialActivity extends BaseActivity {

    private static final String TAG = "GoodsDetialActivity";
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.tv_barcode)
    TextView tvBarcode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_off)
    TextView tvOff;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_standard)
    TextView tvStandard;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.tv_now_price)
    TextView tvNowPrice;
    @BindView(R.id.tv_vip_price)
    TextView tvVipPrice;
    @BindView(R.id.tv_battery)
    TextView tvBattery;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_batch)
    TextView tvBatch;
    @BindView(R.id.tv_last_time)
    TextView tvLastTime;
    @BindView(R.id.tv_qh)
    TextView tvQh;
    @BindView(R.id.tv_set_off)
    TextView tvSetOff;
    @BindView(R.id.tv_icon)
    TextView tvIcon;
    @BindView(R.id.ll_choose)
    LinearLayout llChoose;
    private String serverip;
    private String barcode;
    private String sid;
    private String tinyip;
    private boolean isQH = true;
    private ShopXmlBean shopXmlBean;
    private OkHttpClient client;
    List<ShopXmlBean> shopXmlBeanList = new ArrayList<>();
    private List<ShopXmlBean> shopXmlBeen;
    private PopupWindow popupWindow;
    private View view;
    private ListView pop_list;
    private List<ShopXmlBean> list;
    private boolean isQuery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_goods_detial);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        Person person = getIntent().getParcelableExtra(Constant.PERSON);
        serverip = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverip)) {
            serverip = ApiConstant.BASE_URL;
        }
        Log.i(TAG, "person:" + person.toString());
        tinyip = person.ip;
        barcode = person.barcode;
        sid = person.sid;
        String name = person.name;
        //查询标签，条码
        if (null != tinyip && !"".equals(tinyip)) {
            tvShopName.setText("标签详情");
            initTinyip();
        } else {
            tvShopName.setText("条码详情");
            tvIcon.setVisibility(View.VISIBLE);
            init();
        }
        llChoose.setOnClickListener(view1 -> {
            Log.i(TAG,"shopXmlBeanList:"+shopXmlBeanList.size());
            if (0 != shopXmlBeanList.size() && null!=shopXmlBeanList) {
                showWindow(view1);
            }
        });
    }

    //查询标签
    private void initTinyip() {
        String ip = Constant.formatBASE_HOST(serverip);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.GetLabStatus2(tinyip, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            String xml = s.toString();
            xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            Log.i(TAG, "xml --" + xml);
            xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
            Log.i(TAG, "xml --" + xml);
            //TODO
            try {
                shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                Log.i(TAG, "shopXmlBean:" + shopXmlBean.toString());
                setData(shopXmlBean);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        });
    }

    //查询条码
    private void init() {
        String ip = Constant.formatBASE_HOST(serverip);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.getGoodsInfo2(barcode, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            try {
                String xml = s.toString();
                xml = xml.replace("<ns:GetGoodsInfo2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                Log.i(TAG, "xml --" + xml);
                xml = xml.replace("</ns:return></ns:GetGoodsInfo2Response>", "");
                xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                Log.i(TAG, "xml --" + xml);
                shopXmlBeen = new Xml2Json(xml).Xml2Bean();
                list = new ArrayList<>();
                Log.i(TAG, "shopXmlBeanList --" + shopXmlBeen.toString());
                if (shopXmlBeen != null && shopXmlBeen.size() > 1) {
//                    shopXmlBean = shopXmlBeanList.get(0);
                    for (int i = 0; i < shopXmlBeen.size(); i++) {
                        ShopXmlBean shopXmlBean = shopXmlBeen.get(i);
                        String tinyIp1 = shopXmlBean.TinyIp;
                            //查询标签
                            ShopXmlBean shopXmlBean1 = shopXmlBeen.get(i);
                            if (!list.contains(shopXmlBean1)) {
                                list.add(shopXmlBean1);
                        }
                    }
                    Log.i(TAG,"list:"+list.toString());
                    for (ShopXmlBean xmlBean : list) {
                        String tinyIp = xmlBean.TinyIp;
                        if (!"".equals(tinyIp)&&null!=tinyIp){
                            isQuery = true;
                            queryTinyip(tinyIp);
                        }
                    }
                    if (!isQuery){
                        setData(list.get(0));
                    }
                }else {
                    setData(shopXmlBeen.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 查询标签
     *
     * @param tinyIp
     */
    private void queryTinyip(String tinyIp) {
        String ip = Constant.formatBASE_HOST(serverip);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.GetLabStatus2(tinyIp, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            String xml = s.toString();
            xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            Log.i(TAG, "xml --" + xml);
            xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
            Log.i(TAG, "xml --" + xml);
            //TODO
            try {
                shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                shopXmlBeanList.add(shopXmlBean);
                Log.i(TAG,"shopXmlBean:"+shopXmlBean.toString());
                setData(shopXmlBean);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        });
    }

    //设置数据
    private void setData(ShopXmlBean shopXmlBean) {
        tvIp.setText(shopXmlBean.TinyIp);
        tvBarcode.setText(shopXmlBean.Barcode);
        tvName.setText(shopXmlBean.GName);
        String gStatus = shopXmlBean.GStatus;
        String powerOff = shopXmlBean.PowerOff;
//            tvIcon.setVisibility(View.VISIBLE);
//        if (shopXmlBeanList.size()>0) {
//        }
//        tvState.setText("1".equals(gStatus) ? "缺货" : ("0".equals(gStatus) ? "正常" : "其他状态"));
//        tvQh.setText("1".equals(gStatus) ? "取消缺货" : ("0".equals(gStatus) ? "设置缺货" : "其他状态"));
//        tvSetOff.setText("1".equals(powerOff) ? "关机" : ("0".equals(powerOff) ? "开机" : "其他状态"));
//        tvOff.setText("1".equals(powerOff) ? "已关机" : ("0".equals(powerOff) ? "已开机" : "其他状态"));
        if ("1".equals(gStatus)) {
            isQH = true;
            tvQh.setText("取消缺货");
            tvState.setText("缺货");
            tvQh.setVisibility(View.VISIBLE);
        } else if ("0".equals(gStatus)) {
            isQH = false;
            tvQh.setText("设置缺货");
            tvState.setText("正常");
            tvQh.setVisibility(View.VISIBLE);
        } else {
            tvState.setText("其他状态");
            tvQh.setVisibility(View.GONE);
        }
        if ("1".equals(powerOff)) {
            tvSetOff.setText("关机");
            tvSetOff.setTextColor(Color.GRAY);
            tvSetOff.setClickable(false);
            tvOff.setText("已关机");
            tvSetOff.setVisibility(View.GONE);
        } else if ("0".equals(powerOff)) {
            tvSetOff.setText("关机");
            tvOff.setText("已开机");
            tvSetOff.setVisibility(View.VISIBLE);
        } else {
            tvSetOff.setVisibility(View.GONE);
            tvOff.setText("其他状态");
        }
        tvCode.setText(shopXmlBean.GCode);
        tvType.setText(shopXmlBean.DispStr);
        tvUnit.setText(shopXmlBean.Unit);
        tvStandard.setText(shopXmlBean.Spec);
        tvArea.setText(shopXmlBean.Origin);
        tvOldPrice.setText(shopXmlBean.Oriprice);
        tvNowPrice.setText(shopXmlBean.uptPrice);
        tvVipPrice.setText(shopXmlBean.MemPrice);
        tvBattery.setText(shopXmlBean.Battery);
        tvStock.setText(shopXmlBean.CurStore);
        tvBatch.setText(shopXmlBean.VBatchNO);
        tvLastTime.setText(shopXmlBean.LastDate);
    }

    @OnClick({R.id.iv_back, R.id.tv_qh, R.id.tv_set_off})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_qh:
                //设置缺货
                String gStatus = shopXmlBean.GStatus;
                if ("0".equals(gStatus) || "1".equals(gStatus)) {
                    isQH = !isQH;
                    if (isQH) {
                        setLoseGoods(1);
                        tvState.setText("缺货");
                        tvQh.setText("取消缺货");
                    } else {
                        setLoseGoods(0);
                        tvState.setText("正常");
                        tvQh.setText("设置缺货");
                    }
                }
                break;
            case R.id.tv_set_off:
                //设置关机
                String powerOff = shopXmlBean.PowerOff;
                if ("0".equals(powerOff)) {
                    offTag();
                }
                break;
        }
    }

    /**
     * 关机
     */
    private void offTag() {
        String ip = Constant.formatBASE_HOST(serverip);
        String xml = "<ESLOFF><TINYIP>" + shopXmlBean.TinyIp + "</TINYIP></ESLOFF>";
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(ip) + "/axis2/services/STPdaService2/ESLOFF")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:ESLOFFResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "").
                            replace("</ns:return></ns:ESLOFFResponse>", "");
                    if (result.equals("0")) {
                        Utils.showToast(GoodsDetialActivity.this, "提交关机任务成功");
                        runOnUiThread(() -> {
                            tvSetOff.setClickable(false);
                            tvSetOff.setTextColor(Color.GRAY);
                            shopXmlBean.PowerOff = "1";
                            tvOff.setText("已关机");
                        });
                    } else {
                        Utils.showToast(GoodsDetialActivity.this, "提交关机任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }

    //设置缺货
    private void setLoseGoods(int i) {
        String ip = Constant.formatBASE_HOST(serverip);
        String xml = "<SetAllQH><Data>" + "<TINYIP>" + shopXmlBean.TinyIp + "</TINYIP>" + "<STATUS>" + i + "</STATUS></Data></SetAllQH>";
        Log.i(TAG, "xml:" + xml);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(ip) + "/axis2/services/STPdaService2/SetAllQH")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:SetAllQHResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    result = result.replace("</ns:return></ns:SetAllQHResponse>", "");
                    Log.i(TAG, "result-" + result);
                    if (result.equals("0")) {
                        Utils.showToast(GoodsDetialActivity.this, "提交任务成功");
                        runOnUiThread(() -> {
                            if (isQH) {
                                tvQh.setText("取消缺货");
                                shopXmlBean.GStatus = "1";
                            } else {
                                tvQh.setText("设置缺货");
                                shopXmlBean.GStatus = "0";
                            }
                        });
                    } else {
                        Utils.showToast(GoodsDetialActivity.this, "提交任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }
    private void showWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_list, null);
            pop_list = (ListView) view.findViewById(R.id.pop_list);
            ShopAdapter shopAdapter = new ShopAdapter(this,shopXmlBeanList);
            pop_list.setAdapter(shopAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 180,  WindowManager.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorGaryBg));
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, xPos, 0);
        int[] location = new int[2];
        popupWindow.showAsDropDown(parent, location[0] - popupWindow.getWidth(), location[1]);
        pop_list.setOnItemClickListener((adapterView, view1, position, id) -> {
            if (popupWindow != null) {
                //TODO
                ShopXmlBean shopXmlBean = shopXmlBeanList.get(position);
                setData(shopXmlBean);
                popupWindow.dismiss();
            }
        });
    }
}
