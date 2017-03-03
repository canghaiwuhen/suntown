package com.suntown.smartscreen.price.changePrice;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.GoodsInfo;

import rx.Observable;

/**
 *
 */
public interface ChangeContract {
    interface Model extends BaseModel {
        Observable<String> updatePrice(String xml, String url);
    }

    interface View extends BaseView {
        void updatePriceSuccess();
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void updatePrice(String xml,String url);
        @Override
        public void onStart() {}
    }
}
