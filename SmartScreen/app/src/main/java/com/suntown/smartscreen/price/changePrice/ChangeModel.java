package com.suntown.smartscreen.price.changePrice;


import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.PriceContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 *
 */
public class ChangeModel implements ChangeContract.Model {

    @Override
    public Observable<String> updatePrice(String xml, String url) {
        return new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                        baseUrl(url).build().
                        create(ApiService.class).updatePrice(xml)
                .compose(RxSchedulers.io_main());
    }
}
