package com.android.lgm.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.applications.LGMApplication;

public abstract class LGMCommon extends LinearLayout implements OnClickListener{
	protected Activity context;
	protected LinearLayout childView;
	protected LayoutInflater li;
	protected String label;
	protected String isEmpty = "yes";
	protected LGMApplication application;
	protected TextView tv_alt;
	protected String alt;
	
	public LGMCommon(Activity context, String label, String alt, String isEmpty, int layout) {
		super(context);
		this.context = context;
		this.label = label;		
		this.alt = alt;
		if(null != isEmpty) this.isEmpty = isEmpty;
		
		init(layout);
	}
	
	protected void init(int layout) {
		application = (LGMApplication)context.getApplication();
		
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		childView 	= (LinearLayout)li.inflate(layout, null);
		childView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		tv_alt = (TextView)childView.findViewById(R.id.tv_alt);
		if(null != alt){
			tv_alt.setVisibility(View.VISIBLE);
			tv_alt.setText(alt);
			tv_alt.setTextColor(context.getResources().getColor(R.color.alt_text_color));
		}else{
			tv_alt.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View arg0) {}

	public abstract String getHtmlValue();
	
	public abstract LGMPairValue getValue();

}
