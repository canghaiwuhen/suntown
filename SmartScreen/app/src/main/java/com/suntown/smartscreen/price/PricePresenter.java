package com.suntown.smartscreen.price;

import android.util.Log;

import com.google.gson.Gson;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.data.User;
import com.suntown.smartscreen.login.LoginContract;


/**
 * Created by Administrator on 2017/2/7.
 */
public class PricePresenter extends PriceContract.Presenter{
    private static final String TAG = "LoginPresenter";

    @Override
    public void getGoodsInfo(String userId, String sid, String goods, String url) {
        mRxManager.add(mModel.getGoodsInfo(userId,sid,goods,url).subscribe(s -> {
            Log.i(TAG,"s:"+s);
            String json = s.replace("<ns:getGoodsInfoResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:getGoodsInfoResponse>", "");
            GoodsInfo goodsInfo = new Gson().fromJson(json, GoodsInfo.class);
            Log.i("LoginPresenter","goodsInfo:"+goodsInfo.toString());
            //1 成功 0 失败
            if(0<goodsInfo.ROWS){
                mView.getGoodsInfoSuccess(goodsInfo);
            }else{
                mView.showMsg("没有查询到信息!");
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("网络异常!");
        }));
    }
}
