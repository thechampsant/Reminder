package com.softparticle.miedicinealert.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.database.dao.EventsDAO;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.utils.Reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager {

	private Context context;
	private SQLiteDatabase db;

	private EventsDAO eventsDAO;

	public DatabaseManager(Context context) {

		Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);

		this.context = context;

		SQLiteOpenHelper openHelper = new OpenHelper(this.context);
		db = openHelper.getWritableDatabase();
		LOG.info("DatabaseManager created, db open status: " + db.isOpen());

		eventsDAO = new EventsDAO(db);
	}

	public SQLiteDatabase gedDb() {
		return db;
	}

	public Event[] getAllEvents() {
		return eventsDAO.getAll();
	}

	public void removeEvent(long id) {
		Reminder.turnOFF(context, id);
		eventsDAO.remove(id);
	}

	public void saveEvent(Event event) {
		long id = event.getId();
		if (id < 0)
			event.setId(eventsDAO.insert(event));
		else
			eventsDAO.update(event);
		Reminder.turnON(context, event);
	}

	public Event getEvent(long id) {
		return eventsDAO.get(id);
	}

}
