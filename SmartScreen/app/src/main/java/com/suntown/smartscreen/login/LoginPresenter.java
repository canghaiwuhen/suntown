package com.suntown.smartscreen.login;

import android.util.Log;

import com.google.gson.Gson;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.utils.Constant;
import com.suntown.smartscreen.utils.SPUtils;

import rx.functions.Action1;


/**
 * Created by Administrator on 2017/2/7.
 */
public class LoginPresenter extends LoginContract.Presenter{
    private static final String TAG = "LoginPresenter";

    @Override
    public void login(String name, String pass) {
        mRxManager.add(mModel.login(name, pass).subscribe(s -> {
            Log.i(TAG,"s:"+s);
            String json = s.replace("<ns:loginResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:loginResponse>", "");
            User user = new Gson().fromJson(json, User.class);
            Log.i("LoginPresenter","user:"+user);
            //1 成功 0 失败
            String result = user.RESULT;
            if("1".equals(result)){
                mView.loginSuccess(user);
            }else{
                mView.showMsg("账号或密码错误!");
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("登录失败!");
        }));
    }

}
