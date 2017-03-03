package com.suntown.smartscreen.maintain;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.AllMainTainBean;

import rx.Observable;

/**
 *
 */
public interface MainTainContract {
    interface Model extends BaseModel {
        Observable<String> getAllDispMContents(String userid,String sid,String page,String size, String url);
    }

    interface View extends BaseView {
        void getAllDispMContentsSuccess(AllMainTainBean allMainTainBean);
        void noData();
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getAllDispMContents(String userid,String sid,String page,String size, String url);
        @Override
        public void onStart() {}
    }
}
