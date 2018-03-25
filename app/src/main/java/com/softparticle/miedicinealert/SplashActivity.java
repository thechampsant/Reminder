package com.softparticle.miedicinealert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.android.gms.analytics.GoogleAnalytics;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	Logger LOG = LoggerFactory.getLogger(SplashActivity.class);

	private static int SPLASH_TIME_OUT = 1500;
	private Handler handler;
	private Runnable starter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LOG.trace("[CREATE] SplashActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		handler = new Handler();
		starter = new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);

				finish();
			}
		};
		handler.postDelayed(starter, SPLASH_TIME_OUT);
	}

	@Override
	protected void onStart() {
		LOG.trace("[Start] SplashActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStart(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		LOG.trace("[Stop] SplashActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStop(this);
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		handler.removeCallbacks(starter);
		super.onBackPressed();
	}

}
