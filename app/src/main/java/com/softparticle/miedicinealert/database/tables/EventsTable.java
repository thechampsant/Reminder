package com.softparticle.miedicinealert.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class EventsTable {
	
	public static final String TABLE_NAME = "Events";
	
	public static class EventsColumns implements BaseColumns {
		public static final String NAME = "name";
		public static final String INFO = "info";
		public static final String PERIOD = "period";
		public static final String PERIOD_UNIT = "per_unit";
		public static final String START_TIME = "start_time";
	}
	
	public static void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + EventsTable.TABLE_NAME + " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sb.append(EventsColumns.NAME + " TEXT, ");
		sb.append(EventsColumns.INFO + " TEXT, ");
		sb.append(EventsColumns.PERIOD + " INTEGER, ");
		sb.append(EventsColumns.PERIOD_UNIT + " TEXT, ");
		sb.append(EventsColumns.START_TIME + " TEXT");
		sb.append(");");
		db.execSQL(sb.toString());
	}
	
	public static void onUpgrade(SQLiteDatabase db,
			int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + EventsTable.TABLE_NAME);
		EventsTable.onCreate(db);
	}
	
}
