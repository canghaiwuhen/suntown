package com.suntowns.labeltest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.suntowns.labeltest.utils.Constant;
import com.suntowns.labeltest.utils.PhoneNumUtils;
import com.suntowns.labeltest.utils.String2MD5;
import com.suntowns.labeltest.utils.Utils;
import com.suntowns.labeltest.utils.Xml2Json;


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


/**
 * Created by Administrator on 2016/7/28.
 */
public class RegisterActivity extends Activity {
    //    @BindView(R.id.tv_upload_photo)
//    TextView tvUploadPhoto;

    @BindView(R.id.btn_send_security_code)
    Button btnSendSecurityCode;
    @BindView(R.id.et_register_security_code)
    EditText etRegisterSecurityCode;
    @BindView(R.id.et_register_psw)
    EditText etRegisterPsw;
    @BindView(R.id.et_register_nick)
    EditText etRegisterNick;
    @BindView(R.id.et_register_user_name)
    EditText etRegisterUserName;
    @BindView(R.id.et_register_psw_agin)
    EditText etRegisterPswAgin;
    //    @BindView(R.id.rb_man)
//    RadioButton rbMan;
//    @BindView(R.id.rb_woman)
//    RadioButton rbWoman;
    private String number;
    //    String[] sax = null;
//    int type = 1;
    private OkHttpClient client;
    private String TAG = "RegisterActivity";
    private TimeCount time;
    private String securityCode;
    private String pswMD5;
    private String userNum;
    private String password;
    private String nickName;
    private InputMethodManager systemService;
    private String aginPsw;
    private String aginPswMD5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        client = new OkHttpClient();
        ButterKnife.bind(this);
//        rbWoman.setButtonDrawable(R.drawable.selected_0);
//        rbMan.setButtonDrawable(R.drawable.select);
        etRegisterUserName.addTextChangedListener(textWatcher);
        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        sax = new String[]{"男","女"};
        time = new TimeCount(60000, 1000);
    }

    //注册提交服务器
    public void register_confirm(View view) {
        password = etRegisterPsw.getText().toString().trim();
        aginPsw = etRegisterPswAgin.getText().toString().trim();

        userNum = etRegisterUserName.getText().toString();
        pswMD5 = String2MD5.MD5(password);
        aginPswMD5 = String2MD5.MD5(aginPsw);
        Log.i(TAG, pswMD5 + "");
        nickName = etRegisterNick.getText().toString();
        securityCode = etRegisterSecurityCode.getText().toString();

        if ("".equals(userNum) || "".equals(pswMD5) || "".equals(nickName) || "".equals(securityCode)||"".equals(aginPswMD5)) {
            Utils.showToast(RegisterActivity.this, "信息不全，请填写信息");
            return;
        }

        if (!aginPswMD5.equals(pswMD5)){
            Utils.showToast(RegisterActivity.this, "两次密码输入不一致");
            etRegisterPsw.setText("");
            etRegisterUserName.setText("");
            return;
        }

        if (password.length() < 7) {
            Utils.showToast(RegisterActivity.this, "密保等级过低，请完善密码");
            return;
        }
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return;
        }
        //TODO 发送注册请求
        sendRegsiter();
    }

    //    R.id.rb_man, R.id.rb_woman,
    @OnClick({R.id.btn_send_security_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_security_code:
                number = etRegisterUserName.getText().toString();
                boolean sequence = PhoneNumUtils.isPhoneNumberValid(number);
//                boolean sequence = isPhoneNumberValid(number);
                if (sequence) {
                    senSMS(number);
                    time.start();
//                    btnSendSecurityCode.setEnabled(false);
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "此号码不存在，请重新输入", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void sendRegsiter() {
        new Thread(() -> {
            {
                FormBody body = new FormBody.Builder()
                        .add("moblie", userNum).add("code", securityCode).add("pwd", pswMD5)
                        .build();
                Request reques = new Request.Builder().post(body).url(Constant.format("mobileCodeBack")).build();
                client.newCall(reques).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(RegisterActivity.this, "联网失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json = null;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            Log.i(TAG, json + "");
                            RegisterBean registerBean = new Gson().fromJson(json, RegisterBean.class);
                            String result = registerBean.getRELSUT();
                            if (result.equals("0")) {
                                Utils.showToast(RegisterActivity.this, "注册成功");
                                //TODO 返回登录界面，传递账号密码，保存帐户信息到本地
                                Intent intent = getIntent();
                                intent.putExtra(Constant.USER_NAME, userNum);
                                intent.putExtra(Constant.PASSWORD, password);
                                intent.putExtra(Constant.NICKNAME, nickName);
                                setResult(200, intent);
                                finish();
                            } else if (result.equals("1")) {
                                Utils.showToast(RegisterActivity.this, "帐号已存在");
                            } else {
                                Utils.showToast(RegisterActivity.this, "请确认验证码");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }).start();
    }

    //TODO 发送短信
    private void senSMS(final String number) {
        new Thread(() -> {
            FormBody body = new FormBody.Builder()
                    .add("moblie", number)
                    .build();
            Request reques = new Request.Builder().post(body).url(Constant.format("checkCodeSend")).build();
            client.newCall(reques).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, e.toString());
                    Utils.showToast(RegisterActivity.this, "联网失败，请检查网络");
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
                    SMSCode smsCode = new Gson().fromJson(json, SMSCode.class);
                    String result = smsCode.getRESULT();
                    if (result.equals("0")) {
                        Utils.showToast(RegisterActivity.this, "短信发送成功");
                    } else if (result.equals("1")) {
                        Utils.showToast(RegisterActivity.this, "号码已被注册");
                    }
                }
            });
        }).start();

    }

    TextWatcher textWatcher = new TextWatcher() {
        CharSequence sequence;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            etRegisterUserName.setText(s);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sequence = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = etRegisterUserName.getSelectionStart();
            editEnd = etRegisterUserName.getSelectionEnd();
            if (this.sequence.length() > 3) {
                btnSendSecurityCode.setVisibility(View.VISIBLE);
            }
            if (this.sequence.length() > 11) {
                Toast.makeText(RegisterActivity.this,
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                etRegisterUserName.removeTextChangedListener(this);
                etRegisterUserName.setText(s);
                etRegisterUserName.addTextChangedListener(this);

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
