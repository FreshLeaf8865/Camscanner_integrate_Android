package com.android.lgm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.LGMMail;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.adapters.CompanyAdapter;
import com.android.lgm.applications.LGMApplication;
import com.android.lgm.gmails.DBAdapter;
import com.android.lgm.gmails.GMailSender;
import com.android.lgm.utils.DownloadManager;
import com.android.lgm.utils.FileUtil;
import com.android.lgm.utils.GetCompanyList;
import com.android.lgm.utils.SaveControl;
import com.android.lgm.utils.Statics;
import com.martin.lgmsolutions.R;

public class MainActivity extends Activity implements TransportListener {

	private ListView list_comps;
	private ImageView imv_send_mail;
	private TextView connect_with;
	private ArrayList<LGMCompany> comps;
	private CompanyAdapter cAdapter;
	public static Toast t;
	private ProgressDialog pd;
	private CountDownTimer cDT;
	private boolean ready = false;
	private DBAdapter db;
	private ArrayList<LGMMail> mails;
	private GMailSender sender;
	private LGMApplication application;
	private GetData task;
	private String path = Statics.XML_STORAGE + "/companylist.xml";
	private DownloadManager download;
	private TextView tvNodata;
	private SaveControl save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		application = (LGMApplication) getApplication();

		list_comps = (ListView) findViewById(R.id.list_companies);

		list_comps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				LGMCompany c = comps.get(index);
				File f = new File(Statics.XML_STORAGE + "/" + c.getID()
						+ ".xml");
				if (!f.exists()) {
					showToast(getBaseContext(), "The XML file no available!");
					return;
				}

				Intent i = new Intent(MainActivity.this,
						GenerateGUIActivity.class);
				i.putExtra("COMP_ID", comps.get(index).getID());
				i.putExtra("COMP_NAME", comps.get(index).getCompName());
				i.putExtra("COMP_LOGO", comps.get(index).getLogo());
				startActivity(i);
			}

		});
		save = new SaveControl(this, "MAIN_ACTIVITY");
		sender = new GMailSender(Statics.GMAIL_USER, Statics.GMAIL_PASS);
		pd = new ProgressDialog(this);
		pd.setCancelable(false);

		cDT = new CountDownTimer(3000, 3000) {

			@Override
			public void onTick(long arg0) {
			}

			@Override
			public void onFinish() {
				ready = false;
			}
		};

		imv_send_mail = (ImageView) findViewById(R.id.imv_send_mail);
		connect_with = (TextView) findViewById(R.id.connect_with);
		tvNodata = (TextView) findViewById(R.id.tv_no_data);

		connect_with.setText(getString(R.string.connect_text) + " "
				+ save.getName());

		db = new DBAdapter(this);
		mails = new ArrayList<LGMMail>();

		comps = new ArrayList<LGMCompany>();
		download = new DownloadManager() {
			public void onFinish() {
				if (pd != null)
					pd.dismiss();
				// Log.e("aaaaaaaaaaaaaaaaaaaaa", "DOWNLOAD FINISH");
			};

			@Override
			public void onFinishOne() {
				if (pd != null)
					pd.setMessage("Loading...(" + download.getLastFileCount()
							+ ")");
			}
		};
	}

	public void onLogin(View v) {
		
		new AlertDialog.Builder(this).setTitle("Sign out")
		.setMessage("Are you sure you want to sign out?")
		.setPositiveButton("Yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				i.putExtra("RESET", true);
				startActivity(i);
			}
		}).setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
		
		
	}

	public static void showToast(Context ctx, String txt) {
		// if(null != t) t.cancel();
		t = Toast.makeText(ctx, txt, Toast.LENGTH_SHORT);
		t.show();
	}

	private class GetData extends AsyncTask<Void, Void, Void> {
		private boolean isDeleteOld = false;

		public GetData(boolean isDeleteOld) {
			this.isDeleteOld = isDeleteOld;
		}

		protected void onPreExecute() {
			pd.setMessage("Loading...");
			pd.setCancelable(false);
			pd.show();
		};

		@Override
		protected Void doInBackground(Void... params) {
			try {
				FileUtil.downloadFile(
						Statics.COMP_LIST + "?user_name=" + save.getUserName(),
						path);
				Log.e("aaaaaaaaaaaaaaaaaaaaa", Statics.COMP_LIST
						+ "?user_name=" + save.getUserName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			// Log.e("aaaaaaaaaaaaaaaaaaaaa","Load DATAA");
			loadData();
			// Log.e("aaaaaaaaaaaaaaaaaaaaa", "LOAD DATAA FINISH");
			if (comps != null) {
				// Log.e("aaaaaaaaaaaaaaaaaaaaa", "COMPS "+comps.size());
				// Download new data
				// true: replace old data // false: do nothing
				String XMLPath = "", LogoPath = "";
				if (comps.isEmpty()) {
					pd.dismiss();
				} else {
					for (int i = 0; i < comps.size(); i++) {
						LGMCompany c = comps.get(i);
						XMLPath = Statics.XML_STORAGE + "/" + c.getID()
								+ ".xml";
						LogoPath = Statics.LOGO_STORAGE + "/" + c.getID()
								+ ".jpg";
						File f1 = new File(XMLPath);
						File f2 = new File(LogoPath);

						if (!f1.exists() || (f1.exists() && isDeleteOld)) {
							// Download XMLs file
							try {
								download.download(
										Statics.COMP_BY_ID + c.getID(), XMLPath);
								// Log.i("XML",
								// Statics.XML_STORAGE+"/"+c.getID()+".xml");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (!f2.exists() || (f2.exists() && isDeleteOld)) {
							// Download images file
							try {
								// download.download(c.getLogo(), LogoPath);
								// Log.i("IMAGE",
								// Statics.LOGO_STORAGE+"/"+c.getID()+".jpg");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				if (isDeleteOld) {
					Calendar c = Calendar.getInstance();
					db.updateTime(c.getTimeInMillis());
				}
			} else {
				pd.dismiss();
			}

		};
	}

	private void loadData() {
		if (new File(path).exists()) {
			try {
				comps = new GetCompanyList().parse(new FileInputStream(path));
				if (null != comps) {
					if (comps.size() > 0) {
						cAdapter = new CompanyAdapter(MainActivity.this, comps);
						tvNodata.setVisibility(View.GONE);
					} else {
						cAdapter = null;
						tvNodata.setVisibility(View.VISIBLE);
					}
				} else {
					cAdapter = null;
					tvNodata.setVisibility(View.VISIBLE);
					showToast(MainActivity.this,
							"Could not get data from XML file!");
					pd.dismiss();
				}
				list_comps.setAdapter(cAdapter);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void reloadData(boolean isDelete) {
		if (task != null)
			task.cancel(true);
		task = new GetData(isDelete);
		task.execute();
	}

	public void onUpdateData(View v) {
		if (Statics.isNetworkConnected(getBaseContext())) {
			reloadData(true);
		} else {
			showToast(getBaseContext(), "No Internet connection!");
		}
	}

	public void onSendSavedMail(View v) {
		if (Statics.isNetworkConnected(getBaseContext())) {
			onSendPendingMail();
		} else {
			showToast(getBaseContext(), "No Internet connection!");
		}
	}

	public void onSendPendingMail() {
		new AsyncTask<Void, Void, String>() {
			boolean pdShowing = pd.isShowing();

			protected void onPreExecute() {
				if (!pdShowing) {
					pd.setMessage("Sending your mails that were not sent in the past");
					pd.show();
				}
			};

			@Override
			protected String doInBackground(Void... params) {
				for (LGMMail m : mails) {
					m.setTransportListener(MainActivity.this);
					sender.sendMail(m.getSubject(), // SUBJECT
							m.getBody(), // MAIL BODY
							m.getDisplay(), // DISPLAY NAME
							m.getTo(), // MAIL TO
							m.getCc(), // MAIL CC
							m.getAttaches(), m); // FILE ATTACH

				}

				return "Success";
			}

			@Override
			protected void onPostExecute(String result) {
				if (!pdShowing && pd.isShowing())
					pd.dismiss();
			}

		}.execute();
	}

	@Override
	public void onBackPressed() {
		if (ready) {
			finish();
		} else {
			ready = true;
			showToast(getBaseContext(), "Press back again to exit");
			cDT.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mails = db.getAllMail();
		if (mails.size() > 0 && Statics.isNetworkConnected(getBaseContext())) {
			imv_send_mail.setVisibility(View.VISIBLE);
			onSendPendingMail();
		} else {
			imv_send_mail.setVisibility(View.GONE);
		}

		long last_update = db.getUpdateTime();
		Calendar c = Calendar.getInstance();
		long time = Math.abs(c.getTimeInMillis() - last_update);

		// > 2 days 86400s/day 1000milis/s
		if (time > (2 * 86400 * 1000)) {
			reloadData(true);
		} else {

			loadData();
		}
	}

	@Override
	public void messageDelivered(TransportEvent arg0) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				mails = db.getAllMail();
				if (mails.size() == 0) {
					imv_send_mail.setVisibility(View.GONE);
					MainActivity.showToast(MainActivity.this, "Your mails sent!");
				}
				

			}
		});

	}

	@Override
	public void messageNotDelivered(TransportEvent arg0) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MainActivity.showToast(MainActivity.this,
						"Your mail was not sent!");
				
			}
		});

	}

	@Override
	public void messagePartiallyDelivered(TransportEvent arg0) {
		// TODO Auto-generated method stub

	}
}
