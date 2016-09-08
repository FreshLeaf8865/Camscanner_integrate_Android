package com.android.lgm.views;

import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;
import com.android.lgm.utils.ValidateEditText;

/**
 * @author Ninh Van Ngoc
 * 
 */
public class LGMTextField extends LGMCommon {

	private TextView tv;
	private TextView bar;
	private EditText edt;
	private String name;
	protected String replaceLabel;

	public LGMTextField(Activity context, String label, String replaceLabel,
			String alt, String isEmpty, int layout, String validate, String name) {
		super(context, label, alt, isEmpty, layout);

		this.name = name;
		init(layout);

		this.replaceLabel = replaceLabel;
		if (replaceLabel != null) {
			if (replaceLabel.length() > 0) {
				tv.setText(replaceLabel);
			} else {
				tv.setVisibility(View.GONE);
				edt.setVisibility(View.GONE);
				bar.setVisibility(View.GONE);
			}
		} else {
			tv.setText(label);
		}
		if (validate != null) {
			if (validate.equalsIgnoreCase(Statics.STRING)) {
				edt.setInputType(InputType.TYPE_CLASS_TEXT);
				edt.setSingleLine();
			} else if (validate.equalsIgnoreCase(Statics.INTEGER)) {
				edt.setInputType(InputType.TYPE_CLASS_NUMBER);
			} else if (validate.equalsIgnoreCase(Statics.DECIMAL)) {
				edt.setInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			} else {
				edt.setInputType(InputType.TYPE_CLASS_TEXT);
				edt.setSingleLine();
			}
		}

		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);

		tv = (TextView) childView.findViewById(R.id.tv_textfield);
		bar = (TextView) childView.findViewById(R.id.tv_bar);
		edt = (EditText) childView.findViewById(R.id.edt_textfield);
	}

	@Override
	public LGMPairValue getValue() {
		if (isEmpty.equalsIgnoreCase("no")
				&& !ValidateEditText.validateEditText(edt)
				&& edt.getVisibility() == View.VISIBLE) {
			edt.requestFocus();
			return null;
		}
		if ("Ticket".equals(name)) {
			return new LGMPairValue(name, edt.getText().toString().trim());
		}
		if (replaceLabel != null && replaceLabel.length() > 0) {
			return new LGMPairValue(replaceLabel, edt.getText().toString()
					.trim());
		} else {
			return new LGMPairValue(label, edt.getText().toString().trim());
		}
	}

	@Override
	public String getHtmlValue() {
		String val = edt.getText().toString().trim();
		if (val.length() == 0) {
			if (isEmpty.equalsIgnoreCase("no")
					&& edt.getVisibility() == View.VISIBLE) {
				application.setEdt(edt);
				if (replaceLabel != null && replaceLabel.length() > 0) {
					return Statics.KEY_RETURN + replaceLabel + " is missing!";
				} else {
					return Statics.KEY_RETURN + label + " is missing!";
				}
			} else
				return null;
		}
		if (replaceLabel != null && replaceLabel.length() > 0) {
			return "<b>" + replaceLabel + " : </b>" + val;
		} else {
			return "<b>" + label + " : </b>" + val;
		}
	}

}
