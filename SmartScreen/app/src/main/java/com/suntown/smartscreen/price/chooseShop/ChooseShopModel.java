package com.suntown.smartscreen.price.chooseShop;


import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.PriceContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 *
 */
public class ChooseShopModel implements ChooseShopContract.Model {


    @Override
    public Observable<String> getUserShops(String name,String url) {
        return new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                baseUrl(url).build().
                create(ApiService.class).getUserShops(name)
                .compose(RxSchedulers.io_main());
    }
}
