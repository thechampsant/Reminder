package com.softparticle.miedicinealert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.constants.PropKeys;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.model.EventsAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends TemplateActivity {

	Logger LOG = LoggerFactory.getLogger(MainActivity.class);

	private MedicineAlertApp app;
	private Event[] data;
	private EventsAdapter adapter;

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LOG.trace("[Create] MainActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		app = (MedicineAlertApp) getApplication();
		
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						EditEventActivity.class);
				intent.putExtra(PropKeys.EVENT_ID, id);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(MainActivity.this,
					EditEventActivity.class);
			intent.putExtra(PropKeys.EVENT_ID, -1);
			startActivity(intent);
			return true;
		case R.id.action_about:
			LOG.debug("[About] Display about");
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onStart() {
		LOG.trace("[Start] MainActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStart(this);
		super.onStart();
		refresh();
	}

	@Override
	public void onStop() {
		LOG.trace("[Stop] MainActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStop(this);
		super.onStop();
	}

	public void refresh() {
		data = app.getDatabaseManager().getAllEvents();
		if (data == null)
			data = new Event[0];
		adapter = new EventsAdapter(this, R.layout.event_item, data);
		list.setAdapter(adapter);
	}

}
