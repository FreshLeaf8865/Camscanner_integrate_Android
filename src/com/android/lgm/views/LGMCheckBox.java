package com.android.lgm.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;

/**
 * @author Ninh Van Ngoc
 * 
 */
public class LGMCheckBox extends LGMCommon {
	public static String INTEGER = "interger";
	public static String STRING = "string";

	private TextView tv;
	private HashMap<String, ArrayList<LGMObject>> expands;
	private LinearLayout group_views_cb;
	private RadioGroup group_radios;
	private boolean checked = false;
	private RadioButton curRB;

	public LGMCheckBox(Activity context, String label, String alt,
			String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);

		init(layout);

		tv.setText(label);

		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);
		expands = application.getExpands();

		tv = (TextView) childView.findViewById(R.id.tv_checkbox);

		group_radios = (RadioGroup) childView.findViewById(R.id.group_radios);
		group_views_cb = (LinearLayout) childView
				.findViewById(R.id.group_views_cb);
	}

	public boolean isShowChooosen() {
		if (curRB == null) {
			return false;
		}
		LGMObject obj = (LGMObject) curRB.getTag();
		return (null != obj.getOnChosen());
	}
	public LinearLayout getChoosenView(){
		return group_views_cb;
	}

	@Override
	public String getHtmlValue() {
		if (null == curRB) {
			if (isEmpty.equalsIgnoreCase("no"))
				return Statics.KEY_RETURN + label + " is missing!";
			else
				return null;
		}

		StringBuilder sb = new StringBuilder("<b>").append(label).append(
				" : </b>");
		sb.append((null != curRB) ? curRB.getText() : "");

		// Get data from expand views
		LGMObject obj = (LGMObject) curRB.getTag();
		if (null != obj.getOnChosen())
			for (int i = 0; i < group_views_cb.getChildCount(); i++) {
				View v = group_views_cb.getChildAt(i);
				if (v instanceof LGMCommon) {
					String str = ((LGMCommon) v).getHtmlValue();
					if (null != str)
						sb.append("<br/>").append(str);
				}
			}

		return sb.toString();
	}

	public void setRadioView(ArrayList<LGMObject> radios) {
		for (LGMObject o : radios) {
			final RadioButton rd = new RadioButton(context.getBaseContext());
			LayoutParams lp = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.setMargins(15, 0, 15, 0);
			rd.setLayoutParams(lp);
			rd.setText(o.getLabel());
			rd.setButtonDrawable(R.drawable.bg_cb_custom);
			rd.setTextColor(context.getResources().getColor(R.color.text_color));
			rd.setTag(o);

			if (null != o.getOnChosen()) {
				rd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						clearUnCheckAllRadio(arg0);
						group_views_cb.setVisibility(View.VISIBLE);
					}
				});
			} else {
				rd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						clearUnCheckAllRadio(arg0);
						group_views_cb.setVisibility(View.GONE);
					}
				});
			}

			if (null != o.getOnChosen()) {
				setExpandView(o.getOnChosen());
			}

			group_radios.addView(rd);

			if (null != o.getChecked() && !checked) {
				checked = Boolean.parseBoolean(o.getChecked());
				rd.setChecked(true);
				curRB = rd;
			}
		}

		if (checked) {
			group_views_cb.setVisibility(View.VISIBLE);
		} else {
			group_views_cb.setVisibility(View.GONE);
		}
	}

	private void clearUnCheckAllRadio(View v) {
		RadioButton rb = (RadioButton) v;
		for (int i = 0; i < group_radios.getChildCount(); i++) {
			if (!group_radios.getChildAt(i).equals(v))
				((RadioButton) group_radios.getChildAt(i)).setChecked(false);

		}
		if (!rb.isChecked())
			rb.setChecked(true);
		curRB = rb;
	}

	public void setExpandView(String key) {
		for (LGMObject o : expands.get(key)) {
			group_views_cb.addView(Statics.converLGMObject2View(context, o));
		}
	}

	@Override
	public LGMPairValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
