package com.android.lgm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.Tag;
import android.os.Environment;

import com.android.lgm.Objects.Login;

public class SaveControl {
	private Context mContext;
	private String userName;
	private String password;
	private String gmail;
	private String name;
	private Editor edt;
	private SharedPreferences prefs;
	private String TAG="";
	public SaveControl(Context ctx,String tag) {
		prefs = (SharedPreferences)ctx.getSharedPreferences("LGM_SOLUTION", Context.MODE_PRIVATE);
		edt = prefs.edit();
		mContext=ctx;
		this.TAG=tag;
		reload();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		edt.putString("USERNAME", userName);
		edt.commit();
		this.userName = userName;
		appendLog("SET_USERNAME: "+userName);
	}
	public void reload(){
		userName=this.prefs.getString("USERNAME", "");
		password=this.prefs.getString("PASSWORD", "");
		gmail=this.prefs.getString("GMAIL", "");
		name=this.prefs.getString("NAME", "");
		if(userName==null || password==null || gmail==null || name==null){
			String path=Statics.XML_STORAGE + "/companylist.xml";
			if(new File(path).exists()){
				Login login=null;
				try {
					login = new GetLogin().parse(new FileInputStream(path));
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if(login!=null && login.isStatus()){
					setUserName(login.getUserName());
					setPassword(login.getPassword());
					setGmail(login.getEmail());
					setName(login.getName());
				}
			}
		}
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		edt.putString("PASSWORD", password);
		edt.commit();
		this.password = password;
		//appendLog("SET_PASSWORD: "+password);
	}
	public String getGmail() {
		return gmail;
	}
	public void setGmail(String gmail) {
		edt.putString("GMAIL", gmail);
		edt.commit();
		this.gmail = gmail;
		appendLog("SET_GMAIL: "+gmail);
	}
	public String getName() {
		appendLog("GET_NAME: "+name);
		if(name==null || name.length()<1){
			return userName;
		}else return name;
		
	}
	public void setName(String name) {
		edt.putString("NAME", name);
		edt.commit();
		this.name = name;
		appendLog("SET_NAME: "+name);
	}
	public void appendLog(String text)
	{       
		long time=Calendar.getInstance().getTimeInMillis();

	   File logFile = new File(Environment.getExternalStorageDirectory()+"/logLgmIssue.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append("["+TAG+"]"+text+" ("+android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", time)+")\n");
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}
}
