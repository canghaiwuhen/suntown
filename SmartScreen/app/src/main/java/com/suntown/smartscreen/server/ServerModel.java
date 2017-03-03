package com.suntown.smartscreen.server;


import com.suntown.smartscreen.api.ApiClient;
import com.suntown.smartscreen.login.LoginContract;
import com.suntown.smartscreen.netUtils.RxSchedulers;

import rx.Observable;

/**
 *
 */
public class ServerModel implements ServerContract.Model {

    @Override
    public Observable<String> getServer(String userid) {
        return ApiClient.getInstance().service
                .getServer(userid)
                .compose(RxSchedulers.io_main());
    }
}
