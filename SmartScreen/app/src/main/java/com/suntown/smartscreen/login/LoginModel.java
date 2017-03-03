package com.suntown.smartscreen.login;


import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.api.ApiConstant;
import com.suntown.smartscreen.api.ApiService;
import com.suntown.smartscreen.netUtils.RxSchedulers;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 * Created by baixiaokang on 16/5/2.
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<String> login(String name, String pass) {
//        new Retrofit.Builder().
//                addConverterFactory(GsonConverterFactory.create())
//        addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
//                baseUrl(ApiConstant.formatBASE_HOST(ApiConstant.BASE_URL)).build().
//                create(ApiService.class).login(name, pass)
//                .compose(RxSchedulers.io_main());
        return ApiClient.getInstance().service
                .login(name, pass)
                .compose(RxSchedulers.io_main());
    }

}
