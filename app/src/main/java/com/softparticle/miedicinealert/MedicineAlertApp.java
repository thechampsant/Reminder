package com.softparticle.miedicinealert;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APPLICATION_LOG;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.AVAILABLE_MEM_SIZE;
import static org.acra.ReportField.BUILD;
import static org.acra.ReportField.CRASH_CONFIGURATION;
import static org.acra.ReportField.CUSTOM_DATA;
import static org.acra.ReportField.DEVICE_FEATURES;
import static org.acra.ReportField.DISPLAY;
import static org.acra.ReportField.ENVIRONMENT;
import static org.acra.ReportField.INITIAL_CONFIGURATION;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PACKAGE_NAME;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.SETTINGS_GLOBAL;
import static org.acra.ReportField.SETTINGS_SECURE;
import static org.acra.ReportField.SETTINGS_SYSTEM;
import static org.acra.ReportField.SHARED_PREFERENCES;
import static org.acra.ReportField.STACK_TRACE;
import static org.acra.ReportField.TOTAL_MEM_SIZE;
import static org.acra.ReportField.USER_APP_START_DATE;
import static org.acra.ReportField.USER_CRASH_DATE;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.annotation.SuppressLint;
import android.app.Application;

import com.softparticle.miedicinealert.database.DatabaseManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

@SuppressLint("SdCardPath")
@ReportsCrashes(formKey = "", customReportContent = { APP_VERSION_NAME,
		APP_VERSION_CODE, ANDROID_VERSION, PHONE_MODEL, PACKAGE_NAME, BUILD,
		TOTAL_MEM_SIZE, AVAILABLE_MEM_SIZE, INITIAL_CONFIGURATION,
		CRASH_CONFIGURATION, DISPLAY, USER_APP_START_DATE, USER_CRASH_DATE,
		DEVICE_FEATURES, ENVIRONMENT, SETTINGS_GLOBAL, SETTINGS_SYSTEM,
		SETTINGS_SECURE, SHARED_PREFERENCES, APPLICATION_LOG, CUSTOM_DATA,
		STACK_TRACE, LOGCAT }, formUri = "http://support.appliccon.com/mobile/android/addmobilelog.php", formUriBasicAuthLogin = "support", formUriBasicAuthPassword = "JPW-wwp39DJ", httpMethod = org.acra.sender.HttpSender.Method.POST, mode = ReportingInteractionMode.SILENT, reportType = org.acra.sender.HttpSender.Type.JSON, applicationLogFile = "/data/data/com.appliccon.pillsreminder/files/log/PillsReminder.log")
public class MedicineAlertApp extends Application {

	private Tracker app_tracker;

	private DatabaseManager databaseManager;

	public DatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}

	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		app_tracker = analytics.newTracker(R.xml.app_tracker);
		databaseManager = new DatabaseManager(this);
	}

	public Tracker getAppTracker() {
		return app_tracker;
	}

}
