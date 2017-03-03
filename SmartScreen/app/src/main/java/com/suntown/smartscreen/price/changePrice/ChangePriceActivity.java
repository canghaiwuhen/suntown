package com.suntown.smartscreen.price.changePrice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.price.NumericWheelAdapter;
import com.suntown.smartscreen.price.UpdatePriceActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.suntown.smartscreen.utils.XmlUtils;
import com.suntown.smartscreen.weight.AddPopWindow;
import com.suntown.smartscreen.weight.wheel.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePriceActivity extends BaseActivity<ChangePresenter,ChangeModel> implements ChangeContract.View {

    private static final String TAG = "ChangePriceActivity";
    private static final String TYPE1 = "0";
    private static final String TYPE2 = "1";
    private static final String TYPE3 = "2";

    @BindView(R.id.rl_content)
    RecyclerView rlContent;
//    @BindView(R.id.rl_content)
//    ListView rlContent;
//    private ChangeAdapter changeAdapter;
    private ChangeRlAdapter changeAdapter;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    private ArrayList<GoodsInfo.RECORDBean> recordBeanList;
    private String type;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_price;
    }
    @Override
    protected void initView() {
        type = TYPE1;
        recordBeanList = getIntent().getParcelableArrayListExtra(Constant.RECORD);
        initList(type);
        Log.i(TAG, "recordBeanList:" + recordBeanList.toString());
        initData();
        changeAdapter = new ChangeRlAdapter(this, recordBeanList);
        rlContent.setLayoutManager(new LinearLayoutManager(this));
        rlContent.setHasFixedSize(true);
//        changeAdapter = new ChangeAdapter(R.layout.change_price_item, recordBeanList);
        rlContent.setAdapter(changeAdapter);
        changeAdapter.setOnSwipeAdapterCallBack(new ChangeRlAdapter.OnSwipeAdapterCallBack() {
            @Override
            public void startTimeClick(int position) {
                if (position==0){
                    showDateAndTime(0,position);
                }
            }

            @Override
            public void endTimeClick(int position) {
                if (position==0){
                    showDateAndTime(1,position);
                }
            }
        });
    }

    private void initData() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = df.format(new Date());
        for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
            recordBean.STARTTIME=currentTime;
            recordBean.ENDTIME=currentTime;
        }
    }


    @OnClick({R.id.iv_back, R.id.button,R.id.tv_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.button:
                if (Utils.isFastClick()) {
                    return;
                }
                Log.i(TAG,"recordBeanList:"+recordBeanList.toString());
                for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
                    String starttime = recordBean.STARTTIME;
                    String endtime = recordBean.ENDTIME;
                    String vip = recordBean.VIP;
                    if ("".equals(starttime)||null==starttime|| "".equals(endtime)||null==endtime||"".equals(vip)||null==vip) {
                        Utils.showToast(ChangePriceActivity.this,"请填写完整信息");
                        return;
                   }
                }
                String userid = SPUtils.getString(ChangePriceActivity.this, Constant.USER_ID);
                String sid = SPUtils.getString(ChangePriceActivity.this, Constant.SHOP_ID);
                String xml = XmlUtils.List2Xml(recordBeanList, userid, sid);
                Log.i(TAG,"xml:"+xml);
                String modUrl = SPUtils.getString(this, Constant.MODURL);
                if ("".equals(modUrl)) {
                    modUrl = ApiConstant.BASE_URL;
                }
                mPresenter.updatePrice(xml,modUrl);
                break;
            case R.id.tv_change:
                //弹出dialog
                AddPopWindow addPopWindow = new AddPopWindow(ChangePriceActivity.this);
                addPopWindow.showPopupWindow(view);
                addPopWindow.setmDialogListener(new AddPopWindow.AddPopWindowListener() {
                    @Override
                    public void onTopClick() {
                        type = TYPE1;
                        initList(type);
                        changeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSecondClick() {
                        type = TYPE2;
                        initList(type);
                        changeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onBottomClick() {
                        type = TYPE3;
                        initList(type);
                        changeAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }
//    <DATALIST>
//    <APPINFO>
//    <USERID>suntown</USERID>
//    <PRICETYPE>0</PRICETYPE>
//    <BEGTIME>2017-02-22 09:48</BEGTIME>
//    <SID>571002002</SID>
//    <ENDTIME>2017-02-22 09:49</ENDTIME>
//    </APPINFO>
//    <DATA>
//    <MEMPRICE>12</MEMPRICE>
//    <BARCODE>6901285991240</BARCODE>
//    <TINYIP>0.0.60.119</TINYIP>
//    <NEWPRICE>7</NEWPRICE>
//    <ORIPRICE>
//    </ORIPRICE>
//    </DATA>
//    </DATALIST>

    @Override
    public void updatePriceSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePriceActivity.this);
        builder.setMessage("提交成功是否退出？");
//                builder.setTitle("提示");
        builder.setPositiveButton("确认", (dialog, which) -> {
            Intent intent = new Intent();
            for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
                //  调整会员价
                if (type.equals("0")) {
                    recordBean.MEMPRICE = recordBean.VIP;
                    //调整促销价格
                }else if(type.equals("1")){
                    //  调整现价
//                    recordBean.=recordBean.VIP;
                }else if(type.equals("2")){
                    recordBean.CURPRICE=recordBean.VIP;
                }
            }
            intent.putExtra(Constant.RECORD,recordBeanList);
            setResult(300, intent);
            dialog.dismiss();
            finish();
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
                    //  调整会员价
                    if (type.equals("0")) {
                        recordBean.MEMPRICE = recordBean.VIP;
                        //调整促销价格
                    }else if(type.equals("1")){
                        //  调整现价
//                    recordBean.=recordBean.VIP;
                    }else if(type.equals("2")){
                        recordBean.CURPRICE=recordBean.VIP;
                    }
                }
                changeAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this,msg);
    }

    private void initList(String type) {
        for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
            recordBean.TYPE = type;
        }
    }

    /**
     * 显示全部日期
     */
    private void showDateAndTime(int i,int position){
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMin = c.get(Calendar.MINUTE);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.date_time_picker_layout);
        // 设置宽高
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.AnimBottom);
        year = (WheelView) window.findViewById(R.id.new_year);
        initYear();
        month = (WheelView) window.findViewById(R.id.new_month);
        initMonth();
        day = (WheelView) window.findViewById(R.id.new_day);
        initDay(curYear,curMonth);
        hour = (WheelView) window.findViewById(R.id.new_hour);
        initHour();
        mins = (WheelView) window.findViewById(R.id.new_mins);
        initMins();

        // 设置当前时间
        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        hour.setCurrentItem(curHour);
        mins.setCurrentItem(curMin);

        month.setVisibleItems(7);
        day.setVisibleItems(7);
        hour.setVisibleItems(7);
        mins.setVisibleItems(7);
        // 设置监听
        TextView ok = (TextView) window.findViewById(R.id.set);
        TextView cancel = (TextView) window.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = year.getCurrentItem();
                //%04d年%02d月%02d日 %02d时%02d分
                String time = String.format(Locale.CHINA,"%04d-%02d-%02d %02d:%02d", year.getCurrentItem()+1950,
                        month.getCurrentItem()+1, day.getCurrentItem()+1, hour.getCurrentItem(), mins.getCurrentItem());
//                Toast.makeText(ChangePriceActivity.this, time, Toast.LENGTH_LONG).show();
                if (0==i){
                    for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
                        recordBean.STARTTIME = time;
                    }
                }else{
                    for (GoodsInfo.RECORDBean recordBean : recordBeanList) {
                        recordBean.ENDTIME = time;
                    }
                }
                changeAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return false;
            }
        });
    }
    /**
     * 初始化年
     */
    private void initYear() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1950, 2050);
        numericWheelAdapter.setLabel(" 年");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 12, "%02d");
        numericWheelAdapter.setLabel(" 月");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel(" 日");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }

    /**
     * 初始化时
     */
    private void initHour() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,0, 23, "%02d");
        numericWheelAdapter.setLabel(" 时");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(true);
    }

    /**
     * 初始化分
     */
    private void initMins() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,0, 59, "%02d");
        numericWheelAdapter.setLabel(" 分");
//		numericWheelAdapter.setTextSize(15);  设置字体大小
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(true);
    }

    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

}
