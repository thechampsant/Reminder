package com.softparticle.miedicinealert.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.background.AlarmReceiver;
import com.softparticle.miedicinealert.constants.PropKeys;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.model.Period;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Reminder {

	static Logger LOG = LoggerFactory.getLogger(Reminder.class);

	public static void turnON(Context context, Event event) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		long start_time_ms = event.getStartTime().getTime();
		long period_ms = periodToMs(context, event.getPeriod());
		while (start_time_ms < System.currentTimeMillis())
			start_time_ms += period_ms;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time_ms,
				period_ms, prepareIntent(context, event.getId()));
		LOG.debug("Alarm turned ON for event: " + event.getId() + ". Start: "
				+ Utils.dateToString(event.getStartTime()));
	}

	public static void turnOFF(Context context, long event_id) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(prepareIntent(context, event_id));
		LOG.debug("Alarm turned OFF for event: " + event_id);
	}

	private static PendingIntent prepareIntent(Context context, long event_id) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(String.valueOf(event_id));
		intent.putExtra(PropKeys.EVENT_ID, event_id);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		return alarmIntent;
	}

	private static long periodToMs(Context context, Period period) {
		long ms = 1000;
		int periodUnitPos = Utils.getUnitPosition(context, period.getUnit());
		if (periodUnitPos >= 0) // minutes
			ms *= 60;
		if (periodUnitPos >= 1) // hours
			ms *= 60;
		if (periodUnitPos >= 2) // days
			ms *= 24;
		if (periodUnitPos >= 3) // weeks
			ms *= 7;
		ms *= period.getQuantity();
		return ms;
	}
}
