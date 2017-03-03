package com.suntown.smartscreen.price;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.suntown.smartscreen.CreamaActivity;
import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.base.BaseApplication;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.netUtils.RxManager;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.changePrice.ChangePriceActivity;
import com.suntown.smartscreen.price.chooseShop.ChooseShopActivity;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class UpdatePriceActivity extends BaseActivity<PricePresenter, PriceModel> implements PriceContract.View {

    private static final String TAG = "UpdatePriceActivity";
    private static final int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUESTCODE = 200;
    private static final int RESULTCODE = 300;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_serach)
    EditText etSerach;
    @BindView(R.id.rl_tag_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_data)
    RecyclerView rlData;
    private String shopName;
    private String shopId;
    private String userid;
    private String modUrl;
    ArrayList<GoodsInfo.RECORDBean> recordBeanList = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private DbManager db;
    int currentPosition;

    @Override
    protected int getContentView() {
        return R.layout.activity_update_price;
    }

    @Override
    protected void initView() {
        db = x.getDb(((BaseApplication) getApplication()).getDaoConfig());
        modUrl = SPUtils.getString(this, Constant.MODURL);
        if ("".equals(modUrl)) {
            modUrl = ApiConstant.BASE_URL;
        }
        rlData.setHasFixedSize(true);
        rlData.setLayoutManager(new LinearLayoutManager(this));
        goodsAdapter = new GoodsAdapter(R.layout.query_item, recordBeanList);
        rlData.setAdapter(goodsAdapter);
        goodsAdapter.setOnSwipeAdapterCallBack(position -> {
            String tinyip = recordBeanList.get(position).TINYIP;
            recordBeanList.remove(position);
            tinyipList.remove(tinyip);
            goodsAdapter.notifyDataSetChanged();
        });
        rlData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                GoodsInfo.RECORDBean recordBean = recordBeanList.get(i);
                ArrayList<GoodsInfo.RECORDBean> recordBeen = new ArrayList<>();
                recordBeen.add(recordBean);
                Intent intent = new Intent(UpdatePriceActivity.this, ChangePriceActivity.class);
                intent.putParcelableArrayListExtra(Constant.RECORD,recordBeen);
                currentPosition = i;
                startActivityForResult(intent,REQUESTCODE);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        shopId = SPUtils.getString(this, Constant.SHOP_ID);
        shopName = SPUtils.getString(this, Constant.SHOP_NAME);
        userid = SPUtils.getString(this, Constant.USER_ID);
        tvSearch.setText("".equals(shopName) ? "选择门店" : shopName);
        //监听回车键

        etSerach.setOnEditorActionListener((textView, i, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {
                if (!"".equals(shopName)) {
                    String goods = etSerach.getText().toString().trim();
                    mPresenter.getGoodsInfo(userid, shopId, goods, modUrl);
                } else {
                    Utils.showToast(UpdatePriceActivity.this, "请选择门店");
                }
                //表示本次操作完成 不再往外传播本事件
                etSerach.setText("");
                return true;
            }
            return false;
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_history, R.id.tv_saoyisao,R.id.tv_clear,R.id.tv_modification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                startActivity(new Intent(UpdatePriceActivity.this, ChooseShopActivity.class));
                break;
            case R.id.tv_history:
                //TODO 进入历史界面
                if (!"".equals(shopName)) {
                    Intent intent = new Intent(UpdatePriceActivity.this, HistortyActivity.class);
                    intent.putExtra(Constant.SHOP_NAME, shopName);
                    intent.putExtra(Constant.SHOP_ID, shopId);
                    startActivity(intent);
                }
                break;
            case R.id.tv_saoyisao:
                //TODO 打开相机
                if (shopName.equals("")) {
                    Utils.showToast(this, "请选择门店");
                    return;
                }
                startActivityForResult(new Intent(this, CreamaActivity.class), SCANNIN_GREQUEST_CODE);
                break;
            //删除
            case R.id.tv_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePriceActivity.this);
                builder.setMessage("确定删除吗？");
//                builder.setTitle("提示");
                builder.setPositiveButton("确认", (dialog, which) -> {
                    dialog.dismiss();
                    recordBeanList.clear();
                    tinyipList.clear();
                    rlTitle.setVisibility(View.GONE);
                    goodsAdapter.notifyDataSetChanged();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                builder.create().show();
                break;
            //修改价格
            case R.id.tv_modification:
                //TODO
                Intent intent = new Intent(UpdatePriceActivity.this, ChangePriceActivity.class);
                intent.putParcelableArrayListExtra(Constant.RECORD,recordBeanList);
                startActivity(intent);
                break;
        }
    }

    List<String> tinyipList = new ArrayList<>();
    /**
     * 获取数据成功返回
     *
     * @param goodsInfo
     */
    @Override
    public void getGoodsInfoSuccess(GoodsInfo goodsInfo) {
        Log.i(TAG, "goodsInfo:" + goodsInfo.toString());
        rlTitle.setVisibility(View.VISIBLE);
        List<GoodsInfo.RECORDBean> record = goodsInfo.RECORD;
            for (GoodsInfo.RECORDBean bean : record) {
                String tinyip = bean.TINYIP;
                if (!tinyipList.contains(tinyip)) {
                    tinyipList.add(tinyip);
                    recordBeanList.add(0,bean);
                    //TODO  bean 保存数据库
                    saveData(bean);
                }
            }
        goodsAdapter.notifyDataSetChanged();
    }

    private void saveData(GoodsInfo.RECORDBean bean) {
        //保存数据
        long times = System.currentTimeMillis();
        Person person = new Person(times,shopId,bean.BARCODE,shopName,bean.GNAME,bean.TINYIP,userid,modUrl,bean.CURPRICE, bean.COSTPRICE,bean.MEMPRICE);
            try {
                db.save(person);
            } catch (DbException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void showMsg(String msg) {
        Utils.showToast(this, msg);
//        Snackbar
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i(TAG, "resultStr:" + resultStr);
                    if ("".equals(resultStr)) {
                        Utils.showToast(this, "扫描错误，请重新扫描");
                        return;
                    }
                    etSerach.setText(resultStr);
                    Editable ea = etSerach.getText();
                    etSerach.setSelection(ea.length());
                    mPresenter.getGoodsInfo(userid, shopId, resultStr, modUrl);
                }
                break;
            case REQUESTCODE:
                if(resultCode == RESULTCODE){
                    List<GoodsInfo.RECORDBean> beanList = data.getParcelableArrayListExtra(Constant.RECORD);
                    Log.i(TAG,"beanList:"+beanList.toString());
                    for (GoodsInfo.RECORDBean recordBean : beanList) {
                        for (GoodsInfo.RECORDBean bean : recordBeanList) {
                            if (bean.TINYIP.equals(recordBean.TINYIP)) {
                                bean.CURPRICE = recordBean.CURPRICE;
                                bean.COSTPRICE = recordBean.COSTPRICE;
                                bean.MEMPRICE = recordBean.MEMPRICE;
                            }
                        }

                    }
                    goodsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
