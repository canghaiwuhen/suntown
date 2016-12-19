package com.suntown.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/7/1.
 * <p/>
 * Http的封装，真正执行网络请求： OkHttp封装
 */
public class HttpWrapper {

    private final OkHttpClient okHttpClient;

    public HttpWrapper() {

        okHttpClient = new OkHttpClient();
    }

    private static HttpWrapper sIntance = new HttpWrapper();

    public static HttpWrapper getInstance() {

        return sIntance;
    }

    public byte[] getBytesResponse(BaseRequest request) throws IOException {

        ResponseBody body = getResponseBody(request);
        return body.bytes();
    }

    public InputStream getInputStreamResponse(BaseRequest request) throws IOException {

        ResponseBody body = getResponseBody(request);
        return body.byteStream();
    }

    public String getStringResponse(BaseRequest request) throws IOException {

        ResponseBody body = getResponseBody(request);
        return body.string();
    }


    //执行请求 get请求 ：请求参数url?username=itcast&password=123
    //post    在请求体
    public Reader getReaderResponse(BaseRequest request) throws IOException {

        ResponseBody body = getResponseBody(request);
        return body.charStream();
    }

    private ResponseBody getResponseBody(BaseRequest request) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (request.getMethod() == BaseRequest.HttpMethod.GET) {

            StringBuilder stringBuilder = new StringBuilder(request.getUrl());
            Map<String, String> params = request.getParams();
            if (params != null && params.size() > 0) {

                stringBuilder.append("?");
                Set<String> keySet = params.keySet();
                for (String key : keySet) {

                    //  url?username=itcast&123&password=123
                    stringBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8")).append("&");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            builder.url(stringBuilder.toString()).get();
        } else if (request.getMethod() == BaseRequest.HttpMethod.POST) {

            //添加请求参数到请求体
            FormBody.Builder formBody = new FormBody.Builder();
            Map<String, String> params = request.getParams();
            if (params != null && params.size() > 0) {

                Set<String> keySet = params.keySet();
                for (String key : keySet) {

                    formBody.addEncoded(key, params.get(key));
                }
            }

            builder.url(request.getUrl()).post(formBody.build());
        }

        return okHttpClient.newCall(builder.build()).execute().body();
    }
}
