package com.android.lgm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.Login;

public class CheckDataOnServerTask extends AsyncTask<Void, Void, Login>{
	private ProgressDialog pd;
	private Context context;
	private String url;
	private String path;
	private Login login;
	
	public CheckDataOnServerTask(Context context,String url,String path) {
		this.context=context;
		this.url=url;
		this.path=path;
	}
		
	@Override
	protected Login doInBackground(Void... params) {
		try {
			Log.e("URL",url);
			// Checking Internet connection
			if(isNetworkConnected(context)){
				// If OK then download the XML file
				FileUtil.downloadFile(url, path);
			}
			if(new File(path).exists()){
				login = new GetLogin().parse(new FileInputStream(path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return login;
	}


	public boolean isNetworkConnected(Context context) {
  	  	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  	  	NetworkInfo ni = cm.getActiveNetworkInfo();
  	  	if (ni != null && ni.isConnected()) {
  	  		return true;
  	  	}
  	  	return false;
	}
}
