package com.suntown.cloudmonitoring.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.TagDetail.BindDetialActivity;
import com.suntown.cloudmonitoring.activity.TagDetail.PhotoTypeActivity;
import com.suntown.cloudmonitoring.activity.TagDetail.RegisterActivity;
import com.suntown.cloudmonitoring.activity.TagDetail.SendActivity;
import com.suntown.cloudmonitoring.activity.TagDetail.TagTaskActivity;
import com.suntown.cloudmonitoring.activity.TagDetail.TagTemplateActivity;
import com.suntown.cloudmonitoring.activity.TagDetialActivity;
import com.suntown.cloudmonitoring.bean.ExceptionFlagBean;
import com.suntown.cloudmonitoring.bean.MaptypeBean;
import com.suntown.cloudmonitoring.bean.TagPhotoBean;
import com.suntown.cloudmonitoring.bean.photoBean.ApEslBean;
import com.suntown.cloudmonitoring.bean.photoBean.GoodsUpTaskBean;
import com.suntown.cloudmonitoring.bean.photoBean.ModelFlagBean;
import com.suntown.cloudmonitoring.bean.photoBean.SendBean;
import com.suntown.cloudmonitoring.bean.photoBean.ShelfBean;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/12.
 */
public class PhoteInformationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PhoteInformationFragment";
    private View inflate;
    private String serverIP;
    private String userId;
    private String tinyip;
    private RelativeLayout rlRegister;
    private RelativeLayout rlBind;
    private RelativeLayout rlTagTask;
    private RelativeLayout rlTagStyle;
    private RelativeLayout rlType;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (null==eslWarn) {
//                        Log.i(TAG, "eslWarn-" + eslWarn);
//                        Utils.showToast(getActivity(), "数据为空");
                        tv1.setTextColor(Color.parseColor("#949494"));
                        tvStoreName.setTextColor(Color.parseColor("#949494"));
                        tvType.setTextColor(Color.parseColor("#949494"));
                        tvTask.setTextColor(Color.parseColor("#949494"));
                        textView.setTextColor(Color.parseColor("#949494"));
                    } else {
//                        Log.i(TAG, "shelfGoods-" + shelfGoods + ",ap--" + ap + ",esl-" + esl + ",oriMaptype-" + oriMaptype);
//                        Log.i(TAG, "eslUptTasks-" + eslUptTasks + ",goods-" + goods + ",eslModel--" + eslModel);
                        if (false == bindFlag) {
                            tv1.setTextColor(Color.parseColor("#949494"));
//                            Log.i(TAG, "bindFlag-" + bindFlag);
                        }
//                        else if (null == shelBean.eslWarn.shelfGoods) {
////                            Log.i(TAG, "shelfGoods-" + shelBean.eslWarn.shelfGoods.toString());
//                            tv1.setTextColor(Color.parseColor("#949494"));
//                        }
                        if (false == loginFlag) {
//                            Log.i(TAG, "loginFlag-" + loginFlag);
                            tvStoreName.setTextColor(Color.parseColor("#949494"));
                        }
//                        else if (null == apEslBean.eslWarn.ap || null == apEslBean.eslWarn.esl) {
////                            Log.i(TAG, "apEslBean-" + apEslBean.ap + "  " + apEslBean.esl);
//                            tvStoreName.setTextColor(Color.parseColor("#949494"));
//                        }
                        if (false == mapTypeFlag) {
//                            Log.i(TAG, "mapTypeFlag-" + mapTypeFlag);
                            tvType.setTextColor(Color.parseColor("#949494"));
                        }
//                        else if (maptypeBean.eslWarn.oriMaptype == 0) {
//                            tvType.setTextColor(Color.parseColor("#949494"));
//                        }
                        if (false == taskFlag) {
//                            Log.i(TAG, "taskFlag-" + taskFlag);
                            tvTask.setTextColor(Color.parseColor("#949494"));
//                            null == goodsUpTaskBean.eslWarn.goods ||
                        }
//                        else if (null == goodsUpTaskBean.eslWarn.eslUptTasks) {
////                            Log.i(TAG, "goodsUpTaskBean-" + goodsUpTaskBean.goods + "  " + goodsUpTaskBean.eslUptTasks);
//                            tvTask.setTextColor(Color.parseColor("#949494"));
//                        }
                        if (false == modelFlag) {
//                            Log.i(TAG, "modelFlag-" + modelFlag);
                            textView.setTextColor(Color.parseColor("#949494"));
                        }
//                        else if (null == modelFlagBean.eslWarn.eslModel) {
////                            Log.i(TAG, "modelFlagBean-" + modelFlagBean.eslModel);
//                            textView.setTextColor(Color.parseColor("#949494"));
//                        }
                        if (false==sendFlag){
                            textSend.setTextColor(Color.parseColor("#949494"));
                        }
                    }
                    break;
            }
        }
    };
    private TextView tv1;
    private TextView tvStoreName;
    private TextView tvType;
    private TextView tvTask;
    private TextView textView;
    private ShelfBean shelBean;
    private ApEslBean apEslBean;
    private MaptypeBean maptypeBean;
    private GoodsUpTaskBean goodsUpTaskBean;
    private ModelFlagBean modelFlagBean;
    private ExceptionFlagBean.EslWarnBean eslWarn;
    private boolean modelFlag = false;
    private boolean taskFlag = false;
    private boolean mapTypeFlag = false;
    private boolean loginFlag = false;
    private boolean bindFlag = false;
    private boolean sendFlag = false;;
    private SendBean sendBean;
    private TextView textSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.tag_detial_item2, container, false);
        rlBind = (RelativeLayout) inflate.findViewById(R.id.rl_bind);
        rlBind.setOnClickListener(this);
        rlRegister = (RelativeLayout) inflate.findViewById(R.id.rl_regist);
        rlRegister.setOnClickListener(this);
        rlType = (RelativeLayout) inflate.findViewById(R.id.rl_type);
        rlType.setOnClickListener(this);
        rlTagTask = (RelativeLayout) inflate.findViewById(R.id.rl_tag_task);
        rlTagTask.setOnClickListener(this);
        rlTagStyle = (RelativeLayout) inflate.findViewById(R.id.rl_tag_style);
        rlTagStyle.setOnClickListener(this);
        inflate.findViewById(R.id.rl_state).setOnClickListener(this);
        tinyip = ((TagDetialActivity) getActivity()).tinyip;
        tv1 = (TextView) inflate.findViewById(R.id.tv_1);
        tvStoreName = (TextView) inflate.findViewById(R.id.tv_store_name);
        tvType = (TextView) inflate.findViewById(R.id.tv_type);
        tvTask = (TextView) inflate.findViewById(R.id.tv_task);
        textView = (TextView) inflate.findViewById(R.id.textView);
        textSend = (TextView) inflate.findViewById(R.id.tv_send);
        getData();
        return inflate;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //绑定状态
            case R.id.rl_bind:
                if (false == bindFlag) {
                    return;
                }
                intent = new Intent(getActivity(), BindDetialActivity.class);
                intent.putExtra(Constant.SHELFGOODS, shelBean);
                startActivity(intent);
                break;
            //注册状态
            case R.id.rl_regist:
                if (false == loginFlag) {
                    return;
                }
                intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(Constant.ESL, apEslBean);
                startActivity(intent);
                break;
            //图形类型
            case R.id.rl_type:
                if (false == mapTypeFlag) {
                    return;
                }
                intent = new Intent(getActivity(), PhotoTypeActivity.class);
                int oriMaptype = maptypeBean.eslWarn.oriMaptype;
                intent.putExtra(Constant.ORIMATYPE, oriMaptype);
                startActivity(intent);
                break;
            //标签任务
            case R.id.rl_tag_task:
//                null == goodsUpTaskBean.eslWarn.goods ||
                if (false == taskFlag) {
                    return;
                }
                intent = new Intent(getActivity(), TagTaskActivity.class);
                intent.putExtra(Constant.ESLUPTASKS, goodsUpTaskBean);
                startActivity(intent);
                break;
            //标签模板
            case R.id.rl_tag_style:
                if (false == modelFlag) {
                    return;
                }
                intent = new Intent(getActivity(), TagTemplateActivity.class);
                intent.putExtra(Constant.ESLMODEL, modelFlagBean);
                startActivity(intent);
                break;
            //发送状态
            case R.id.rl_state:
                Log.i(TAG,"sendFlag:"+sendFlag);
                if (false == sendFlag) {
                    return;
                }
                intent = new Intent(getActivity(), SendActivity.class);
                intent.putExtra(Constant.SENDBEAN, sendBean);
                startActivity(intent);
                break;
        }
    }

    //联网获取数据

    private void getData() {
        OkHttpClient client = ((TagDetialActivity) getActivity()).client;
        Map<String, String> params = new HashMap<>();
        serverIP = SPUtils.getString(getActivity(), Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            serverIP = SPUtils.getString(getActivity(), Constant.SERVER_IP);
            userId = SPUtils.getString(getActivity(), Constant.USER_ID);
        } else {
            userId = SPUtils.getString(getActivity(), Constant.SUB_USER_ID);
        }
        Log.i(TAG,"serverIP"+serverIP+",userId"+userId+",tinyip"+tinyip);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.USER_ID, userId).
                add(Constant.TINYIP, tinyip).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/eslrmsh/phone/esl/iPCheckProcess")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    Log.i(TAG, "string-" + string);
                    ExceptionFlagBean exceptionFlagBean = new Gson().fromJson(string, ExceptionFlagBean.class);
                    eslWarn = exceptionFlagBean.eslWarn;
                    Log.i(TAG, "eslWarn-" + eslWarn.toString());
                    bindFlag = eslWarn.bindFlag;
                    loginFlag = eslWarn.loginFlag;
                    mapTypeFlag = eslWarn.mapTypeFlag;
                    taskFlag = eslWarn.taskFlag;
                    modelFlag = eslWarn.modelFlag;
                    sendFlag = eslWarn.sendFlag;
                    if (bindFlag == true) {
                        shelBean = new Gson().fromJson(string, ShelfBean.class);
                    }
                    if (loginFlag == true) {
                        //ap esl
                        apEslBean = new Gson().fromJson(string, ApEslBean.class);
                    }
                    if (mapTypeFlag == true) {
                        maptypeBean = new Gson().fromJson(string, MaptypeBean.class);
//                        int oriMaptype = maptypeBean.eslWarn.oriMaptype;
                        Log.i(TAG, "maptypeBean-" + maptypeBean.eslWarn.oriMaptype);
                    }
                    if (taskFlag == true) {
                        //eslUptTasks goods
                        goodsUpTaskBean = new Gson().fromJson(string, GoodsUpTaskBean.class);
//                        Log.i(TAG, "apEslBean-" + goodsUpTaskBean.toString());
                    }
                    if (modelFlag == true) {
                        //eslUptTasks goods
                        modelFlagBean = new Gson().fromJson(string, ModelFlagBean.class);
//                        Log.i(TAG, "apEslBean-" + modelFlagBean.toString());
                    }
                    if (eslWarn.sendFlag == true) {
                        //eslUptTasks goods
                        sendBean = new Gson().fromJson(string, SendBean.class);
//                        Log.i(TAG, "apEslBean-" + sendBean.toString());
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }


}
