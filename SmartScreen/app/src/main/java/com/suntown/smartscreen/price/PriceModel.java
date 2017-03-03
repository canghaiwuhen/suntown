package com.suntown.smartscreen.price;


import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.login.LoginContract;
import com.suntown.smartscreen.netUtils.RxSchedulers;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 * Created by baixiaokang on 16/5/2.
 */
public class PriceModel implements PriceContract.Model {

    @Override
    public Observable<String> getGoodsInfo(String userId, String sid, String goods, String url) {
        return new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                        baseUrl(url).build().
                        create(ApiService.class).getGoodsInfo(userId, sid, goods)
                .compose(RxSchedulers.io_main());
    }
}
