package com.suntown.scannerproject.change.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.change.XmlUtils;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.weight.LoadingDialog;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ChangeTinyipFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ChangeTinyipFragment";
    private View inflate;
    private EditText etBarcode;
    private EditText etTinyip;
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private boolean isCodeClick = true;
    private String serverip;
    private String sid;
    private String userId;
    private LoadingDialog dialog;
    private boolean isSend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.change_goods, container, false);
        initUi();

        return inflate;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }

    private void initUi() {
        sid = SPUtils.getString(getActivity(), Constant.SID);
        userId = SPUtils.getString(getActivity(), Constant.USER_CODE);
        serverip = SPUtils.getString(getActivity(), Constant.SUBSERVER_IP);
        if ("".equals(serverip)){
            serverip = ApiConstant.BASE_URL;
        }
        ((TextView) inflate.findViewById(R.id.tv_barcode)).setText("请扫描更换前标签:");
        ((TextView) inflate.findViewById(R.id.tv_tinyip)).setText("请扫描更换后标签:");

        etBarcode = (EditText) inflate.findViewById(R.id.et_barcode);
        etTinyip = (EditText) inflate.findViewById(R.id.et_tinyip);
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);
        inflate.findViewById(R.id.ll_main).setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
        etBarcode.setOnFocusChangeListener((view, b) -> {
            if (b){
                isCodeClick = true;
            }else {
                isCodeClick = false;
            }
        });
        dialog = new LoadingDialog(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    /**
     * 提交数据
     * @param view
     */
    @Override
    public void onClick(View view) {
        //更换前标签
        String barcode = etBarcode.getText().toString().trim();
        //更换后标签
        String tinyip = etTinyip.getText().toString().trim();
        if (!"".equals(barcode)&&!"".equals(tinyip)){
            if (barcode.equals(tinyip)){
                Utils.showToast(getActivity(),"输入的标签不能相同");
            }else{
                commit(barcode,tinyip);
                dialog.show();
            }
        }else{
            Utils.showToast(getActivity(),"输入的标签不能为空");
        }
    }

    private void commit(String oldTinyip, String tinyip) {
        String deviceNum = SPUtils.getString(getActivity(), Constant.DEVICE_NUM);
        String ip = Constant.formatBASE_HOST(serverip);
        String xml = XmlUtils.String2Xml(oldTinyip, tinyip, sid, userId, deviceNum);
        Log.i(TAG,"xml:"+xml);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.Commit_LabCHG(xml).compose(RxSchedulers.io_main()).subscribe(s -> {
            dialog.dismiss();
            String string = s.toString();
            string = string.replace("<ns:Commit_LabCHGResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            string = string.replace("</ns:return></ns:Commit_LabCHGResponse>", "");
            Log.i(TAG, "string:" + string);
            if ("0".equals(string)) {
                etTinyip.setText("");
                etBarcode.setText("");
                Utils.showToast(getActivity(), "标签更换成功");
            } else {
                Utils.showToast(getActivity(), "标签更换失败");
            }
        }, throwable -> {
            dialog.dismiss();
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isSend=true;
        }else{
            isSend=false;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);

                if (isSend){
                    if (message.startsWith(".")) {
                        //转成标签 Integer.parseInt(String s, int radix)
                        String tinyip = FormatString.fromTinyip(message);
                        message = tinyip;
                    }
                    if (message.contains(".")){
                        if (isCodeClick) {
                            etBarcode.setText(message);
                        }else{
                            etTinyip.setText(message);
                        }
                        if (etBarcode.isFocusable()){
                            etBarcode.setFocusable(false);
                            etTinyip.setFocusable(true);
                            etTinyip.setFocusableInTouchMode(true);
                            etTinyip.requestFocus();
                            etTinyip.findFocus();
                            etTinyip.setSelection(etTinyip.getText().length());
                        }else {
                            etTinyip.setFocusable(false);
                            etBarcode.setFocusable(true);
                            etBarcode.setFocusableInTouchMode(true);
                            etBarcode.requestFocus();
                            etBarcode.findFocus();
                            etBarcode.setSelection(etBarcode.getText().length());
                        }
                    }else{
//                    Utils.showToast(getActivity(),"请输入正确的标签");
                    }

                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
}
