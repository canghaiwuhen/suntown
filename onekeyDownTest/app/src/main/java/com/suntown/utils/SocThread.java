package com.suntown.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocThread extends Thread {
	private  String socketStr;
//	private int port = 13000;
	private String TAG = "socket thread";
	private int timeout = 10000;

	public Socket client = null;
	OutputStream out;
	InputStream in;
//	BufferedWriter out;
//	BufferedReader in;
	public boolean isRun = true;
	Handler inHandler;
	Handler outHandler;
	Context ctx;
	private String TAG1 = "===send===";
	String moduleip;
//	String str;
	byte[] bytes;
	public SocThread(Handler handlerin, Handler handlerout, Context context, String module_ip,byte[] buff) {
		inHandler = handlerin;
		outHandler = handlerout;
		ctx = context;
		moduleip=module_ip;
//		str=socketStr;
		bytes=buff;
		Log.i(TAG, "创建线程socket");
	}
	public void conn() {
		try {
			Log.i(TAG, "连接中");
			client = new Socket();
			SocketAddress remoteAddr = new InetSocketAddress(moduleip,Constant.PORT);
			client.connect(remoteAddr,timeout);
			Log.i(TAG, "连接成功");
			in = client.getInputStream();
			out = client.getOutputStream();
			Log.i(TAG, "输入输出流获取成功");
		} catch (UnknownHostException e) {
			Log.i(TAG, "连接错误UnknownHostException 重新获取");
			e.printStackTrace();
			conn();
		} catch (IOException e) {
			Log.i(TAG, "连接服务器io错误");
			e.printStackTrace();
		} catch (Exception e) {
			Log.i(TAG, "连接服务器错误Exception" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 实时接受数据
	 */
	@Override
	public void run() {
		Log.i(TAG, "线程socket开始运行");
		Log.i(TAG, "1.run开始");
		conn();
//		String line = "";
		byte[] buffer = new byte[64];
		while (isRun) {
			try {
				if (client .isConnected()) {
					send(bytes);
					Log.i(TAG, "2.检测数据");
					if (!client.isInputShutdown()) {
//						if ((line = in.readline()) != null) {
						int read = in.read(buffer);
						if (read!=-1) {
//							Log.i(TAG, "3.getdata" + line + " len=" + line.length());
							Log.i(TAG, "4.start set Message");
							byte[] bytes = new byte[read];
							for (int i = 0; i < read; i++) {
								bytes[i] = buffer[i];
							}
							Message msg = inHandler.obtainMessage();
							msg.obj = bytes;
							inHandler.sendMessage(msg);// 结果返回UI处理
							Log.i(TAG1, "5.send to handler");
						}
					}
				} else {
					Log.i(TAG, "没有可用连接");
					conn();
				}
			} catch (Exception e) {
				Log.i(TAG, "数据接收错误" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送数据
	 * 
	 * @param
	 */
	public void send(byte[] bytes) {
		try {
			if (client != null) {
				Log.i(TAG1, "发送" + bytes + "至" + client.getInetAddress().getHostAddress() + ":" + String.valueOf(client.getPort()));
				out.write(bytes,0,bytes.length);
				out.flush();
				Log.i(TAG1, "发送成功");
				Message msg = outHandler.obtainMessage();
				msg.obj = bytes;
				msg.what = 1;
				outHandler.sendMessage(msg);// 结果返回给UI处理
			} else {
				Log.i(TAG, "client 不存在");
				Message msg = outHandler.obtainMessage();
				msg.obj = bytes;
				msg.what = 0;
				outHandler.sendMessage(msg);// 结果返回给UI处理
				Log.i(TAG, "连接不存在重新连接");
				conn();
			}

		} catch (Exception e) {
			Log.i(TAG1, "send error");
			e.printStackTrace();
		} finally {
			Log.i(TAG1, "发送完毕");

		}
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		try {
			if (client != null) {
				Log.i(TAG, "close in");
				in.close();
				Log.i(TAG, "close out");
				out.close();
				Log.i(TAG, "close client");
				client.close();
			}
		} catch (Exception e) {
			Log.i(TAG, "close err");
			e.printStackTrace();
		}

	}
}
