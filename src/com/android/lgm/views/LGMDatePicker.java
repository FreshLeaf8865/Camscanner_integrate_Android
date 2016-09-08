package com.android.lgm.views;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;

public class LGMDatePicker extends LGMCommon {
	private TextView tvName, tvValue;
	private Button btnTimepicker;
	private int mYear, mMonth, mDay;

	public LGMDatePicker(Activity context, String label, String alt,
			String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		init(layout);

		tvName.setText(label);
		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);

		tvName = (TextView) childView.findViewById(R.id.tv_timepicker_title);
		tvValue = (TextView) childView.findViewById(R.id.tv_timepicker_value);
		btnTimepicker = (Button) childView.findViewById(R.id.btn_timepicker);
		btnTimepicker.setOnClickListener(this);

		tvValue.setText(getResources().getString(R.string.not_date_available));
	}

	@Override
	public LGMPairValue getValue() {
		return null;
	}

	@Override
	public String getHtmlValue() {
		String val = tvValue.getText().toString().trim();
		if (val.equals(context.getResources().getString(
				R.string.not_date_available))) {
			if (isEmpty.toLowerCase().equals("no"))
				return Statics.KEY_RETURN + label + " is missing!";
			else
				return null;
		}
		return "<b>" + label + " : </b>" + val;
	}

	public void setDateValue(String date) {
		tvValue.setText(date);
	}

	public void setDateValue() {
		mMonth++;
		tvValue.setText(mYear + "-" + (mMonth < 10 ? "0" + mMonth : mMonth)
				+ "-" + (mDay < 10 ? "0" + mDay : mDay));
	}

	@Override
	public void onClick(View arg0) {
		new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;

				setDateValue();
			}
		}, mYear, mMonth, mDay).show();
	}
}
