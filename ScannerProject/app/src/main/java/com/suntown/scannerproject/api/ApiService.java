package com.suntown.scannerproject.api;



import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    /**
     * 登录
     */
    @GET("/axis2/services/STPdaService2/VerifyUserLogin")
    Observable<String> login(@Query("usercode") String userCode,
                             @Query("pswd") String pswd);

    /**
     * 获取所有门店信息
     */
    @GET("/axis2/services/STPdaService2/GetShopList")
    Observable<String> getShopList(@Query("userid") String userCode);

    /**
     * 查询货架信息
     */
    @GET("/axis2/services/STPdaService2/GetShelfDataForPad")
    Observable<String> getShelfList(@Query("sfid") String sfid,
                                   @Query("sid") String sid);

    /**
     * 查询商品信息
     */
    @GET("/axis2/services/STPdaService2/GetGoodsInfo2")
    Observable<String> getGoodsInfo2(@Query("Barcode") String barcode,
                                    @Query("Sid") String sid);


    /**
     * 查询标签信息
     */
    @GET("/axis2/services/STPdaService2/GetLabStatus2")
    Observable<String> GetLabStatus2(@Query("tinyip") String tinyip,
                                    @Query("sid") String sid);

    /**
     * 提交商品更换
     */
    @GET("/axis2/services/STPdaService2/ShelfLabGoodsCHG2")
    Observable<String> ShelfLabGoodsCHG2(@Query("sid") String sid,
                                         @Query("ip") String ip,
                                         @Query("barcode") String barcode);


    /**
     * 提交标签更换
     */
    @GET("/axis2/services/STPdaService2/Commit_LabCHG")
    Observable<String> Commit_LabCHG(@Query("xml") String xml);

    /**
     * 陈列维护 查询标签
     */
    @GET("/axis2/services/STPdaService2/Get_DispData2")
    Observable<String> Get_DispData2(@Query("tip") String ip,
                                     @Query("sid") String sid);

    /**
     * 陈列维护 提交
     */
    @GET("/axis2/services/STPdaService2/Commit_DispData")
    Observable<String> Commit_DispData(@Query("xml") String xml);

}
