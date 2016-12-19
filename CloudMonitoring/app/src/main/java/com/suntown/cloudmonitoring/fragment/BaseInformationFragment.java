package com.suntown.cloudmonitoring.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.GoodsDetialActivity;
import com.suntown.cloudmonitoring.activity.TagDetialActivity;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.bean.Person;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/12.
 */
public class BaseInformationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BaseInformationFragment";
    private View inflate;
    private String serverIP;
    private String userId;
    private String sid;
    private String tinyip;
    private TextView shopName;
    private TextView shopId;
    private TextView address;
    private TextView size;
    private TextView code;
    private TextView name;
    private TextView goods;
    private TextView price;
    private TextView oldPrice;
    private TextView changePrice;
    private TextView moveTime;
    private TextView registerTime;
    private TagDetialBean tagDetialBean;
    private TextView tvBattry;
    private TextView tvOff;
    private TextView tvType;
    private TextView tvSoft;
    private TextView tvPriceType;
    private TextView tvPriceVip;
    private TextView tvSignal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.tag_detial_item1, container, false);
        initView();

//        TagDetialBean tagDetialBean = ((TagDetialActivity) getActivity()).tagDetialBean;
//        setData(tagDetialBean);
        getData();
        LinearLayout llGoodsInfo = (LinearLayout) inflate.findViewById(R.id.ll_goods_info);
        llGoodsInfo.setOnClickListener(this);
//        if (null!=tagDetialBean){
//            llGoodsInfo.setOnClickListener(view -> {
//                Utils.showToast(getActivity(),"点击了");
//                Intent intent = new Intent(getActivity(), GoodsDetialActivity.class);
//                Person person = new Person();
//                person.barcode=tagDetialBean.barcode;
//                person.sid=tagDetialBean.sid;
//                intent.putExtra(Constant.PERSON,person);
//                startActivity(intent);
//            });
//        }
        return inflate;
    }


    //获取标签详情
    private void getData() {
        Map<String, String> params = new HashMap<>();
        serverIP = SPUtils.getString(getActivity(), Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            serverIP = SPUtils.getString(getActivity(), Constant.SERVER_IP);
            userId = SPUtils.getString(getActivity(), Constant.USER_ID);
        } else {
            userId = SPUtils.getString(getActivity(), Constant.SUB_USER_ID);
        }
        Log.i(TAG, "serverIP-" + serverIP+",userId:"+userId+",tinyip:"+tinyip+",sid:"+sid);
        params.put(Constant.USER_ID, userId);
        params.put(Constant.TINYIP, tinyip);
        params.put(Constant.SID, sid);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(serverIP).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<TagDetialBean> tagDetial = service.getTAGDetial(params);
        tagDetial.compose(RxSchedulers.io_main()).subscribe(tagDetialBean -> {
            this.tagDetialBean = tagDetialBean;
            String activityDate = tagDetialBean.activityDate;
            Log.i(TAG, "tagDetialBean--" + tagDetialBean.toString());
            setData(tagDetialBean);
        }, throwable -> {

        });
    }

    private void setData(TagDetialBean tagDetialBean) {
        shopName.setText(tagDetialBean.sname);
        shopId.setText(tagDetialBean.sid);
        address.setText(tagDetialBean.origin);
//        size.setText(tagDetialBean.);
        code.setText(tagDetialBean.barcode);
        name.setText(tagDetialBean.gname);
        goods.setText(tagDetialBean.aid);
        price.setText(tagDetialBean.uptprice);
        oldPrice.setText(tagDetialBean.oriprice + "");
        tvPriceType.setText(tagDetialBean.pricetype);
        tvPriceVip.setText(tagDetialBean.memprice);
        changePrice.setText(tagDetialBean.priceAddDate);
        moveTime.setText(tagDetialBean.activityDate);
        registerTime.setText(tagDetialBean.lastdate);
        tvBattry.setText(tagDetialBean.battery);
        tvOff.setText(tagDetialBean.poweroff);
        tvType.setText(tagDetialBean.hwtype);
        tvSoft.setText(tagDetialBean.swversion);
        tvSignal.setText(tagDetialBean.lqi);

    }

    private void initView() {
        sid = ((TagDetialActivity) getActivity()).sid;
        tinyip = ((TagDetialActivity) getActivity()).tinyip;
        shopName = (TextView) inflate.findViewById(R.id.tv_shop_name);
        shopId = (TextView) inflate.findViewById(R.id.tv_shop_id_name);
        address = (TextView) inflate.findViewById(R.id.tv_address);
        //库存  条码  名称  货位
        size = (TextView) inflate.findViewById(R.id.tv_size);
        code = (TextView) inflate.findViewById(R.id.tv_code);
        name = (TextView) inflate.findViewById(R.id.tv_name);
        goods = (TextView) inflate.findViewById(R.id.tv_goods);
        price = (TextView) inflate.findViewById(R.id.tv_price);
        oldPrice = (TextView) inflate.findViewById(R.id.tv_oldprice);
        changePrice = (TextView) inflate.findViewById(R.id.tv_change_price);
        moveTime = (TextView) inflate.findViewById(R.id.tv_last_move_time);
        registerTime = (TextView) inflate.findViewById(R.id.tv_last_register);
        tvBattry = (TextView) inflate.findViewById(R.id.tv_battry);
        tvOff = (TextView) inflate.findViewById(R.id.tv_off);
        tvType = (TextView) inflate.findViewById(R.id.tv_type);
        tvSoft = (TextView) inflate.findViewById(R.id.tv_soft);
        tvPriceType = (TextView) inflate.findViewById(R.id.tv_price_type);
        tvPriceVip = (TextView) inflate.findViewById(R.id.tv_price_vip);
        tvSignal = (TextView) inflate.findViewById(R.id.tv_signal);

    }

    @Override
    public void onClick(View view) {
        if (null != tagDetialBean) {
            Intent intent = new Intent(getActivity(), GoodsDetialActivity.class);
            Person person = new Person();
            person.barcode = tagDetialBean.barcode;
            person.sid = tagDetialBean.sid;
            intent.putExtra(Constant.PERSON, person);
            startActivity(intent);
        }
    }
}
