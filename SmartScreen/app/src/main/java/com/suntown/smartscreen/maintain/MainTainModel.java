package com.suntown.smartscreen.maintain;


import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.netUtils.RxSchedulers;
import com.suntown.smartscreen.price.changePrice.ChangeContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 *
 */
public class MainTainModel implements MainTainContract.Model {

    @Override
    public Observable<String> getAllDispMContents(String userid, String sid, String page, String size, String url) {
        return new Retrofit.Builder().
                addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                        baseUrl(url).build().
                        create(ApiService.class).getAllDispMContents(userid,sid,page,size)
                .compose(RxSchedulers.io_main());
    }
}
