package com.android.lgm.utils;

import android.content.Context;

public class DPAndPixelConvert {
	//convert DB to pixel
	public static int PixelFromDp(Context context,int dp){
		return (int)(dp*context.getResources().getDisplayMetrics().density);
	}
	public static int DpFromPixel(Context context,int pixel){
		return (int)(pixel/context.getResources().getDisplayMetrics().density);
	}
}
