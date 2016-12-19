package com.suntown.cloudmonitoring.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.Message.ApMessageActivity;
import com.suntown.cloudmonitoring.activity.Message.GooViewListener;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.SmsTaskBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends Activity {

    private static final String TAG = "MessageActivity";
    //AP监控
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_message1)
    TextView tvMessage1;
    //变价监控
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_message2)
    TextView tvMessage2;
    //电量监控
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_message3)
    TextView tvMessage3;
    //电量降幅监控
    @BindView(R.id.tv_count1)
    TextView tvCount1;
    @BindView(R.id.tv_count2)
    TextView tvCount2;
    @BindView(R.id.tv_count3)
    TextView tvCount3;
    @BindView(R.id.tv_time4)
    TextView tvTime4;
    @BindView(R.id.tv_count4)
    TextView tvCount4;
    @BindView(R.id.tv_message4)
    TextView tvMessage4;
    @BindView(R.id.tv_time5)
    TextView tvTime5;
    @BindView(R.id.tv_count5)
    TextView tvCount5;
    @BindView(R.id.tv_message5)
    TextView tvMessage5;
    private String userId;
    private String serverIp;
    private int sendCount1;

    private SmsTaskBean.ApSmstasksBean apSmstasks;
    private SmsTaskBean.LowSmstasksBean lowSmstasks;
    private SmsTaskBean.NoUptSmstasksBean noUptSmstasks;
    private SmsTaskBean.NoLoginSmstasksBean noLoginSmstasksBean;
    private SmsTaskBean.OtherSmstasksBean otherSmstasksBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        getMessage();
    }

    private void getMessage() {
        userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        if ("".equals(userId)) {
            userId = SPUtils.getString(this, Constant.USER_ID);
            serverIp = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            serverIp = SPUtils.getString(this, Constant.SUBSERVER_IP);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.LOOKSTATUS, "0");
        params.put(Constant.USER_ID, userId);
        Log.i(TAG, "userID:" + userId);
        ApiClient.getInstance().mApiService.getSmsTaskCount(params).compose(RxSchedulers.io_main()).subscribe(smsTaskBean -> {
            Log.i(TAG, "smsTaskBean:" + smsTaskBean);
            //AP
            apSmstasks = smsTaskBean.apSmstasks;
            //变价
            noUptSmstasks = smsTaskBean.noUptSmstasks;
            //低电量
            lowSmstasks = smsTaskBean.lowSmstasks;
            //注册
            noLoginSmstasksBean = smsTaskBean.noLoginSmstasks;
            //其他
            otherSmstasksBean = smsTaskBean.otherSmstasks;
            initView();

        }, throwable -> {

        });
    }

    private void initView() {
//        smsTaskBean = getIntent().getParcelableExtra(Constant.MESSAGE);
        if (null != apSmstasks) {
            sendCount1 = apSmstasks.sendCount;
            tvTime1.setText(apSmstasks.sendTimeStr);
            tvMessage1.setText(apSmstasks.content);
            int sendCount = apSmstasks.sendCount;
            Log.i(TAG,"apSmstasksBean:"+sendCount);
            if (sendCount == 0) {
                tvCount1.setVisibility(View.GONE);
            } else {
                tvCount1.setVisibility(View.VISIBLE);
                if (sendCount > 99) {
//                    tvCount1.setFlagText("99+");
                    tvCount1.setText("99+");
                } else {
                    tvCount1.setText(sendCount1 + "");
                }
                tvCount1.setTag(sendCount1);
                GooViewListener mGooListener = new GooViewListener(this, tvCount1) {
                    @Override
                    public void onDisappear(PointF mDragCenter) {
                        super.onDisappear(mDragCenter);
//                        mRemoved.add(position);
//                        notifyDataSetChanged();
//                        Utils.showToast(mContext, "Cheers! We have get rid of it!");
                    }
                    @Override
                    public void onReset(boolean isOutOfRange) {
                        super.onReset(isOutOfRange);
//                        notifyDataSetChanged();
//                        Utils.showToast(mContext, isOutOfRange ? "Are you regret?" : "Try again!");
                    }
                };
                tvCount1.setOnTouchListener(mGooListener);
            }

        }
        if (null != noUptSmstasks) {
            int sendCount2 = noUptSmstasks.sendCount;
            tvTime2.setText(noUptSmstasks.sendTimeStr);
            String content = noUptSmstasks.content;
            Log.i(TAG,"tvMessage2:"+content);
            Log.i(TAG,"noUptSmstasksBean:"+sendCount2);
            tvMessage2.setText(content);
            int sendCount1 = noUptSmstasks.sendCount;
            if (sendCount1 == 0) {
                tvCount2.setVisibility(View.GONE);
            } else {
                tvCount2.setVisibility(View.VISIBLE);
                if (sendCount1 > 99) {
                    tvCount2.setText("99+");
                } else {
                    tvCount2.setText(sendCount2 + "");
                }
            }
        }
        if (null != lowSmstasks) {
            int sendCount3 = lowSmstasks.sendCount;
            tvTime3.setText(lowSmstasks.sendTimeStr);
            String content = lowSmstasks.content;
            Log.i(TAG,"tvMessage3:"+content);
            Log.i(TAG,"sendCount3:"+sendCount3);
            tvMessage3.setText(content);
            int sendCount2 = lowSmstasks.sendCount;
            if (sendCount2 == 0 || null == lowSmstasks) {
                tvCount3.setVisibility(View.GONE);
            } else {
                tvCount3.setVisibility(View.VISIBLE);
                if (sendCount2 > 99) {
                    tvCount3.setText("99+");
                } else {
                    tvCount3.setText(sendCount3 + "");
                }
            }
        }
        if (null != noLoginSmstasksBean) {
            int sendCount3 = noLoginSmstasksBean.sendCount;
            tvTime4.setText(noLoginSmstasksBean.sendTimeStr);
            String content = noLoginSmstasksBean.content;
            Log.i(TAG,"tvMessage4:"+content);
            Log.i(TAG,"sendCount4:"+sendCount3);
            tvMessage4.setText(content);
            int sendCount2 = noLoginSmstasksBean.sendCount;
            if (sendCount2 == 0 || null == noLoginSmstasksBean) {
                tvCount4.setVisibility(View.GONE);
            } else {
                tvCount4.setVisibility(View.VISIBLE);
                if (sendCount2 > 99) {
                    tvCount4.setText("99+");
                } else {
                    tvCount4.setText(sendCount3 + "");
                }
            }
        }
        if (null != otherSmstasksBean) {
            int sendCount3 = otherSmstasksBean.sendCount;
            tvTime5.setText(otherSmstasksBean.sendTimeStr);
            String content = otherSmstasksBean.content;
            Log.i(TAG,"tvMessage5:"+content);
            Log.i(TAG,"tvMessage5:"+sendCount3);
            tvMessage5.setText(content);
            int sendCount2 = otherSmstasksBean.sendCount;
            if (sendCount2 == 0 || null == otherSmstasksBean) {
                tvCount5.setVisibility(View.GONE);
            } else {
                tvCount5.setVisibility(View.VISIBLE);
                if (sendCount2 > 99) {
                    tvCount5.setText("99+");
                } else {
                    tvCount5.setText(sendCount3 + "");
                }
            }
        }
    }


    @OnClick({R.id.rl_ap, R.id.rl_change, R.id.rl_battery, R.id.iv_back,R.id.rl_register,R.id.rl_other})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_ap:
                intent = new Intent(MessageActivity.this, ApMessageActivity.class);
                intent.putExtra(Constant.SMSTYPE, 1);
                startActivity(intent);
                break;
            case R.id.rl_change:
                intent = new Intent(MessageActivity.this, ApMessageActivity.class);
                intent.putExtra(Constant.SMSTYPE, 7);
                startActivity(intent);
                break;
            case R.id.rl_battery:
                intent = new Intent(MessageActivity.this, ApMessageActivity.class);
                intent.putExtra(Constant.SMSTYPE, 6);
                startActivity(intent);
                break;
            case R.id.rl_register:
                intent = new Intent(MessageActivity.this, ApMessageActivity.class);
                intent.putExtra(Constant.SMSTYPE, 10);
                startActivity(intent);
                break;
            case R.id.rl_other:
                intent = new Intent(MessageActivity.this, ApMessageActivity.class);
                intent.putExtra(Constant.SMSTYPE, 0);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
