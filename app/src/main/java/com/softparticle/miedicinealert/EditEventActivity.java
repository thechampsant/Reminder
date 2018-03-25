package com.softparticle.miedicinealert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softparticle.miedicinealert.constants.MessageTypes;
import com.softparticle.miedicinealert.constants.PropKeys;
import com.softparticle.miedicinealert.model.Event;
import com.softparticle.miedicinealert.model.Period;
import com.softparticle.miedicinealert.utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class EditEventActivity extends TemplateActivity {

	Logger LOG = LoggerFactory.getLogger(EditEventActivity.class);

	private MedicineAlertApp app;
	private CommonAlert alert;
	private SimpleDateFormat time_format;
	private SimpleDateFormat date_format;
	private Calendar calendar;
	private Event event;

	private EditText et_name;
	private EditText et_info;
	private Spinner sp_period_q;
	private Spinner sp_period_u;
	private Button btn_time;
	private Button btn_date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LOG.trace("[Create] EditEventActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);

		app = (MedicineAlertApp) getApplication();
		long id = getIntent().getLongExtra(PropKeys.EVENT_ID, -1);
		if (id != -1)
			event = app.getDatabaseManager().getEvent(id);
		time_format = new SimpleDateFormat("HH:mm", Locale.getDefault());
		date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		alert = new CommonAlert(this);

		et_name = (EditText) findViewById(R.id.et_name);
		et_info = (EditText) findViewById(R.id.et_info);
		sp_period_q = (Spinner) findViewById(R.id.spin_per_q);
		sp_period_u = (Spinner) findViewById(R.id.spin_per_u);
		btn_time = (Button) findViewById(R.id.btn_time);
		btn_date = (Button) findViewById(R.id.btn_date);
		Button btn_save = (Button) findViewById(R.id.btn_save);

		sp_period_q.setAdapter(new ArrayAdapter<String>(this,
				R.layout.spinner_item, Utils.getQuantities()));
		sp_period_u.setAdapter(ArrayAdapter.createFromResource(this,
				R.array.period_units, R.layout.spinner_item));

		calendar = Calendar.getInstance();
		if (event != null) {
			et_name.setText(event.getName());
			et_info.setText(event.getInfo());
			sp_period_q.setSelection(event.getPeriod().getQuantity() - 1);
			sp_period_u.setSelection(Utils.getUnitPosition(this, event
					.getPeriod().getUnit()));
			calendar.setTimeInMillis(event.getStartTime().getTime());
		}
		if (savedInstanceState != null) {
			calendar.setTimeInMillis(savedInstanceState.getLong("calendar"));
		}

		btn_time.setText(time_format.format(calendar.getTime()));
		btn_date.setText(date_format.format(calendar.getTime()));
		btn_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerDialog time_dialog = new TimePickerDialog(
						EditEventActivity.this, new OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								btn_time.setText(time_format.format(calendar
										.getTime()));
							}
						}, calendar.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);
				time_dialog.show();
			}
		});
		btn_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog date_dialog = new DatePickerDialog(
						EditEventActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								calendar.set(Calendar.YEAR, year);
								calendar.set(Calendar.MONTH, monthOfYear);
								calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								btn_date.setText(date_format.format(calendar
										.getTime()));
							}
						}, calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				date_dialog.show();
			}
		});
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = et_name.getText().toString();
				String info = et_info.getText().toString();
				if (name.isEmpty()) {
					alert.showMessage(MessageTypes.EMPTY_NAME);
				} else {
					if (event == null)
						event = new Event();
					event.setName(name);
					event.setInfo(info);
					event.setPeriod(new Period(Integer.parseInt(sp_period_q
							.getSelectedItem().toString()), sp_period_u
							.getSelectedItem().toString()));
					event.setStartTime(Utils.stringToDate(btn_date.getText()
							+ " " + btn_time.getText()));
					app.getDatabaseManager().saveEvent(event);
					EditEventActivity.this.finish();
				}
			}
		});
	}

	@Override
	public void onStart() {
		LOG.trace("[Start] EditEventActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStart(this);
		super.onStart();
	}

	@Override
	public void onStop() {
		LOG.trace("[Stop] EditEventActivity");
		GoogleAnalytics.getInstance(getBaseContext()).reportActivityStop(this);
		super.onStop();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putLong("calendar", calendar.getTimeInMillis());
	}

}
