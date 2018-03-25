package com.softparticle.miedicinealert.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.softparticle.miedicinealert.database.tables.EventsTable;
import com.softparticle.miedicinealert.database.tables.EventsTable.EventsColumns;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.model.Period;
import com.softparticle.miedicinealert.utils.Utils;

public class EventsDAO implements DAO<Event> {

	private static final String INSERT = "insert into "
			+ EventsTable.TABLE_NAME + "(" + EventsColumns._ID + ", "
			+ EventsColumns.NAME + ", " + EventsColumns.INFO + ", "
			+ EventsColumns.PERIOD + ", " + EventsColumns.PERIOD_UNIT + ", "
			+ EventsColumns.START_TIME + ") values (?, ?, ?, ?, ?, ?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public EventsDAO(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(EventsDAO.INSERT);
	}

	@Override
	public long insert(Event event) {
		insertStatement.clearBindings();
		insertStatement.bindNull(1);
		insertStatement.bindString(2, event.getName());
		insertStatement.bindString(3, event.getInfo());
		insertStatement.bindLong(4, event.getPeriod().getQuantity());
		insertStatement.bindString(5, event.getPeriod().getUnit());
		insertStatement.bindString(6, Utils.dateToString(event.getStartTime()));
		return insertStatement.executeInsert();
	}

	@Override
	public void update(Event event) {
		final ContentValues values = new ContentValues();
		values.put(EventsColumns.NAME, event.getName());
		values.put(EventsColumns.INFO, event.getInfo());
		values.put(EventsColumns.PERIOD, event.getPeriod().getQuantity());
		values.put(EventsColumns.PERIOD_UNIT, event.getPeriod().getUnit());
		values.put(EventsColumns.START_TIME,
				Utils.dateToString(event.getStartTime()));
		db.update(EventsTable.TABLE_NAME, values, BaseColumns._ID + " = ?",
				new String[] { String.valueOf(event.getId()) });

	}

	@Override
	public void remove(long id) {
		db.delete(EventsTable.TABLE_NAME, BaseColumns._ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	@Override
	public Event get(long id) {
		Event event = null;
		Cursor c = db.query(EventsTable.TABLE_NAME, new String[] {
				EventsColumns.NAME, EventsColumns.INFO, EventsColumns.PERIOD,
				EventsColumns.PERIOD_UNIT, EventsColumns.START_TIME },
				BaseColumns._ID + " = ?", new String[] { String.valueOf(id) },
				null, null, null);
		if (c.moveToFirst()) {
			event = new Event();
			event.setId(id);
			event.setName(c.getString(0));
			event.setInfo(c.getString(1));
			event.setPeriod(new Period(c.getInt(2), c.getString(3)));
			event.setStartTime(Utils.stringToDate(c.getString(4)));
		}
		if (!c.isClosed()) {
			c.close();
		}
		return event;
	}

	@Override
	public Event[] getAll() {
		Event[] events = null;
		Cursor c = db.query(EventsTable.TABLE_NAME, new String[] {
				EventsColumns._ID, EventsColumns.NAME, EventsColumns.INFO,
				EventsColumns.PERIOD, EventsColumns.PERIOD_UNIT,
				EventsColumns.START_TIME }, null, null, null, null, null);
		if (c.moveToFirst()) {
			events = new Event[c.getCount()];
			for (int i = 0; i < c.getCount(); i++) {
				events[i] = new Event();
				events[i].setId(c.getLong(0));
				events[i].setName(c.getString(1));
				events[i].setInfo(c.getString(2));
				events[i].setPeriod(new Period(c.getInt(3), c.getString(4)));
				events[i].setStartTime(Utils.stringToDate(c.getString(5)));
				c.moveToNext();
			}
		}
		if (!c.isClosed()) {
			c.close();
		}
		return events;
	}

}
