package com.suntown.smartscreen.price;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.base.BaseApplication;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistortyActivity extends BaseActivity {
    private static final String TAG = "HistortyActivity";
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.listview)
    ListView listView;
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
    private DbManager db;
    private String shopName;
    private String sid;
    private String serverIP;
    private String userid;

    @Override
    protected int getContentView() {
        return R.layout.activity_historty;
    }

    @Override
    protected void initView() {
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        shopName = intent.getStringExtra(Constant.SHOP_NAME);
        tvSearch.setText(shopName);
        sid = intent.getStringExtra(Constant.SHOP_ID);
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
        serverIP = SPUtils.getString(this, Constant.MODURL);
        userid = SPUtils.getString(this, Constant.USER_ID);
        if ("".equals(serverIP)) {
            serverIP = ApiConstant.BASE_URL;
        }
        if (!"".equals(sid)) {
            //查询数据
            queryData();
        }
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
                } if (null!=twoDaythList&&0!=twoDaythList.size()){
                    Date dates=new Date(twoDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,twoDaythList);
                } if (null!=threeDaythList&&0!=twoDaythList.size()){
                    Date dates=new Date(threeDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,threeDaythList);
                } if (null!=fourDaythList&&0!=fourDaythList.size()){
                    Date dates=new Date(fourDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,fourDaythList);
                }if (null!=fiveDaythList&&0!=fiveDaythList.size()){
                    Date dates=new Date(fiveDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,fiveDaythList);
                }if (null!=sixDaythList&&0!=sixDaythList.size()){
                    Date dates=new Date(sixDaythList.get(0).time);
                    String format = dateFormater.format(dates);
                    daythlist.add(format);
                    listMap.put(format,sixDaythList);
                }if (null!=sevenDaythList&&0!=sevenDaythList.size()){
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

    @OnClick({R.id.iv_back, R.id.tv_delete})
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
        }
    }


}
