package com.android.lgm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.event.TransportEvent;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.LGMMail;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.applications.LGMApplication;
import com.android.lgm.gmails.DBAdapter;
import com.android.lgm.gmails.GMailSender;
import com.android.lgm.utils.MultipartUtility;
import com.android.lgm.utils.SaveControl;
import com.android.lgm.utils.Statics;
import com.android.lgm.utils.ValidateEditText;
import com.android.lgm.utils.XMLParser;
import com.android.lgm.utils.ZBarConstants;
import com.android.lgm.views.LGMCamera;
import com.android.lgm.views.LGMCheckBox;
import com.android.lgm.views.LGMCommon;
import com.android.lgm.views.LGMSelect;
import com.android.lgm.views.LGMSignature;
import com.android.lgm.views.LGMTextField;
import com.intsig.csopen.sdk.CSOpenAPI;
import com.intsig.csopen.sdk.CSOpenAPIParam;
import com.intsig.csopen.sdk.CSOpenApiFactory;
import com.intsig.csopen.sdk.CSOpenApiHandler;
import com.intsig.csopen.sdk.ReturnCode;
import com.intsig.csopen.util.Log;
import com.martin.lgmsolutions.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class GenerateGUIActivity extends Activity {

	private LinearLayout group_gui, group_btn_gui;
	private ImageView imv_logo_gui;
	private TextView tv_name_gui;
	private ArrayList<LGMObject> objs;
	private LGMCompany infor;
	private HashMap<String, ArrayList<LGMObject>> expands;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private String compID = "", compLogo = "", compName = "";
	private LGMApplication application;
	private GMailSender sender;
	private ProgressDialog pd;
	// private ArrayList<LGMPairValue> files;
	private DBAdapter db;
	private String ticket = "_", status = "_";
	private SaveControl save;

	//new added by moontech
	private CSOpenAPI mApi;
	private String mOutputImagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_generate_gui);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		save = new SaveControl(this, "GENERATE_GUI_ACTIVITY");
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.no_image_available)
				.showImageForEmptyUri(R.drawable.no_image_available).showImageOnFail(R.drawable.no_image_available)
				.cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		// show debug info of CSOpenAPI SDK, remove this sentence if you build release version
		Log.setLevel(Log.LEVEL_DEBUG);
		mApi = CSOpenApiFactory.createCSOpenApi(this, Statics.APP_KEY, null);
		init();

		new GetData().execute();
	}

	private void init() {
		application = (LGMApplication) getApplication();

		group_gui = (LinearLayout) findViewById(R.id.group_gui);
		group_btn_gui = (LinearLayout) findViewById(R.id.group_btn_gui);
		imv_logo_gui = (ImageView) findViewById(R.id.imv_logo_gui);
		tv_name_gui = (TextView) findViewById(R.id.tv_name_gui);

		((ImageView) findViewById(R.id.imv_refresh)).setVisibility(View.INVISIBLE);

		compID = getIntent().getStringExtra("COMP_ID");
		compLogo = getIntent().getStringExtra("COMP_LOGO");
		compName = getIntent().getStringExtra("COMP_NAME");

		sender = new GMailSender(Statics.GMAIL_USER, Statics.GMAIL_PASS);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);

		db = new DBAdapter(this);
	}

	private void loadData() {
		if (null != expands) {
			application.setExpands(expands);
		}

		// File f = new File(Statics.LOGO_STORAGE + "/" + compID + ".jpg");
		// if (!f.exists()) {
		// imv_logo_gui.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
		// } else {
		// imageLoader.displayImage(compLogo, imv_logo_gui, options,
		// new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String imageUri, View view) {
		// }
		//
		// @Override
		// public void onLoadingFailed(String imageUri, View view,
		// FailReason failReason) {
		// }
		//
		// @Override
		// public void onLoadingComplete(String imageUri,
		// View view, Bitmap loadedImage) {
		// imv_logo_gui.setImageBitmap(loadedImage);
		// }
		//
		// @Override
		// public void onLoadingCancelled(String imageUri,
		// View view) {
		// }
		// });
		// }

		imageLoader.displayImage(compLogo, imv_logo_gui, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				imv_logo_gui.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});

		tv_name_gui.setText(compName);

		if (null != objs) {
			for (LGMObject o : objs) {
				group_gui.addView(Statics.converLGMObject2View(this, o));
			}
		}
	}

	public void onSubmit(View v) {
		new AsyncTask<String, Void, String>() {
			String mError;

			protected void onPreExecute() {
				application.setEdt(null);
				pd.setMessage("Sending mail...");
				pd.show();
			};

			@Override
			protected String doInBackground(String... params) {
				LGMMail mail = getHtmlValueToSendMail();

				if (mail.getBody().contains(Statics.KEY_RETURN))
					return mail.getBody();

				StringBuilder sb = new StringBuilder("Ticket#").append(ticket).append(" / ").append(status)
						.append(" / ").append(compName);

				mail.setBody("Dear " + infor.getCompName() + ",<br/><br/>" + mail.getBody()
						+ "<br/><b>Nom du technicien : </b>" + save.getName()
						+ "<br/><br/>Thank You,<br/><br/>LGM Solution");
				mail.setID(0);
				mail.setDisplay(save.getName());
				mail.setTo(infor.getMailAddress());
				mail.setCc(save.getGmail().trim());
				mail.setSubject(sb.toString());

				String url = "http://www.lgmsolution.com/intranet/lib/sendMail.php";
				boolean isFileFound = true;
				if (Statics.isNetworkConnected(getBaseContext())) {
					try {
						MultipartUtility utility = new MultipartUtility(url, "UTF-8");
						utility.addFormField("name", mail.getDisplay());
						utility.addFormField("to", mail.getTo());
						// utility.addFormField("to", "hdquang@sdc.ud.edu.vn");
						utility.addFormField("subject", mail.getSubject());
						utility.addFormField("content", mail.getBody());
						// utility.addFormField("cc", "nvngoc@sdc.ud.edu.vn");
						utility.addFormField("cc", mail.getCc());
						int i = 0;
						for (LGMPairValue attachfile : mail.getAttaches()) {
							if (attachfile != null) {
								File file = new File(attachfile.getValue());
								if (file.exists()) {
									// double megabytes = file.length() / 1024.0
									// /
									// 1024.0;
									// if (megabytes > 1) {
									Bitmap bmp = BitmapFactory.decodeFile(attachfile.getValue());
									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									bmp.compress(CompressFormat.JPEG, 70, bos);
									if (bmp != null && !bmp.isRecycled()) {
										bmp.recycle();
										bmp = null;
										System.gc();
									}

									File tempfile = new File(
											file.getPath().substring(0, file.getPath().lastIndexOf("/")) + "/LGMTEMP"
													+ System.currentTimeMillis() + ".jpg");
									if (!tempfile.exists()) {
										tempfile.createNewFile();
									}

									FileOutputStream outStream = null;
									outStream = new FileOutputStream(tempfile);
									outStream.write(bos.toByteArray());
									outStream.flush();
									outStream.close();
									// }
									utility.addFilePart("images[" + i++ + "]", tempfile);
									tempfile.delete();
								} else {
									isFileFound = false;
									Log.e("Register", "file not exist ");
								}
							}
						}
						return utility.connect();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						return "Error";
					} catch (IOException e) {
						e.printStackTrace();
						return "Error";
					} catch (Exception e) {
						e.printStackTrace();
						return "Error";
					}
				} else {
					long mail_id = db.addMail(mail);
					for (LGMPairValue p : mail.getAttaches()) {
						db.addAttach(mail_id, p);
					}
					return "Failt";
				}
			}

			protected void onPostExecute(String result) {
				pd.dismiss();
				EditText edt = application.getEdt();
				if (null != edt) {
					edt.setError(ValidateEditText.setColor("Missing data."));
					edt.requestFocus();
				}
				if (result.contains(Statics.KEY_RETURN)) {
					result = result.replace(Statics.KEY_RETURN, "");
					MainActivity.showToast(getBaseContext(), result);
				} else if (result.equals("Failt")) {
					MainActivity.showToast(getBaseContext(), "Your mail will be sent when Network Signal is better.");
					finish();
				} else {
					if (result.contains("100")) {
						MainActivity.showToast(getBaseContext(), "Sent email successfully");
						finish();
					} else {
						MainActivity.showToast(getBaseContext(), "Failed sending email.");
					}
				}
			};
		}.execute();

	}

	public void onSubmit_Old(View v) {
		new AsyncTask<Void, Void, String>() {
			protected void onPreExecute() {
				application.setEdt(null);
				pd.setMessage("Sending mail...");
				pd.show();
			};

			@Override
			protected String doInBackground(Void... params) {

				LGMMail mail = getHtmlValueToSendMail();

				if (mail.getBody().contains(Statics.KEY_RETURN))
					return mail.getBody();

				StringBuilder sb = new StringBuilder("Ticket#").append(ticket).append(" / ").append(status)
						.append(" / ").append(compName);

				mail.setBody("Dear " + infor.getCompName() + ",<br/><br/>" + mail.getBody()
						+ "<br/><b>Nom du technicien : </b>" + save.getName()
						+ "<br/><br/>Thank You,<br/><br/>LGM Solution");
				mail.setID(0);
				mail.setDisplay(save.getName());
				mail.setTo(infor.getMailAddress());
				mail.setCc(save.getGmail().trim());
				mail.setSubject(sb.toString());
				if (Statics.isNetworkConnected(getBaseContext())) {

					return sender.sendMail(mail.getSubject(), // MAIL SUBJECT
							mail.getBody(), // MAIL BODY
							mail.getDisplay(), // DISPLAY NAME
							mail.getTo(), // MAIL TO
							mail.getCc(), // MAIL CC
							mail.getAttaches(), mail); // FILE ATTACH

				} else {
					long mail_id = db.addMail(mail);
					for (LGMPairValue p : mail.getAttaches()) {
						db.addAttach(mail_id, p);	
					}
					return "Failt";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				pd.dismiss();
				EditText edt = application.getEdt();
				if (null != edt) {
					edt.setError(ValidateEditText.setColor("Missing data."));
					edt.requestFocus();
				}
				if (result.contains(Statics.KEY_RETURN)) {
					result = result.replace(Statics.KEY_RETURN, "");
					MainActivity.showToast(getBaseContext(), result);
				} else if (result.equals("Failt")) {
					MainActivity.showToast(getBaseContext(), "Your mail will be sent when Network Signal is better.");
					finish();
				}
			}

		}.execute();
	}

	public void onDiscard(View v) {
		new AlertDialog.Builder(this).setTitle("Notice").setMessage("Are you sure you wish to discard the changes?")
				.setPositiveButton("Discard", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				}).setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private void getViewFormXML(InputStream in) {
		XMLParser xp = new XMLParser();
		xp.parse(in);

		objs = xp.getObjs();
		infor = xp.getInfor();
		expands = xp.getConditions();
	}

	private class GetData extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			pd.setMessage("Loading...");
			pd.show();
		};

		@Override
		protected Void doInBackground(Void... params) {
			try {
				getViewFormXML(new FileInputStream(Statics.XML_STORAGE + "/" + compID + ".xml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			loadData();
			pd.dismiss();
			group_btn_gui.setVisibility(View.VISIBLE);
		};
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String filepath = "";
		if (requestCode == Statics.CAMERA_REQUEST && resultCode == RESULT_OK && null != application.getCamera()) {
			filepath = application.getFileUri().getPath();
			mOutputImagePath = filepath;
			Log.e("TAG ", "filepath "+filepath);
			if(filepath != null){
				if(mApi.isCamScannerInstalled()){
					go2CamScanner(filepath);
				}else{
					Toast.makeText(this, "You should Install cam Scanner First", Toast.LENGTH_LONG).show();
					application.getCamera().setImageBitmap(filepath);
					application.setCamera(null);
				}
			}
		} else
			if (requestCode == Statics.GALLERY_REQUEST && resultCode == RESULT_OK && null != application.getCamera()) {
			filepath = getPath(data.getData());
			mOutputImagePath = filepath;
			Log.e("TAG ", "filepath "+filepath);
			if(filepath != null){
				if(mApi.isCamScannerInstalled()){
					go2CamScanner(filepath);
				}else{
					Toast.makeText(this, "You should Install cam Scanner First", Toast.LENGTH_LONG).show();
					application.getCamera().setImageBitmap(filepath);
					application.setCamera(null);
				}
			}
			
		} else if (requestCode == Statics.SIGNATURE_REQUEST && resultCode == RESULT_OK
				&& null != application.getSignature() && null != data) {
			filepath = data.getStringExtra("SIGNATURE_PATH");
			application.getSignature().setImageBitmap(filepath);
			application.setSignature(null);
		} else if (requestCode == Statics.BARCODE_REQUEST && null != application.getBarcode() && null != data) {

			if (resultCode == RESULT_OK) {
				// String contents = data.getStringExtra("SCAN_RESULT");
				// if (contents != null) {
				// application.getBarcode().setValue(contents.toString());
				// }
				String barcode = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				application.getBarcode().setValue(barcode);
			}
		}else if (requestCode == Statics.REQ_CODE_CALL_CAMSCANNER && resultCode == RESULT_OK
				&& null != data) {
				mApi.handleResult(requestCode, resultCode, data, new CSOpenApiHandler() {
				
				@Override
				public void onSuccess() {
					new AlertDialog.Builder(GenerateGUIActivity.this)
						.setTitle("Success")
						.setMessage("Image scanned")
						.setPositiveButton(android.R.string.ok, null)
						.create().show();
					
					application.getCamera().setImageBitmap(mOutputImagePath);
					application.setCamera(null);
					
				}
				
				@Override
				public void onError(int errorCode) {
					String msg = handleResponse(errorCode);
					new AlertDialog.Builder(GenerateGUIActivity.this)
						.setTitle(R.string.a_title_reject)
						.setMessage(msg)
						.setPositiveButton(android.R.string.ok, null)
						.create().show();
				}
				
				@Override
				public void onCancel() {
					new AlertDialog.Builder(GenerateGUIActivity.this)
						.setMessage(R.string.a_msg_cancel)
						.setPositiveButton(android.R.string.ok, null)
						.create().show();
				}
			});
		}

	}
	private void go2CamScanner(String path) {
		mOutputImagePath = path;
		modify(new File(mOutputImagePath));
		CSOpenAPIParam param = new CSOpenAPIParam(mOutputImagePath, 
				mOutputImagePath, null, null, 1.0f);
		boolean res = mApi.scanImage(this, Statics.REQ_CODE_CALL_CAMSCANNER, param);
		
		android.util.Log.d("Tag", "send to CamScanner result: " + res);
	}
	public void modify(File file) 
    {
        int index = file.getName().indexOf(".");
        //print filename
//        System.out.println( "modify" +file.getName().substring(0, index));
        //print extension
//        System.out.println( "modify" +file.getName().substring(index));
        String name = file.getName().substring(0, index);
        String ext = file.getName().substring(index);
        if(ext.equalsIgnoreCase(".png")){//SC not support .png file so we have converted with .jpg
        	  mOutputImagePath = file.getParent() + "/"+name+".jpg";
        	  try {
				copy(file,new File(mOutputImagePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
       
//        Log.e("modify mOutputImagePath ", mOutputImagePath);
       
    }
	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	    if(src.exists())
	    	src.delete();
	    Log.e("modify mOutputImagePath ", "exists "+dst.exists());
	}
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public LGMMail getHtmlValueToSendMail() {
		LGMMail mail = new LGMMail() {
			@Override
			public void messageDelivered(TransportEvent arg0) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MainActivity.showToast(getBaseContext(), "The mail has been sent");
					}
				});
				if (this.getAttaches() != null) {
					for (LGMPairValue value : getAttaches()) {
						File f = new File(value.getValue());
						f.delete();
					}
				}
				finish();

			}

			@Override
			public void messageNotDelivered(TransportEvent arg0) {
				long mail_id = db.addMail(this);
				for (LGMPairValue p : this.getAttaches()) {
					db.addAttach(mail_id, p);
				}
				finish();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MainActivity.showToast(getBaseContext(),
								"Your mail will be sent when Network Signal is better.");

					}
				});
			}

			@Override
			public void messagePartiallyDelivered(TransportEvent arg0) {
				long mail_id = db.addMail(this);
				for (LGMPairValue p : this.getAttaches()) {
					db.addAttach(mail_id, p);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MainActivity.showToast(getBaseContext(),
								"Your mail will be sent when Network Signal is better.");

					}
				});
			}
		};
		ArrayList<LGMPairValue> attat = new ArrayList<LGMPairValue>();
		mail.setAttaches(attat);
		String bd = getHtmlValueToSendMail(mail, group_gui);
		if (bd != null) {
			mail.setBody(bd);
		}

		return mail;
	}

	private String getHtmlValueToSendMail(LGMMail mail, ViewGroup layout) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < layout.getChildCount(); i++) {
			View v = layout.getChildAt(i);
			if (v instanceof LGMCommon) {
				if (v instanceof LGMCamera) {
					LGMPairValue p = ((LGMCamera) v).getValue();
					if (p.getValue().contains(Statics.KEY_RETURN)) {
						mail.setBody(p.getValue());
						return null;
					}
					mail.getAttaches().add(p);
					continue;
				} else if (v instanceof LGMSignature) {
					LGMPairValue p = ((LGMSignature) v).getValue();
					if (p.getValue().contains(Statics.KEY_RETURN)) {
						mail.setBody(p.getValue());
						return null;
					}
					mail.getAttaches().add(p);
					continue;
				}
				String val = ((LGMCommon) v).getHtmlValue();
				if (null == val)
					continue;
				if (val.contains(Statics.KEY_RETURN)) {
					mail.setBody(val);
					return null;
				}

				if (v instanceof LGMTextField) {
					LGMPairValue p = ((LGMTextField) v).getValue();
					if (p.getName().equals("Ticket")) {
						ticket = p.getValue();
					}
				}

				if (v instanceof LGMSelect) {
					LGMPairValue p = ((LGMSelect) v).getValue();
					if (p.getName().equals("CmdStatus")) {
						status = p.getValue();
					}
					for (LGMPairValue cameraValue : ((LGMSelect) v).getCameraAndSignatureValue()) {
						if (cameraValue.getValue().contains(Statics.KEY_RETURN)) {
							mail.setBody(cameraValue.getValue());
							return null;
						}
						mail.getAttaches().add(cameraValue);
					}					
				}

				sb.append(val).append("<br/>");
				if (v instanceof LGMCheckBox) {
					if (((LGMCheckBox) v).isShowChooosen()) {
						getHtmlValueToSendMail(mail, ((LGMCheckBox) v).getChoosenView());
						/*
						 * if (bd != null) { sb.append(bd); }
						 */
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void finish() {

		super.finish();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Notice").setMessage("Are you sure you wish to discard the changes?")
				.setPositiveButton("Discard", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				}).setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}
	private String handleResponse(int code){
		switch(code){
		case ReturnCode.OK:
			return getString(R.string.a_msg_api_success);
		case  ReturnCode.INVALID_APP:	
			return getString(R.string.a_msg_invalid_app);
		case ReturnCode.INVALID_SOURCE:
			return getString(R.string.a_msg_invalid_source);
		case ReturnCode.AUTH_EXPIRED:
			return getString(R.string.a_msg_auth_expired);
		case ReturnCode.MODE_UNAVAILABLE:
			return getString(R.string.a_msg_mode_unavailable);
		case ReturnCode.NUM_LIMITED:
			return getString(R.string.a_msg_num_limit);
		case ReturnCode.STORE_JPG_ERROR:
			return getString(R.string.a_msg_store_jpg_error);
		case ReturnCode.STORE_PDF_ERROR:
			return getString(R.string.a_msg_store_pdf_error);
		case ReturnCode.STORE_ORG_ERROR:
			return getString(R.string.a_msg_store_org_error);
		case ReturnCode.APP_UNREGISTERED:
			return getString(R.string.a_msg_app_unregistered);
		case ReturnCode.API_VERSION_ILLEGAL:
			return getString(R.string.a_msg_api_version_illegal);
		case ReturnCode.DEVICE_LIMITED:
			return getString(R.string.a_msg_device_limited);
		case ReturnCode.NOT_LOGIN:
			return getString(R.string.a_msg_not_login);
		default:
			return "Return code = " + code;
		}
	}
	/*
	 * @Override public void messageDelivered(TransportEvent arg0) {
	 * runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { MainActivity.showToast(getBaseContext(),
	 * "The mail has been sent");
	 * 
	 * } }); finish();
	 * 
	 * }
	 * 
	 * @Override public void messageNotDelivered(TransportEvent arg0) {
	 * runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { MainActivity.showToast(getBaseContext(),
	 * "The mail was not sent");
	 * 
	 * } });
	 * 
	 * 
	 * }
	 * 
	 * @Override public void messagePartiallyDelivered(TransportEvent arg0) {
	 * runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { MainActivity.showToast(getBaseContext(),
	 * "The mail was not sent");
	 * 
	 * } });
	 * 
	 * }
	 */
}
