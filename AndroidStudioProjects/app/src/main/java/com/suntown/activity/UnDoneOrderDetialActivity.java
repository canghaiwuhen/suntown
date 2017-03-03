package com.suntown.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suntown.R;
import com.suntown.bean.WaitConfirmBean;
import com.suntown.utils.Constant;
import com.suntown.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnDoneOrderDetialActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.iv_goods_photo)
    ImageView ivGoodsPhoto;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_tag_name)
    TextView tvTagName;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private WaitConfirmBean.RECORDBean recordBean;
    private WaitConfirmBean.RECORDBean.ORDERINFOBean orderinfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_done_order);
        ButterKnife.bind(this);
        recordBean = getIntent().getParcelableExtra("recordBean");
        init();
    }

    private void init() {
        List<WaitConfirmBean.RECORDBean.ORDERINFOBean> orderinfo = recordBean.getORDERINFO();
        orderinfoBean = orderinfo.get(0);
        String[] address = recordBean.getADDRESS().split("\\/");
//        String name = address[0];
////        String number = address[1];
//        String addres = address[2];
        String name = address.length > 0 ? address[0] : "未选择";
//        String number = address[1];
        String addres = address.length > 2 ? address[2] : "未选择";
        tvName.setText(name);
        tvAddress.setText(addres);
        tvOrderNumber.setText(recordBean.getFORMNO());
        tvTagName.setText(orderinfoBean.getTINYIP());
//        Picasso.with(context).load(UsageExampleListViewAdapter.eatFoodyImages[0]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.future_studio_launcher).noFade().into(imageViewFade);
//        ivGoodsPhoto.setImageResource(orderinfoBean.getIMGPATH());
        Picasso.with(this).load(Constant.formatImage(orderinfoBean.getIMGPATH())).error(R.drawable.no_photo).into(ivGoodsPhoto);
        tvGoodsName.setText(orderinfoBean.getGNAME());
        tvPrice.setText("￥" + recordBean.getMONEY());
        tvNum.setText("×"+orderinfoBean.getNUM());
    }

    @OnClick({R.id.iv_back, R.id.tv_confirm, R.id.iv_goods_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                showDialog();
                break;
            case R.id.iv_goods_photo:
                Intent inten = new Intent(UnDoneOrderDetialActivity.this, PicActivity.class);
                inten.putExtra("picName", Constant.formatImage(orderinfoBean.getIMGPATH()));
                startActivity(inten);
                break;
        }
    }

    private void showDialog() {
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请确认收货");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确认", (dialog, which) -> {
            dialog.dismiss();
            //TODO 向服务器发起请求
        });
    }


}
