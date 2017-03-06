package com.suntown.suntownshop.receiver;

import com.suntown.suntownshop.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Keep Service Running BroadcastReceiver Called by system(ACTION_TIME_TICK)
 * every minute Start Service When Called
 * 
 * @author Ken
 *
 */
public class CheckServiceBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(intent.ACTION_TIME_TICK)) {
			// ¼ì²éservice×´Ì¬
			if (!Constants.isServiceRunning(context,
					"com.suntown.suntownshop.service.LocalService")) {
				context.startService(new Intent(
						"com.suntown.suntownshop.SERVICE"));
			}
		}
	}

}
