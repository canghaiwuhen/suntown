package com.suntown.suntownshop.asynctask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.suntown.suntownshop.model.Category;

import android.os.AsyncTask;

/**
 * ����Post���ݵ���������AsyncTask
 * ��Ҫ Post�����ݴ����HashMap<key,value>�У����ݵĲ���Ϊkey,ֵ Ϊvalue��ΪString����
 * ���ûص��ӿ�OnCompleteCallback���ط������ӿڷ��ص�����
 * @author ken
 *
 */
public class PostAsyncTask extends
		AsyncTask<HashMap<String, String>, Void, Boolean> {
	String url;
	OnCompleteCallback callback;
	String strResult = "";

	/**
	 * ����ʵ��ʱ����������ӿ�URL�ͻص��ӿ�ʵ��
	 * 
	 * @param url
	 * @param callback
	 */
	public PostAsyncTask(String url, OnCompleteCallback callback) {
		this.url = url;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(HashMap<String, String>... params) {
		// TODO Auto-generated method stub
		try {
			// Simulate network access.
			//����HttpPostʵ��,urlΪ�ӿڵ�ַ
			HttpPost httpPost = new HttpPost(url);
			//��������list
			List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(
					params[0].entrySet());
			for (Entry<String, String> param : list) {
				httpParams.add(new BasicNameValuePair(param.getKey(), param
						.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(httpParams, HTTP.UTF_8));
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);  
		    HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code:"+code);
			if (code == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				return true;
			}else{
				strResult = EntityUtils.toString(httpResponse.getEntity());
				return false;
			}
		} catch (Exception e) {
			strResult = e.getMessage();
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		callback.onComplete(result, strResult);
		super.onPostExecute(result);
	}
	/**
	 * PostAsyncTask��ɺ�Ļص��ӿ�
	 * @author ken
	 *
	 */
	public interface OnCompleteCallback {
		/**
		 * 
		 * @param isOk �Ƿ����
		 * @param msg �ӷ������ӿ�ȡ�õ�����
		 */
		public void onComplete(boolean isOk, String msg);
	}
}
