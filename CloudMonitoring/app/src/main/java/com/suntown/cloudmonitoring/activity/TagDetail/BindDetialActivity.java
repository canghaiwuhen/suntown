package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.TagPhotoBean;
import com.suntown.cloudmonitoring.bean.photoBean.ShelfBean;
import com.suntown.cloudmonitoring.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindDetialActivity extends BaseActivity {

    private static final String TAG = "BindDetialActivity";
    @BindView(R.id.tv_sid)
    TextView tvSid;
    @BindView(R.id.tv_goods_self_num)
    TextView tvGoodsSelfNum;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_code)
    TextView tvGoodsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_detial);
        ButterKnife.bind(this);
        ShelfBean shelBean = getIntent().getParcelableExtra(Constant.SHELFGOODS);
        Log.i(TAG,shelBean.toString());
        ShelfBean.EslWarnBean.ShelfGoodsBean shelfGoods = shelBean.eslWarn.shelfGoods;
        if (null!=shelfGoods){
            tvSid.setText(shelfGoods.sid);
            tvGoodsSelfNum.setText(shelfGoods.aid);
            tvGoodsName.setText(shelfGoods.gname);
            tvGoodsCode.setText(shelfGoods.barcode);
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
