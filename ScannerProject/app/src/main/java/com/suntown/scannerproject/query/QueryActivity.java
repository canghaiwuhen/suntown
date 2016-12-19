package com.suntown.scannerproject.query;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.base.BaseApplication;
import com.suntown.scannerproject.bean.FiltrateBean;
import com.suntown.scannerproject.bean.ShopXmlBean;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.query.adapter.GoodsListAdapter;
import com.suntown.scannerproject.query.adapter.OldListAdapter;
import com.suntown.scannerproject.query.bean.Person;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.Xml2Json;
import com.suntown.scannerproject.weight.NestedListView;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

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


public class QueryActivity extends BaseActivity {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_serach)
    EditText etSerach;
    @BindView(R.id.lv_old_data)
    NestedListView lvOldData;
    private static final String TAG = "QueryActivity";
    @BindView(R.id.rl_tag_title)
    RelativeLayout rlTagTitle;
    @BindView(R.id.rl_goods_title)
    RelativeLayout rlGoodsTitle;
    @BindView(R.id.lv_goods)
    NestedListView lvGoods;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    private String goodsString;
    private String shopName = "";
    private OkHttpClient client;
    private List<Person> oldList = new ArrayList<>();
    private List<Person> goodsList = new ArrayList<>();
    private OldListAdapter adapter = new OldListAdapter(this, oldList);
    private GoodsListAdapter goodsAdapter = new GoodsListAdapter(this, goodsList);
    public ArrayList<FiltrateBean> filtrateBean;
    private DbManager db;
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private String sid;
    private String serverIp;
    private String userId;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init();
    }

    private void init() {
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        oldList.clear();
        goodsList.clear();
        lvOldData.setAdapter(adapter);
        lvGoods.setAdapter(goodsAdapter);
        sid = SPUtils.getString(this, Constant.SID);
        shopName = SPUtils.getString(this, Constant.SHOP_NAME);
        tvSearch.setText(shopName);
        serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        userId = SPUtils.getString(this, Constant.USER_CODE);
        if ("".equals(serverIp)) {
            serverIp = ApiConstant.BASE_URL;
        }
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, intentFilter);
        rlMain.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
        //监听回车键
        etSerach.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                if (!shopName.equals("")) {
                    goodsString = etSerach.getText().toString().trim();
                    boolean isTagNum = goodsString.contains(".");
                    if (isTagNum) {
                        //TODO 查询标签
                        queryTag(goodsString);
                    } else {
                        //TODO 查询条码
                        queryCode(goodsString);
                    }
                } else {
                    Utils.showToast(this, "请选择门店");
                }
            }
            return true;
        });
        //点击跳转详情
        lvOldData.setOnItemClickListener((adapterView, view, i, l) -> {
            Person person = oldList.get(i);
            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
            Intent intent = new Intent(QueryActivity.this, GoodsDetialActivity.class);
            intent.putExtra(Constant.PERSON, person);
            startActivity(intent);
        });
        //商品详情
        lvGoods.setOnItemClickListener((adapterView, view, i, l) -> {
            //TODO
            Person person = goodsList.get(i);
            Intent intent = new Intent(QueryActivity.this, GoodsDetialActivity.class);
            intent.putExtra(Constant.PERSON, person);
            startActivity(intent);
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        queryData();
//    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_close, R.id.tv_clear, R.id.tv_filtrate, R.id.tv_close_goods, R.id.tv_lose_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
//                startActivity(new Intent(this, ChooseShopActivity.class));
                break;
            case R.id.tv_clear:
                try {
                    for (Person person : oldList) {
                        String ip = person.ip;
                        List<Person> personList = db.selector(Person.class).where("ip", "=", ip).
                                and("sid", "=", sid).and("userId", "=", userId).findAll();
                        Log.i(TAG, "personList-" + personList);
                        db.delete(personList);
                    }
                    Log.i(TAG, "删除成功-");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                tinyipList.clear();
                oldList.clear();
                lvOldData.setVisibility(View.GONE);
                rlTagTitle.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_filtrate:
                startActivity(new Intent(QueryActivity.this, HistortyActivity.class));
                break;
            //设置缺货
            case R.id.tv_lose_goods:
                if (goodsList.size() == 0) {
                    return;
                }
                setLoseGoods();
                break;
            //TODO 关机
            case R.id.tv_close:
                if (oldList.size() == 0) {
                    return;
                }
                closeTag();
                break;
            //清空商品
            case R.id.tv_close_goods:
                //TODO 从数据库删除
                try {
                    for (Person person : goodsList) {
                        String barcode = person.barcode;
                        List<Person> personList = db.selector(Person.class).where("barcode", "=", barcode).
                                where("sid", "=", sid).where("userId", "=", userId).findAll();
                        Log.i(TAG, "personList-" + personList);
                        db.delete(personList);
                    }
                    goodsList.clear();
                    lvGoods.setVisibility(View.GONE);
                    rlGoodsTitle.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "删除成功-");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    //设置缺货
    private void setLoseGoods() {
//        XStream stream = new XStream();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SetAllQH>";
        //0 正常  1  缺货
        for (Person person : goodsList) {
            String ip = person.ip;
            Log.i(TAG, "ip-" + ip);
            xml += "<Data>" + "<TINYIP>" + ip + "</TINYIP>" + "<STATUS>" + 1 + "</STATUS></Data>";
        }
        xml += "</SetAllQH>";
        //TODO 设置缺货
        Log.i(TAG, "xml--" + xml);
        submitLoseGoods(xml);
    }


    //批量关机
    private void closeTag() {
//        XStream stream = new XStream();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ESLOFF>";
        for (Person person : oldList) {
            String ip = person.ip;
            xml += "<TINYIP>" + ip + "</TINYIP>";

        }
        xml += "</ESLOFF>";
        Log.i(TAG, "xml--" + xml);
        submitService(xml);
    }

    /**
     * 提交缺货任务
     *
     * @param xml
     */
    private void submitLoseGoods(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/SetAllQH")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:SetAllQHResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    Log.i(TAG, "result-" + result);
                    result = result.replace("</ns:return></ns:SetAllQHResponse>", "");
                    Log.i(TAG, "result-" + result);
                    if (result.equals("0")) {
                        Utils.showToast(QueryActivity.this, "提交缺货任务成功");
                        runOnUiThread(() -> {
                            goodsList.clear();
                            rlGoodsTitle.setVisibility(View.GONE);
                            goodsAdapter.notifyDataSetChanged();
                            addressList.clear();
                        });
                    } else {
                        Utils.showToast(QueryActivity.this, "提交缺货任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(QueryActivity.this, "网络错误，请重试");
                }
            });
        }).start();
    }

    /**
     * 提交关机任务
     *
     * @param xmlStr
     */
    private void submitService(String xmlStr) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xmlStr).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/ESLOFF")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    result = result.replace("<ns:ESLOFFResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    result = result.replace("</ns:return></ns:ESLOFFResponse>", "");
                    if (result.equals("0")) {
                        Utils.showToast(QueryActivity.this, "提交关机任务成功");
                        runOnUiThread(() -> {
                            oldList.clear();
                            tinyipList.clear();
                            rlTagTitle.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        Utils.showToast(QueryActivity.this, "提交关机任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(QueryActivity.this, "网络错误，请重试");
                }
            });
        }).start();
    }

    /**
     * 条件查询数据库
     */
    List<String> code = new ArrayList<>();
    List<String> ipList = new ArrayList<>();

    private void queryData() {
        addressList.clear();
        tinyipList.clear();

        ipList.clear();
        code.clear();
        oldList.clear();
        goodsList.clear();
        List<Person> personList = null;
        long times = System.currentTimeMillis();
        long lastDay = 24 * 60 * 60 * 1000;
        try {
            Log.i(TAG, "sid:" + sid + ",userid:" + userId + ",serverIp:" + serverIp);
            personList = db.selector(Person.class).where("sid", "=", sid).and("serverip", "=", serverIp).
                    and("userid", "=", userId).orderBy("id").findAll();
            if (null != personList || "".equals(personList)) {
                Log.i(TAG, "personList-" + personList.toString());
                for (Person person : personList) {
                    long time = person.time;
                    String Sid = person.sid;
                    if (sid.equals(Sid)) {
                        if (times - time <= lastDay) {
                            String barcode = person.barcode;
                            String ip = person.ip;
                            if (barcode != null) {
                                if (!code.contains(barcode)) {
                                    goodsList.add(person);
                                    code.add(barcode);
                                }
                            } else {
                                if (!ipList.contains(ip)) {
                                    ipList.add(ip);
                                    oldList.add(person);
                                }
                            }
                        }
                    }
                }
                if (oldList.size() > 0) {
                    lvOldData.setVisibility(View.VISIBLE);
                    rlTagTitle.setVisibility(View.VISIBLE);
                }
                if (goodsList.size() > 0) {
                    lvGoods.setVisibility(View.VISIBLE);
                    rlGoodsTitle.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                goodsAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    List<String> tinyipList = new ArrayList<>();

    /**
     * 查询标签
     *
     * @param tagIp TODO
     */
    private void queryTag(String tagIp) {
        Log.i(TAG, "serverIp :" + serverIp + "  tagIp  " + tagIp + "  sid  " + sid);
        String ip = Constant.formatBASE_HOST(serverIp);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).GetLabStatus2(tagIp, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            String xml = s.toString();
            xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            Log.i(TAG, "xml --" + xml);
            xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
            Log.i(TAG, "xml --" + xml);
            //TODO
            try {
                ShopXmlBean shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                String tinyIp = shopXmlBean.TinyIp;
                if (null != tinyIp) {
                    if (!tinyipList.contains(tinyIp)) {
                        tinyipList.add(tinyIp);
                        //不存Barcode gname
                        long times = System.currentTimeMillis();
                        Person person = new Person();
                        person.setUserid(userId);
                        person.setIp(tinyIp);
                        person.serverip = serverIp;
                        person.setSid(sid);
                        person.setTime(times);
                        person.setName(shopXmlBean.GName);
                        Log.i(TAG, "person-" + person.toString());
                        oldList.add(0,person);
                        lvOldData.setVisibility(View.VISIBLE);
                        rlTagTitle.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        goodsAdapter.notifyDataSetChanged();
                        Log.i(TAG, "oldList--" + oldList.toString());
                        //TODO 保存到数据库
                        SaveData(person);
                    }
                } else {
                    Utils.showToast(this, "没有查询到标签信息");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {

        });
    }

    List<String> addressList = new ArrayList<>();

    /**
     * 查询条码
     *
     * @param
     */
    private void queryCode(String goodsCode) {
        String ip = Constant.formatBASE_HOST(serverIp);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getGoodsInfo2(goodsCode, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            try {
                String xml = s.toString();
                xml = xml.replace("<ns:GetGoodsInfo2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                Log.i(TAG, "xml --" + xml);
                xml = xml.replace("</ns:return></ns:GetGoodsInfo2Response>", "");
                xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                Log.i(TAG, "xml --" + xml);
                List<ShopXmlBean> shopXmlBeanList = null;
                shopXmlBeanList = new Xml2Json(xml).PullXml();
                if (shopXmlBeanList != null) {
                    Log.i(TAG, "shopXmlBeanList --" + shopXmlBeanList.toString());
                    for (ShopXmlBean shopXmlBean : shopXmlBeanList) {
                        String gName = shopXmlBean.GName;
                        String barcode = shopXmlBean.Barcode;
                        String tinyIp = shopXmlBean.TinyIp;
                        if (!addressList.contains(barcode)) {
                            addressList.add(barcode);
                            long times = System.currentTimeMillis();
                            Log.i(TAG, "time--" + times);
                            //不存tinyIp sname
                            Person person = new Person();
                            person.setGname(gName);
                            person.setSid(sid);
                            person.setIp(tinyIp);
                            person.setServerip(serverIp);
                            person.setBarcode(barcode);
                            person.setUserid(userId);
                            person.setTime(times);
                            goodsList.add(0,person);
                            //TODO 保存到数据库
                            SaveData(person);
                        }
                        lvGoods.setVisibility(View.VISIBLE);
                        rlGoodsTitle.setVisibility(View.VISIBLE);
                        goodsAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, "goodsList--" + goodsList.toString());
                    }
                } else {
                    Utils.showToast(QueryActivity.this, "尚无此商品信息");
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (DbException e) {
                e.printStackTrace();
                Log.i(TAG, "保存数据库失败");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, throwable -> {

        });
    }

    //保存数据
    private void SaveData(Person person) {
        //取出所有数据
        long times = System.currentTimeMillis();
        long lastDay = times - 24 * 60 * 60 * 1000;
        try {
//            List<Person> personList = db.selector(Person.class).where("sid", "=", sid)//.and("userid", "=", userId)
//                    .and("time", ">", lastDay).and("time", "<", times).orderBy("id").findAll();
            db.save(person);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                //TODO
                if ("".equals(message)) {
                    return;
                }
                //TODO  查询服务器，获取信息
                if (message.startsWith(".")) {
                    //                                      16
                    //转成标签 Integer.parseInt(String s, int radix)
                    String tinyip = FormatString.fromTinyip(message);
                    message = tinyip;
                }
//                etSerach.setText(message);
                if (message.contains(".")) {
                    //查询标签
                    queryTag(message);
                } else {
                    //查询条码
                    queryCode(message);
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

    }

    /**
     * 回收键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(QueryActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}