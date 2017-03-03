package com.suntown.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.R;
import com.suntown.bean.LoginBean;
import com.suntown.utils.Constant;
import com.suntown.utils.SPUtils;
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
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateNickNameActivity extends BaseActivity {

    @BindView(R.id.iv_cancel)
    TextView ivCancel;
    @BindView(R.id.iv_save)
    TextView ivSave;
    @BindView(R.id.et_update_nick)
    EditText etUpdateNick;
    @BindView(R.id.iv_clear_psw)
    ImageView ivClearPsw;
    private String nickeName;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick_name);
        client = new OkHttpClient();
        ButterKnife.bind(this);
       init();

    }

    private void init() {
        String nickname = SPUtils.getString(this, Constant.NICKNAME);
        etUpdateNick.setText(nickname);
    }

    @OnClick({R.id.iv_cancel, R.id.iv_save, R.id.iv_clear_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_save:
                nickeName = etUpdateNick.getText().toString();
                if ("".equals(nickeName)){
                    Utils.showToast(UpdateNickNameActivity.this,"请输入昵称");
                }else{
                    sendUpdateNikeName();
                }
                break;
            case R.id.iv_clear_psw:
                etUpdateNick.setText("");
                break;
        }
    }

    private void sendUpdateNikeName() {
        RequestBody formBody = new FormBody.Builder()
                .add(Constant.MEMID, SPUtils.getString(UpdateNickNameActivity.this, Constant.MEMID))
                .add("nickname",nickeName)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST("updatenikename"))
                .post(formBody)
                .build();
        new Thread(() -> {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.showToast(UpdateNickNameActivity.this,"联网失败，请检查网络");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        Log.i("MainActivity",is.toString());
                        String json = null;
                        try {
                            json = new Xml2Json(is).Pull2Xml();
                            Log.i("MainActivity",json+"");
                            LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            Log.d("LoginActivity", "loginBean:" + loginBean.getRESULT());
                            String result = loginBean.getRESULT();
                            if ("0".equals(result)){
                                Utils.showToast(UpdateNickNameActivity.this,"昵称修改成功");
                                runOnUiThread(() -> {
                                    {
                                        Log.i("nickeName","nickeName:"+nickeName);
                                        SPUtils.put(UpdateNickNameActivity.this,Constant.NICKNAME,nickeName);
                                        finish();
                                    }
                                });
                            }else{
                                Utils.showToast(UpdateNickNameActivity.this,"网络异常,请重新输入");
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }).start();
    }

}
