package com.suntown.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Administrator on 2016/7/1.
 */
public class NetUtil {

    private static ConnectivityManager connectivityManager;

    public static void init(Context context) {

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static <Res extends BaseResponse> void sendRequest(BaseRequest request, Class<Res> responseClass, Callback callback) {

        //请求之前检查网络
        if (checkNet()) {

            new NetTask().execute(new NetBean(request, responseClass, callback));
        } else {

            callback.onError(request, new IllegalStateException("请检查网络"));
        }
    }

    //将请求的BaseRequest， Callback传递给NetTask
    private static class NetBean {

        public NetBean(BaseRequest request, Class<? extends BaseResponse> responseClass, Callback callback) {
            this.request = request;
            this.callback = callback;
            this.responseClass = responseClass;
        }

        BaseRequest request;
        Callback callback;
        Class<? extends BaseResponse> responseClass;

        //json解析封装的结果对象BaseResponse response
        BaseResponse response;
        Exception exception;
    }

    //需要的对象：BaseRequest， Callback
    private static class NetTask extends AsyncTask<NetBean, Void, NetBean> {


        @Override
        protected NetBean doInBackground(NetBean[] params) {

            NetBean netBean = params[0];

            Reader readerResponse = null;
            try {
                readerResponse = HttpWrapper.getInstance().getReaderResponse(netBean.request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BaseResponse response = new Gson().fromJson(readerResponse, netBean.responseClass);
                    netBean.response = response;

            return netBean;
        }

        @Override
        protected void onPostExecute(NetBean netBean) {

            //出现了异常
            if (netBean.exception != null) {

                netBean.callback.onError(netBean.request, netBean.exception);
            } else {

                if (netBean.response!=null && netBean.response.success()) {
                    netBean.callback.onSuccess(netBean.request, netBean.response);
                } else {

                    netBean.callback.onOther(netBean.request, netBean.response);
                }
            }

        }
    }


    private static boolean checkNet() {

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }


}
