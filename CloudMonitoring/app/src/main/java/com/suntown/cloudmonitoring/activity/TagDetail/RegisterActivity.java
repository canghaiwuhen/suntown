package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.photoBean.ApEslBean;
import com.suntown.cloudmonitoring.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_apip)
    TextView tvApip;
    @BindView(R.id.tv_appddr)
    TextView tvAppddr;
    @BindView(R.id.tv_soft_version)
    TextView tvSoftVersion;
    @BindView(R.id.tv_tag_type)
    TextView tvTagType;
    @BindView(R.id.tv_last_login_time)
    TextView tvLastLoginTime;
    @BindView(R.id.tv_last_move_time)
    TextView tvLastMoveTime;
    @BindView(R.id.tv_rssi_f0)
    TextView tvRssiF0;
    @BindView(R.id.tv_rssi_f1)
    TextView tvRssiF1;
    @BindView(R.id.tv_is_off)
    TextView tvIsOff;
    @BindView(R.id.tv_off_time)
    TextView tvOffTime;
    private String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ApEslBean apEslBean = getIntent().getParcelableExtra(Constant.ESL);
        if (null!=apEslBean) {
            ApEslBean.EslWarnBean eslWarn = apEslBean.eslWarn;
            ApEslBean.EslWarnBean.ApBean ap = eslWarn.ap;
            ApEslBean.EslWarnBean.EslBean esl = eslWarn.esl;
            Log.i(TAG,"ap-"+ap.toString());
            Log.i(TAG,"esl-"+esl.toString());
            tvApip.setText(ap.apid);
            tvAppddr.setText(ap.apaddr);
            tvSoftVersion.setText(esl.swversion);
            tvTagType.setText(esl.type);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lastDate = esl.lastDate;
            if (0!=lastDate){
                Date date = new Date(lastDate);
                String time = sdf.format(date);
                tvLastLoginTime.setText(time);
            }
            long activityDate = esl.activityDate;
            if (0!=activityDate){
                Date date1 = new Date(activityDate);
                String date2 = sdf.format(date1);
                tvLastMoveTime.setText(date2);
            }

            tvRssiF0.setText(esl.rssi_fo);
            tvRssiF1.setText(esl.lqi);
            int poweroff = esl.poweroff;
            tvIsOff.setText(poweroff==0?"已开机":"已关机");
            long powerDate = esl.powerDate;
            if (0!=powerDate){
                Date date3 = new Date(powerDate);
                String date4 = sdf.format(date3);
                Log.i(TAG,"date4"+date4);
                tvOffTime.setText(date4);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
