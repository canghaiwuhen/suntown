package com.suntown.net;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/1.
 * <p/>
 * 封装：请求方式， 请求地址， 请求参数
 */
public interface BaseRequest {

    public enum HttpMethod {

        GET, POST;
    }

    public abstract HttpMethod getMethod();

    public abstract String getUrl();

    public abstract Map<String, String> getParams();
}
