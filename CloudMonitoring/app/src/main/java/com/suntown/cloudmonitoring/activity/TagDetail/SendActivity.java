package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.photoBean.SendBean;
import com.suntown.cloudmonitoring.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActivity extends BaseActivity {


    @BindView(R.id.tv_end_type)
    TextView tvEndType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        SendBean sendBean = getIntent().getParcelableExtra(Constant.SENDBEAN);
        tvEndType.setText("发送成功");
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
