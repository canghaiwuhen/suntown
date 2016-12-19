package com.suntown.scannerproject.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.bean.DisplayBean;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.FormatString;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.weight.LoadingDialog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class DisplayDActivity extends BaseActivity {
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCN_CUST_EX_SCODE = "scannerdata";
    private static final String SCN_CUST_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";
    private static final String SCN_CUST_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";
    private static final String TAG = "DisplayDActivity";

    @BindView(R.id.et_tinyip)
    EditText etTinyip;
    @BindView(R.id.tv_barcode)
    TextView tvBarcode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_disp_row)
    EditText etDispRow;
    @BindView(R.id.et_disp_col)
    EditText etDispCol;
    @BindView(R.id.et_disp_depth)
    EditText etDispDepth;
    @BindView(R.id.et_r_amount)
    EditText etRAmount;
    @BindView(R.id.et_alter_amount)
    EditText etAlterAmount;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    private String serverip;
    private String sid;
    private InputMethodManager systemService;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_d);
        ButterKnife.bind(this);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        IntentFilter intentFilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
        registerReceiver(receiver, intentFilter);
        tvName.setText("陈列维护");
        sid = SPUtils.getString(this, Constant.SID);
        serverip = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverip)) {
            serverip = ApiConstant.BASE_URL;
        }
        dialog = new LoadingDialog(this);
        etTinyip.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etTinyip.setOnEditorActionListener((v, actionId, event) -> {
            String trim = etTinyip.getText().toString().trim();
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                if (!"".equals(trim)&&trim.contains("."))
                queryTag(trim);
            }
            return true;
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(SCN_CUST_EX_SCODE);
                if (message.startsWith(".")) {
                    //                                      16
                    //转成标签 Integer.parseInt(String s, int radix)
                    String tinyip = FormatString.fromTinyip(message);
                    message = tinyip;
                }
                if (message.contains(".")) {
                    etTinyip.setText(message);
                    //TODO 查询
                    queryTag(message);
                } else {
                    Utils.showToast(DisplayDActivity.this, "请输入正确的标签");
                }

            }
        }
    };

    private void queryTag(String message) {
//        Get_DispData2
        String ip = Constant.formatBASE_HOST(serverip);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.Get_DispData2(message, sid).compose(RxSchedulers.io_main()).subscribe(s -> {
            String xml = s.toString();
            xml = xml.replace("<ns:Get_DispData2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            xml = xml.replace("</ns:return></ns:Get_DispData2Response>", "");
            xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
            Log.i(TAG, "xml:" + xml);
            XStream xstream = new XStream(new DomDriver());
            xstream.processAnnotations(DisplayBean.class);
            DisplayBean displayBean = (DisplayBean) xstream.fromXML(xml);//xml格式的字符串
            Log.i(TAG, "displayBean:" + displayBean);
            tvBarcode.setText(displayBean.Barcode);
            tvGoodsName.setText(displayBean.GNAME);
            etDispRow.setText(displayBean.DispRow);
            etDispCol.setText(displayBean.DispCol);
            etDispDepth.setText(displayBean.DispDepths);
            etAlterAmount.setText(displayBean.AlterCnt);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(DisplayDActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (!Utils.isFastClick()) {
                    //提交
                    String tinyip = etTinyip.getText().toString().trim();
                    String barcode = tvBarcode.getText().toString().trim();
                    String dispRow = etDispRow.getText().toString().trim();
                    String dispCol = etDispCol.getText().toString().trim();
                    String dispDepth = etDispDepth.getText().toString().trim();
                    String rAmount = etRAmount.getText().toString().trim();
                    String alterAmount = etAlterAmount.getText().toString().trim();
                    String deviceNum = SPUtils.getString(this, Constant.DEVICE_NUM);
//                    && !"".equals(rAmount) && !"".equals(alterAmount)
                    if (!"".equals(tinyip) && !"".equals(barcode) && !"".equals(tvGoodsName) && !"".
                            equals(dispRow) && !"".equals(dispCol) && !"".equals(dispDepth) ) {
                        dialog.show();
                        String xml = FormatString.toXml(tinyip, barcode, dispRow, dispCol, dispDepth, rAmount, alterAmount, sid, deviceNum);
                        commitData(xml);
                    } else {
                        Utils.showToast(this, "请完善信息");
                    }
                }
                break;
        }
    }

    /**
     * 提交数据
     */
    private void commitData(String xml) {
        String ip = Constant.formatBASE_HOST(serverip);
        Log.i(TAG, "xml:" + xml);

        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        service.Commit_DispData(xml).compose(RxSchedulers.io_main()).subscribe(s -> {
            String string = s.toString();
            Log.i(TAG, "xml:" + xml);
            string = string.replace("<ns:Commit_DispDataResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            string = string.replace("</ns:return></ns:Commit_DispDataResponse>", "");
            Log.i(TAG, "xml:" + xml);
            if ("0".equals(string)) {
                Utils.showToast(DisplayDActivity.this, "任务提交成功");
                dialog.dismiss();
                //TODO 数据清空
                clearData();
            } else {
                dialog.dismiss();
                Utils.showToast(DisplayDActivity.this, "任务提交失败，请确认信息");
            }
        }, throwable -> {
            dialog.dismiss();
        });
    }

    private void clearData() {
        etTinyip.setText("");
        tvBarcode.setText("");
        tvGoodsName.setText("");
        etDispRow.setText("");
        etDispCol.setText("");
        etDispDepth.setText("");
        etRAmount.setText("");
        etAlterAmount.setText("");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                systemService.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
