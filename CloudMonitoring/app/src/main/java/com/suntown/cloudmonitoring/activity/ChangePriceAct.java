package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.ChangePriceExpandAdapter;
import com.suntown.cloudmonitoring.adapter.ShopAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Item0;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.bean.TagInfoBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.weight.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePriceAct extends Activity {

    private static final String TAG = "ChangePriceAct";
    @BindView(R.id.bChart)
    ColumnChartView bChart;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_defaultNum)
    TextView tvDefaultNum;
    @BindView(R.id.ev_item)
    ExpandableListView evItem;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    private int rows;
    private List<RegisterMor.RECORDBean> record;
    private List<RegisterMor.RECORDBean> beanList;
    private Map<String, List<RegisterMor.RECORDBean>> map;
    private List<Item0> itemList;
    private Map<String, List<String>> addressmap;
    private List<String> arddressList;
    private PopupWindow popupWindow;
    private ListView lvList;
    private ColumnChartData data;
    private ChangePriceExpandAdapter adapter;
    private List<String> host;
    private InputMethodManager systemService;
    private LoadingDialog dialog;

    private String userid;
    private LinearLayout llNormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_price);
        ButterKnife.bind(this);
        init();
        dialog = new LoadingDialog(this);
        dialog.show();
        etSearch.addTextChangedListener(watcher);
        llNormal = (LinearLayout) findViewById(R.id.ll_normal);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void init() {
        host = new ArrayList<>();
        String serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, userid);
        Log.i(TAG,"userid-"+userid+", ip-"+serverIP);
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.getChangePriceDefeat(params).compose(RxSchedulers.io_main()).subscribe(registerMor -> {
            record = registerMor.RECORD;
            Log.i(TAG, "RECORD:" + record);
            itemList = new ArrayList<>();
            arddressList = new ArrayList<>();
            if (registerMor.ROWS > 0) {
                map = new HashMap<>();
                addressmap = new HashMap<>();
                for (RegisterMor.RECORDBean recordBean : record) {
                    if (recordBean == null) {
                        break;
                    }
                    String sname = recordBean.SNAME;
                    String sid = recordBean.SID;
                    String aname = recordBean.ANAME;
                    String tinyip = recordBean.TINYIP;
                    //条目集合
                    if (map.containsKey(sid)) {
                        beanList = map.get(sid);
                        beanList.add(recordBean);
                    } else {
                        itemList.add(new Item0(sname, sid));
                        beanList = new ArrayList<>();
                        beanList.add(recordBean);
                    }
                    map.put(sid, beanList);
                    //地区集合添加
                    if (addressmap.containsKey(aname)) {
                        arddressList = addressmap.get(aname);
                        arddressList.add(tinyip);
                    } else {
                        host.add(aname);
                        arddressList = new ArrayList<>();
                        arddressList.add(tinyip);
                    }
                    addressmap.put(aname, arddressList);
                }
                rows = registerMor.ROWS;
                initView();
            } else {
                dialog.dismiss();
                //TODO 没有数据
                llNormal.setVisibility(View.VISIBLE);
                rlMain.setVisibility(View.GONE);
            }
        }, throwable -> {
            dialog.dismiss();
            llNormal.setVisibility(View.VISIBLE);
            rlMain.setVisibility(View.GONE);
        });
    }

    //设置数据
    private void initView() {
        Log.i("changePriceAct", "addressmap:" + addressmap);
        Log.i("changePriceAct", "itemList:" + itemList);
        Log.i("changePriceAct", "map:" + map);
        llNormal.setVisibility(View.GONE);
        rlMain.setVisibility(View.VISIBLE);
        tvDefaultNum.setText("所有门店变价失败的标签数量:" + rows);
        int numSubcolumns = 1;//设置每个柱状图显示的颜色数量(每个柱状图显示多少块)
        int numColumns = host.size();//柱状图的数量
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                String aname = host.get(i);
                List<String> list = addressmap.get(aname);
                Log.i("test", "list:" + list.toString());
                values.add(new SubcolumnValue(list.size(), Color.parseColor("#FEB737")));
                AxisValue value = new AxisValue(i);
                value.setLabel(host.get(i));
                axisValues.add(value);
            }
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        data = new ColumnChartData(columns);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("");
        axisY.setName("失败数量");
        axisX.setValues(axisValues);
        axisY.setLineColor(Color.GRAY);
        axisX.setLineColor(Color.GRAY);
        axisY.setTextColor(Color.parseColor("#D94368"));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        bChart.setColumnChartData(data);

        //设置适配器
        adapter = new ChangePriceExpandAdapter(this, itemList, map);
        Collections.sort(itemList, (item0, item1) -> {
            if (Integer.parseInt(item0.sid)>Integer.parseInt(item1.sid)) {
                return 1;
            }
            return -1;
        });
        evItem.setAdapter(adapter);
        dialog.dismiss();
        String s = host.get(0);
        tvAddress.setText(s);
        changeDate(s);
        adapter.setOnWaitFlagClickListener(position -> {
            if (evItem.isGroupExpanded(position)) {  //如果是打开状态则关闭
                evItem.collapseGroup(position);
            } else { //如果是关闭状态则打开
                evItem.expandGroup(position);
            }
        });
        evItem.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String sid = itemList.get(groupPosition).sid;
            List<RegisterMor.RECORDBean> recordBeanList = map.get(sid);
            RegisterMor.RECORDBean recordBean = recordBeanList.get(childPosition);
            String sid1 = recordBean.SID;
            String tinyip = recordBean.TINYIP;
            Log.i(TAG,"sid1-"+sid1+",tinyip-"+tinyip+",sid"+sid);
            InfoBean infoBean = new InfoBean(sid1, tinyip);
            Intent intent = new Intent(ChangePriceAct.this, TagDetialActivity.class);
            intent.putExtra(Constant.INFO_BEAN, infoBean);
            startActivity(intent);
            return true;
        });
    }

    //editText监听
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

        }

        //查询标签
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            String tag = s.toString();
            //查询标签  并显示
            if (map.size()!=0){
                map.clear();
            }
            itemList.clear();
            for (RegisterMor.RECORDBean recordBean : record) {
                if (recordBean.TINYIP.equals(tag)) {
                    String sname = recordBean.SNAME;
                    String sid = recordBean.SID;
                    if (map.containsKey(sid)) {
                        beanList = map.get(sid);
                        beanList.add(recordBean);
                    } else {
                        itemList.add(new Item0(sname, sid));
                        beanList = new ArrayList<>();
                        beanList.add(recordBean);
                    }
                    map.put(sid, beanList);
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @OnClick({R.id.iv_back, R.id.tv_address, R.id.iv_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_address:
            case R.id.iv_arrow:
                //展开区域信息
                showPopupWindow(view);
                break;
        }
    }

    private void showPopupWindow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.group_list, null);
            lvList = (ListView) view.findViewById(R.id.lv_list);
            ShopAdapter adapter = new ShopAdapter(this, host);
            lvList.setAdapter(adapter);
            popupWindow = new PopupWindow(view,180, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.whiteGary));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        int[] location = new int[2];
        popupWindow.showAsDropDown(parent, location[0] - popupWindow.getWidth(), location[1]);
        lvList.setOnItemClickListener((parent1, view, position, id) -> {
            String areaName = host.get(position);
            tvAddress.setText(areaName);
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            changeDate(areaName);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeDate(String areaName) {
        map.clear();
        itemList.clear();
        //更改适配器内容
        for (RegisterMor.RECORDBean recordBean : record) {
            if (recordBean.ANAME.equals(areaName)) {
                String sname = recordBean.SNAME;
                String sid = recordBean.SID;
                if (map.containsKey(sid)) {
                    beanList = map.get(sid);
                    beanList.add(recordBean);
                } else {
                    itemList.add(new Item0(sname, sid));
                    beanList = new ArrayList<>();
                    beanList.add(recordBean);
                }
                map.put(sid, beanList);
            }
        }
        Collections.sort(itemList, (item0, item1) -> {
            if (Integer.parseInt(item0.sid)>Integer.parseInt(item1.sid)) {
                return 1;
            }
            return -1;
        });
        adapter.notifyDataSetChanged();
    }
}
