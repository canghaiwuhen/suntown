package com.suntown.suntownshop;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.suntown.suntownshop.Constants.Config;
import com.suntown.suntownshop.handler.CrashHandler;
import com.suntown.suntownshop.receiver.CheckServiceBroadcastReceiver;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.widget.Toast;
/**
 * 主程序
 *
 * @author 钱凯
 * @version 2014年12月21日 上午9:55:06
 *
 */
public class UILApplication extends Application {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		// 未捕获异常处理
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}
		if (!Constants.isServiceRunning(getApplicationContext(),
				"com.suntown.suntownshop.service.LocalService")) {
			startService(new Intent("com.suntown.suntownshop.SERVICE"));
		}
		initReceiver();
		initImageLoader(getApplicationContext());
		super.onCreate();

	}

	private void initReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		CheckServiceBroadcastReceiver receiver = new CheckServiceBroadcastReceiver();
		registerReceiver(receiver, filter);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}
}
