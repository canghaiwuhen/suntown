package com.suntown.cloudmonitoring.api;


import com.suntown.cloudmonitoring.bean.APInfoBean;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.BaseBean;
import com.suntown.cloudmonitoring.bean.BattVoltageBean;
import com.suntown.cloudmonitoring.bean.ChildServerBean;
import com.suntown.cloudmonitoring.bean.LoginBean;
import com.suntown.cloudmonitoring.bean.MessageBean;
import com.suntown.cloudmonitoring.bean.PushBean;
import com.suntown.cloudmonitoring.bean.RegisterMor;
import com.suntown.cloudmonitoring.bean.SmsTaskBean;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.bean.TagPhotoBean;
import com.suntown.cloudmonitoring.bean.TestBAttertDangerBean;
import com.suntown.cloudmonitoring.bean.UpdateBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/user/getUserByUsernameAndPassword")
    Observable<LoginBean> login(@FieldMap Map<String, String> map);

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/user/modifyPassword")
    Observable<BaseBean> updatePsw(@FieldMap Map<String, String> map);

    /**
     * 获取子服务器
     */
    @FormUrlEncoded
    @POST("eslrmsh/phone/system/getModuleInfo")
    Observable<ChildServerBean> getChildServer(@FieldMap Map<String, String> map);

    /**
     * 获取AP列表
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/ap/getUserApList")
    Observable<APInfoBean> getAPForm(@FieldMap Map<String, String> map);

    /**
     *  获取所有门店信息
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/shop/getUserShopList")
    Observable<AllShopBean> getAllShopInfo(@FieldMap Map<String, String> map);

    /**
     *  获取电量降浮标签
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/getEslsByUserShopsVoltageDescender")
//    Observable<BatteryDangerBean> getBatteryDangerInfo(@FieldMap Map<String, String> map);
    Observable<TestBAttertDangerBean> getBatteryDangerInfo(@FieldMap Map<String, String> map);

    /**
     *  获取变价失败标签
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/getUserNoUptPriceEsls")
    Observable<RegisterMor> getChangePriceDefeat(@FieldMap Map<String, String> map);
    /**
     *  获取未注册标签
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/getUserNoRetEsls")
    Observable<RegisterMor> getNoRegisterLabel(@FieldMap Map<String, String> map);

    /**
     *  获取低电量标签
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/getUserLowBatterysEsls")
    Observable<RegisterMor> getUserLowBattery(@FieldMap Map<String, String> map);

    /**
     *  查询标签详情(userid,tinyip,sid)
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/getTinyipInfo")
    Observable<TagDetialBean> getTAGDetial(@FieldMap Map<String, String> map);

    /**
     *获取标签电量变化（2,3,4）
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/loginVoltage")
    Observable<BattVoltageBean> getTAGVoltage(@FieldMap Map<String, String> map);

    /**
     * 获取未读信息
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/sms/getSmsTaskCount")
    Observable<SmsTaskBean> getSmsTaskCount(@FieldMap Map<String, String> map);

    /**
     * 获取图文详情
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/esl/iPCheckProcess")
    Observable<TagPhotoBean> getPhotoDetial(@FieldMap Map<String, String> map);

    /**
     * 查询每种类型信息的详情
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/sms/getSmsTasksByLookStatus")
    Observable<MessageBean> getSmsTaskMessage(@FieldMap Map<String, String> map);

    /**
     * 更新信息状态
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/sms/updateSmsTaskLookStatus")
    Observable<UpdateBean> getUpdateMessage(@FieldMap Map<String, String> map);

    /**
     * 上传PushID
     */
    @FormUrlEncoded
    @POST("/eslrmsh/phone/user/getUserPushId")
    Observable<BaseBean> upPushId(@FieldMap Map<String, String> map);
}
