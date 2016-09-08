package com.android.lgm.views;

import com.android.lgm.CaptureSignature;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.Statics;
import com.martin.lgmsolutions.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LGMSignature extends LGMCommon{

	private TextView tv;
	private ImageView imv;
	private Button btn;
	private Bitmap bmp;
	private String path = "";
	public LGMSignature(Activity context, String label, String alt, String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);
		
		init(layout);
		
		tv.setText(label);
		this.addView(childView);
	}
	
	@Override
	protected void init(int layout) {
		super.init(layout);
		
		tv = (TextView)childView.findViewById(R.id.tv_camera);
		imv = (ImageView)childView.findViewById(R.id.imv_camera);
		btn = (Button)childView.findViewById(R.id.btn_camera);
		btn.setText("Signature");
		btn.setOnClickListener(this);
	}

	@Override
	public String getHtmlValue(){
		return null;//new File(path).getName();
	}
	
	@Override
	public void onClick(View arg0) {
		application.setSignature(this);
		
		context.startActivityForResult(new Intent(context, CaptureSignature.class), Statics.SIGNATURE_REQUEST);
	}
	
	public void setImageBitmap(String path){
		this.path = path;
		
		if(null != bmp) bmp.recycle();
		Bitmap bmp = BitmapFactory.decodeFile(path);
		this.bmp = bmp;
		imv.setImageBitmap(bmp);
	}
	
	public void setImageBitmap(Bitmap bmp){
		if(null != this.bmp) this.bmp.recycle();
		this.bmp = bmp;
		imv.setImageBitmap(bmp);
	}
	
	public Bitmap getImageBitmap(){
		return bmp;
	}
	
	public LGMPairValue getValue() {
		if(isEmpty.toLowerCase().equals("no") && path.equals("")){
			return new LGMPairValue("", Statics.KEY_RETURN+label+" is missing!");
		}
		
		String filename = label.toLowerCase()+".jpg";
		if(filename.contains(" ")){
			filename = filename.replace(" ", "_");
		}
		return new LGMPairValue(filename, path);
	}
}
