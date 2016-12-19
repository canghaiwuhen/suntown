package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.photoBean.GoodsUpTaskBean;
import com.suntown.cloudmonitoring.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagTaskActivity extends BaseActivity {

    @BindView(R.id.tv_task_id)
    TextView tvTaskId;
    @BindView(R.id.tv_price_type)
    TextView tvPriceType;
    @BindView(R.id.tv_local_photo)
    TextView tvLocalPhoto;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.tv_now_price)
    TextView tvNowPrice;
    @BindView(R.id.tv_vip_price)
    TextView tvVipPrice;
    @BindView(R.id.tv_price_time)
    TextView tvPriceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_task);
        ButterKnife.bind(this);
        GoodsUpTaskBean goodsUpTaskBean = getIntent().getParcelableExtra(Constant.ESLUPTASKS);
        GoodsUpTaskBean.EslWarnBean.EslUptTasksBean eslUptTasks = goodsUpTaskBean.eslWarn.eslUptTasks;
//        GoodsUpTaskBean.EslWarnBean.EslUptTasksBean.EslBean esl = eslUptTasks.esl;
        if (null!=eslUptTasks){
            tvTaskId.setText(eslUptTasks.taskid+"");
            int bitmapid = eslUptTasks.partMap;
            tvPriceType.setText(eslUptTasks.pricetype + "");
            tvLocalPhoto.setText(bitmapid == 1 ? "局部图形" : "全部图形");
            tvOldPrice.setText(eslUptTasks.oriprice);
            tvNowPrice.setText(eslUptTasks.uptprice);
            tvVipPrice.setText(eslUptTasks.memprice);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long adddate = eslUptTasks.adddate;
            if (0!=adddate){
                Date date = new Date(adddate);
                String time = sdf.format(date);
                tvPriceTime.setText(time);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
