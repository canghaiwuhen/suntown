package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.weight.NestedListView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
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

public class HistoryDetialActivity extends Activity {

    private static final String TAG = "HistoryDetialActivity";
    @BindView(R.id.rl_tag_title)
    RelativeLayout rlTagTitle;
    @BindView(R.id.lv_old_data)
    NestedListView lvOldData;
    @BindView(R.id.rl_goods_title)
    RelativeLayout rlGoodsTitle;
    @BindView(R.id.lv_goods)
    NestedListView lvGoods;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    private String serverIP;
    private List<AllShopBean.RECORDBean> record;
    private ArrayList filtrateBean;
    private String sid;
    private String shopName;
    private List<Person> personList;
    private List<Person> oldList = new ArrayList<>();
    private List<Person> goodsList = new ArrayList<>();
    private OldListAdapter adapter = new OldListAdapter(this, oldList);
    private GoodsListAdapter goodsAdapter = new GoodsListAdapter(this, goodsList);
    private DbManager db;
    private String userid;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detial);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        client = new OkHttpClient();
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        personList = intent.getParcelableArrayListExtra(Constant.PERSON);
        String name = intent.getStringExtra(Constant.SHOP_NAME);
        tvSearch.setText(name);
//        Log.i(TAG, "personList-" + personList.toString());
        //TODO 设置数据
        initData();
        lvOldData.setAdapter(adapter);
        lvOldData.setOnItemClickListener((adapterView, view, i, l) -> {
            Person person = oldList.get(i);
            InfoBean infoBean = new InfoBean(person.sid, person.ip);
            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
            Intent intent1 = new Intent(HistoryDetialActivity.this, TagDetialActivity.class);
            intent1.putExtra(Constant.INFO_BEAN, infoBean);
            startActivity(intent1);
        });
        lvGoods.setAdapter(goodsAdapter);
        //商品详情
        lvGoods.setOnItemClickListener((adapterView, view, i, l) -> {
            Person person = goodsList.get(i);
            Log.i(TAG, "sid-" + person.sid + ",ip-" + person.ip);
            Intent intent2 = new Intent(HistoryDetialActivity.this, GoodsDetialActivity.class);
            intent2.putExtra(Constant.PERSON,  person);
            startActivity(intent2);
        });
        netService();
    }
    List<String> code = new ArrayList<>();
    List<String> ipList = new ArrayList<>();
    private void initData() {
        Log.i(TAG,"personList-"+personList.toString());
        for (Person person : personList) {
            String barcode = person.barcode;
            String ip = person.ip;
            if (barcode != null) {
                if (!code.contains(barcode)) {
                    code.add(barcode);
                    goodsList.add(person);
                }
            }else{
                if (!ipList.contains(ip)) {
                    ipList.add(ip);
                    oldList.add(person);
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

    private void netService() {
        Map<String, String> params = new HashMap<>();
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)){
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        }else{
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        params.put(Constant.USER_ID, userid);
        String ip = Constant.formatBASE_HOST(serverIP);
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

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_close, R.id.tv_clear, R.id.tv_close_goods, R.id.tv_lose_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                break;
            //标签关机
            case R.id.tv_close:
                if (oldList.size() == 0) {
                    return;
                }
                closeTag();
                break;
            //清空 标签
            case R.id.tv_clear:
                try {
                    for (Person person : oldList) {
                        String ip = person.ip;
                        List<Person> personList = db.selector(Person.class).where("ip", "=", ip).
                                where("sid", "=", sid).where("userId", "=",userid).findAll();
                        Log.i(TAG,"personList-"+personList);
                        db.delete(personList);
                    }
                    Log.i(TAG,"删除成功");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                oldList.clear();
                lvOldData.setVisibility(View.GONE);
                rlTagTitle.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_close_goods:
                //TODO 从数据库删除
                try {
                    for (Person person : goodsList) {
                        String barcode = person.barcode;
                        List<Person> personList = db.selector(Person.class).where("barcode", "=", barcode).
                                where("sid", "=", sid).where("userId", "=", userid).findAll();
                        Log.i(TAG,"personList-"+personList);
                        db.delete(personList);
                    }
                    goodsList.clear();
                    lvGoods.setVisibility(View.GONE);
                    rlGoodsTitle.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG,"删除成功");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            //缺货
            case R.id.tv_lose_goods:
                if (goodsList.size() == 0) {
                    return;
                }
                setLoseGoods();
                break;
        }
    }

    /**
     * 缺货
     */
    private void setLoseGoods() {
        String xml = "<SetAllQH>";
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

    /**
     * 缺货提交
     * @param xml
     */
    private void submitLoseGoods(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/SetAllQH")
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
                        Utils.showToast(HistoryDetialActivity.this, "提交缺货任务成功");
                        runOnUiThread(() -> {
                            goodsList.clear();
                            rlGoodsTitle.setVisibility(View.GONE);
                            goodsAdapter.notifyDataSetChanged();
                        });
                    } else {
                        Utils.showToast(HistoryDetialActivity.this, "提交缺货任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }

    /**
     * 标签关机
     */
    private void closeTag() {
        String xml = "<ESLOFF>";
        for (Person person : oldList) {
            String ip = person.ip;
            xml += "<TINYIP>" + ip + "</TINYIP>";

        }
        xml += "</ESLOFF>";
        Log.i(TAG, "xml--" + xml);
        submitService(xml);
    }

    /**
     * 关机任务
     * @param xml
     */
    private void submitService(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/ESLOFF")
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
                        Utils.showToast(HistoryDetialActivity.this, "提交关机任务成功");
                        runOnUiThread(() -> {
                            oldList.clear();
                            rlTagTitle.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            for (Person person : oldList) {
                                String ip = person.ip;
                                List<Person> personList = null;
                                try {
                                    personList = db.selector(Person.class).where("ip", "=", ip).
                                            where("sid", "=", sid).where("userId", "=", userid).findAll();
                                    db.delete(personList);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                     Log.i(TAG,"删除失败");
                                }
                                Log.i(TAG,"删除成功");
                                Log.i(TAG,"personList-"+personList);
                            }
                        });
                    } else {
                        Utils.showToast(HistoryDetialActivity.this, "提交关机任务失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                }
            });
        }).start();
    }
}
