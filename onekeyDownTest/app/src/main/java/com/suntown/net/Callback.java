package com.suntown.net;

/**
 * Created by Administrator on 2016/7/1.
 *
 *  请求结果回调
 *
 *  为了回调的时候告知当前的响应是哪次请求发出的，响应的方法把请求的BaseRequest传入
 */
public interface Callback<Res extends BaseResponse> {

    public abstract void onError(BaseRequest request, Exception e);

    public abstract void onOther(BaseRequest request, Res response);

    public abstract void onSuccess(BaseRequest request, Res response);
}
