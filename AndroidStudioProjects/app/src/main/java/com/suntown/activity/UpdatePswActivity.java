package com.suntown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
import com.suntown.utils.String2MD5;
import com.suntown.utils.Utils;
import com.suntown.utils.Xml2Json;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePswActivity extends BaseActivity implements View.OnClickListener {

    private Button savePsw;
    private TextView cancel;
    private EditText old_Psw;
    private EditText new_Psw;
    private EditText confirm_Psw;
    private OkHttpClient client;
    private String psw;
    private String confirmPsw;
    private String oldpsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_psw);
        client = new OkHttpClient();
        init();
    }

    private void init() {
        savePsw = ((Button) findViewById(R.id.btn_save));
        cancel = ((TextView) findViewById(R.id.tv_cancel));
        old_Psw = ((EditText) findViewById(R.id.et_old_psw));
        new_Psw = ((EditText) findViewById(R.id.et_new_psw));
        confirm_Psw = ((EditText) findViewById(R.id.et_confirm_psw));
        savePsw.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                psw = SPUtils.getString(UpdatePswActivity.this, Constant.LOGIN_PWD);
                Log.d("UpdatePswActivity", psw);
                String oldPsw =  old_Psw.getText().toString();
                String newPsw = new_Psw.getText().toString();
                confirmPsw = confirm_Psw.getText().toString();
                if (Utils.isFastClick()) {
                    return;
                }
                if (new_Psw.length()<6){
                    Utils.showToast(UpdatePswActivity.this,"密码安全等级过低，请重新设置密码");
                }
                oldpsw = String2MD5.MD5(oldPsw);
                newPsw = String2MD5.MD5(newPsw);
                confirmPsw = String2MD5.MD5(confirmPsw);
                if("".equals(oldPsw)||"".equals(newPsw)||"".equals(confirmPsw)){
                    Utils.showToast(UpdatePswActivity.this,"密码不能为空");
                }else if (!newPsw.equals(oldpsw)){
                    Utils.showToast(UpdatePswActivity.this,"两次输入的密码不相同");
                }else if(!oldpsw.equals(psw)){
                    Utils.showToast(UpdatePswActivity.this,"原密码输入错误");
                }else{
                    UpdatePwd();
                }
                break;
            case R.id.tv_cancel:
                finish();
            break;
        }
    }

    private void UpdatePwd() {
        RequestBody formBody = new FormBody.Builder()
                .add("memid", SPUtils.getString(UpdatePswActivity.this,Constant.MEMID))
                .add("opwd",psw)
                .add("npwd",confirmPsw)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.format("updatepwd"))
                .post(formBody)
                .build();
        new Thread(() -> {
            {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(UpdatePswActivity.this,"登陆失败，请检查网络");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        String json = null;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            Log.i("loginActivity",json+"");
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                            String result = loginBean.getRESULT();
                            if ( result.equals("0")){
                                Utils.showToast(UpdatePswActivity.this,"密码修改成功");
                                SPUtils.put(UpdatePswActivity.this,Constant.LOGIN_PWD,confirmPsw);
                                startActivity(new Intent(UpdatePswActivity.this,LoginActivity.class));
                                finish();
                            }else{
                                Utils.showToast(UpdatePswActivity.this,"密码修改失败，请重试");
                                runOnUiThread(() -> {
                                    old_Psw.setText("");
                                    new_Psw.setText("");
                                    confirm_Psw.setText("");
                                });
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}
