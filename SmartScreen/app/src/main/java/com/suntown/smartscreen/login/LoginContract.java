package com.suntown.smartscreen.login;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.User;

import rx.Observable;

/**
 * Created by baixiaokang on 16/4/29.
 */
public interface LoginContract {
    interface Model extends BaseModel {
        Observable<String> login(String name, String pass);
    }

    interface View extends BaseView {
        void loginSuccess(User user);
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String name, String pass);
        @Override
        public void onStart() {}
    }
}
