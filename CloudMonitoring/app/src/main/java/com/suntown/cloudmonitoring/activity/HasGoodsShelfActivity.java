package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.adapter.RecyclerMainAdapter;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.InfoBean;
import com.suntown.cloudmonitoring.bean.Item2;
import com.suntown.cloudmonitoring.bean.ShelfInfoBean;
import com.suntown.cloudmonitoring.bean.ShelfItemBean;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.utils.CommitShelfData;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.FormatString;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.ShelfXml2Json;
import com.suntown.cloudmonitoring.utils.SpaceItemDecoration;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.WrapContentLinearLayoutManager;
import com.suntown.cloudmonitoring.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import okhttp3.ResponseBody;

public class HasGoodsShelfActivity extends BaseActivity {

    private static final String TAG = "HasGoodsShelfActivity";
    private static final int ON_SCANNIN_GREQUEST_CODE = 1;
    private static final int MORE_SCANNIN_GREQUEST_CODE = 2;
    private static final int RESULT = 4;
    private static final int SHELF_SCANNER = 3;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.et_goods_shelf)
    EditText etGoodsShelf;
    @BindView(R.id.rl_main)
    RecyclerView rlMain;
    @BindView(R.id.tb_long_scanner)
    ToggleButton tbLongScanner;
    @BindView(R.id.tb_one_scanner)
    ToggleButton tbOneScanner;
    @BindView(R.id.et_num)
    EditText etNum;
    private OkHttpClient client;
    private String sid;
    private String userId;
    private String serverIp;
    public static List<Integer> indexList = new ArrayList<>();
    public static List<ShelfItemBean> beanList;
    public static Map<Integer, List<ShelfItemBean>> listMap = new HashMap<>();
    public static boolean isOnScanner;
    public static boolean isMoreScanner;
    private RecyclerMainAdapter mainAdapter;
    public int currentParentPosition;
    public int currentChildPosition;
    private InputMethodManager manager;
    private String goodsShelf;
    private boolean isConstant = false;
    private int Row;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_goods_shelf);
        ButterKnife.bind(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sid = getIntent().getStringExtra(Constant.SID);
        String sname = getIntent().getStringExtra(Constant.SNAME);
        tvShopName.setText(sname);
        client = new OkHttpClient();
        queryService();
        initData(10);
        mainAdapter = new RecyclerMainAdapter(this, indexList, listMap);
        rlMain.setLayoutManager(new WrapContentLinearLayoutManager(this));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addItemDecoration(new SpaceItemDecoration(dimensionPixelSize));
        rlMain.setAdapter(mainAdapter);

        etGoodsShelf.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                goodsShelf = etGoodsShelf.getText().toString().trim();
                if(!"".equals(goodsShelf)){
                    if (!Utils.isFastClick()) {
                        queryShelfInfo(goodsShelf);
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
                String goodsShelf = etGoodsShelf.getText().toString().trim();
                List<ShelfItemBean> shelfItemBeanList = listMap.get(group);
                ShelfItemBean shelfItemBean = shelfItemBeanList.get(position);
                boolean isClick = shelfItemBean.isClick;
                String gname = shelfItemBean.gname;
                String tag = shelfItemBean.tag;
                String barcode = shelfItemBean.barcode;
                //点击删除
                if (isClick) {
                    int size = shelfItemBeanList.size();
                    if (size > 3 && position==size-1) {
                        shelfItemBeanList.remove(position);
                        mainAdapter.notifyDataSetChanged();
                    } else {
                        Log.i(TAG, "shelfItemBean:" + shelfItemBean.toString());
                        shelfItemBean.barcode = "";
                        shelfItemBean.tag = "";
                        shelfItemBean.gname = "";
                        mainAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (!"".equals(gname) && !"".equals(tag) && !"".equals(barcode) && !isOnScanner && !isMoreScanner) {
                        //TODO  跳转到标签详情
                        InfoBean infoBean = new InfoBean(sid, tag);
                        Intent intent = new Intent(HasGoodsShelfActivity.this, HasGoodsDetialActivity.class);
                        intent.putExtra(Constant.INFO_BEAN, infoBean);
                        intent.putExtra(Constant.BARCODE, barcode);
                        intent.putExtra(Constant.NAME, gname);
                        startActivityForResult(intent,RESULT);
//                        startActivity(intent);
                    } else {
                        if ("".equals(goodsShelf)) {
                            Utils.showToast(HasGoodsShelfActivity.this, "请选择货架");
                            return;
                        }
                        if (isOnScanner == false && isMoreScanner == false) {
                            Utils.showToast(HasGoodsShelfActivity.this, "请选择扫描方式");
                            return;
                        }
                        //TODO 跳转到扫码界面
                        if (isOnScanner) {
                            Intent intent = new Intent(HasGoodsShelfActivity.this, CreamaActivity.class);
                            intent.putExtra(Constant.IS_ON_SCANN, true);
                            intent.putExtra(Constant.SCANN_TYPE, 1);
                            startActivityForResult(intent, ON_SCANNIN_GREQUEST_CODE);
                        } else {
                            //多扫
                            Intent intent = new Intent(HasGoodsShelfActivity.this, CreamaActivity.class);
                            intent.putExtra(Constant.IS_ON_SCANN, false);
                            intent.putExtra(Constant.SCANN_TYPE, 2);
                            startActivityForResult(intent, MORE_SCANNIN_GREQUEST_CODE);
                        }
                    }
                }
            }

            //子条目删除条目
            @Override
            public void onChildItemDeleteClick(View view, int group, int position) {
                List<ShelfItemBean> itemBeanList = listMap.get(group);
                int size = itemBeanList.size();
                if (size > 3) {
                    itemBeanList.remove(position);
                    mainAdapter.notifyDataSetChanged();
                }
            }

            //条目添加
            @Override
            public void onAddClick(View view, int group) {
                Log.i(TAG, "" + group);
                List<ShelfItemBean> itemBeanList = listMap.get(group);
                ShelfItemBean e = new ShelfItemBean("", "", "");
                ShelfItemBean shelfItemBean = itemBeanList.get(0);
                e.isClick = shelfItemBean.isClick;
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


    private void queryService() {
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
    }

    /**
     * 查询货架信息
     *
     * @param goodsShelf
     */
    private void queryShelfInfo(String goodsShelf) {
        Log.i(TAG, serverIp + "  " + sid + "  " + goodsShelf);
        etGoodsShelf.setText(goodsShelf);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.SFID, goodsShelf).
                add(Constant.SID, sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIp) + "/axis2/services/STPdaService2/GetShelfDataForPad")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String xml = response.body().string();
                        xml = xml.replace("<ns:GetShelfDataForPadResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                        xml = xml.replace("</ns:return></ns:GetShelfDataForPadResponse>", "");
                        xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                        Log.i(TAG, xml);
                        ShelfInfoBean shelfInfoBean = new ShelfXml2Json(xml).PullXml();
                        List<ShelfInfoBean.Shelf_Allocation> shelf_allocations = shelfInfoBean.Shelf_Allocations;
                        ShelfInfoBean.ShelfBean shelfBean1 = shelfInfoBean.shelfBean;
                        Log.i(TAG, "shelfInfoBean" + shelfInfoBean.toString());
                        for (Integer integer : indexList) {
                            List<ShelfItemBean> shelfItemBeanList = listMap.get(integer);
                            for (ShelfItemBean shelfItemBean : shelfItemBeanList) {
                                shelfItemBean.barcode = "";
                                shelfItemBean.gname = "";
                                shelfItemBean.tag = "";
                            }
                        }
                        if (null == shelfBean1) {
                            Utils.showToast(HasGoodsShelfActivity.this, "没有查询到该货架数据");
                        } else {
                            String exist = shelfBean1.Exist;
                                if (null != exist) {
                                    Utils.showToast(HasGoodsShelfActivity.this, "此货架在其他门店已经存在");
                                    runOnUiThread(() -> etGoodsShelf.setText(""));
                                } else if (null == shelf_allocations) {
                                    Utils.showToast(HasGoodsShelfActivity.this, "没有查询到该货架数据");
                                } else {
                                    SPUtils.put(HasGoodsShelfActivity.this,Constant.SHELFGOODS,goodsShelf);
                                //设置数据
                                    int num= 0;
                                Log.i(TAG, "长度:" + shelf_allocations.size());
                                ShelfInfoBean.ShelfBean shelfBean = shelfInfoBean.shelfBean;
                                for (ShelfInfoBean.Shelf_Allocation shelf_allocation : shelf_allocations) {
                                    int colNumber = Integer.parseInt(shelf_allocation.ColNumber);
                                    int rowNumber = Integer.parseInt(shelf_allocation.RowNumber);
                                    String tinyip = shelf_allocation.Tinyip;
                                    String barcode = shelf_allocation.Barcode;
                                    String gname = shelf_allocation.GName;
                                    num = num>rowNumber?num:rowNumber;
//                                List<ShelfItemBean> shelfItemBeen = listMap.get(10 - rowNumber);
                                    int size = indexList.size();
                                    if (rowNumber<=size) {
                                        Integer integer = indexList.get(size - rowNumber);
                                        List<ShelfItemBean> shelfItemBeen = listMap.get(integer);
                                        int i = colNumber - 1;
                                        if (shelfItemBeen.size() >i) {
                                            shelfItemBeen.add(colNumber - 1, new ShelfItemBean(tinyip, barcode, gname));
                                            shelfItemBeen.remove(colNumber);
                                        } else{
                                            for (int i1 = shelfItemBeen.size(); i1 < colNumber-1; i1++) {
                                                shelfItemBeen.add(new ShelfItemBean("", "", ""));
                                            }
                                            shelfItemBeen.add(colNumber - 1, new ShelfItemBean(tinyip, barcode, gname));
                                        }
                                    }
                                }
                                    Row = Row>num?Row:num;
                            }
                        }
                        runOnUiThread(() -> {
                            initRow(Row);
                            mainAdapter.notifyDataSetChanged();
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
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.iv_back, R.id.tb_long_scanner, R.id.tb_one_scanner, R.id.tv_go, R.id.tv_confirm, R.id.tv_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                showDialog();
                break;
            //多扫
            case R.id.tb_long_scanner:
                if (isOnScanner) {
                    isOnScanner = !isOnScanner;
                    isMoreScanner = !isMoreScanner;
                } else {
                    isMoreScanner = !isMoreScanner;
                }
                tbOneScanner.setChecked(isOnScanner);
                tbLongScanner.setChecked(isMoreScanner);
                break;
            //单扫
            case R.id.tb_one_scanner:
                if (isMoreScanner) {
                    isMoreScanner = !isMoreScanner;
                    isOnScanner = !isOnScanner;
                } else {
                    isOnScanner = !isOnScanner;
                }
                tbOneScanner.setChecked(isOnScanner);
                tbLongScanner.setChecked(isMoreScanner);
                break;
            //定位条目
            case R.id.tv_go:
                String lineNumStr = etNum.getText().toString().trim();
                if (!"".equals(lineNumStr)) {
                    int i1 = Integer.parseInt(lineNumStr);
                    goodsShelf = etGoodsShelf.getText().toString().trim();
                    if ("".equals(goodsShelf)) {
                        Utils.showToast(this, "请输入货架编号");
                        return;
                    }
                    if (i1 > 0 && i1 < indexList.size()) {
                        Log.i(TAG,"Row:"+Row);
                        if (i1<Row){
                            i1=Row;
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
                        Row=i1;
                        Log.i(TAG,"Row:"+Row);
                        Log.i(TAG, "indexList-" + indexList.toString());
                        int y = 1;
                        for (int i = indexList.size(); i < i1; i++) {
                            y++;
                            Integer max = Collections.max(indexList);
//                            Integer integer = indexList.get(indexList.size() - 1);
                            indexList.add(0, max + y);
                            beanList = new ArrayList<>();
                            for (int j = 0; j < 3; j++) {
                                ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                                itemBean.isClick = false;
                                beanList.add(itemBean);
                            }
                            listMap.put(max + y, beanList);
                        }
                    }
                    queryShelfInfo(goodsShelf);
                    mainAdapter.notifyDataSetChanged();
                    Log.i(TAG, "indexList-" + indexList.size());
                    Log.i(TAG, "indexList-" + indexList.toString());
                }else{
                    goodsShelf = etGoodsShelf.getText().toString().trim();
                    if (!"".equals(goodsShelf)){
                        queryShelfInfo(goodsShelf);
                    }
                }
                break;
            case R.id.tv_confirm:
                //TODO 非空判断，提交数据
                String goodsShelf = etGoodsShelf.getText().toString().trim();
                if ("".equals(goodsShelf)) {
                    Utils.showToast(this, "货架不能为空");
                    return;
                }
                //将 集合转换成xml
                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String szImei = TelephonyMgr.getDeviceId();
//                ShelfItemBean shelfItemBean = listMap.get(9).get(0);
//                Log.i(TAG,"shelfItemBean--"+shelfItemBean.toString());
                goodsShelf = goodsShelf.toUpperCase();
                if (goodsShelf.length()==15) {
                    Log.i(TAG, "indexList--" + indexList.toString());
                    Log.i(TAG, "listMap--" + listMap.toString());
                    String xml = CommitShelfData.productData(indexList, listMap, goodsShelf, sid, szImei);
                    Log.i(TAG, "xml--" + xml);
                    subServer(xml);
                }else{
                    Utils.showToast(this,"货架长度必须等于15");
                }

                break;
            case R.id.tv_goods:
                //TODO 扫描形成货架
                Intent intent = new Intent(HasGoodsShelfActivity.this, CreamaActivity.class);
                intent.putExtra(Constant.IS_ON_SCANN, true);
                startActivityForResult(intent, SHELF_SCANNER);
                break;
        }
    }

    /**
     * 限定行数
     * @param i1
     */
    private void initRow(int i1) {
        if (i1 < Row) {
            i1 = Row;
        }
        Row=0;
        if (i1 > 0 && i1 < indexList.size()) {
            ArrayList<Integer> integers = new ArrayList<>();
            for (int i = indexList.size() - 1; i > indexList.size() -1 - i1; i--) {
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
                for (int j = 0; j < 3; j++) {
                    ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                    beanList.add(itemBean);
                }
                listMap.put(max + y, beanList);
            }
        }
        mainAdapter.notifyDataSetChanged();
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
                    Log.i(TAG, "xml:" + xml);
                    if ("0".equals(xml)) {
                        Utils.showToast(HasGoodsShelfActivity.this, "数据提交成功");
//                        runOnUiThread(() -> {
//                            for (Integer integer : indexList) {
//                                List<ShelfItemBean> shelfItemBeanList = listMap.get(integer);
//                                for (ShelfItemBean shelfItemBean : shelfItemBeanList) {
//                                    shelfItemBean.tag="";
//                                    shelfItemBean.barcode="";
//                                    shelfItemBean.gname="";
//                                }
//                            }
//                            mainAdapter.notifyDataSetChanged();
//                        });
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
        String self = etGoodsShelf.getText().toString();
        if ("".equals(self)){
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您还没有提交数据，是否确认退出");
            builder.setNegativeButton("确定", (dialogInterface, i) -> {
                finish();
                dialogInterface.dismiss();
            });
            builder.setPositiveButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        }
    }


    /**
     * 扫码返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //单扫返回数据
            case ON_SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    Log.i(TAG,"esultStr:"+resultStr);
                    ShelfItemBean shelfItemBean = listMap.get(currentParentPosition).get(currentChildPosition);
                    if (resultStr.startsWith(".")) {
                        //                                      16
                        //转成标签 Integer.parseInt(String s, int radix)
                        String tinyip = FormatString.fromTinyip(resultStr);
                        resultStr = tinyip;
                    }
                    if (resultStr.contains(".")) {
                        isConstant=false;
                        for (Integer integer : indexList) {
                            List<ShelfItemBean> shelfItemBeanList = listMap.get(integer);
                            for (ShelfItemBean shelfItemBean1 : shelfItemBeanList) {
                                if (resultStr.equals(shelfItemBean1.tag)) {
                                    isConstant = true;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setMessage("该标签已存在该货架，是否替换?");
                                    String finalResultStr = resultStr;
                                    builder.setPositiveButton("确认", (dialog, which) -> {
                                        isConstant = false;
                                        shelfItemBean1.tag="";
                                        shelfItemBean.tag = finalResultStr;
                                        mainAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    });
                                    builder.setNegativeButton("取消", (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                                    builder.create().show();
                                    break;
                                }
                            }
                        }
                        if (!isConstant){
                            shelfItemBean.tag = resultStr;
                        }
                    } else {
                        shelfItemBean.barcode = resultStr;
                    }
                    if (!"".equals(shelfItemBean.barcode)) {
                        //TODO 查询商品信息
                        getGoodsName(shelfItemBean.barcode,currentChildPosition);
                    }
                    mainAdapter.notifyDataSetChanged();
                }
                break;
            //多扫返回数据
            case MORE_SCANNIN_GREQUEST_CODE:
                if (resultCode == 200) {
                    ArrayList<String> dataExtraList = data.getStringArrayListExtra(Constant.RESULT_CODE);
                    //设置数据
                    List<ShelfItemBean> shelfItemBeanList = listMap.get(currentParentPosition);
                    Log.i(TAG, "dataExtraList" + dataExtraList.toString());
                    for (int i = 0; i < dataExtraList.size(); i++) {
                        int position = currentChildPosition +i;
                        String str = dataExtraList.get(i);
                        Log.i(TAG, position + "    " + shelfItemBeanList.size());
                        if (position >= shelfItemBeanList.size()) {
                            if (str.startsWith(".")) {
                                //                                      16
                                //转成标签 Integer.parseInt(String s, int radix)
                                String tinyip = FormatString.fromTinyip(str);
                                str = tinyip;
                            }
                            if (str.contains(".")) {
                                isConstant=false;
                                for (Integer integer : indexList) {
                                    List<ShelfItemBean> shelfItemBeanList1 = listMap.get(integer);
                                    for (ShelfItemBean shelfItemBean1 : shelfItemBeanList1) {
                                        if (str.equals(shelfItemBean1.tag)) {
                                            isConstant = true;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                            builder.setMessage("该标签已存在该货架，是否替换?");
                                            String finalStr = str;
                                            builder.setPositiveButton("确认", (dialog, which) -> {
                                                shelfItemBean1.tag="";
                                                isConstant = false;
                                                shelfItemBeanList.add(new ShelfItemBean(finalStr, "", ""));
                                                mainAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            });
                                            builder.setNegativeButton("取消", (dialog, which) -> {
                                                dialog.dismiss();
                                            });
                                            builder.create().show();
                                            break;
                                        }
                                    }
                                }
                                if (!isConstant){
                                    shelfItemBeanList.add(new ShelfItemBean(str, "", ""));
                                }
                            } else {
                                shelfItemBeanList.add(new ShelfItemBean("", str, ""));
                            }
                        } else {
                            ShelfItemBean shelfItemBean = shelfItemBeanList.get(position);
                            if (str.startsWith(".")) {
                                //                                      16
                                //转成标签 Integer.parseInt(String s, int radix)
                                String tinyip = FormatString.fromTinyip(str);
                                str = tinyip;
                            }
                            if (str.contains(".")) {
                                isConstant=false;
                                for (Integer integer : indexList) {
                                    List<ShelfItemBean> shelfItemBeanList1 = listMap.get(integer);
                                    for (ShelfItemBean shelfItemBean1 : shelfItemBeanList1) {
                                        if (str.equals(shelfItemBean1.tag)) {
                                            isConstant = true;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                            builder.setMessage("该标签已存在该货架，是否替换?");
                                            String finalStr1 = str;
                                            builder.setPositiveButton("确认", (dialog, which) -> {
                                                shelfItemBean1.tag="";
                                                shelfItemBean.tag = finalStr1;
                                                isConstant=false;
                                                mainAdapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            });
                                            builder.setNegativeButton("取消", (dialog, which) -> {
                                                dialog.dismiss();
                                            });
                                            builder.create().show();
                                            break;
                                        }
                                    }
                                }
                                if (!isConstant){
                                    shelfItemBean.tag = str;
                                }
                            } else {
                                shelfItemBean.barcode = str;
                            }
                        }
                    }
                    //TODO 循环获取 名称
                    Log.i(TAG, "shelfItemBeanList--" + shelfItemBeanList.toString());
                    for (int i = 0; i < shelfItemBeanList.size(); i++) {
                        ShelfItemBean shelfItemBean = shelfItemBeanList.get(i);
                        String barcode = shelfItemBean.barcode;
                        String gname = shelfItemBean.gname;
                        if (!"".equals(barcode)) {
                            //TODO 查询商品信息
//                            Log.i(TAG,"i"+i+",currentChildPosition:"+currentChildPosition);
//                            int i1 = currentChildPosition + i;
                            getGoodsName(barcode, i);
                        }
                    }
                    mainAdapter.notifyDataSetChanged();
                }
                break;
            case SHELF_SCANNER:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(Constant.RESULT_CODE);
                    if (resultStr.contains(".")) {
                        //TODO 查询货架 queryShelfInfo(goodsShelf);
                        queryData(resultStr);
                    }  else if (resultStr.length()==15){
                        queryShelfInfo(resultStr);
                    }else {
                        Utils.showToast(this, "请输入正确的货架信息");
                    }
                }
                break;
            case RESULT:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra(Constant.NAME);
                    String barcode = data.getStringExtra(Constant.BARCODE);
                    String tinyip = data.getStringExtra(Constant.TINYIP);
                    Log.i(TAG,"tinyip:"+tinyip+",barcode:"+barcode+" ,name:"+name);
                    for (Integer integer : indexList) {
                        List<ShelfItemBean> shelfItemBeanList1 = listMap.get(integer);
                        for (ShelfItemBean itemBean : shelfItemBeanList1) {
                            if (tinyip.equals(itemBean.tag)) {
                                itemBean.tag="";
                                }
                            }
                        }
                    ShelfItemBean shelfItemBean = listMap.get(currentParentPosition).get(currentChildPosition);
                    shelfItemBean.tag=tinyip;
                    shelfItemBean.barcode=barcode;
                    shelfItemBean.gname=name;
                    mainAdapter.notifyDataSetChanged();
                }
            break;
        }
    }

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
                    Log.i(TAG, "xml:" + xml);
                    xml = xml.replace("</ns:return></ns:GetLabStatus2Response>", "");
                    xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                    Log.i(TAG, "xml:" + xml);
                    try {
                        ShopXmlBean shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                        Log.i(TAG, "shopXmlBean:" + shopXmlBean);
                        runOnUiThread(() -> {
                            String sfid = shopXmlBean.SFID;
                            if (null != sfid) {
                                //TODO
                                etGoodsShelf.setText(sfid);
                                queryShelfInfo(sfid);
                            } else {
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
    private void getGoodsName(String barcode,int i ) {
        Log.i(TAG, "barcode=" + barcode + ",  sid" + sid );
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
                            Log.i(TAG, "shelfItemBeanList --" + shelfItemBeanList.toString());
                            Log.i(TAG, "shelfItemBeanList --" + shelfItemBeanList.size());
                            ShelfItemBean shelfItemBean = shelfItemBeanList.get(i);
                            shelfItemBean.gname = gName;
                            runOnUiThread(() -> {
                                Log.i(TAG, "查询到数据：" + shelfItemBean.tag + " " + shelfItemBean.barcode + " " + shelfItemBean.gname);
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
            for (int j = 0; j < 3; j++) {
//                beanList.add(new ShelfItemBean(j + "标签", j + "条码", j + "名称"));
                ShelfItemBean itemBean = new ShelfItemBean("", "", "");
                itemBean.isClick = false;
                beanList.add(itemBean);
            }
            listMap.put(i, beanList);
        }
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
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

//    /**
//     * 返回键按下
//     *
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            showDialog();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
