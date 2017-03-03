package com.suntown.smartscreen.server;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.ServerBean;

import rx.Observable;

/**
 *
 */
public interface ServerContract {
    interface Model extends BaseModel {
        Observable<String> getServer(String userid);
    }

    interface View extends BaseView {
        void getSuccess(ServerBean serverBean);
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getServer(String userid);
        @Override
        public void onStart() {}
    }
}
