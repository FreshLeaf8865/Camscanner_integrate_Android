package com.android.lgm.utils;

import java.util.regex.Pattern;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class ValidateEditText {
	public static String PATTERN_CHAR_NUM = "[a-zA-Z0-9-]+";
	public static String PATTERN_NAME_FORMAT = "[a-zA-Z0-9._-]+";
	public static String PATTERN_NAME = "[a-zA-Z0-9 .,-]+";
	public static String PATTERN_NUM = "[0-9-]+";
	public static String PATTERN_SIZE = "([0-9-])*[xX][0-9]+";
	/*
	 * constructor
	 */
	public ValidateEditText(){}
	
	/*
	 * public methods
	 */
	public static boolean validateEmail(EditText edt, int limit){
		String email = edt.getText().toString().trim();
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		if(email.length() == 0){
			edt.setError(setColor("Missing data"));
			return false;
		}else if(email.length() > limit){
			edt.setError(setColor("The data must less than "+limit+" characters"));
			return false;
		}else if(!pattern.matcher(email).matches()){
	    	edt.setError(setColor("The data entered is invalid email address"));
			return false;
	    }
	    return true;
	}
	
	public static boolean validateEditText(EditText edt){
		String text = edt.getText().toString().trim();		
		if(text.length() == 0){			
			edt.setError(setColor("Missing data."));			
			return false;
		} else {			
			return true;
		}
		
	}
	
	public static boolean validateNormalEditText(EditText edt, String pat, int limit){
		String text = edt.getText().toString().trim();
		Pattern pattern = Pattern.compile(pat);
		if(text.length() == 0){
			return true;
		}
		
		if(text.length() > limit){
			edt.setError(setColor("The data must less than "+limit+" characters"));
			return false;
		}else if(!pattern.matcher(text).matches()){
	    	edt.setError(setColor("Wrong format data"));
			return false;
	    }
	    return true;
	}
	
	public static boolean validateForcedEditText(EditText edt, String pat, int limit){
		String text = edt.getText().toString().trim();
		Pattern pattern = Pattern.compile(pat);
		if(text.length() == 0){
			edt.setError(setColor("Missing data."));
			return false;
		}else if(text.length() > limit){
			edt.setError(setColor("The data must less than "+limit+" characters"));
			return false;
		}else if(!pattern.matcher(text).matches()){
	    	edt.setError(setColor("Wrong format data"));
			return false;
	    }
	    return true;
	}
	
	public static SpannableStringBuilder setColor(String str){
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(0xffdb0d0d);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
		ssbuilder.setSpan(fgcspan, 0, str.length(), 0);
		return ssbuilder;
	}
}
