package com.android.lgm.views;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.FileUtil;
import com.android.lgm.utils.Statics;
import com.martin.lgmsolutions.R;

public class LGMCamera extends LGMCommon {

	private TextView tv;
	private ImageView imv;
	private Button btn;
	private Bitmap bmp;
	private String path = "";

	public LGMCamera(Activity context, String label, String alt,
			String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);
		init(layout);

		tv.setText(label);
		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);
		tv = (TextView) childView.findViewById(R.id.tv_camera);
		imv = (ImageView) childView.findViewById(R.id.imv_camera);
		btn = (Button) childView.findViewById(R.id.btn_camera);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		application.setCamera(this);

		final String[] items = new String[] { "Camera", "Gallery" };
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Choose a intent");
		alert.setNegativeButton("OK", null);
		alert.setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					File folder = new File(Statics.MEDIA_STORAGE);
					folder.mkdirs();
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
							Locale.US).format(new Date());
					Uri fileUri = Uri.fromFile(new java.io.File(
							Statics.MEDIA_STORAGE + java.io.File.separator
									+ "IMG_" + timeStamp + ".jpg"));

					application.setFileUri(fileUri);

					Intent cameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					context.startActivityForResult(cameraIntent,
							Statics.CAMERA_REQUEST);
				} else {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					context.startActivityForResult(i, Statics.GALLERY_REQUEST);
				}

			}
		});
		alert.show();
	}

	public void setImageBitmap(String path) {
		this.path = path;

		if (null != bmp)
			bmp.recycle();
		Bitmap bmp = FileUtil.decodeSampledBitmapFile(path, 200, 200);
		this.bmp = bmp;
		imv.setImageBitmap(bmp);
	}

	public void setImageBitmap(Bitmap bmp) {
		if (null != this.bmp)
			this.bmp.recycle();
		this.bmp = bmp;
		imv.setImageBitmap(bmp);		
	}

	public Bitmap getImageBitmap() {
		return bmp;
	}

	@Override
	public String getHtmlValue() {
		return null;
	}

	public LGMPairValue getValue() {
		if (isEmpty.equalsIgnoreCase("no") && path.equals("")) {
			return new LGMPairValue("", Statics.KEY_RETURN + label
					+ " is missing!");
		}

		String filename = label.toLowerCase() + ".jpg";
		if (filename.contains(" ")) {
			filename = filename.replace(" ", "_");
		}
		return new LGMPairValue(filename, path);
	}
}
