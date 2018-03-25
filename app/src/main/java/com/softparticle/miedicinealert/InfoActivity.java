package com.softparticle.miedicinealert;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.constants.PropKeys;
import com.softparticle.miedicinealert.constants.Properties;
import com.softparticle.miedicinealert.model.Event;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends TemplateActivity {

	Logger LOG = LoggerFactory.getLogger(InfoActivity.class);

	private MedicineAlertApp app;
	private ScheduledExecutorService scheduler;
	private Event event;
	private TextToSpeech tts;
	private boolean isTTSenabled;
	private int previousVolume;
	private AudioManager am;
	private MediaPlayer alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LOG.trace("[Create] InfoActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		getSupportActionBar().hide();
		app = (MedicineAlertApp) getApplication();
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		previousVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);

		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		alarm = MediaPlayer.create(getApplicationContext(), notification);
		tts = new TextToSpeech(this, new OnInitListener() {
			@Override
			public void onInit(int status) {
				isTTSenabled = false;
				if (status == TextToSpeech.SUCCESS) {
					int result = tts.setLanguage(Locale.getDefault());
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						LOG.info("TTS", "This Language is not supported");
					} else {
						isTTSenabled = true;
					}
				} else {
					LOG.warn("TTS initialization failed");
				}
			}
		});

		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_info = (TextView) findViewById(R.id.tv_info);
		Button btn_turn_off = (Button) findViewById(R.id.btn_turn_off);

		long event_id = getIntent().getLongExtra(PropKeys.EVENT_ID, -1);
		event = app.getDatabaseManager().getEvent(event_id);
		if (event != null) {
			tv_name.setText(event.getName());
			tv_info.setText(event.getInfo());
			btn_turn_off.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					scheduler.shutdown();
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							previousVolume, 0);
					InfoActivity.this.finish();
				}
			});
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
					if (isTTSenabled && !event.getInfo().isEmpty()) {
						speakOut();
					} else {
						if (!alarm.isPlaying())
							alarm.start();
					}
				}
			}, 0, Properties.SIGNAL_PAUSE, TimeUnit.SECONDS);
		} else {
			LOG.warn("Event is null for event_id = " + event_id);
			finish();
		}
	}

	@Override
	public void onStart() {
		LOG.trace("[Start] InfoActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStart(this);
		super.onStart();
	}

	@Override
	public void onStop() {
		LOG.trace("[Stop] InfoActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStop(this);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		am.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	private void speakOut() {
		LOG.debug("Speak: " + event.getInfo());
		tts.speak(event.getInfo(), TextToSpeech.QUEUE_FLUSH, null);
	}

}
