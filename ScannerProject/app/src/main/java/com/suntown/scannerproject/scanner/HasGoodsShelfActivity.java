package com.suntown.scannerproject.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.base.BaseApplication;
import com.suntown.scannerproject.bean.Item1;
import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.bean.ShopXmlBean;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.scanner.adapter.RecyclerMainAdapter;
import com.suntown.scannerproject.scanner.bean.ShelfInfoBean;
import com.suntown.scannerproject.scanner.bean.ShelfItemBean;
import com.suntown.scannerproject.scanner.util.CommitShelfData;
import com.suntown.scannerproject.scanner.util.ShelfXml2Json;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.SpaceItemDecoration;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.utils.WrapContentLinearLayoutManager;
import com.suntown.scannerproject.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

public class HasGoodsShelfActivity extends BaseActivity {
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private static final String TAG = "HasGoodsShelfActivity";
    private static final int MAX_INT = 10000;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.et_goods_shelf)
    EditText etGoodsShelf;
    @BindView(R.id.rl_main)
    RecyclerView rlMain;
    @BindView(R.id.et_num)
    EditText etNum;

    private OkHttpClient client;
    private String sid;
    private String userId;
    public static List<Integer> indexList = new ArrayList<>();
    public static List<ShelfItemBean> beanList;
    public static Map<Integer, List<ShelfItemBean>> listMap = new HashMap<>();
    //    public static boolean isOnScanner;
    private RecyclerMainAdapter mainAdapter;
    public int currentParentPosition;
    public int currentChildPosition;
    int clickParentPosition = MAX_INT;
    int clickChildPosition = MAX_INT;
    private InputMethodManager manager;
    private String goodsShelf;
    private String serverIp;
    //    private boolean isClickShelf = true;
    private boolean isConstant = false;
    private DbManager db;
    int Row = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_goods_shelf);
        ButterKnife.bind(this);
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Item1 item = getIntent().getParcelableExtra(Constant.ITEM);
        sid = item.sid;
        tvShopName.setText(item.sname);
        client = new OkHttpClient();
        queryService();
        initData(10);
        mainAdapter = new RecyclerMainAdapter(this, indexList, listMap);
        Log.i(TAG, listMap.toString());
        rlMain.setLayoutManager(new WrapContentLinearLayoutManager(this));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addItemDecoration(new SpaceItemDecoration(dimensionPixelSize));
        rlMain.setAdapter(mainAdapter);
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, intentFilter);
        etGoodsShelf.setOnClickListener(view -> requestFocus());
        etGoodsShelf.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                goodsShelf = etGoodsShelf.getText().toString().trim();
                if (!"".equals(goodsShelf)) {
//                    isClickShelf=false;
                    etGoodsShelf.setFocusable(false);
                    if (!Utils.isFastClick()) {
                        querDataBase(goodsShelf);
                    }
                }
                return true;
            }
            return false;
        });
        mainAdapter.setOnGroupItemClickListener(new RecyclerMainAdapter.OnGroupItemClickListener() {
            //子条目点击事件
            @Override
            public void onChildItemClick(View view, int group, int position) {
                currentParentPosition = group;
                currentChildPosition = position;
                clickParentPosition = MAX_INT;
                clickChildPosition = MAX_INT;
                Log.i(TAG, "indexList:" + indexList.toString());
                Log.i(TAG, "currentParentPosition:" + currentParentPosition + ",currentChildPosition:" + currentChildPosition);
                setTouch(currentParentPosition, currentChildPosition);
                mainAdapter.notifyDataSetChanged();
                goodsShelf = etGoodsShelf.getText().toString().trim();
                List<ShelfItemBean> shelfItemBeanList = listMap.get(group);
                ShelfItemBean shelfItemBean = shelfItemBeanList.get(position);
                boolean isClick = shelfItemBean.isClick;
                String gname = shelfItemBean.gname;
                String tag = shelfItemBean.tag;
                String barcode = shelfItemBean.barcode;
                //点击删除
                if (isClick) {
                    int size = shelfItemBeanList.size();
                    if (size > 6 && position == size - 1) {
                        shelfItemBeanList.remove(position);
                        mainAdapter.notifyDataSetChanged();
                    } else {
//                        Log.i(TAG, "shelfItemBean:" + shelfItemBean.toString());
                        shelfItemBean.barcode = "";
                        shelfItemBean.tag = "";
                        shelfItemBean.gname = "";
                        mainAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (!"".equals(gname) && !"".equals(tag) && !"".equals(barcode)) {

                    } else {
                        if ("".equals(goodsShelf)) {
                            Utils.showToast(HasGoodsShelfActivity.this, "请选择货架");
                            return;
                        }
//                        if (isOnScanner == false) {
//                            Utils.showToast(HasGoodsShelfActivity.this, "请打开扫描");
//                            return;
//                        }
                        //TODO 发送广播
//                        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
//                            Intent scannerIntent = new Intent(SCN_CUST_ACTION_START);
//                            sendBroadcast(scannerIntent);
//                        });
                    }
                }
            }

            //子条目删除条目
            @Override
            public void onChildItemDeleteClick(View view, int group, int position) {
                List<ShelfItemBean> itemBeanList = listMap.get(group);
                int size = itemBeanList.size();
                if (size > 6) {
                    itemBeanList.remove(position);
                    mainAdapter.notifyDataSetChanged();
                }
//                currentParentPosition=group;
//                currentChildPosition=position;
            }

            //条目添加
            @Override
            public void onAddClick(View view, int group) {
                Log.i(TAG, "" + group);
                List<ShelfItemBean> itemBeanList = listMap.get(group);
                ShelfItemBean e = new ShelfItemBean("", "", "");
                ShelfItemBean shelfItemBean = itemBeanList.get(0);
                e.isClick = shelfItemBean.isClick;
                shelfItemBean.isTouch = false;
                itemBeanList.add(e);
                mainAdapter.notifyDataSetChanged();
            }


            //条目减
            @Override
            public void onCutClick(View view, int group) {
                List<ShelfItemBean> itemBeanList = listMap.get(group);
                for (ShelfItemBean shelfItemBean : itemBeanList) {
                    shelfItemBean.isClick = !shelfItemBean.isClick;
                }
                mainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onItemDrag() {
                mainAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setTouch(int parent, int child) {
        for (Integer integer : indexList) {
            List<ShelfItemBean> itemBeanList = listMap.get(integer);
            for (ShelfItemBean shelfItemBean : itemBeanList) {
                shelfItemBean.isTouch = false;
            }
        }
        List<ShelfItemBean> shelfItemBeen = listMap.get(parent);
        ShelfItemBean shelfItemBean = shelfItemBeen.get(child);
        shelfItemBean.isTouch = true;
    }


    private void queryService() {
        serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        userId = SPUtils.getString(this, Constant.USER_CODE);
        if ("".equals(serverIp)) {
            serverIp = ApiConstant.BASE_URL;
        }
    }

    /**
     * 查询货架信息
     *
     * @param goodsShelf
     */
    private void queryShelfInfo(String goodsShelf) {
        String ip = Constant.formatBASE_HOST(serverIp);
        String usercode = SPUtils.getString(this, Constant.USER_CODE);
//        Log.i(TAG, "ip:" + ip + ":usercode:" + usercode);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getShelfList(goodsShelf, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            String xml = s.toString().trim();
            xml = xml.replace("<ns:GetShelfDataForPadResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            xml = xml.replace("</ns:return></ns:GetShelfDataForPadResponse>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
//            Log.i(TAG, "xml" + xml);
            try {
                ShelfInfoBean shelfInfoBean = new ShelfXml2Json(xml).PullXml();
                List<ShelfInfoBean.Shelf_Allocation> shelf_allocations = shelfInfoBean.Shelf_Allocations;
                ShelfInfoBean.ShelfBean shelfBean1 = shelfInfoBean.shelfBean;
//                Log.i(TAG, "shelfBean1:" + shelfBean1.toString());
//                Log.i(TAG, "shelf_allocations:" + shelf_allocations.toString());
                for (Integer integer : indexList) {
                    List<ShelfItemBean> shelfItemBeanList = listMap.get(integer);
                    int size = shelfItemBeanList.size();
                    if (size >6) {
                        for (int i = 0; i < size - 6; i++) {
                            shelfItemBeanList.remove(0);
                        }
                    }
                    for (ShelfItemBean shelfItemBean : shelfItemBeanList) {
                        shelfItemBean.barcode = "";
                        shelfItemBean.gname = "";
                        shelfItemBean.tag = "";
                        shelfItemBean.isClick = false;
                        shelfItemBean.isTouch = false;
                    }
                }
                if (null == shelfBean1) {
//                    Utils.showToast(HasGoodsShelfActivity.this, "没有查询到该货架数据");
                    etGoodsShelf.setText(goodsShelf);
                    etGoodsShelf.setFocusable(false);
                    mainAdapter.notifyDataSetChanged();
                } else {
                    String exist = shelfBean1.Exist;
                    if (null != exist) {
                        Utils.showToast(HasGoodsShelfActivity.this, "此货架在其他门店已经存在");
                        runOnUiThread(() -> etGoodsShelf.setText(""));
                    } else if (null == shelf_allocations) {
                        Utils.showToast(HasGoodsShelfActivity.this, "没有查询到该货架数据");
                    } else {
                        etGoodsShelf.setText(goodsShelf);
                        SPUtils.put(HasGoodsShelfActivity.this, Constant.SHELFGOODS, goodsShelf);
                        //设置数据
//                        Log.i(TAG, "长度:" + shelf_allocations.size());
                        ShelfInfoBean.ShelfBean shelfBean = shelfInfoBean.shelfBean;
                        int num = 0;
                        for (ShelfInfoBean.Shelf_Allocation shelf_allocation : shelf_allocations) {
                            int colNumber = Integer.parseInt(shelf_allocation.ColNumber);
                            int rowNumber = Integer.parseInt(shelf_allocation.RowNumber);
                            String tinyip = shelf_allocation.Tinyip;
                            String barcode = shelf_allocation.Barcode;
                            String gname = shelf_allocation.GName;
                            num = num > rowNumber ? num : rowNumber;
//                                List<ShelfItemBean> shelfItemBeen = listMap.get(10 - rowNumber);
                            int size = indexList.size();
                            if (rowNumber <= size) {
                                Integer integer = indexList.get(size - rowNumber);
                                List<ShelfItemBean> shelfItemBeen = listMap.get(integer);
                                int i = colNumber - 1;
                                if (shelfItemBeen.size() > i) {
                                    ShelfItemBean itemBean = new ShelfItemBean(tinyip, barcode, gname);
                                    itemBean.isClick = false;
                                    itemBean.isTouch = false;
                                    shelfItemBeen.add(colNumber - 1, itemBean);
                                    shelfItemBeen.remove(colNumber);
                                } else {
                                    for (int i1 = shelfItemBeen.size(); i1 < colNumber - 1; i1++) {
                                        ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                                        shelfItemBeen.add(itemBean);
                                        itemBean.isClick = false;
                                        itemBean.isTouch = false;
                                    }
                                    shelfItemBeen.add(colNumber - 1, new ShelfItemBean(tinyip, barcode, gname));
                                }
                            }
                        }
                        Row = num;
                    }
                mainAdapter.notifyDataSetChanged();
                }
//                setTouch(currentParentPosition,currentChildPosition);
                mainAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }, throwable -> {

        });
    }


    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.iv_back, R.id.tv_go, R.id.tv_confirm, R.id.tv_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if ("".equals(etGoodsShelf.getText().toString())){
                    finish();
                }else{
                    showDialog();
                }
                break;

            //条目限制
            case R.id.tv_go:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                //TODO 取出最大的
                String lineNumStr = etNum.getText().toString().trim();
                if (!"".equals(lineNumStr)) {
                    int i1 = Integer.parseInt(lineNumStr);
                    goodsShelf = etGoodsShelf.getText().toString().trim();
                    etGoodsShelf.setFocusable(false);
                    if ("".equals(goodsShelf)) {
//                        isClickShelf=true;
                        requestFocus();
                        Utils.showToast(this, "请输入货架编号");
                        return;
                    }
                    if (i1 > 0 && i1 < indexList.size()) {
                        Log.i(TAG, "Row:" + Row);
                        if (i1 < Row) {
                            i1 = Row;
                        }
                        ArrayList<Integer> integers = new ArrayList<>();
                        for (int i = indexList.size() - 1; i > indexList.size() - 1 - i1; i--) {
                            Integer index = indexList.get(i);
                            integers.add(index);
                        }
                        indexList.clear();
                        for (int i = 0; i < integers.size(); i++) {
                            indexList.add(0, integers.get(i));
                        }
                        Map<Integer, List<ShelfItemBean>> map = new HashMap<>();
                        for (Integer integer : indexList) {
                            List<ShelfItemBean> shelfItemBeanList = listMap.get(integer);
                            map.put(integer, shelfItemBeanList);
                        }
                        listMap.clear();
                        listMap.putAll(map);
                    } else if (i1 >= indexList.size()) {
//                        Log.i(TAG, "indexList-" + indexList.toString());
                        int y = 1;
                        for (int i = indexList.size(); i < i1; i++) {
                            y++;
                            Integer max = Collections.max(indexList);
//                            Integer integer = indexList.get(indexList.size() - 1);
                            indexList.add(0, max + y);
                            beanList = new ArrayList<>();
                            for (int j = 0; j < 6; j++) {
                                ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                                itemBean.isClick = false;
                                itemBean.isTouch = false;
                                beanList.add(itemBean);
                            }
                            listMap.put(max + y, beanList);
                        }
                    }
                    currentParentPosition = indexList.get(0);
                    currentChildPosition = 0;
                    setTouch(currentParentPosition, currentChildPosition);
//                    querDataBase(goodsShelf);
                    queryShelfInfo(goodsShelf);
                    mainAdapter.notifyDataSetChanged();
//                    Log.i(TAG, "indexList-" + indexList.size());
//                    Log.i(TAG, "indexList-" + indexList.toString());
                } else {
                    goodsShelf = etGoodsShelf.getText().toString().trim();
                    if (!"".equals(goodsShelf)) {
//                        isClickShelf=false;
                        etGoodsShelf.setFocusable(false);
//                        querDataBase(goodsShelf);
                        queryShelfInfo(goodsShelf);
                    }
                }
                mainAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_confirm:
                //TODO 非空判断，提交数据
                String goodsShelf = etGoodsShelf.getText().toString().trim();
                if ("".equals(goodsShelf)) {
//                    isClickShelf=true;
                    requestFocus();
                    Utils.showToast(this, "货架不能为空");
                    return;
                }
                //将 集合转换成xml
                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String szImei = TelephonyMgr.getDeviceId();
//                ShelfItemBean shelfItemBean = listMap.get(9).get(0);
//                Log.i(TAG,"shelfItemBean--"+shelfItemBean.toString());
                goodsShelf = goodsShelf.toUpperCase();
                if (goodsShelf.length() == 15) {
//                    Log.i(TAG, "indexList--" + indexList.toString());
//                    Log.i(TAG, "listMap--" + listMap.toString());
                    String xml = CommitShelfData.productData(indexList, listMap, goodsShelf, sid, szImei);
//                    Log.i(TAG, "xml--" + xml);
                    subServer(xml);
                } else {
                    Utils.showToast(this, "货架长度必须等于15");
                }

                break;
            case R.id.tv_goods:
                //TODO 扫描形成货架
//                isClickShelf = true;
                requestFocus();
                Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
                    Intent scannerIntent = new Intent(SCN_CUST_ACTION_START);
                    sendBroadcast(scannerIntent);
                });
                break;
        }
    }


    private void requestFocus() {
        etGoodsShelf.setFocusable(true);
        etGoodsShelf.setFocusableInTouchMode(true);
        etGoodsShelf.requestFocus();
        etGoodsShelf.findFocus();
    }

    /**
     * 提交数据到服务器
     */
    private void subServer(String xml) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.XML, xml).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/Commit_ScanData2")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    String xml = string.replace("<ns:Commit_ScanData2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                    xml = xml.replace("</ns:return></ns:Commit_ScanData2Response>", "");
//                    Log.i(TAG, "xml:" + xml);
                    if ("0".equals(xml)) {
                        Utils.showToast(HasGoodsShelfActivity.this, "数据提交成功");
                        //删除数据库
                        deleteData();
                    } else {
                        Utils.showToast(HasGoodsShelfActivity.this, "数据提交成失败");
                    }
                }
            });
        }).start();
    }

    /**
     * 弹出对话框
     */
    private void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("提示")
                .setContentText("您还没有提交数据，是否确认退出")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(sDialog -> sDialog.dismiss()).setConfirmClickListener(sDialog -> {
            saveData();
            Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                finish();
            });
            sDialog.dismiss();
        }).show();
    }

    /**
     * 查询货架
     *
     * @param tinyip
     */
    private void queryData(String tinyip) {
        RequestBody formBody = new FormBody.Builder().
                add(Constant.TINYIP, tinyip).
                add(Constant.SID, sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetLabStatus2")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
//                    Log.i(TAG, "xml:" + xml);
                    xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
                    xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
//                    Log.i(TAG, "xml:" + xml);
                    try {
                        ShopXmlBean shopXmlBean = new Xml2Json(xml).pullXml2Bean();
//                        Log.i(TAG, "shopXmlBean:" + shopXmlBean);
                        runOnUiThread(() -> {
                            String sfid = shopXmlBean.SFID;
                            if (null != sfid) {
                                etGoodsShelf.setText(sfid);
                                etGoodsShelf.setFocusable(false);
                                querDataBase(sfid);
                            } else {
                                requestFocus();
                                Utils.showToast(HasGoodsShelfActivity.this, "暂无该货架信息");
                            }
                        });
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        }).start();
    }

    /**
     * 查询商品信息
     *
     * @param barcode
     */
    private void getGoodsName(String barcode, int i) {
        Log.i(TAG, "barcode=" + barcode + ",  sid" + sid);
        RequestBody formBody = new FormBody.Builder().
                add("arg1", barcode).
                add("arg0", sid).build();
//        add(Constant.SID, "571002002").build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetGoodsInfo3")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        ResponseBody body = response.body();
                        String xml = body.string();
                        xml = xml.replace("<ns:GetGoodsInfo3Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replace("</ns:return></ns:GetGoodsInfo3Response>", "");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replaceAll("&lt;", "<");
                        xml = xml.replaceAll("&gt;", ">");
                        Log.i(TAG, "xml --" + xml);
                        xml = xml.replaceAll("&#xd;", "");
//                        List<ShopXmlBean> shopXmlBeanList = new Xml2Json(xml).PullXml();
                        Item2 item2 = new Xml2Json(xml).PullGoodsXml();
                        if (!"".equals(item2.GName) && null != item2.GName) {
                            String gName = item2.GName;
                            List<ShelfItemBean> shelfItemBeanList = listMap.get(currentParentPosition);
//                            Log.i(TAG, "shelfItemBeanList --" + shelfItemBeanList.toString());
//                            Log.i(TAG, "shelfItemBeanList --" + shelfItemBeanList.size());
                            ShelfItemBean shelfItemBean = shelfItemBeanList.get(i);
                            shelfItemBean.gname = gName;
                            runOnUiThread(() -> {
//                                Log.i(TAG, "查询到数据：" + shelfItemBean.tag + " " + shelfItemBean.barcode + " " + shelfItemBean.gname);
                                mainAdapter.notifyDataSetChanged();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Utils.showToast(HasGoodsShelfActivity.this, "没有该商品信息");
//                                mainAdapter.notifyDataSetChanged();
                            });
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        }).start();
    }

    /**
     * 初始化集合
     */
    private void initData(int index) {
        listMap.clear();
        indexList.clear();
        for (int i = 0; i < index; i++) {
            indexList.add(i);
            beanList = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
//                beanList.add(new ShelfItemBean(j + "标签", j + "条码", j + "名称"));
                ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                itemBean.isClick = false;
                itemBean.isTouch = false;
                beanList.add(itemBean);
            }
            currentChildPosition = 0;
            currentParentPosition = 0;
            listMap.put(i, beanList);
        }
        setTouch(currentParentPosition, currentChildPosition);
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

    /**
     * 返回键按下
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ("".equals(etGoodsShelf.getText().toString())){
                finish();
            }else{
                showDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                Log.i(TAG, "message:" + message);
                if (message.startsWith(".")) {
                    //转成标签 Integer.parseInt(String s, int radix)
                    String tin = FormatString.fromTinyip(message);
                    message = tin;
                }
                Log.i(TAG, "message:" + message);
                if (etGoodsShelf.isFocusable()) {
                    //TODO 查询 货架
                    if (message.contains(".")) {
                        //TODO 查询货架 判断数据库中有无该货架信息
                        queryData(message);
//                        querDataBase(message);
                        //查询货架信息
                    } else if (message.length()==15){
                        querDataBase(message);
                    }else {
//                        Utils.showToast(context, "请输入正确的货架信息");
                    }
                } else {
                        //多扫
                        Log.i(TAG, "currentParentPosition:" + currentParentPosition + ",currentChildPosition:" + currentChildPosition);
                        if (clickParentPosition != currentParentPosition || clickChildPosition != currentChildPosition) {
                            clickParentPosition = currentParentPosition;
                            clickChildPosition = currentChildPosition;
                        } else {
                            currentChildPosition++;
                            clickChildPosition++;
                        }
                        Log.i(TAG, "clickParentPosition:" + clickParentPosition + ",clickChildPosition:" + clickChildPosition);
                        Log.i(TAG, "i:" + currentChildPosition);
                        List<ShelfItemBean> shelfItemBeanList = listMap.get(clickParentPosition);
                        Log.i(TAG, "shelfItemBeanList:" + shelfItemBeanList.toString());
                        if (clickChildPosition >= shelfItemBeanList.size()) {
                            ShelfItemBean e = new ShelfItemBean("", "", "");
                            e.isClick = false;
                            e.isTouch = false;
                            shelfItemBeanList.add(e);
                        }
                        setTouch(clickParentPosition, clickChildPosition);
                        mainAdapter.notifyDataSetChanged();
                        ShelfItemBean shelfItemBean = shelfItemBeanList.get(clickChildPosition);
                        Log.i(TAG, "shelfItemBean:" + shelfItemBean);
                        if (message.contains(".")) {
                            isConstant = false;
                            //TODO 查询是否存在于其他货架
                            String ip = Constant.formatBASE_HOST(serverIp);
                            String usercode = SPUtils.getString(context, Constant.USER_CODE);
                            Log.i(TAG, "ip:" + ip + ":usercode:" + usercode);
                            Retrofit retrofit = new Retrofit.Builder().
                                    addConverterFactory(ScalarsConverterFactory.create())
                                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
                            String finalMessage = message;
                            Log.i(TAG, "listMap3:" + listMap.toString());
                            retrofit.create(ApiService.class).GetLabStatus2(message, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
                                Log.i(TAG, "listMap-3:" + listMap.toString());
                                try {
                                    String xml = s.toString();
                                    xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                                    Log.i(TAG, "xml:" + xml);
                                    xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
                                    xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                                    Log.i(TAG, "xml:" + xml);
                                    Log.i(TAG, "listMap4:" + listMap.toString());
                                    ShopXmlBean shopXmlBean = null;
                                    shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                                    String sfid = shopXmlBean.SFID;
                                    goodsShelf = etGoodsShelf.getText().toString().trim();
                                    Log.i(TAG, "sfid:" + sfid + ", goodsShelf" + goodsShelf);
                                    if (!goodsShelf.equals(sfid) && null != sfid) {
                                        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("提示")
                                                .setContentText("该标签已存在其他货架，是否替换")
                                                .setCancelText("取消")
                                                .setConfirmText("确定")
                                                .showCancelButton(true)
                                                .setCancelClickListener(sDialog -> sDialog.dismiss())
                                                .setConfirmClickListener(sweetAlertDialog -> {
                                                    for (int i = 0; i < indexList.size(); i++) {
                                                        List<ShelfItemBean> shelfItemBeen = listMap.get(indexList.get(i));
                                                        for (ShelfItemBean shelfItemBean1 : shelfItemBeen) {
                                                            if (finalMessage.equals(shelfItemBean1.tag)) {
                                                                isConstant = true;
                                                                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                                                                        .setTitleText("提示")
                                                                        .setContentText("该标签已存在该货架，是否替换")
                                                                        .setCancelText("取消")
                                                                        .setConfirmText("确定")
                                                                        .showCancelButton(true)
                                                                        .setCancelClickListener(sDialog -> sDialog.dismiss())
                                                                        .setConfirmClickListener(sweetAlertDialog1 -> {
                                                                            isConstant = false;
                                                                            shelfItemBean1.tag = "";
                                                                            shelfItemBean.tag = finalMessage;
                                                                            mainAdapter.notifyDataSetChanged();
                                                                            int num = indexList.size() - clickParentPosition;
                                                                            Row = Row > num ? Row : num;
                                                                            sweetAlertDialog1.dismiss();
                                                                        })
                                                                        .show();
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!isConstant) {
                                                        shelfItemBean.tag = finalMessage;
                                                        int num = indexList.size() - clickParentPosition;
                                                        Row = Row > num ? Row : num;
                                                    }
                                                    sweetAlertDialog.dismiss();
                                                    mainAdapter.notifyDataSetChanged();
                                                })
                                                .show();
                                        mainAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.i(TAG, "listMap:" + listMap.toString());
                                        for (Integer integer : indexList) {
                                            List<ShelfItemBean> shelfItemBeen = listMap.get(integer);
                                            for (ShelfItemBean shelfItemBean1 : shelfItemBeen) {
                                                if (finalMessage.equals(shelfItemBean1.tag)) {
                                                    Log.i(TAG, "finalMessage:" + finalMessage);
                                                    isConstant = true;
                                                    new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                                                            .setTitleText("提示")
                                                            .setContentText("该标签已存在该货架，是否替换")
                                                            .setCancelText("取消")
                                                            .setConfirmText("确定")
                                                            .showCancelButton(true)
                                                            .setCancelClickListener(sDialog -> sDialog.dismiss())
                                                            .setConfirmClickListener(sweetAlertDialog1 -> {
                                                                isConstant = false;
                                                                shelfItemBean1.tag = "";
                                                                shelfItemBean.tag = finalMessage;
                                                                mainAdapter.notifyDataSetChanged();
                                                                sweetAlertDialog1.dismiss();
                                                                int num = indexList.size() - clickParentPosition;
                                                                Row = Row > num ? Row : num;
                                                            })
                                                            .show();
                                                    break;
                                                }
                                            }
                                        }
                                        if (!isConstant) {
                                            shelfItemBean.tag = finalMessage;
                                            int num = indexList.size() - clickParentPosition;
                                            Row = Row > num ? Row : num;
                                        }
                                        mainAdapter.notifyDataSetChanged();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            shelfItemBean.barcode = message;
                            int num = indexList.size() - clickParentPosition;
                            Row = Row > num ? Row : num;
                        }
                        if (!"".equals(shelfItemBean.barcode)) {
                            //TODO 查询商品信息
                            Log.i(TAG, "position:" + clickChildPosition);
                            getGoodsName(shelfItemBean.barcode, clickChildPosition);
                        }
//                    mainAdapter.notifyDataSetChanged();
                    }
                    mainAdapter.notifyDataSetChanged();
                    }

            }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 查询数据库
     *
     * @param sfid
     */
    private void querDataBase(String sfid) {
        etGoodsShelf.setText(sfid);
        etGoodsShelf.setFocusable(false);
        try {
            List<ShelfItemBean> shelfItemBeanList = db.selector(ShelfItemBean.class).where("sid", "=", sid).and("user", "=", userId).and("sfid", "=", sfid).findAll();
            if (null != shelfItemBeanList && 0 != shelfItemBeanList.size()) {
                for (Integer integer : indexList) {
                    List<ShelfItemBean> shelfItemBeen = listMap.get(integer);
                    for (ShelfItemBean shelfItemBean : shelfItemBeen) {
                        shelfItemBean.barcode = "";
                        shelfItemBean.gname = "";
                        shelfItemBean.tag = "";
                    }
                }
                Log.i(TAG, "shelfItemBeanList1-" + shelfItemBeanList.toString());
                int num = 0;
                for (ShelfItemBean shelfItemBean : shelfItemBeanList) {
                    String barcode = shelfItemBean.barcode;
                    String tinyip = shelfItemBean.tag;
                    String gname = shelfItemBean.gname;
                    String RowNumber = shelfItemBean.RowNumber;
                    String ColNumber = shelfItemBean.ColNumber;
                    int rowNumber = Integer.parseInt(RowNumber);
                    int colNumber = Integer.parseInt(ColNumber);
                    num = num > rowNumber ? num : rowNumber;
                    int size = indexList.size();
                    if (rowNumber <= size) {
                        Integer integer = indexList.get(size - rowNumber);
                        List<ShelfItemBean> shelfItemBeen = listMap.get(integer);
                        int i = colNumber - 1;
                        if (shelfItemBeen.size() > i) {
                            shelfItemBeen.add(colNumber - 1, new ShelfItemBean(tinyip, barcode, gname));
                            shelfItemBeen.remove(colNumber);
                        } else {
                            for (int i1 = shelfItemBeen.size(); i1 < colNumber - 1; i1++) {
                                shelfItemBeen.add(new ShelfItemBean("", "", ""));
                            }
                            shelfItemBeen.add(colNumber - 1, new ShelfItemBean(tinyip, barcode, gname));
                        }
                    }
                    setTouch(currentParentPosition, currentChildPosition);
                    runOnUiThread(() -> mainAdapter.notifyDataSetChanged());
                }
                Row = num;
            } else {
                //联网查询数据
                queryShelfInfo(sfid);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存数据到数据库
     */
    public void saveData() {
        String shelfNum = etGoodsShelf.getText().toString().trim();
        try {
            if (!"".equals(shelfNum)) {
                deleteData();
//                isClickShelf=false;
                etGoodsShelf.setFocusable(false);
                for (int i = 0; i < indexList.size(); i++) {
                    int rowNumber = indexList.size() - i;
                    List<ShelfItemBean> shelfItemBeanList = listMap.get(indexList.get(i));
                    for (int y = 0; y < shelfItemBeanList.size(); y++) {
                        int colNumber = y + 1;
                        long times = System.currentTimeMillis();
                        ShelfItemBean shelfItemBean = shelfItemBeanList.get(y);
                        String barcode = shelfItemBean.barcode;
                        String gname = shelfItemBean.gname;
                        String tag = shelfItemBean.tag;
                        shelfItemBean.sfid = shelfNum;
                        shelfItemBean.sid = sid;
                        shelfItemBean.user = userId;
                        shelfItemBean.ColNumber = colNumber + "";
                        shelfItemBean.RowNumber = rowNumber + "";
                        Log.i(TAG, shelfItemBean.toString());
                        shelfItemBean.time = times;
                        if (!"".equals(tag) || !"".equals(barcode)) {
                            db.save(shelfItemBean);
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     */
    public void deleteData() {
        String shelfNum = etGoodsShelf.getText().toString().trim();
        try {
            List<ShelfItemBean> shelfItemBeanList = db.selector(ShelfItemBean.class).where("sid", "=", sid).and("user", "=", userId).and("sfid", "=", shelfNum).findAll();
            if (null!=shelfItemBeanList) {
                Log.i(TAG,"shelfItemBeanList:"+shelfItemBeanList.toString());
                db.delete(shelfItemBeanList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
