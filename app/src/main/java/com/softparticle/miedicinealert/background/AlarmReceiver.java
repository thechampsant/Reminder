package com.softparticle.miedicinealert.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.InfoActivity;
import com.softparticle.miedicinealert.constants.PropKeys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	Logger LOG = LoggerFactory.getLogger(AlarmReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.debug("Alarm intent received: id = "
				+ intent.getLongExtra(PropKeys.EVENT_ID, -1));
		Intent reminderIntent = new Intent(context, InfoActivity.class);
		reminderIntent.putExtras(intent.getExtras());
		reminderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(reminderIntent);
	}

}
