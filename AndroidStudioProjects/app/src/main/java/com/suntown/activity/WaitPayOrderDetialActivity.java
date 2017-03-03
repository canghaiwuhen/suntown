package com.suntown.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.AddressBean;
import com.suntown.bean.LoginBean;
import com.suntown.bean.TinyipAddressBean;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.AlipayUtils;
import com.suntown.utils.Constant;
import com.suntown.utils.PayResult;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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

public class WaitPayOrderDetialActivity extends BaseActivity {

    private static final String TAG = "WaitPayOrderDetialActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.iv_goods_photo)
    ImageView ivGoodsPhoto;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_tag_name)
    TextView tvTagName;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_all_price)
    TextView tvAllPrice;
    private WaitConfirmBean.RECORDBean recordBean;
    private List<WaitConfirmBean.RECORDBean.ORDERINFOBean> orderinfo;
    private WaitConfirmBean.RECORDBean.ORDERINFOBean orderinfoBean;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        uploadServer();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(WaitPayOrderDetialActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(WaitPayOrderDetialActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private String gname;
    private String money;
    private String returnAddress;
    private OkHttpClient client;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_pay_order);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        recordBean = getIntent().getParcelableExtra("recordBean");
        position = getIntent().getIntExtra("position", 0);
        init();
    }

    private void init() {
        orderinfo = recordBean.getORDERINFO();
        orderinfoBean = orderinfo.get(0);
        String[] address = recordBean.getADDRESS().split("\\/");
        String name = address.length > 0 ? address[0] : "未选择";
//        String number = address[1];
        String addres = address.length > 2 ? address[2] : "未选择";
        tvName.setText(name == "" ? "未选择" : name);
        tvAddress.setText(addres);
        tvOrderNumber.setText(recordBean.getFORMNO());
        String tinyip = orderinfoBean.getTINYIP();
        getAddressforTinyip(tinyip);
        tvTagName.setText(tinyip);
//        Picasso.with(context).load(UsageExampleListViewAdapter.eatFoodyImages[0]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.future_studio_launcher).noFade().into(imageViewFade);
//        ivGoodsPhoto.setImageResource(orerinfoBean.getIMGPATH());
        Picasso.with(this).load(Constant.formatImage(orderinfoBean.getIMGPATH())).error(R.drawable.no_photo).into(ivGoodsPhoto);
        gname = orderinfoBean.getGNAME();
        tvGoodsName.setText(gname);
        money = recordBean.getMONEY();
        tvPrice.setText("￥" + money);
        tvAllPrice.setText("￥" + money);
    }


    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.iv_goods_photo, R.id.ll_item, R.id.iv_cut, R.id.iv_add})
    public void onClick(View view) {
        Intent intent;
        String num;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_goods_photo:
                intent = new Intent(WaitPayOrderDetialActivity.this, PicActivity.class);
                intent.putExtra("picName", Constant.formatImage(orderinfoBean.getIMGPATH()));
                startActivity(intent);
                break;
            case R.id.ll_item:
                intent = new Intent(WaitPayOrderDetialActivity.this, AddressCenterActivity.class);
                intent.putExtra("isWaitPay", true);
                startActivityForResult(intent, 200);
                break;
            case R.id.btn_confirm:
                //TODO 跳转支付宝界面
                if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
                    return;
                }
                String address = tvAddress.getText().toString();
                if ("未选择".equals(address)) {
                    intent = new Intent(WaitPayOrderDetialActivity.this, AddressCenterActivity.class);
                    intent.putExtra("isWaitPay", true);
                    startActivityForResult(intent, 200);
                } else {
                    Alipay();
//                    uploadServer();
                }
                break;
            case R.id.iv_add:
                 num = tvNum.getText().toString();
                int i = Integer.parseInt(num);
                if (i<=100) {
                    int nowNum = i + 1;
                    tvNum.setText(nowNum+"");
                    Double v = Double.parseDouble(money);
                    BigDecimal bigDecimal = new BigDecimal(v * nowNum);
                    String format = new DecimalFormat("0.00").format(bigDecimal);
                    tvAllPrice.setText("￥" +format);
                }
                break;
            case R.id.iv_cut:
                num = tvNum.getText().toString();
                int y = Integer.parseInt(num);
                if (y>1){
                    y--;
                    tvNum.setText(y+"");
                    Double v1 = Double.parseDouble(money);
                    BigDecimal bigDecimal = new BigDecimal(v1*y);
                    String format = new DecimalFormat("0.00").format(bigDecimal);
                    tvAllPrice.setText("￥" + format);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 300) {
            //TODO
//            Intent intent = getIntent();
            AddressBean.RECORDBean recordBean = data.getParcelableExtra(Constant.RECORD_BEAN);
            if (!"".equals(recordBean)) {
                Log.i("text", "recordBean:" + recordBean.toString());
//                String[] split = returnAddress.split("/");
                tvName.setText(recordBean.RECEIVER);
                tel.setText(recordBean.PHONE);
                tvAddress.setText(recordBean.ADDRESS);
            }
        }
    }

    private void Alipay() {
        String Alipaysfid = SPUtils.getString(this, Constant.ALIPAYSFID);
        String Alipaysfzh = SPUtils.getString(this, Constant.ALIPAYSFZH);
        String Alipaysfrsa = SPUtils.getString(this, Constant.ALIPAYSFRSA);

        if (TextUtils.isEmpty(Alipaysfid) || TextUtils.isEmpty(Alipaysfrsa) || TextUtils.isEmpty(Alipaysfzh)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialog, which) -> {
                    }).show();
            return;
        }

        String price = tvAllPrice.getText().toString();
        String allPrice = price.replace("￥","");
        String orderInfo = AlipayUtils.getOrderInfo(gname, gname, allPrice, Alipaysfid, Alipaysfzh);
//        String orderInfo = AlipayUtils.getOrderInfo(gname, gname, money, Alipaysfid, Alipaysfzh);
//        String orderInfo = AlipayUtils.getOrderInfo(gname, gname, "0.01", Alipaysfid, Alipaysfzh);
        String sign = AlipayUtils.sign(orderInfo, Alipaysfrsa);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayUtils.getSignType();
        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(WaitPayOrderDetialActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo, true);
            Message msg = new Message();
            msg.what = 1;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    //获取标签地址
    private void getAddressforTinyip(String tinyip) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, tinyip);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getAddressforTinyip(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s.toString());
                String json = s.replace("<ns:getAddressforTinyipResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getAddressforTinyipResponse>", "");
                TinyipAddressBean tinyipAddressBean = new Gson().fromJson(json, TinyipAddressBean.class);
                if (tinyipAddressBean.ROWS>0) {
                    TinyipAddressBean.RECORDBean recordBean = tinyipAddressBean.RECORD.get(0);
                    tvName.setText(recordBean.RECEIVER);
                    tvAddress.setText(recordBean.ADDRESS);
                    tel.setText(recordBean.PHONE);
                }

            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
        });
    }

    // arg0 = orsderstr(0,2)arg1=formno  arg2= address
    private void uploadServer() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, "0");
        //订单编号
        params.put(Constant.ARG1,recordBean.getFORMNO());
        params.put(Constant.ARG2, returnAddress);
        params.put(Constant.ARG3, tvNum.getText().toString());
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).confirmOrderNew(params).compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "s:"+s);
                String json = s.replace("<ns:confirmOrderNewResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:confirmOrderNewResponse>", "");
                Log.i(TAG,"json:"+json);
                LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                if ("1".equals(loginBean.getRESULT())) {
                    Utils.showToast(WaitPayOrderDetialActivity.this, "支付成功，正在安排发货");
                    //TODO  回到条目  并删除已付款条目
                    Intent intent = new Intent();
                    setResult(150, intent);
                    finish();
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
        });
//
//        RequestBody body = new FormBody.Builder()
//                //待修改  517069323604891 389742601370469 134907256934875
//                .add(Constant.ARG0, "0").add(Constant.ARG1, "0")
//                .add(Constant.ARG2, returnAddress)
//                .add(Constant.ARG3, tvNum.getText().toString())
//                .build();
//        final Request request = new Request.Builder()
//                .url(Constant.formatBASE_HOST("confirmOrderNew"))
//                .post(body).build();
//        new Thread(() -> {
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Utils.showToast(WaitPayOrderDetialActivity.this, "请检查网络");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    InputStream is = response.body().byteStream();
//                    String json;
//                    try {
//                        json = new Xml2Json(is).Pull2Xml();
//                        LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
//                        if ("0".equals(loginBean.getRESULT())) {
//                            runOnUiThread(() -> {
//                                    Utils.showToast(WaitPayOrderDetialActivity.this, "支付成功，正在安排发货");
//                                    //TODO  回到条目  并删除已付款条目
//                                    Intent intent = new Intent();
//                                    setResult(150, intent);
//                                    finish();
//                            });
//                        }
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }).start();
    }
}
