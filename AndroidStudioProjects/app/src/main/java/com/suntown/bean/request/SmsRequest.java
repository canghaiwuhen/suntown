package com.suntown.bean.request;

import com.suntown.net.BaseRequest;
import com.suntown.net.BaseResponse;
import com.suntown.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/31.
 */
public class SmsRequest extends BaseResponse implements BaseRequest {
    String number;
    public SmsRequest(String string){
        number=string;
    }


    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return Constant.format("checkCodeSend");
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("moblie",number);
        return map;
    }
}
