package com.android.lgm.views;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;

public class LGMListViewDialog extends Dialog implements android.view.View.OnClickListener{
	/*
	 * private fields
	 */
	private Activity context;
	private LayoutInflater li;
	private View v;
	private Button btn_ok, btn_cancel;
	private OnOkClickListener onOk;
	private LinearLayout group_lv_dialog;
	private ArrayList<LGMObject> viewFields;
	private ArrayList<LGMPairValue> results;
	/*
	 * constructor
	 */
	public LGMListViewDialog(Activity context, ArrayList<LGMObject> viewFields) {
		super(context, R.style.full_screen_dialog);
		this.context = context;
		this.viewFields = viewFields;
		init();

		this.setContentView(v);
		this.setCancelable(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	}

	/*
	 * initialize method
	 */
	private void init() {
		li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.layout_listview_dialog, null);
		
		group_lv_dialog = (LinearLayout)v.findViewById(R.id.group_lv_dialog);
		
		btn_ok = (Button)v.findViewById(R.id.btn_ok_dialog);
		btn_cancel = (Button)v.findViewById(R.id.btn_cancel_dialog);
		
		
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		results = new ArrayList<LGMPairValue>();
		
		generateGUI();
	}

	
	private void generateGUI() {
		for(LGMObject o:viewFields){
			group_lv_dialog.addView(Statics.converLGMObject2View(context, o));
		}
	}

	public void setOnOkClickListener(OnOkClickListener onOk){
		this.onOk = onOk;
	}
	
	public interface OnOkClickListener{
		public void onOkClick(ArrayList<LGMPairValue> results);
	}
	
	public void show() {
		super.show();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_ok_dialog){
			results.clear();
			
			for(int i=0; i<group_lv_dialog.getChildCount(); i++){
				if(group_lv_dialog.getChildAt(i) instanceof LGMCommon){
					LGMPairValue p = ((LGMCommon)group_lv_dialog.getChildAt(i)).getValue();
					if(null != p){
						results.add(p);						
					}else{
						return;
					}
				}
			}
			if(null != onOk) onOk.onOkClick(results);
			this.dismiss();
		}else if(v.getId() == R.id.btn_cancel_dialog){
			this.dismiss();
		}
	}

}
