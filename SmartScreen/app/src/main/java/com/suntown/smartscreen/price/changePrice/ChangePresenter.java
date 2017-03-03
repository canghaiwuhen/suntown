package com.suntown.smartscreen.price.changePrice;

import android.util.Log;

import com.google.gson.Gson;
import com.suntown.smartscreen.data.BaseBean;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.data.User;

import rx.functions.Action1;


/**
 * Created by Administrator on 2017/2/7.
 */
public class ChangePresenter extends ChangeContract.Presenter{
    private static final String TAG = "ChangePresenter";

    @Override
    public void updatePrice(String xml, String url) {
        mRxManager.add(mModel.updatePrice(xml,url).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s);
                String json = s.replace("<ns:updatePriceResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:updatePriceResponse>", "");
                Log.i(TAG,"s:"+s);
                BaseBean baseBean = new Gson().fromJson(json, BaseBean.class);
                Log.i("LoginPresenter","baseBean:"+baseBean.toString());
                if ("1".equals(baseBean.REUSLT)) {
                    mView.updatePriceSuccess();
                }else{
                    mView.showMsg("变价失败");
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("网络异常!");
        }));
    }
}
