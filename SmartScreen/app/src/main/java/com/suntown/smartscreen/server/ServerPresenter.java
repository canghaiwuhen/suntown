package com.suntown.smartscreen.server;

import android.util.Log;

import com.google.gson.Gson;
import com.suntown.smartscreen.data.ServerBean;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.login.LoginContract;

import rx.functions.Action1;


/**
 * Created by Administrator on 2017/2/7.
 */
public class ServerPresenter extends ServerContract.Presenter{
    private static final String TAG = "ServerPresenter";

    @Override
    public void getServer(String userid) {
        mRxManager.add(mModel.getServer(userid).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s);
                String json = s.replace("<ns:getUserModulesResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getUserModulesResponse>", "");
                ServerBean serverBean = new Gson().fromJson(json, ServerBean.class);
                Log.i("LoginPresenter","serverBean:"+serverBean);
                if(0<serverBean.ROWS){
                    mView.getSuccess(serverBean);
                }else{
                    mView.showMsg("暂无数据");
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("联网失败，请重试!");
        }));
    }

//    @Override
//    public void login(String name, String pass) {
//        mRxManager.add(mModel.login(name, pass).subscribe(s -> {
//            Log.i(TAG,"s:"+s);
//            String json = s.replace("<ns:loginResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
//            json = json.replace("</ns:return></ns:loginResponse>", "");
//            User user = new Gson().fromJson(json, User.class);
//            Log.i("LoginPresenter","user:"+user);
//            //1 成功 0 失败
//            String result = user.RESULT;
//            if("1".equals(result)){
//                mView.loginSuccess();
//            }else{
//                mView.showMsg("账号或密码错误!");
//            }
//        }, throwable -> {
//            Log.i(TAG,"throwable:"+throwable);
//            mView.showMsg("登录失败!");
//        }));
//    }

}
