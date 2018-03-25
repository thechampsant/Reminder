package com.softparticle.miedicinealert;


import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

public class TemplateActivity extends AppCompatActivity {

	private AdView adView;
	//use your own AdMob ID
	private static final String AD_UNIT_ID = "ca-app-pub-1701219126323577/9129048448";

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}



}
