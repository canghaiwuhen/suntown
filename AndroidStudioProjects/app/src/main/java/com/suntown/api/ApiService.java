package com.suntown.api;



import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

//    /**
//     * 登录
//     */
//    @GET("/axis2/services/STPdaService2/VerifyUserLogin")
//    Observable<String> login(@Query("usercode") String userCode,
//                             @Query("pswd") String pswd);

    /**
     *上传头像
     */
    @FormUrlEncoded
    @POST("uploadAvatar")
    Observable<String> updatePic(@FieldMap Map<String, String> map);

    /**
     *获取家庭成员
     */
    @FormUrlEncoded
    @POST("getOKFMList")
    Observable<String> getFamilyMember(@FieldMap Map<String, String> map);

    /**
     *获取验证消息条数
     */
    @FormUrlEncoded
    @POST("getPushNUMs")
    Observable<String> getMessageCount(@FieldMap Map<String, String> map);

    /**
     *重置pushNum
     */
    @FormUrlEncoded
    @POST("resetPushNUM")
    Observable<String> resetPushNUM(@FieldMap Map<String, String> map);

    /**
     *获取验证消息
     */
    @FormUrlEncoded
    @POST("getUserFriendReq")
    Observable<String> getUserFriendReq(@FieldMap Map<String, String> map);

    /**
     * 设置拒绝或接受添加
     */
    @FormUrlEncoded
    @POST("addFriendResp")
    Observable<String> getAddFriendResp(@FieldMap Map<String, String> map);

    /**
     * 查找好友
     */
    @FormUrlEncoded
    @POST("getUserInfoByPHO")
    Observable<String> getFriendByTel(@FieldMap Map<String, String> map);

    /**
     * 添加好友
     */
    @FormUrlEncoded
    @POST("addFriend")
    Observable<String> addFriend (@FieldMap Map<String, String> map);

    /**
     * 删除好友
     */
    @FormUrlEncoded
    @POST("delOKFM")
    Observable<String> delOKFM (@FieldMap Map<String, String> map);

    /**
     * 删除地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("deleteAddress")

    Observable<String> deleteAddress(@FieldMap Map<String, String> params);

    /**
     * 获取所有地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("getAllAddress")
    Observable<String> getAllAddress(@FieldMap Map<String, String> params);

    /**
     * 修改地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("updateAddress")
    Observable<String> upDateAddress(@FieldMap Map<String, String> params);

    /**
     * 设置默认地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("updateAddressDefault")
    Observable<String> updateAddressDefault(@FieldMap Map<String, String> params);

    /**
     * 确认付款
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("confirmOrderNew")
    Observable<String> confirmOrderNew(@FieldMap Map<String, String> params);

    /**
     * 获取用户
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("GetMemUsers")
    Observable<String> GetMemUsers(@FieldMap Map<String, String> params);
    /**
     * 获取历史订单
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("getHistoryOrderNew")
    Observable<String> getHistoryOrderNew(@FieldMap Map<String, String> params);

    /**
     * 删除订单
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("deleteOrder")
    Observable<String> deleteOrder(@FieldMap Map<String, String> params);

    /**
     * 获取标签地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("getAddressforTinyip")
    Observable<String> getAddressforTinyip(@FieldMap Map<String, String> params);

    /**
     * 设置标签地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("setAddressforTinyip")
    Observable<String> setAddressforTinyip(@FieldMap Map<String, String> params);

    /**
     * 更改用户信息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("addUserInfo")
    Observable<String> addUserInfo(@FieldMap Map<String, String> params);


    /**
     * 将用户和标签绑定
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Set_oked_user")
    Observable<String> setOkedUser(@FieldMap Map<String, String> params);

    /**
     * 设置标签WIFI地址
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("setWIFIAndIP")
    Observable<String> setWIFIIP(@FieldMap Map<String, String> params);

    /**
     * 模糊查询商品
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Getalikegoods_info")
    Observable<String> getLikeGoodsInfo(@FieldMap Map<String, String> params);

}
