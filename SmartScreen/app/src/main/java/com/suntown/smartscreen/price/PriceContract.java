package com.suntown.smartscreen.price;


import com.suntown.smartscreen.base.BaseModel;
import com.suntown.smartscreen.base.BasePresenter;
import com.suntown.smartscreen.base.BaseView;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.data.User;

import rx.Observable;

/**
 * Created by baixiaokang on 16/4/29.
 */
public interface PriceContract {
    interface Model extends BaseModel {
        Observable<String> getGoodsInfo(String userId,String sid,String goods,String url);
    }

    interface View extends BaseView {
        void getGoodsInfoSuccess(GoodsInfo goodsInfo);
        void showMsg(String msg);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getGoodsInfo(String userId,String sid,String goods,String url);
        @Override
        public void onStart() {}
    }
}
