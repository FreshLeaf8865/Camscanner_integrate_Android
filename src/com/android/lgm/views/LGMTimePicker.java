package com.android.lgm.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;
import com.android.lgm.views.LGMTimePickerDialog.OnOkClickListener;

public class LGMTimePicker extends LGMCommon {

	private TextView tvName, tvValue;
	private Button btnTimepicker;
	private Calendar mCalendar;
	private LGMTimePickerDialog mTimerDialog;

	public LGMTimePicker(Activity context, String label, String alt,
			String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);

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

		mCalendar = Calendar.getInstance();
	}

	@Override
	public String getHtmlValue() {
		String val = tvValue.getText().toString().trim();
		if (val.equals(context.getResources().getString(
				R.string.not_time_available))) {
			if (isEmpty.toLowerCase().equals("no"))
				return Statics.KEY_RETURN + label + " is missing!";
			else
				return null;
		}
		return "<b>" + label + " : </b>" + val;
	}

	public void setTimeValue(String time) {
		tvValue.setText(time);
	}

	@Override
	public void onClick(View arg0) {
		if (mTimerDialog != null) {
			mTimerDialog.dismiss();
			mTimerDialog = null;
		}
		mTimerDialog = new LGMTimePickerDialog(context, label,
				mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE),
				mCalendar.get(Calendar.AM_PM));
		mTimerDialog.setOnOkClickListener(new OnOkClickListener() {

			@Override
			public void onOkClick(int h, int m, int a_p) {
				mCalendar.set(Calendar.HOUR, h);
				mCalendar.set(Calendar.MINUTE, m);
				mCalendar.set(Calendar.AM_PM, a_p);
				SimpleDateFormat simple = new SimpleDateFormat("hh:mm aa",
						Locale.getDefault());

				tvValue.setText(simple.format(mCalendar.getTime()));
			}
		});
		mTimerDialog.show();
	}

	@Override
	public LGMPairValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
