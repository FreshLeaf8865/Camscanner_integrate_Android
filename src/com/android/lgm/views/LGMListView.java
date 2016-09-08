package com.android.lgm.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lgm.MainActivity;
import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;
import com.android.lgm.views.LGMListViewDialog.OnOkClickListener;

/**
 * @author Ninh Van Ngoc
 *
 */
public class LGMListView extends LGMCommon{
	
	private TextView tv;
	private ImageView btn_add_lv;
	
	private LinearLayout table, tr_first;
	private ArrayList<LGMObject> viewFields;
	
	
	public LGMListView(Activity context, String label, String alt, String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);

		init(layout);
		
		tv.setText(label);
		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);

		tv			= (TextView)childView.findViewById(R.id.tv_listview);
		table = (LinearLayout)childView.findViewById(R.id.tb_lisview);
		tr_first = (LinearLayout)childView.findViewById(R.id.tr_first);
		btn_add_lv = (ImageView)childView.findViewById(R.id.btn_add_lv);
		btn_add_lv.setOnClickListener(this);
	}

	public void setListViewField(ArrayList<LGMObject> viewFields){
		this.viewFields = viewFields;
		
		tr_first.setWeightSum(viewFields.size()+1);
		for(LGMObject o:viewFields){
			tr_first.addView(getTextView(o.getLabel()));
		}
		
		tr_first.addView(getTextView("Action"));
	}
	
	private TextView getTextView(String name){
		TextView tv = new TextView(context);
		LayoutParams lp = new LayoutParams(1, LayoutParams.WRAP_CONTENT, 1f);
		lp.setMargins(3, 0, 3, 0);
		
		tv.setLayoutParams(lp);
		tv.setText(name);
		tv.setTextColor(context.getResources().getColor(R.color.white_color));
		tv.setSingleLine();
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	@Override
	public String getHtmlValue(){
		if(table.getChildCount() == 1){
			if(isEmpty.toLowerCase().equals("no"))
				return Statics.KEY_RETURN + label+" is missing!";
			else
				return null;
		}
		
		StringBuilder sb = new StringBuilder("<br/>");
		sb.append("<b>").append(label).append("</b><br/><br/>");
		for(int i=1; i<table.getChildCount(); i++){
			LinearLayout ll = (LinearLayout)table.getChildAt(i);
			sb.append("&nbsp;&nbsp;&nbsp;").append(i).append(".&nbsp;&nbsp;");
			for(int j=0; j<viewFields.size(); j++){
				if(j>0) sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("<b>").append(viewFields.get(j).getName()).append(" : </b>");
				TextView tv = (TextView)ll.getChildAt(j);
				sb.append(tv.getText()).append("<br/>");
			}
		}
		
		return sb.toString();
	}

	@Override
	public void onClick(View v) {
		LGMListViewDialog d = new LGMListViewDialog(context, viewFields);
		d.setOnOkClickListener(new OnOkClickListener() {

			@Override
			public void onOkClick(ArrayList<LGMPairValue> results) {				
				addNewRow(results);
			}
			
			
		});
		d.show();
	}
	
	private void addNewRow(ArrayList<LGMPairValue> results){
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setPadding(0, 10, 0, 10);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		ll.setWeightSum(results.size()+1);
		ll.setBackgroundColor(context.getResources().getColor(R.color.bg_listview_color));
		
		for(LGMPairValue p:results){
			ll.addView(getTextView(p.getValue()));
		}
		
		ll.addView(getDeleteButton(ll));
		table.addView(ll);
		tr_first.setVisibility(View.VISIBLE);
	}
	
	private ImageView getDeleteButton(final View v){
		ImageView imv = new ImageView(context);
		imv.setLayoutParams(new LayoutParams(1, LayoutParams.WRAP_CONTENT, 1f));
		imv.setClickable(true);
		imv.setImageResource(R.drawable.ic_delete);
		imv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(context)
	        	    .setTitle("Notice")
	        	    .setMessage("Are you sure want to delete this item?")
	        	    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							table.removeView(v);
							if(table.getChildCount()==1){
								tr_first.setVisibility(View.GONE);
							}
						}
	        	        
	        	     })
	        	     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    				
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {}
	    			})
	        	    .show();
			}
		});
		return imv;
	}

	@Override
	public LGMPairValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
