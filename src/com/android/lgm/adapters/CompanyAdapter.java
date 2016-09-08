package com.android.lgm.adapters;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.utils.Statics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CompanyAdapter extends BaseAdapter {

	private ArrayList<LGMCompany> comps;
	private Context context;
	private LayoutInflater inflater;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public CompanyAdapter(Activity context, ArrayList<LGMCompany> comps) {
		this.context = context;
		this.comps = comps;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.no_image_available)
			.showImageForEmptyUri(R.drawable.no_image_available)
			.showImageOnFail(R.drawable.no_image_available)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		final LGMCompany c = getItem(position);
		
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_list, null);
		}
		
		final ImageView imv_logo = (ImageView) convertView.findViewById(R.id.imv_logo);
		TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
		
		tv_name.setText(c.getCompName());
		String path = c.getLogo();
		File f = new File(Statics.LOGO_STORAGE+"/"+c.getID()+".jpg");
		if(f.exists()){
			f.getPath();
		}
		
		imageLoader.displayImage(path, imv_logo, options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				imv_logo.setImageBitmap(loadedImage);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {}
		});
		
		
		return convertView;

	}

	public int getCount() {
		return comps.size();
	}

	
	public LGMCompany getItem(int arg0) {
		return comps.get(arg0);
	}

	
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
