package com.softparticle.miedicinealert.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {

	Logger LOG = LoggerFactory.getLogger(RebootReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		LOG.debug("Reboot intent received");
		Intent restoreIntent = new Intent(context, RestoreAlarmService.class);
		context.startService(restoreIntent);
	}

}
