package com.suntown.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suntown.R;
import com.suntown.utils.PhoneNumUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoserPswActivity extends BaseActivity {


    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_security_code)
    EditText etSecurityCode;
    @BindView(R.id.btn_send_security_code)
    Button btnSendSecurityCode;
    private InputMethodManager systemService;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loser_psw);
        ButterKnife.bind(this);
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
//                    senSMS(number);
                    time.start();
                } else {
                    Toast.makeText(LoserPswActivity.this,
                            "此号码不存在，请重新输入", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btn_confirm:
                //重置密码
                break;
        }
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
}
