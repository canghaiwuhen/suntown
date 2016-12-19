package com.suntown.cloudmonitoring.activity.TagDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoTypeActivity extends BaseActivity {

    @BindView(R.id.tv_photo_type)
    TextView tvPhotoType;
    private String TAG = "PhotoTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_type);
        ButterKnife.bind(this);
        int orimatype = getIntent().getIntExtra(Constant.ORIMATYPE, 2);
//        Log.i(TAG,"orimatype-"+orimatype);
        //-1 无图形任务  0 主图形  1插播图形 2 缺货
//        if (orimatype==-1) {
//            tvPhotoType.setText("无图形任务");
//        }else if (orimatype==0){
//            tvPhotoType.setText("主图形");
//        }else if (orimatype==1){
//            tvPhotoType.setText("插播图形");
//        }else if (orimatype==2){
//            tvPhotoType.setText("缺货");
//        }
        tvPhotoType.setText(-1==orimatype?"无图形任务":(0==orimatype?"主图形":(1==orimatype?"插播图形":"缺货")));
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
