package com.suntown.scannerproject.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.scannerproject.MainActivity;
import com.suntown.scannerproject.R;
import com.suntown.scannerproject.api.ApiConstant;
import com.suntown.scannerproject.api.ApiService;
import com.suntown.scannerproject.base.BaseActivity;
import com.suntown.scannerproject.input.InputAndOutputActivity;
import com.suntown.scannerproject.netUtils.RxSchedulers;
import com.suntown.scannerproject.utils.Constant;
import com.suntown.scannerproject.utils.SPUtils;
import com.suntown.scannerproject.utils.String2MD5;
import com.suntown.scannerproject.utils.Utils;
import com.suntown.scannerproject.weight.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_setting)
    ImageView tvSetting;
    @BindView(R.id.tv_code)
    TextView tvCode;
    private InputMethodManager systemService;
    private LoadingDialog dialog;
    private String serverip;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverip = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverip)) {
            serverip = ApiConstant.BASE_URL;
        }
        Log.i(TAG, "serverip:" + serverip);
        String appVersionName = Utils.getAppVersionName(this);
        tvCode.setText("".equals(appVersionName)?"杭州升腾科技":"杭州升腾科技 V"+appVersionName);
        etUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etUsername.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEND ||i == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        });
        etPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE ||i == EditorInfo.IME_ACTION_SEND) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        });
    }

    //登录
    public void btn_confirm_login(View view) {
        String userName = etUsername.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();
        passWord = "0x" + String2MD5.MD5(passWord);
        Log.i(TAG, "userName:" + userName + ",passWord:" + passWord);
//        http://192.168.0.240:8080/axis2/services/STPdaService2/VerifyUserLogin
        String ip = Constant.formatBASE_HOST(serverip);
        if ("".equals(userName) ) {
            etUsername.setError("账号不能为空");
            Utils.showToast(LoginActivity.this, "账号不能为空");
            return;
        }if ("".equals(passWord)) {
            etUsername.setError("密码不能为空");
            Utils.showToast(LoginActivity.this, "密码不能为空");
            return;
        }
        if (Utils.isFastClick()) {
//            Utils.showToast(LoginActivity.this,"点击太过频繁");
            return;
        }
        dialog = new LoadingDialog(this);
        dialog.show();
        String password = passWord;
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<String> login = service.login(userName, passWord);
        login.compose(RxSchedulers.io_main()).subscribe(string -> {
            String xml = string.toString().trim();
            xml = xml.replace("<ns:VerifyUserLoginResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            xml = xml.replace("</ns:return></ns:VerifyUserLoginResponse>", "");
            Log.i(TAG, "xml:" + xml);
            if ("0".equals(xml)) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                SPUtils.put(LoginActivity.this, Constant.USER_CODE, userName);
                SPUtils.put(LoginActivity.this, Constant.PSWD, password);
                finish();
                dialog.dismiss();
            } else {
                Utils.showToast(LoginActivity.this, "登录失败，请检查账号密码及端口");
                dialog.dismiss();
            }
        }, throwable -> {
            Log.i(TAG, "throwable:" + throwable.toString());
            Utils.showToast(LoginActivity.this, "网络连接异常，请检查网络及IP");
            dialog.dismiss();
        });
    }
    /**
     * 回收键盘
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//			/*隐藏软键盘*/
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if(inputMethodManager.isActive()){
//                inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
//            }
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 设置网络
     */
    @OnClick(R.id.tv_setting)
    public void onClick() {
        startActivity(new Intent(LoginActivity.this, SettingPortAct.class));
    }
}
