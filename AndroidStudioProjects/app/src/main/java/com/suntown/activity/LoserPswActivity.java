package com.suntown.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.PhoneNumUtils;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoserPswActivity extends BaseActivity {


    private static final String TAG = "LoserPswActivity";
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_security_code)
    EditText etSecurityCode;
    @BindView(R.id.btn_send_security_code)
    Button btnSendSecurityCode;
    private InputMethodManager systemService;
    private TimeCount time;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loser_psw);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        etUserName.addTextChangedListener(textWatcher);
        time = new TimeCount(60000, 1000);
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

    @OnClick({R.id.btn_send_security_code, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_security_code:
                //发送验证码
                String number = etUserName.getText().toString();
                boolean sequence = PhoneNumUtils.isPhoneNumberValid(number);
                if (sequence) {
                    senSMS(number);
                    time.start();
                } else {
                    Toast.makeText(LoserPswActivity.this,
                            "此号码不存在，请重新输入", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btn_confirm:
                // 发送验证码和手机号 重置密码
                String securityCode = etSecurityCode.getText().toString();
                String userName = etUserName.getText().toString();
                sendURL(userName,securityCode);
                break;
        }
    }

    /**
     * 发送请求
     * @param userName
     * @param securityCode
     */
    private void sendURL(String userName, String securityCode) {

    }

    TextWatcher textWatcher = new TextWatcher() {
        CharSequence sequence;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sequence = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = etUserName.getSelectionStart();
            editEnd = etUserName.getSelectionEnd();
            if (this.sequence.length() > 3) {
                btnSendSecurityCode.setVisibility(View.VISIBLE);
            }
            if (this.sequence.length() > 11) {
                Toast.makeText(LoserPswActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                etUserName.removeTextChangedListener(this);
                etUserName.setText(s);
                etUserName.addTextChangedListener(this);

            }
        }
    };

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            btnSendSecurityCode.setText("获取验证码");
            btnSendSecurityCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            btnSendSecurityCode.setClickable(false);//防止重复点击
            btnSendSecurityCode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    /**
     *  发送短信  接口不同
     * @param number
     */
    private void senSMS(final String number) {
        new Thread(() -> {
            FormBody body = new FormBody.Builder()
                    .add("moblie", number)
                    .build();
            Request reques = new Request.Builder().post(body).url(Constant.formatBASE_HOST("checkCodeSend")).build();
            client.newCall(reques).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, e.toString());
                    Utils.showToast(LoserPswActivity.this, "联网失败，请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    String json = null;
                    try {
                        json = new Xml2Json(is).Pull2Xml();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                    String result = loginBean.getRESULT();
                    if (result.equals("0")) {
                        Utils.showToast(LoserPswActivity.this, "短信发送成功");
                    } else if (result.equals("1")) {
                        Utils.showToast(LoserPswActivity.this, "号码已被注册");
                    } else if (result.equals("2")) {
                        Utils.showToast(LoserPswActivity.this, "短信发送失败");
                    }
                }
            });
        }).start();
    }
}
