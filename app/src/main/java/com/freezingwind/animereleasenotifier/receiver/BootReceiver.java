package com.freezingwind.animereleasenotifier.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.freezingwind.animereleasenotifier.helpers.AlarmHelper;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			BootReceiver.scheduleAlarm(context);
		}
	}

	// Schedule alarm
	public static void scheduleAlarm(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		int updateInterval = Integer.parseInt(sharedPrefs.getString("updateInterval", "60"));

		AlarmHelper alarmHelper = new AlarmHelper(context, 1000 * 60 * updateInterval, 1000) {
			@Override
			protected PendingIntent pendingIntent(Context context, int flags) {
				Intent intent = new Intent(context, AlarmReceiver.class);
				return PendingIntent.getBroadcast(context, 0, intent, flags);
			}
		};

		alarmHelper.scheduleUnconditionally();
	}
}
