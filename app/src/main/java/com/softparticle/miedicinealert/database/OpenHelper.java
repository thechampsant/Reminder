package com.softparticle.miedicinealert.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.constants.Properties;
import com.softparticle.miedicinealert.database.tables.EventsTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

	Logger LOG = LoggerFactory.getLogger(OpenHelper.class);
	
	private static final int DATABASE_VERSION = 2;
	
	OpenHelper(final Context context) {
		super(context, Properties.DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onOpen(final SQLiteDatabase db) {
		super.onOpen(db);
		if(!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
			Cursor c =db.rawQuery("PRAGMA foreign_keys", null);
			if(c.moveToFirst()) {
				int result = c.getInt(0);
				LOG.info("SQLite foreign key support (1 is on, 0 is off): " + result);
			} else {
				LOG.info("SQLite foreign key support NOT AVAILABLE");
			}
			if(!c.isClosed()) {
				c.close();
			}
		}
	}
	
	public void onCreate(final SQLiteDatabase db) {
		LOG.info("DataHelper.OpenHelper onCreate creating database " + Properties.DATABASE_NAME);
		EventsTable.onCreate(db);
	}
	
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		LOG.info("SQLiteOpenHelper onUpgrade - oldVersion: " + oldVersion + " newVersion: " + newVersion);
		EventsTable.onUpgrade(db, oldVersion, newVersion);
	}

}