package com.android.lgm.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMPairValue;

public class LGMText extends LGMCommon{

	private TextView tv;
	private String value;
	public LGMText(Activity context, String label, String alt, String value, String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);
		
		init(layout);
		this.value = value;
		tv.setText(label);
		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);
		tv = (TextView)childView.findViewById(R.id.tv_text);
	}
	
	
	@Override
	public String getHtmlValue(){
		String res = "<b>" + label + "</b>";
		if(null != value){
			res += " : " + value;
		}
		return res;
	}

	@Override
	public LGMPairValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
