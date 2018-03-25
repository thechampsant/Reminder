package com.softparticle.miedicinealert.model;

import com.softparticle.miedicinealert.MainActivity;
import com.softparticle.miedicinealert.MedicineAlertApp;
import com.softparticle.miedicinealert.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventsAdapter extends ArrayAdapter<Event> {
	Context context;
	int layoutResourceId;
	Event data[] = null;
	MedicineAlertApp app = null;

	public EventsAdapter(Context context, int layoutResourceId, Event[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.app = (MedicineAlertApp) ((Activity) context).getApplication();
	}

	public long getItemId(int position) {
		return data[position].getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ViewHolder();
			holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
			holder.tv_period = (TextView) row.findViewById(R.id.tv_period);
			holder.btn_delete = (Button) row.findViewById(R.id.btn_delete);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		holder.tv_name.setText(data[position].getName());
		holder.tv_period.setText(context.getResources().getString(
				R.string.period)
				+ " "
				+ data[position].getPeriod().getQuantity()
				+ " "
				+ data[position].getPeriod().getUnit());

		final int pos = position;
		holder.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.getDatabaseManager().removeEvent(data[pos].getId());
				((MainActivity) context).refresh();
			}
		});
		return row;
	}

	private class ViewHolder {
		TextView tv_name;
		TextView tv_period;
		Button btn_delete;
	}

}
