package com.suntown.smartscreen.price.chooseShop;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.AllShopBean;

import java.util.List;

import rx.Observable;

/**
 *
 */
public interface ChooseShopContract {
    interface Model extends BaseModel {
        Observable<String> getUserShops(String name, String url);
    }

    interface View extends BaseView {
        void getUserShopsSuccess(List<AllShopBean> allShopBeanList);
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getUserShops(String name, String url);
        @Override
        public void onStart() {}
    }
}
