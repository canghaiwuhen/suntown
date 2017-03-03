package com.suntown.smartscreen.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    /**
     * 登录
     */
    @GET("login")
    Observable<String> login(@Query("arg0") String username, @Query("arg1") String password);

//    @FormUrlEncoded
//    @POST("login")
//    Observable<String> login(@Field("arg0") String usernam,
//                                  @Field("arg1") String password
//    );
    /**
     * 获取子服务器
     */
    @GET("getUserModules")
    Observable<String> getServer(@Query("arg0") String userid);

    /**
     *  更改密码
     */
    @GET("mdfLogPwd")
    Observable<String> mdfLogPwd(@Query("arg0") String userid,@Query("arg1") String oldPsw,@Query("arg2") String newPsw);

    /**
     * 获取门店
     */
    @GET("getUserShops")
    Observable<String> getUserShops(@Query("arg0") String userid);

    /**
     * 查询标签或条码信息
     */
    @GET("getGoodsInfo")
    Observable<String> getGoodsInfo(@Query("arg0") String userid,@Query("arg1") String sid,@Query("arg2") String goods);


//    @GET("updatePrice")
//    Observable<String> updatePrice(@Query("arg0") String xml);

    /**
     * 变价
     * @param xml
     * @return
     */
    @FormUrlEncoded
    @POST("updatePrice")
    Observable<String> updatePrice(@Field("arg0") String xml);


    /**
     *获取用户所有模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getAllDispMContents")
    Observable<String> getAllDispMContents(@Field("arg0") String userid,@Field("arg1") String sid
            ,@Field("arg2") String page
            ,@Field("arg3") String pageSize);

    /**
     *获取模板详情
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getDMDetail")
    Observable<String> getDMDetail(@Field("arg0") String dmcode,@Field("arg1") String number);


    /**
     *获取模板详情
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getDispMShops")
    Observable<String> getDispMShops(@Field("arg0") String dmcode);


    /**
     *停用 启用模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("ActiveGDM")
    Observable<String> setActiveGDM(@Field("arg0") String sid ,@Field("arg1") String dmcode,@Field("arg2") String activ);

    /**
     * 分配模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("AllocDispM")
    Observable<String> AllocDispM(@Field("arg0") String xml);

    /**
     * 获取AP列表
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getAPList")
    Observable<String> getAPList(@Field("arg0") String userid,@Field("arg1") String sid);

    /**
     * 获取门店分配的模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getGoodsDispMs")
    Observable<String> getGoodsDispMs(@Field("arg0") String sid,@Field("arg1") String userid);

    /**
     * 获取待分配模板类型
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getUnAllocedDMTypes")
    Observable<String> getUnAllocedDMTypes(@Field("arg0") String userid,@Field("arg1") String sid);

    /**
     * 获取模板类型下所有的模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getDMListFor")
    Observable<String> getDMListFor(@Field("arg0") String userid,@Field("arg1") String sid,@Field("arg2") String type,@Field("arg3") String nodata);
    /**
     *  提交选择的模板
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("ShopAllocDispM")
    Observable<String> ShopAllocDispM(@Field("arg0") String xml);

    /**
     *  查找门店货架列表
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getShelfList")
    Observable<String> getShelfList(@Field("arg0") String userid,@Field("arg1") String shopId);

    /**
     *  获取货架信息
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("getShelfData")
    Observable<String> getShelfData(@Field("arg0") String sfid,@Field("arg1") String sid);

}
