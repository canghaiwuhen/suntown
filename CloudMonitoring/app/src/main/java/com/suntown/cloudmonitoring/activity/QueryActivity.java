package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.GoodsListAdapter;
import com.suntown.cloudmonitoring.adapter.OldListAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.InOutBean;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.FormatString;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;
import com.suntown.cloudmonitoring.weight.NestedListView;
import com.thoughtworks.xstream.XStream;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
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
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import static com.suntown.cloudmonitoring.R.id.lv_goods;


public class QueryActivity extends BaseActivity {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_serach)
    EditText etSerach;
    @BindView(R.id.lv_old_data)
    NestedListView lvOldData;
    private static final String TAG = "QueryActivity";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.rl_tag_title)
    RelativeLayout rlTagTitle;
    @BindView(R.id.rl_goods_title)
    RelativeLayout rlGoodsTitle;
    @BindView(lv_goods)
    NestedListView lvGoods;

    private List<AllShopBean.RECORDBean> record;
    private String sid = "";
    private String goodsString;
    private String shopName = "";
    private OkHttpClient client;
    private List<Person> oldList = new ArrayList<>();
    private List<Person> goodsList = new ArrayList<>();
    private OldListAdapter adapter = new OldListAdapter(this, oldList);
    private GoodsListAdapter goodsAdapter = new GoodsListAdapter(this, goodsList);
    private String userId;
    public ArrayList<FiltrateBean> filtrateBean;
    private DbManager db;
    private String serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        lvOldData.setAdapter(adapter);
        lvGoods.setAdapter(goodsAdapter);
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
        sid = SPUtils.getString(this, Constant.SID);
        if (!"".equals(sid)){
            shopName = SPUtils.getString(this, Constant.SHOPNAME);
            tvSearch.setText(shopName);
//            queryData();
        }

//        queryData();
        findViewById(R.id.tv_saoyisao).setOnClickListener(view -> {
            if (shopName.equals("")) {
                Utils.showToast(this, "请选择门店");
                return;
            }
            Intent intent = new Intent(this, CreamaActivity.class);
            intent.putExtra(Constant.IS_ON_SCANN, true);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });
        //监听回车键
        etSerach.setOnEditorActionListener((textView, i, keyEvent) -> {
            Log.i(TAG, "点击了回车");
            //TODO 查询服务
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
            return true;
        });
        //点击跳转详情
        lvOldData.setOnItemClickListener((adapterView, view, i, l) -> {
            Person person = oldList.get(i);
            InfoBean infoBean = new InfoBean(person.sid, person.ip);
            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
            Intent intent = new Intent(QueryActivity.this, TagDetialActivity.class);
            intent.putExtra(Constant.INFO_BEAN, infoBean);
            startActivity(intent);
        });
        //商品详情
        lvGoods.setOnItemClickListener((adapterView, view, i, l) -> {
            Person person = goodsList.get(i);
            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
            Intent intent = new Intent(QueryActivity.this, GoodsDetialActivity.class);
            intent.putExtra(Constant.PERSON,  person);
            startActivity(intent);
        });
        netService();
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_close, R.id.tv_clear, R.id.tv_filtrate, R.id.tv_close_goods, R.id.tv_lose_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                if ("".equals(filtrateBean)) {
                    Utils.showToast(QueryActivity.this, "数据加载中，请稍后");
                } else if (filtrateBean == null) {
                    Utils.showToast(QueryActivity.this, "暂无门店信息");
                } else {
                    Intent intent = new Intent(this, FiltrateRegisterAct.class);
                    intent.putParcelableArrayListExtra("record", filtrateBean);
                    Log.i(TAG, filtrateBean.toString());
                    startActivityForResult(intent, 200);
                }
                break;
            case R.id.tv_clear:
                try {
                    for (Person person : oldList) {
                        String ip = person.ip;
                        List<Person> personList = db.selector(Person.class).where("ip", "=", ip).
                                where("sid", "=", sid).where("userId", "=", userId).findAll();
                        Log.i(TAG,"personList-"+personList);
                        db.delete(personList);
                    }
                    Log.i(TAG,"删除成功-");
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
                if (filtrateBean != null) {
                    Intent intent = new Intent(QueryActivity.this, HistortyActivity.class);
                    intent.putExtra(Constant.SHOP_NAME, shopName);
                    intent.putExtra(Constant.SHOP_NUM, sid);
                    startActivity(intent);
                } else {
                    Utils.showToast(QueryActivity.this, "数据加载中，请稍后");
                }
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
//                addressList.clear();
                //TODO 从数据库删除
                try {
                    for (Person person : goodsList) {
                        String barcode = person.barcode;
                        List<Person> personList = db.selector(Person.class).where("barcode", "=", barcode).
                                where("sid", "=", sid).where("userId", "=", userId).findAll();
                        Log.i(TAG,"personList-"+personList);
                        db.delete(personList);
                    }
                    goodsList.clear();
                    lvGoods.setVisibility(View.GONE);
                    rlGoodsTitle.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG,"删除成功-");
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
            Log.i(TAG,"ip-"+ip);
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
                    result = result.replace("<ns:SetAllQHResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    Log.i(TAG,"result-"+result);
                    result = result.replace("</ns:return></ns:SetAllQHResponse>","");
                    Log.i(TAG,"result-"+result);
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
                    result = result.replace("<ns:ESLOFFResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    result = result.replace("</ns:return></ns:ESLOFFResponse>","");
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
     * 查询门店信息
     */
    private void netService() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, userId);
        String ip = Constant.formatBASE_HOST(serverIp);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllShopBean> apForm = service.getAllShopInfo(params);
        apForm.compose(RxSchedulers.io_main()).subscribe(allShopBean -> {
            record = allShopBean.RECORD;
            filtrateBean = new ArrayList<>();
            for (AllShopBean.RECORDBean recordBean : record) {
                FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                filtrateBean.add(bean);
            }

        }, throwable -> {

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == 300) {
                    String name = data.getStringExtra(Constant.STRING_DATA);
                    if ("".equals(name)) {
                        return;
                    }
                    for (AllShopBean.RECORDBean recordBean : record) {
                        if (recordBean.SNAME.equals(name)) {
                            tvSearch.setText(name);
                            sid = recordBean.SID;
                            Log.i(TAG, "SID:" + sid);
                            shopName = name;
                            //查询数据库。显示信息
                            SPUtils.put(this,Constant.SID,sid);
                            SPUtils.put(this,Constant.SHOPNAME,shopName);
//                            queryData();
                        }
                    }
                }
                break;
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    Log.i(TAG, "resultStr:" + resultStr);
                    if (resultStr.equals("")) {
                        return;
                    }
                    if (resultStr.startsWith(".")) {
                        //                                      16
                        //转成标签 Integer.parseInt(String s, int radix)
                        String tinyip = FormatString.fromTinyip(resultStr);
                        resultStr = tinyip;
                    }
//                    etSerach.setText(resultStr);
                    //TODO  查询服务器，获取信息
                    if (resultStr.contains(".")) {
                        //查询标签
                        queryTag(resultStr);
                    } else {
                        //查询条码
                        queryCode(resultStr);
                    }

                }
                break;
        }

    }

    /**
     * 条件查询数据库
     */
    List<String> code = new ArrayList<>();
    List<String> ipList = new ArrayList<>();

//    private void queryData() {
//        ipList.clear();
//        oldList.clear();
//        List<Person> personList = null;
//        long times = System.currentTimeMillis();
//        long lastDay = 24 * 60 * 60 * 1000;
//        try {
//            Log.i(TAG,"sid:"+ sid+",userid:"+userId+",serverIp:"+serverIp);
//            personList = db.selector(Person.class).where("sid", "=", sid).where("serverip", "=", serverIp).
//                    where("userid", "=", userId).orderBy("id").findAll();
//            if (null != personList || "".equals(personList)) {
//                Log.i(TAG, "personList-" + personList.toString());
//                for (Person person : personList) {
//                    long time = person.time;
//                    String Sid = person.sid;
//                    if (sid.equals(Sid)){
//                        if(times-time<=lastDay){
//                            String barcode = person.barcode;
//                            String ip = person.ip;
//                            if (barcode != null) {
//                                if (!code.contains(barcode)) {
//                                    goodsList.add(person);
//                                    code.add(barcode);
//                                }
//                            }else{
//                                if (!ipList.contains(ip)) {
//                                    ipList.add(ip);
//                                    oldList.add(person);
//                                }
//                            }
//                        }
//                    }
//                }
//                if (oldList.size() > 0) {
//                    lvOldData.setVisibility(View.VISIBLE);
//                    rlTagTitle.setVisibility(View.VISIBLE);
//                }
//                if (goodsList.size() > 0) {
//                    lvGoods.setVisibility(View.VISIBLE);
//                    rlGoodsTitle.setVisibility(View.VISIBLE);
//                }
//                adapter.notifyDataSetChanged();
//                goodsAdapter.notifyDataSetChanged();
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
////        return personList;
//    }

    List<String> tinyipList = new ArrayList<>();

    /**
     * 查询标签
     *
     * @param tagIp TODO
     */
    private void queryTag(String tagIp) {
        Log.i(TAG,"userId:"+userId+"tagIp:"+tagIp);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, userId);
        params.put(Constant.TINYIP, tagIp);
        params.put(Constant.SID, sid);
        Log.i(TAG, userId + "  " + tagIp + " " + sid + "  " + serverIp);
        String ip = Constant.formatBASE_HOST(serverIp);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<TagDetialBean> tagDetial = service.getTAGDetial(params);
        tagDetial.compose(RxSchedulers.io_main()).subscribe(tagDetialBean -> {
            String gname = tagDetialBean.gname;
            long times = System.currentTimeMillis();
            Log.i(TAG, "time--" + times);
            Log.i(TAG, "activityDate--" + tagDetialBean.toString());
            String tinyip = tagDetialBean.tinyip;
            String SID = tagDetialBean.sid;
            Log.i(TAG, "sid--" + sid);
            Log.i(TAG, "tinyip--" + tinyip);
            if(sid.equals(SID)){
                if (!tinyipList.contains(tinyip)) {
                    tinyipList.add(tinyip);
                    //不存Barcode gname
                    Person person = new Person();
                    person.setUserid(userId);
                    person.setIp(tinyip);
                    person.serverip=serverIp;
                    person.setSid(tagDetialBean.sid);
                    person.setTime(times);
                    person.setName(tagDetialBean.sname);
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
            }else{
                Utils.showToast(QueryActivity.this, "没有查询到该标签信息");
            }

        }, throwable -> {

        });
    }


    List<String> addressList = new ArrayList<>();

    /**
     * 查询条码
     *
     * @param goodsCode
     */
    private void queryCode(String goodsCode) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.BARCODE, goodsCode).
                add(Constant.SID, sid).build();
        Log.i(TAG, goodsCode + ",," + sid + ",," + serverIp);
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetGoodsInfo")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String xml = response.body().string();
                        xml = xml.replace("<ns:GetGoodsInfoResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replace("</ns:return></ns:GetGoodsInfoResponse>", "");
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
                                    addressList.add(0,barcode);
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
                                    goodsList.add(person);
                                    //TODO 保存到数据库
                                    SaveData(person);
                                }
                                runOnUiThread(() -> {
                                    lvGoods.setVisibility(View.VISIBLE);
                                    rlGoodsTitle.setVisibility(View.VISIBLE);
                                    goodsAdapter.notifyDataSetChanged();
                                    adapter.notifyDataSetChanged();
                                });
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
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
//                    Utils.showToast(QueryActivity.this, "尚无此商品信息");
                }
            });
        }).start();
    }

    //保存数据
    private void SaveData(Person person) {
        //取出所有数据
//            long times = System.currentTimeMillis();
//            long lastDay = times - 24 * 60 * 60 * 1000;
        try {
//                List<Person> personList = db.selector(Person.class).where("sid", "=", sid)//.and("userid", "=", userId)
//                        .and("time", ">", lastDay).and("time", "<", times).orderBy("id").findAll();
            db.save(person);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}