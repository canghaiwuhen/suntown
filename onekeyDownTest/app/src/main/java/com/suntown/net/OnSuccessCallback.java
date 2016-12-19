package com.suntown.net;

/**
 * Created by Administrator on 2016/7/1.
 */
public interface OnSuccessCallback<Res extends BaseResponse> {

    public void onResultSuccess(BaseRequest request, Res response);
}
