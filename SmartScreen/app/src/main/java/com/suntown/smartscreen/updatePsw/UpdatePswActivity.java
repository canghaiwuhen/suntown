package com.suntown.smartscreen.updatePsw;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.suntown.smartscreen.R;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.base.BaseActivity;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;
import com.suntown.smartscreen.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class UpdatePswActivity extends BaseActivity {


    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_confirm_psw)
    EditText etConfirmPsw;

    @Override
    protected int getContentView() {
        ButterKnife.bind(this);
        return R.layout.activity_update_psw;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tv_cancel, R.id.btn_confirm_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.btn_confirm_update:
                initPsw();
                //TODO
                break;
        }
    }

    private void initPsw() {
        String oldPsw = etOldPsw.getText().toString().trim();
        String newPsw = etNewPsw.getText().toString().trim();
        String confirmPsw = etConfirmPsw.getText().toString().trim();
        String savePsw = SPUtils.getString(this, Constant.PASS_WORD);
        String userid = SPUtils.getString(this, Constant.USER_ID);
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
            UpdatePsw(userid,oldPsw,confirmPsw);
        }
    }
    //返回值1成功 0失败 arg0 arg1arg2
    private void UpdatePsw(String userid, String oldPsw, String confirmPsw) {
        new Retrofit.Builder().
        addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(ApiConstant.formatBASE_HOST(ApiConstant.BASE_URL)).build().
                create(ApiService.class).mdfLogPwd(userid,oldPsw, confirmPsw)
                .compose(RxSchedulers.io_main()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                String json = s.replace("<ns:mdfLogPwdResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:mdfLogPwdResponse>", "");

            }
        }, throwable -> {

        });
    }
}
