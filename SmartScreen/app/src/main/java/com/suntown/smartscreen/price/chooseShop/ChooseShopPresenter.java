package com.suntown.smartscreen.price.chooseShop;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suntown.smartscreen.data.AllShopBean;
import com.suntown.smartscreen.price.PriceContract;

import java.lang.reflect.Type;
import java.util.List;


/**
 *
 */
public class ChooseShopPresenter extends ChooseShopContract.Presenter{
    private static final String TAG = "LoginPresenter";


    @Override
    public void getUserShops(String name, String url) {
        mRxManager.add(mModel.getUserShops(name,url).subscribe(s -> {
            Log.i(TAG,"s:"+s);
            String json = s.replace("<ns:getUserShopsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
            json = json.replace("</ns:return></ns:getUserShopsResponse>", "");
            Type listType = new TypeToken<List<AllShopBean>>(){}.getType();
            List<AllShopBean> allShopBeanList = new Gson().fromJson(json, listType);
//            Log.i("LoginPresenter","allShopBeanList:"+allShopBeanList.toString());
            //1 成功 0 失败
            if(0!=allShopBeanList.size()){
                mView.getUserShopsSuccess(allShopBeanList);
            }else{
                mView.showMsg("没有门店信息!");
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("网络异常!");
        }));
    }
}
