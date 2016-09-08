package com.android.lgm.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.AsyncTask;
import android.util.Log;

public class DownloadManager {
	public static final int DEFAULT_BUFFER_SIZE_IN_BYTES =8*1024;
	private ArrayList<QueueFile> lst;
	private DownloadTask download1;
	private DownloadTask download2;
	private DownloadTask download3;
	private DownloadTask download4;
	private Object obj;
	private Object obj1;
	
	public DownloadManager() {
		lst=new ArrayList<DownloadManager.QueueFile>(100);
	}
	public void download(String url,String path){
		if(url==null || url.length()<5 ) return;
		else{

			lst.add(new QueueFile(url, path));
			
			if(download1==null){
				download1=new DownloadTask(){
					@Override
					protected void onPostExecute(Void result) {
						download1=null;
						if(download2==null && download3==null && download4==null){
							onFinish();
						}
					}
				};
				download1.execute();
			}else if(download2==null){
				download2=new DownloadTask(){
					@Override
					protected void onPostExecute(Void result) {
						download2=null;
						if(download1==null && download3==null && download4==null){
							onFinish();
						}
					}
				};
				download2.execute();
			}else if(download3==null){
				download3=new DownloadTask(){
					@Override
					protected void onPostExecute(Void result) {
						download3=null;
						if(download2==null && download1==null && download4==null){
							onFinish();
						}
					}
				};
				download3.execute();
			}else if(download4==null){
				download4=new DownloadTask(){
					@Override
					protected void onPostExecute(Void result) {
						download4=null;
						if(download2==null && download3==null && download1==null){
							onFinish();
						}
					}
				};
				download4.execute();
			}
		}
	};
	public class DownloadTask extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			synchronized (lst) {
				while(lst.size()>0){
					QueueFile file=lst.remove(0);
					if(file!=null){
						String url=file.getUrl();
						String path=file.getPath();
						try {
							   Log.e("=--------begin--------", path);
							downloadFile(url, path);
							   Log.e("=--------finish--------", path);
						} catch (Exception e) {
							e.printStackTrace();
							deleteFiles(path);
						}
					}
					publishProgress("");
				}
				
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(String... values) {
			onFinishOne();
		}
	}
	public class QueueFile{
		private String url;
		private String path;
		public QueueFile(String url, String path) {
			super();
			this.url = url;
			this.path = path;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
	}
	public void onFinish(){
		
	}
	public void onFinishOne(){
		
	}
	public int getLastFileCount(){
		return lst.size();
	}
	public File downloadFile(String requestUrl,String path) throws Exception {
		File f = new File(path);
		if(f.exists()) f.delete();
		try {
			FileUtils.copyURLToFile(new URL(requestUrl), f);
			return f;
		} catch (Exception e) {
			throw e;
		}
		
		
		/*
		File mFile = null;
        InputStream reader = null;
        FileOutputStream output=null;
        byte buffer[] = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];
        try {
        	DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(requestUrl);
			HttpResponse resp = client.execute(post);
			//prepare sdcard
			//begin event
			reader = new BufferedInputStream(resp.getEntity().getContent());
			output=new FileOutputStream(path);
            int readByte = 0;
            synchronized (buffer) {
                while ((readByte = reader.read(buffer)) != -1) {
                	output.write(buffer,0,readByte);
                }
                output.flush();
				output.close();
				reader.close();
            }
         
            return mFile;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        */        
    }
	public boolean isDownloading(){
		if(lst!=null && lst.size()>0){
			for (QueueFile item : lst) {
				Log.e("FILE", item.getPath());
			}
			return true;
		}
		else return false;
	}
	public boolean isHasFile(String path){
		if(lst!=null && lst.size()>0){
			for (QueueFile item : lst) {
				if(item.getPath().equals(path)){
					return true;
				}
			}
		}
		return false;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Object getObj1() {
		return obj1;
	}
	public void setObj1(Object obj1) {
		this.obj1 = obj1;
	}
	public boolean deleteFiles(String path) {
	    File file = new File(path);
	    if (file.exists()) {
	        String deleteCmd = "rm -r " + path;
	        Runtime runtime = Runtime.getRuntime();
	        try {
	            runtime.exec(deleteCmd);
	        } catch (IOException e) { 
	        	return false;
	        }
	    }
	    return true;
	}
	
}
