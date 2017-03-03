package com.suntown.scannerproject.change.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.weight.LoadingDialog;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ChangeGoodsFragment extends Fragment implements View.OnClickListener {
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";

    private View inflate;
    private EditText etBarcode;
    private EditText etTinyip;
    private String sid;
    private String serverip;
    private LoadingDialog dialog;
    private boolean isSend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.activity_change_goods, container, false);
        initUi();
        return inflate;
    }

    private void initUi() {

        sid = SPUtils.getString(getActivity(), Constant.SID);
        serverip = SPUtils.getString(getActivity(), Constant.SUBSERVER_IP);
        if ("".equals(serverip)){
            serverip = ApiConstant.BASE_URL;
        }
        ((TextView) inflate.findViewById(R.id.tv_barcode)).setText("请扫描更换的商品条码:");
        ((TextView) inflate.findViewById(R.id.tv_tinyip)).setText("请扫描标签:");
        etBarcode = (EditText) inflate.findViewById(R.id.et_barcode);
        etTinyip = (EditText) inflate.findViewById(R.id.et_tinyip1);
        inflate.findViewById(R.id.ll_main).setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);
        etBarcode.setOnClickListener(view -> requestBarcodeFocus());
        etTinyip.setOnClickListener(view -> requestFocus());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        getActivity().registerReceiver(receiver, intentFilter);
        dialog = new LoadingDialog(getActivity());
    }

    /**
     * 提交数据
     * @param view
     */
    @Override
    public void onClick(View view) {
        String barcode = etBarcode.getText().toString().trim();
        String tinyip = etTinyip.getText().toString().trim();
        if (!"".equals(barcode)&&!"".equals(tinyip)){
            dialog.show();
            commit(barcode,tinyip);
        }else{
            Utils.showToast(getActivity(),"输入的标签或条码不能为空");
        }
    }

    private void commit(String barcode, String tinyip) {
        String ip = Constant.formatBASE_HOST(serverip);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.ShelfLabGoodsCHG2(sid,tinyip,barcode).compose(RxSchedulers.io_main()).subscribe(s -> {
            dialog.dismiss();
            String xml = s.toString();
            xml = xml.replace("<ns:ShelfLabGoodsCHG2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            xml = xml.replace("</ns:return></ns:ShelfLabGoodsCHG2Response>", "");
            if ("1".equals(xml)) {
                Utils.showToast(getActivity(), "商品更换成功");
                etTinyip.setText("");
                etBarcode.setText("");
            } else if ("0".equals(xml)) {
                Utils.showToast(getActivity(), "商品更换失败");
            } else if ("-1".equals(xml)) {
                Utils.showToast(getActivity(), "商品条码不存在");
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
                if (isSend) {
                    if (message.startsWith(".")) {
                        //                                      16
                        //转成标签 Integer.parseInt(String s, int radix)
                        String tinyip = FormatString.fromTinyip(message);
                        message = tinyip;
                    }
                    if (message.contains(".")){
                        etTinyip.setText(message);
                        etTinyip.setFocusable(false);
                        requestBarcodeFocus();
                    }else{
                        etBarcode.setText(message);
                        etBarcode.setFocusable(false);
                        requestFocus();
                    }
                }
            }
        }
    };

    private void requestBarcodeFocus() {
        etBarcode.setFocusable(true);
        etBarcode.setFocusableInTouchMode(true);
        etBarcode.requestFocus();
        etBarcode.findFocus();
        etBarcode.setSelection(etBarcode.getText().length());
    }

    private void requestFocus() {
        etTinyip.setFocusable(true);
        etTinyip.setFocusableInTouchMode(true);
        etTinyip.requestFocus();
        etTinyip.findFocus();
        etTinyip.setSelection(etTinyip.getText().length());
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);

    }
}
