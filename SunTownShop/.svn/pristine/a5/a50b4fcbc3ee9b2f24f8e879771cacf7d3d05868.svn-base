package com.suntown.suntownshop.receiver;

import com.suntown.suntownshop.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Boot Completed BroadcastReceiver,Called when system Boot/Reboot Completed
 * 
 * @author ken
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			if (!Constants.isServiceRunning(context,
					"com.suntown.suntownshop.service.LocalService")) {
				context.startService(new Intent("com.suntown.suntownshop.SERVICE"));
			}
		}

	}

}
