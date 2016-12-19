package com.suntown.scannerproject.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.adapter.ShopListDapter;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.ShopListBean;
import com.suntown.scannerproject.bean.ShopResult;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.weight.LoadingDialog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zhikaizhang.indexview.PinyinComparator;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChooseShopActivity extends BaseActivity {

    private static final String TAG = "ChooseShopActivity";
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.tv_name)
    TextView tvName;
    private String serverip;
    private ShopListDapter shopListDapter;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shop);
        ButterKnife.bind(this);
        dialog = new LoadingDialog(this);
        dialog.show();
        init();
    }

    private void init() {
        tvName.setText("选择门店");
        serverip = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverip)) {
            serverip = ApiConstant.BASE_URL;
        }
        String ip = Constant.formatBASE_HOST(serverip);
        String usercode = SPUtils.getString(this, Constant.USER_CODE);
        Log.i(TAG, "ip:" + ip + ":usercode:" + usercode);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.getShopList(usercode).compose(RxSchedulers.io_main()).subscribe(string -> {
            String xml = string.toString().trim();
            xml = xml.replace("<ns:GetShopListResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            xml = xml.replace("</ns:return></ns:GetShopListResponse>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
            Log.i(TAG, "xml:" + xml);
            pullXMl(xml);
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
            Utils.showToast(ChooseShopActivity.this, "网络连接异常，请重试");
        });
    }

    private void pullXMl(String xml) {
        XStream xstream = new XStream(new DomDriver()); //注意：不是new Xstream(); 否则报错：
        xstream.processAnnotations(ShopResult.class);
        ShopResult shopResultList = (ShopResult) xstream.fromXML(xml);
        List<ShopListBean> shopListBean = shopResultList.shopListBean;
        Collections.sort(shopListBean, new PinyinComparator<ShopListBean>() {
            @Override
            public int compare(ShopListBean s1, ShopListBean s2) {
                return compare(s1.SName, s2.SName);
            }
        });
        Log.i(TAG, "shopResult:" + shopResultList.toString());
        shopListDapter = new ShopListDapter(this, shopListBean);
        dialog.dismiss();
        lvList.setAdapter(shopListDapter);
        lvList.setOnItemClickListener((adapterView, view, i, l) -> {
            ShopListBean shopListBean1 = shopListBean.get(i);
            String sid = shopListBean1.SID;
            String sName = shopListBean1.SName;
            Log.i(TAG, "sid:" + sid+",sName:"+sName);
            SPUtils.put(ChooseShopActivity.this, Constant.SID, sid);
            SPUtils.put(ChooseShopActivity.this, Constant.SHOP_NAME, sName);
            finish();
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
