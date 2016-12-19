package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.TagPhotoBean;
import com.suntown.cloudmonitoring.bean.photoBean.ModelFlagBean;
import com.suntown.cloudmonitoring.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagTemplateActivity extends BaseActivity {

    @BindView(R.id.tv_r_dmcode)
    TextView tvRDmcode;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_task_id)
    TextView tvTaskId;
    @BindView(R.id.tv_x)
    TextView tvX;
    @BindView(R.id.tv_dx)
    TextView tvDx;
    @BindView(R.id.tv_specwidth)
    TextView tvSpecwidth;
    @BindView(R.id.tv_rw_y)
    TextView tvRwY;
    @BindView(R.id.tv_rw_dy)
    TextView tvRwDy;
    @BindView(R.id.tv_now_date)
    TextView tvNowDate;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_template);
        ButterKnife.bind(this);
        ModelFlagBean modelFlagBean = getIntent().getParcelableExtra(Constant.ESLMODEL);
        ModelFlagBean.EslWarnBean.EslModelBean eslModel = modelFlagBean.eslWarn.eslModel;
        if (null!=eslModel){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tvRDmcode.setText(eslModel.r_dmCode+"");
            tvType.setText(eslModel.r_type+"");
            tvTaskId.setText(eslModel.taskId+"");
            tvX.setText(eslModel.x+"");
            tvDx.setText(eslModel.dx+"");
            tvSpecwidth.setText(eslModel.specwidth+"");
            tvRwY.setText(eslModel.rw_y+"");
            tvRwDy.setText(eslModel.rw_dy+"");
            long r_nowDate = eslModel.r_nowDate;
            Date date = new Date(r_nowDate);
            String time = sdf.format(date);
            tvNowDate.setText(time);

            long r_startDate = eslModel.r_startDate;
            Date date1 = new Date(r_startDate);
            String times = sdf.format(date1);
            tvStartDate.setText(times);
            tvStartTime.setText(eslModel.dm_starttime+"");
            tvEndTime.setText(eslModel.dm_endtime+"");
        }

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
