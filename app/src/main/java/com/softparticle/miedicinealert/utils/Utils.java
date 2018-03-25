package com.softparticle.miedicinealert.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.softparticle.miedicinealert.constants.Properties;
import com.softparticle.miedicinealert.R;

public class Utils {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT,
				Locale.getDefault());
		return sdf.format(date);
	}

	public static Date stringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT,
				Locale.getDefault());
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String[] getQuantities() {
		String[] quantities = new String[Properties.MAX_QUANTITY - 1];
		for (int i = 1; i < Properties.MAX_QUANTITY; i++)
			quantities[i - 1] = String.valueOf(i);
		return quantities;
	}
	
	public static int getUnitPosition(Context context, String unit) {
		String[] units = context.getResources().getStringArray(R.array.period_units);
		for(int i=0;i<units.length;i++)
			if(units[i].equals(unit))
				return i;
		return 0;
	}

}
