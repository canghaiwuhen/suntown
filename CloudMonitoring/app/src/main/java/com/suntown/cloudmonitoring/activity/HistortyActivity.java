package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.HistoryAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.base.BaseApplication;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class HistortyActivity extends Activity {
    private static final String TAG = "HistortyActivity";
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.listview)
    ListView listView;
    private String shopName;
    private String sid;
    private DbManager db;
    private List<AllShopBean.RECORDBean> record;
    private ArrayList<FiltrateBean> filtrateBean;
    private long currentTime = System.currentTimeMillis();
    private long lastDayTime = 3600 * 24 * 1000;
    private long lastTwoTime = 3600 * 24 * 1000*2;
    private long lastThreeTime =  3600 * 24 * 1000*3;
    private long lastFourTime = 3600 * 24 * 1000*4;
    private long lastFiveTime =  3600 * 24 * 1000*5;
    private long lastSixTime = 3600 * 24 * 1000*6;
    private long lastSevenTime =  3600 * 24 * 1000*7;
    private List<Person> oneDaythList;
    private List<Person> twoDaythList ;
    private List<Person> threeDaythList ;
    private List<Person> fourDaythList ;
    private List<Person> fiveDaythList ;
    private List<Person> sixDaythList ;
    private List<Person> sevenDaythList ;
    private List<String> daythlist =new ArrayList<>();
    private Map<String,List<Person>> listMap;
    private HistoryAdapter adapter= new HistoryAdapter(this,daythlist);
    private LinearLayout llNormal;
    private String serverIP;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historty);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        shopName = intent.getStringExtra(Constant.SHOP_NAME);
        sid = intent.getStringExtra(Constant.SHOP_NUM);
        Log.i(TAG, "sid--" + sid + "--shopname--" + shopName);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String s = daythlist.get(i);
            List<Person> personList = listMap.get(s);
            Intent intent1 = new Intent(HistortyActivity.this,HistoryDetialActivity.class);
            intent1.putParcelableArrayListExtra(Constant.PERSON, (ArrayList<? extends Parcelable>) personList);
            intent1.putExtra(Constant.SHOP_NAME,shopName);
            startActivity(intent1);
        });
        if (!"".equals(shopName)) {
            tvSearch.setText(shopName);
        }
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)){
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        }else{
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        netService();
        if (!"".equals(sid)) {
            //查询数据
            queryData();
        }
    }

    private void netService() {
        Log.i(TAG,"serverIp-"+serverIP);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, userid);
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllShopBean> apForm = service.getAllShopInfo(params);
        apForm.compose(RxSchedulers.io_main()).subscribe(allShopBean -> {
            record = allShopBean.RECORD;
            Log.i(TAG,"record-"+record);
            filtrateBean = new ArrayList<>();
            for (AllShopBean.RECORDBean recordBean : record) {
                FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                filtrateBean.add(bean);
            }
        }, throwable -> {

        });
    }

    private void queryData() {
        daythlist.clear();
        oneDaythList = new ArrayList<>();
        twoDaythList = new ArrayList<>();
        threeDaythList = new ArrayList<>();
        fourDaythList = new ArrayList<>();
        fiveDaythList = new ArrayList<>();
        sixDaythList = new ArrayList<>();
        sevenDaythList = new ArrayList<>();
        listMap = new HashMap<>();
//            List<Person> personList = null;
//
//                if (null != personList || "".equals(personList)) {
//                    Log.i(TAG, "personList-" + personList.toString());
//                    for (Person person : personList) {
//                        long time = person.time;
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
//                    if (oldList.size() > 0) {
//                        lvOldData.setVisibility(View.VISIBLE);
//                        rlTagTitle.setVisibility(View.VISIBLE);
//                    }
//                    if (goodsList.size() > 0) {
//                        lvGoods.setVisibility(View.VISIBLE);
//                        rlGoodsTitle.setVisibility(View.VISIBLE);
//                    }
//                    adapter.notifyDataSetChanged();
//                    goodsAdapter.notifyDataSetChanged();
//                }
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
////        return personList;
//        }
        // 查询所有有数据
        try {
            Log.i(TAG,"sid-"+sid+",serverip-"+serverIP+",userid-"+userid);
            List<Person> personList = db.selector(Person.class).where("sid", "=", sid).and("serverip", "=", serverIP).
                    and("userid", "=", userid).orderBy("id").findAll();
//            List<Person> personList = db.selector(Person.class).where("sid", "=", sid).orderBy("id").findAll();
//            personList-[Person{id=1, time=1478854021252, sid='571002002', barcode='6908180138985',
//                    name='null', gname='大湖苹果汁255毫升', ip='192.57.1.236', userid='suntown',
//                    serverip='http://www.smartesl.com.cn'}]
            if (null==personList) {
                Utils.showToast(this, "暂无信息");
            } else {
                Log.i(TAG,"personList-"+personList.toString());
                for (Person person : personList) {
                    long time = person.time;
                    long cuttime = currentTime - time;
                    //一天
                    if (cuttime < lastDayTime) {
                        oneDaythList.add(person);
                    // 两天
                    }else if (cuttime > lastDayTime && cuttime < lastTwoTime) {
                        twoDaythList.add(person);
                    }else if (cuttime > lastTwoTime && cuttime < lastThreeTime) {
                        threeDaythList.add(person);
                    }else if (cuttime > lastThreeTime && cuttime < lastFourTime) {
                        fourDaythList.add(person);
                    }else if (cuttime > lastFourTime && cuttime < lastFiveTime) {
                        fiveDaythList.add(person);
                    }else if (cuttime > lastFiveTime && cuttime < lastSixTime) {
                        sixDaythList.add(person);
                    }else if (cuttime > lastSixTime && cuttime < lastSevenTime) {
                        sevenDaythList.add(person);
                    }
                }
                SimpleDateFormat dateFormater= new SimpleDateFormat("yyyy-MM-dd");
                if (null!=oneDaythList&&0!=oneDaythList.size()){
                    Date dates=new Date(oneDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,oneDaythList);
                }else if (null!=twoDaythList&&0!=twoDaythList.size()){
                    Date dates=new Date(twoDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,twoDaythList);
                }else if (null!=threeDaythList&&0!=twoDaythList.size()){
                    Date dates=new Date(threeDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,threeDaythList);
                }else if (null!=fourDaythList&&0!=fourDaythList.size()){
                    Date dates=new Date(fourDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,fourDaythList);
                }else if (null!=fiveDaythList&&0!=fiveDaythList.size()){
                    Date dates=new Date(fiveDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,fiveDaythList);
                }else if (null!=sixDaythList&&0!=sixDaythList.size()){
                    Date dates=new Date(sixDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,sixDaythList);
                }else if (null!=sevenDaythList&&0!=sevenDaythList.size()){
                    Date dates=new Date(sevenDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,sevenDaythList);
                }
                if(0!=daythlist.size()){
                    llNormal.setVisibility(View.GONE);
                }
                Log.i(TAG,"listMap-"+listMap.toString());
                Log.i(TAG,"list-"+personList.toString());
                Log.i(TAG,"dayList-"+daythlist.toString());
                adapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_arrow, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_delete:
                try {
                    db.delete(Person.class);
                    Log.i(TAG,"删除成功");
                    Utils.showToast(HistortyActivity.this,"数据库已清空");
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.i(TAG,"删除失败");
                }
                break;
            case R.id.tv_search:
            case R.id.iv_arrow:
                if (filtrateBean != null) {
                    Intent intent = new Intent(HistortyActivity.this, FiltrateRegisterAct.class);
                    intent.putParcelableArrayListExtra("record", filtrateBean);
                    startActivityForResult(intent, 200);
                } else {
                    Utils.showToast(HistortyActivity.this, "数据加载中，请稍后");
                }
                break;
        }
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
                            queryData();
                        }
                    }
                }
                break;
        }
    }
}
