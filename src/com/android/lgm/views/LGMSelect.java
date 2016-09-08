package com.android.lgm.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMOption;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;

/**
 * @author Ninh Van Ngoc
 *
 */
public class LGMSelect extends LGMCommon {
	public static String INTEGER = "interger";
	public static String STRING = "string";

	private LinearLayout group_expand_sl;
	private TextView tv;
	private Spinner spin;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> strops;
	private HashMap<String, Integer> index;
	private HashMap<String, ArrayList<LGMObject>> expands;
	private String name;

	public LGMSelect(Activity context, String label, String alt, String isEmpty, int layout, String name) {
		super(context, label, alt, isEmpty, layout);
		this.name = name;
		init(layout);

		tv.setText(label);
		spin.setPrompt(label);

		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);
		expands = application.getExpands();

		group_expand_sl = (LinearLayout) childView.findViewById(R.id.group_expand_sl);
		index = new HashMap<String, Integer>();

		tv = (TextView) childView.findViewById(R.id.tv_select);
		spin = (Spinner) childView.findViewById(R.id.spin_select);

		strops = new ArrayList<String>();
	}

	public void setOptions(final ArrayList<LGMOption> options) {
		for (LGMOption op : options) {
			strops.add(op.getText());

			if (null != op.getOnChosen() && !index.containsKey(op.getOnChosen())) {
				index.put(op.getOnChosen(), index.size());

				LinearLayout ll = new LinearLayout(context.getBaseContext());
				ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				ll.setOrientation(LinearLayout.VERTICAL);

				for (LGMObject o : expands.get(op.getOnChosen())) {
					ll.addView(Statics.converLGMObject2View(context, o));
				}

				group_expand_sl.addView(ll);
			}
		}
		adapter = new ArrayAdapter<String>(context.getBaseContext(), android.R.layout.simple_spinner_item, strops);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);

		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				for (int i = 0; i < group_expand_sl.getChildCount(); i++) {
					String KEY = options.get(pos).getOnChosen();
					if (null != KEY && index.get(KEY) == i) {
						group_expand_sl.getChildAt(i).setVisibility(View.VISIBLE);
					} else {
						group_expand_sl.getChildAt(i).setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public LGMPairValue getValue() {
		return new LGMPairValue(name, strops.get(spin.getSelectedItemPosition()));
	}

	@Override
	public String getHtmlValue() {
		StringBuilder sb = new StringBuilder("<b>").append(label).append(" : </b>");
		try {
			sb.append(strops.get(spin.getSelectedItemPosition()));
		} catch (Exception e) {
		}

		// Get data from expand views
		for (int i = 0; i < group_expand_sl.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) group_expand_sl.getChildAt(i);
			if (ll.getVisibility() == View.VISIBLE) {
				for (int j = 0; j < ll.getChildCount(); j++) {
					View v = ll.getChildAt(j);
					if (v instanceof LGMCommon) {
						String str = ((LGMCommon) v).getHtmlValue();
						if (null != str)
							sb.append("<br/>").append(str);
					}
				}
			}
		}

		return sb.toString();
	}

	public List<LGMPairValue> getCameraAndSignatureValue() {
		List<LGMPairValue> result = new ArrayList<LGMPairValue>();
		for (int i = 0; i < group_expand_sl.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) group_expand_sl.getChildAt(i);
			if (ll.getVisibility() == View.VISIBLE) {
				for (int j = 0; j < ll.getChildCount(); j++) {
					View v = ll.getChildAt(j);
					if (v instanceof LGMCamera) {
						if (((LGMCamera) v).getValue() != null) {
							result.add(((LGMCamera) v).getValue());
						}
					} else if (v instanceof LGMSignature) {
						if (((LGMSignature) v).getValue() != null) {
							result.add(((LGMSignature) v).getValue());
						}
					}
					
				}
			}
		}
		return result;
	}

	public Spinner getSpinner() {
		return spin;
	}
}
