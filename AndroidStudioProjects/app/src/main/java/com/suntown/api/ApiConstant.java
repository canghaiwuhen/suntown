package com.suntown.api;

import android.os.Environment;


public class  ApiConstant {
    // 文件位置
    public static String SAVE_FILE_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/hicoupon/merchant";
//    public static String BASE_URL = "http://192.168.0.240:8080/";
//    public static String BASE_URL = "www.iesl.com.cn/";

    public static String BASE_URL = "http://www.smartesl.com.cn";
//    public static String BASE_URL="http://192.168.0.132:8082";

    public static String formatBASE_HOST(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return "http://" + url;
        }
    }
}
