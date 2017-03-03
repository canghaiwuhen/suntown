package com.suntown.smartscreen.maintain;

import android.util.Log;

import com.google.gson.Gson;
import com.suntown.smartscreen.data.AllMainTainBean;
import com.suntown.smartscreen.data.BaseBean;
import com.suntown.smartscreen.price.changePrice.ChangeContract;

import rx.functions.Action1;


/**
 * Created by Administrator on 2017/2/7.
 */
public class MainTainPresenter extends MainTainContract.Presenter{
    private static final String TAG = "ChangePresenter";

    @Override
    public void getAllDispMContents(String userid, String sid, String page, String size, String url) {
        mRxManager.add(mModel.getAllDispMContents(userid,sid,page,size,url).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"s:"+s);
                String json = s.replace("<ns:getAllDispMContentsResponse xmlns:ns=\"http://services.suntown.com\"><ns:return>", "");
                json = json.replace("</ns:return></ns:getAllDispMContentsResponse>", "");
                Log.i(TAG,"s:"+s);
                AllMainTainBean allMainTainBean= new Gson().fromJson(json, AllMainTainBean.class);
                if (0<allMainTainBean.ROWS) {
                    mView.getAllDispMContentsSuccess(allMainTainBean);
                }else{
                    mView.noData();
                }
            }
        }, throwable -> {
            Log.i(TAG,"throwable:"+throwable);
            mView.showMsg("网络异常!");
        }));
    }

}
