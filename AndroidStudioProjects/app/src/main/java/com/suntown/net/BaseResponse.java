package com.suntown.net;

/**
 * Created by Administrator on 2016/7/1.
 * <p/>
 * 响应json数据的封装
 */
public abstract class BaseResponse<T> {

    //响应码
    public int code;

    public T data;

    public String error;

    public boolean success() {

        return code >= 200 && code < 300;
    }

}
