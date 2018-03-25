package com.softparticle.miedicinealert.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.MedicineAlertApp;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.utils.Reminder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RestoreAlarmService extends Service {

	Logger LOG = LoggerFactory.getLogger(RestoreAlarmService.class);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOG.trace("[Start] RestoreAlarmService");
		MedicineAlertApp app = (MedicineAlertApp) getApplication();
		Event[] events = app.getDatabaseManager().getAllEvents();
		if (events != null) {
			for (Event event : events) {
				Reminder.turnON(this, event);
			}
		}
		return START_NOT_STICKY;
	}

}
