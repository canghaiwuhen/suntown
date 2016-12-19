package com.suntown.cloudmonitoring.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiClient;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.BaseBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class UpdatePswActivity extends BaseActivity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_confirm_psw)
    EditText etConfirmPsw;
    @BindView(R.id.btn_confirm_update)
    Button btnConfirmUpdate;
    private String oldPsw;
    private String newPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_cancel, R.id.btn_confirm_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.btn_confirm_update:
                init();
                break;
        }
    }

    private void init() {
        oldPsw = etOldPsw.getText().toString().trim();
        newPsw = etNewPsw.getText().toString().trim();
        String confirmPsw = etConfirmPsw.getText().toString().trim();
        String savePsw = SPUtils.getString(this, Constant.PASS_WORD);
        if (newPsw.length()<6){
            Utils.showToast(UpdatePswActivity.this,"密码安全等级过低，请重新设置密码");
            return;
        }
        if("".equals(oldPsw)||"".equals(newPsw)||"".equals(confirmPsw)){
            Utils.showToast(UpdatePswActivity.this,"密码不能为空");
        }else if (!newPsw.equals(confirmPsw)){
            Utils.showToast(UpdatePswActivity.this,"两次输入的密码不相同");
        }else if(!oldPsw.equals(savePsw)){
            Utils.showToast(UpdatePswActivity.this,"原密码输入错误");
        }else{
            UpdatePsw();
        }
    }
    //TODO 向服务器发起更新请求  成功，跳转loginactvity
    private void UpdatePsw() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, SPUtils.getString(this, Constant.USER_ID));
        params.put("oldPassword",oldPsw);
        params.put("newPassword",newPsw);
        ApiClient.getInstance().mApiService.updatePsw(params).compose(RxSchedulers.io_main()).subscribe(baseBean -> {
            if (1== baseBean.getResultCode()){
                Utils.showToast(UpdatePswActivity.this,"密码修改成功");
                finish();
            }else{
                Utils.showToast(UpdatePswActivity.this,"修改密码失败，请重试");
            }
        }, throwable -> {
            Utils.showToast(UpdatePswActivity.this,"修改密码失败，请重试");
            Log.i("UpdatePswActivity","throwable:"+throwable.toString());
        });
    }
}
