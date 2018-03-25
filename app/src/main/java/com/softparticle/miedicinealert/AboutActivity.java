package com.softparticle.miedicinealert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutActivity extends Activity {

	Logger LOG = LoggerFactory.getLogger(AboutActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LOG.trace("[Create] AboutActivity");
		setContentView(R.layout.activity_about);

	

	}

	@Override
	protected void onStart() {
		LOG.trace("[Start] AboutActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStart(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		LOG.trace("[Stop] AboutActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStop(this);
		super.onStop();
	}

}
