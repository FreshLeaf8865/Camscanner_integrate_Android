package com.android.lgm.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.views.LGMBarcode;
import com.android.lgm.views.LGMCamera;
import com.android.lgm.views.LGMCheckBox;
import com.android.lgm.views.LGMDatePicker;
import com.android.lgm.views.LGMListView;
import com.android.lgm.views.LGMSelect;
import com.android.lgm.views.LGMSignature;
import com.android.lgm.views.LGMText;
import com.android.lgm.views.LGMTextField;
import com.android.lgm.views.LGMTimePicker;

public class Statics {
	//jtest		jeremie
	public static String TEXT ="text";
	public static String CHECKBOX ="checkbox";
	public static String TEXTFIELD ="textfield";
	public static String DATEPICKER ="datepicker";
	public static String TIMEPICKER ="timepicker";
	public static String BARCODE ="barcode";
	public static String LISTVIEW ="listview";
	public static String SELECT ="select";
	public static String CAMERA ="camera";
	public static String SIGNATURE ="signature";
	
	public static String INTEGER ="integer";
	public static String STRING ="string";
	public static String DECIMAL = "decimal";
	
	public static String COMP_LIST = "http://www.lgmsolution.com/LGMForm/companylist2.php";
	//public static String COMP_LIST = "http://10.0.2.2/lgm/companylist.php";
	public static String COMP_BY_ID = "http://www.lgmsolution.com/LGMForm/xml.php?ID=";
	public static final String APP_KEY = "4M2DybKhBFSeEFYJr9QyL6yQ";//4M2DybKhBFSeEFYJr9QyL6yQ
	public static int GALLERY_REQUEST = 111;
	public static int CAMERA_REQUEST = 222;
	public static int SIGNATURE_REQUEST = 333;
	public static int BARCODE_REQUEST = 444;
	public static int REQ_CODE_CALL_CAMSCANNER = 555;
	public static String MEDIA_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/LGMSolution/images";
	public static String LOGO_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/LGMSolution/logos";
	public static String XML_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/LGMSolution/xmls";
	
	public static String GMAIL_USER = "noreply.lgmsolution@gmail.com";
	public static String GMAIL_PASS = "lgm123!@#";
	
	public static String KEY_RETURN = "##$$null$$##";
	
	
	public static View converLGMObject2View(Activity act, LGMObject o){

		if(o.getType().equals(TEXT)){
			return new LGMText(act, o.getLabel(), o.getAlt(), o.getValue(), o.getIsEmpty(), R.layout.layout_text);
		}else if(o.getType().equals(CHECKBOX)){
			LGMCheckBox cb =  new LGMCheckBox(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_checkbox);
			if(null != o.getSubObjects()){
				cb.setRadioView(o.getSubObjects());
			}
			return cb;
		}else if(o.getType().equals(TEXTFIELD)){
			return new LGMTextField(act, o.getLabel(),o.getReplaceLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_textfield, o.getValidate(),o.getName());
		}else if(o.getType().equals(DATEPICKER)){
			return new LGMDatePicker(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_timepicker);
		}else if(o.getType().equals(TIMEPICKER)){
			return new LGMTimePicker(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_timepicker);
		}else if(o.getType().equals(BARCODE)){
			return new LGMBarcode(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_barcode);
		}else if(o.getType().equals(LISTVIEW)){
			LGMListView lv =  new LGMListView(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_listview);
			if(null != o.getSubObjects()){
				lv.setListViewField(o.getSubObjects());
			}
			return lv;
		}else if(o.getType().equals(SELECT)){
			LGMSelect sl =  new LGMSelect(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_select,o.getName());
			if(null != o.getOptions()){
				sl.setOptions(o.getOptions());
			}
			return sl;
		}else if(o.getType().equals(CAMERA)){
			return new LGMCamera(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_camera);
		}else if(o.getType().equals(SIGNATURE)){
			return new LGMSignature(act, o.getLabel(), o.getAlt(), o.getIsEmpty(), R.layout.layout_camera);
		}
		
		return new TextView(act);

	}
	
	public static boolean isNetworkConnected(Context context) {
  	  	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  	  	NetworkInfo ni = cm.getActiveNetworkInfo();
  	  	if (ni != null && ni.isConnected()) {
  	  		return true;
  	  	}
  	  	return false;
	}
}
