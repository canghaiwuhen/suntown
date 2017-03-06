package com.suntown.suntownshop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
/**
 * 文件管理类
 * @author Administrator
 *
 */
public class FileManager {
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return null;
		}
		return sdDir.toString();

	}
	/**
	 * 获取文件保存目录，如果有SD卡保存到SD卡中，没有SD卡保存到DATA目录
	 * @return
	 */
	public static String getDataPath(Context context) {
		File dir = null;
		String path = getSDPath();
		if (path == null) {
			//dir = Environment.getDataDirectory();
			path = context.getApplicationInfo().dataDir;//dir.toString();
		}
		path = path + "/suntowndata";
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}
	/**
	 * 读取文本文件中的字符串
	 * @param fileName 文件路径
	 * @return 返回该文本的所有字符串数据
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}
	
	/**
	 * 获取BASE64编码的文件数据流
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileBase64(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT)); // 进行Base64编码
			return uploadBuffer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
