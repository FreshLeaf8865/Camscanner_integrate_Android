package com.android.lgm.views;

import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.martin.lgmsolutions.R;

public class LGMTimePickerDialog extends Dialog implements
		android.view.View.OnClickListener, OnTimeChangedListener {
	/*
	 * private fields
	 */
	private Context context;
	private LayoutInflater li;
	private View v;
	private TimePicker tp;
	private Button btn_30, btn_15, btn_5, btn_1, btn_done, btn_now, btn_cancel;
	private OnOkClickListener onOk;
	private Calendar mCalendar;
	private int CUR_BUTTON_INDEX;
	private LinearLayout group_btn_mins;
	private Button mBtnAMPM;

	/*
	 * constructor
	 */
	public LGMTimePickerDialog(Context context, String title, int h, int m,
			int a_p) {
		super(context);
		this.context = context;
		this.mCalendar = Calendar.getInstance(Locale.getDefault());
		this.mCalendar.set(Calendar.HOUR, h);
		this.mCalendar.set(Calendar.MINUTE, m);
		this.mCalendar.set(Calendar.AM_PM, a_p);
		init();
		this.setTitle(title);
		this.setContentView(v);
		this.setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	/*
	 * initialize method
	 */
	private void init() {
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.layout_timepicker_dialog, null);

		group_btn_mins = (LinearLayout) v.findViewById(R.id.group_btn_mins);

		tp = (TimePicker) v.findViewById(R.id.tpk_tpd);
		btn_30 = (Button) v.findViewById(R.id.btn_tpd_30);
		btn_15 = (Button) v.findViewById(R.id.btn_tpd_15);
		btn_5 = (Button) v.findViewById(R.id.btn_tpd_5);
		btn_1 = (Button) v.findViewById(R.id.btn_tpd_1);
		btn_done = (Button) v.findViewById(R.id.btn_tpd_done);
		btn_now = (Button) v.findViewById(R.id.btn_tpd_now);
		btn_cancel = (Button) v.findViewById(R.id.btn_tpd_cancel);

		btn_30.setOnClickListener(this);
		btn_15.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_done.setOnClickListener(this);
		btn_now.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		tp.setCurrentHour(mCalendar.get(Calendar.HOUR));
		tp.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
		tp.setIs24HourView(false);
		tp.setFocusable(false);
		ViewGroup vg = (ViewGroup) tp.getChildAt(0);
		try {
			mBtnAMPM = (Button) vg.getChildAt(vg.getChildCount() - 1);
		} catch (Exception e) {
		}
		if (mCalendar.get(Calendar.AM_PM)==Calendar.PM) {
			mBtnAMPM.setText("PM");
			mCalendar.set(Calendar.AM_PM, Calendar.PM);
		} else {
			mBtnAMPM.setText("AM");
			mCalendar.set(Calendar.AM_PM, Calendar.AM);
		}
		if (mBtnAMPM != null) {
			mBtnAMPM.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (mBtnAMPM.getText().toString().equals("AM")) {
						mBtnAMPM.setText("PM");
						mCalendar.set(Calendar.AM_PM, Calendar.PM);
					} else {
						mBtnAMPM.setText("AM");
						mCalendar.set(Calendar.AM_PM, Calendar.AM);
					}
					tp.setOnTimeChangedListener(null);
					// updateTime(tp, mCalendar.get(Calendar.HOUR_OF_DAY),
					// mCalendar.get(Calendar.MINUTE));
					tp.setOnTimeChangedListener(LGMTimePickerDialog.this);
				}
			});
		}
		tp.setOnTimeChangedListener(this);

		CUR_BUTTON_INDEX = 1;
		group_btn_mins.getChildAt(1).setBackgroundResource(
				R.drawable.tab_button_selected);
	}

	/*
	 * public methods
	 */

	private void updateTime(TimePicker timepicker, int h, int m) {
		h = h % 12;
		int hour = mCalendar.get(Calendar.HOUR);
		int min = mCalendar.get(Calendar.MINUTE);
		if (min != m) {
			int minute = mCalendar.get(Calendar.MINUTE);
			int dTime;
			if (hour < h && !(hour == 0 && h == 11) || (hour == h && min < m)) {
				switch (CUR_BUTTON_INDEX) {
				case 0: // increase 30 mins
					dTime = (minute / 30 + 1) * 30 - minute;
					break;

				case 1: // increase 15 mins
					dTime = (minute / 15 + 1) * 15 - minute;
					break;

				case 2: // increase 5 mins
					dTime = (minute / 5 + 1) * 5 - minute;
					break;
				default:
					dTime = 1;
					break;
				}

			} else {
				switch (CUR_BUTTON_INDEX) {
				case 0: // decrease 30 mins
					dTime = minute % 30 == 0 ? -30 : 30 * (minute / 30)
							- minute;
					break;

				case 1: // decrease 15 mins
					dTime = minute % 15 == 0 ? -15 : 15 * (minute / 15)
							- minute;
					break;

				case 2: // decrease 5 mins
					dTime = minute % 5 == 0 ? -5 : 5 * (minute / 5) - minute;
					break;
				default:
					dTime = -1;
					break;
				}
			}
			mCalendar.add(Calendar.MINUTE, dTime);
		} else {
			mCalendar.set(Calendar.HOUR_OF_DAY, h);
		}
		if (tp != null) {
			tp.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
			tp.setCurrentHour(mCalendar.get(Calendar.HOUR));
		}
	}

	public void setOnOkClickListener(OnOkClickListener onOk) {
		this.onOk = onOk;
	}

	public interface OnOkClickListener {
		public void onOkClick(int h, int m, int am_pm);
	}

	public void show() {
		super.show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_tpd_30) {
			updateButton(0);
		} else if (v.getId() == R.id.btn_tpd_15) {
			updateButton(1);
		} else if (v.getId() == R.id.btn_tpd_5) {
			updateButton(2);
		} else if (v.getId() == R.id.btn_tpd_1) {
			updateButton(3);
		} else if (v.getId() == R.id.btn_tpd_done) {
			if (null != onOk)

				onOk.onOkClick(mCalendar.get(Calendar.HOUR), mCalendar
						.get(Calendar.MINUTE), mBtnAMPM.getText().toString()
						.equals("AM") ? Calendar.AM : Calendar.PM);
			this.dismiss();
		} else if (v.getId() == R.id.btn_tpd_now) {
			Calendar c = Calendar.getInstance();
			mCalendar.set(Calendar.HOUR, c.get(Calendar.HOUR));
			mCalendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
			tp.setCurrentHour(c.get(Calendar.HOUR));
			tp.setCurrentMinute(c.get(Calendar.MINUTE));
		} else if (v.getId() == R.id.btn_tpd_cancel) {
			this.dismiss();
		}
	}

	private void updateButton(int index) {
		if (CUR_BUTTON_INDEX == index)
			return;
		group_btn_mins.getChildAt(CUR_BUTTON_INDEX).setBackgroundResource(
				R.drawable.tab_button);
		group_btn_mins.getChildAt(index).setBackgroundResource(
				R.drawable.tab_button_selected);
		CUR_BUTTON_INDEX = index;
	}

	@Override
	public void onTimeChanged(TimePicker timepicker, int h, int m) {
		timepicker.setOnTimeChangedListener(null);
		updateTime(timepicker, h, m);
		timepicker.setOnTimeChangedListener(this);
	}
}
