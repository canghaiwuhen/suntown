package com.suntown.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.api.ApiService;
import com.suntown.bean.FriendBean;
import com.suntown.netUtils.RxSchedulers;
import com.suntown.utils.Constant;
import com.suntown.utils.QRCodeUtil;
import com.suntown.utils.SPUtils;
import com.suntown.utils.Utils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddActivity extends BaseActivity {

    private static final int REQUEST = 100;
    private static final String TAG = "AddActivity";
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.iv_qh)
    ImageView ivQh;
    @BindView(R.id.tv_messgae)
    TextView tvMessgae;
    private int intExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        intExtra = getIntent().getIntExtra(Constant.FLAG, 0);
        if (1 == intExtra) {
            startActivityForResult(new Intent(this, CreamaActivity.class), REQUEST);
        }
        String uerName = SPUtils.getString(this, Constant.LOGIN_USER_NAME);
        Bitmap qrImage = QRCodeUtil.createGreenImage(uerName);
        tvNumber.setText(uerName);
        ivQh.setImageBitmap(qrImage);
        etNumber.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                queryFriend(etNumber.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i(TAG, "resultStr:" + resultStr);
                    boolean result = resultStr.matches("[0-9]+");
                    if (result == true) {
                        etNumber.setText(resultStr);
                        queryFriend(resultStr);
                    } else {
                        Utils.showToast(this, "请扫描正确的二维码");
                    }
                }
                break;
        }
    }

    private void queryFriend(String resultStr) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.ARG0, resultStr);
        String ip = Constant.BASE_HOST;
        Retrofit retrofit = new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        retrofit.create(ApiService.class).getFriendByTel(params).compose(RxSchedulers.io_main()).subscribe(s -> {
            Log.i(TAG, "s:" + s.toString());
            String result = s.replace("<ns:getUserInfoByPHOResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            result = result.replace("</ns:return></ns:getUserInfoByPHOResponse>", "");
            Log.i(TAG, "s:" + result);
            FriendBean friendBean = new Gson().fromJson(result, FriendBean.class);
            if (friendBean.ROWS > 0) {
                Intent intent = new Intent(AddActivity.this, FriendDetialAct.class);
                intent.putExtra(Constant.RECORD_BEAN,friendBean);
                intent.putExtra(Constant.INT,intExtra);
                startActivity(intent);
                finish();
            } else {
                tvMessgae.setVisibility(View.VISIBLE);
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
