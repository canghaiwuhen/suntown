package com.suntown.scannerproject.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.Item1;
import com.suntown.scannerproject.scanner.HasGoodsShelfActivity;
import com.suntown.scannerproject.scanner.NoGoodsShelfActivity;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScannerActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    private Item1 item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        item = getIntent().getParcelableExtra(Constant.ITEM);
        tvName.setText(item.sname);
    }

    @OnClick({R.id.iv_back, R.id.tv_has_goods, R.id.tv_no_goods})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_has_goods:
                if (!Utils.isFastClick()) {
                    intent = new Intent(this, HasGoodsShelfActivity.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
            case R.id.tv_no_goods:
                if (!Utils.isFastClick()) {
                    intent = new Intent(this, NoGoodsShelfActivity.class);
                    intent.putExtra(Constant.ITEM, item);
                    startActivity(intent);
                }
                break;
        }
    }
}
