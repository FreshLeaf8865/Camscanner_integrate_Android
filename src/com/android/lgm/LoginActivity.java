package com.android.lgm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.Login;
import com.android.lgm.applications.LGMApplication;
import com.android.lgm.gmails.DBAdapter;
import com.android.lgm.utils.CheckDataOnServerTask;
import com.android.lgm.utils.DownloadManager;
import com.android.lgm.utils.GetLogin;
import com.android.lgm.utils.SaveControl;
import com.android.lgm.utils.Statics;
import com.android.lgm.utils.ValidateEditText;
import com.martin.lgmsolutions.R;

public class LoginActivity extends Activity {

	private EditText edt_username, edt_password;
	private LGMApplication application;
	private CheckDataOnServerTask task;
	private DownloadManager download;
	private ProgressDialog pd;
	private SharedPreferences prefs;
	private Button btnLaunch;
	private String path = Statics.XML_STORAGE + "/companylist.xml";
	private boolean isRegister;
	private SaveControl save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_password = (EditText) findViewById(R.id.edt_password);
		btnLaunch = (Button) findViewById(R.id.btn_launch);
		((ImageView) findViewById(R.id.imv_refresh)).setVisibility(View.INVISIBLE);

		// init folder
		new File(Statics.MEDIA_STORAGE).mkdirs();
		new File(Statics.LOGO_STORAGE).mkdirs();
		new File(Statics.XML_STORAGE).mkdirs();
		application = (LGMApplication) getApplication();
		prefs = getSharedPreferences("LGM_SOLUTION", Activity.MODE_PRIVATE);
		save = new SaveControl(this, "LOGIN_ACTIVITY");
		isRegister = prefs.getBoolean("IS_REGISTER", false);
		// check if reset
		if (getIntent().getBooleanExtra("RESET", false)) {
			new DownloadManager().deleteFiles(path);
			btnLaunch.setClickable(true);
		} else {
			checkForLogin();
		}
		download = new DownloadManager() {
			public void onFinish() {
				if (pd != null)
					pd.dismiss();
				new DBAdapter(LoginActivity.this).updateTime(System.currentTimeMillis());
				redirect();
			};

			@Override
			public void onFinishOne() {
				if (pd != null)
					pd.setMessage("Loading...(" + download.getLastFileCount() + ")");
			}
		};

	}

	@Override
	protected void onDestroy() {
		if (pd != null && pd.isShowing())
			pd.dismiss();
		if (task != null)
			task.cancel(true);
		super.onDestroy();
	}

	private void checkForLogin() {
		if (new File(path).exists() && isRegister) {
			btnLaunch.setClickable(false);
			Login login = null;
			try {
				login = new GetLogin().parse(new FileInputStream(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (login != null && login.isStatus()) {
				save.setUserName(login.getUserName());
				save.setPassword(login.getPassword());
				save.setGmail(login.getEmail());
				save.setName(login.getName());
				redirect();
			} else {
				btnLaunch.setClickable(true);
			}

		} else {
			btnLaunch.setClickable(true);
		}
	}

	public void onLaunch(View v) {
		String username = edt_username.getText().toString().trim();
		String password = edt_password.getText().toString().trim();
		if (!ValidateEditText.validateEditText(edt_username)) {
			return;
		}
		if (!ValidateEditText.validateEditText(edt_password)) {
			return;
		}
		/*
		 * if(!ValidateEditText.va(edt_gmail, 75)){ return; }
		 */
		if (task != null)
			task.cancel(true);
		task = new CheckDataOnServerTask(this, Statics.COMP_LIST + "?user_name=" + username + "&password=" + password,
				path) {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(LoginActivity.this);
				pd.setMessage("Login...");
				pd.show();
			}

			@Override
			protected void onPostExecute(Login login) {
				if (pd != null && pd.isShowing())
					pd.dismiss();
				if (null != login) {
					if (login.isStatus()) {
						save.setUserName(login.getUserName());
						save.setPassword(login.getPassword());
						save.setGmail(login.getEmail());
						save.setName(login.getName());
						try {
							Editor box = prefs.edit();
							box.putBoolean("IS_REGISTER", true);
							box.commit();
						} catch (Exception e) {
							Log.i("SAVEERROR", e.getMessage());
						}
						if (login.getLstCompany() != null) {
							pd = new ProgressDialog(LoginActivity.this);
							pd.setMessage("Loading...");
							pd.setCancelable(false);
							pd.show();
							for (int i = 0; i < login.getLstCompany().size(); i++) {
								LGMCompany c = login.getLstCompany().get(i);
								String XMLPath = Statics.XML_STORAGE + "/" + c.getID() + ".xml";
								String LogoPath = Statics.LOGO_STORAGE + "/" + c.getID() + ".jpg";

								// Download XMLs file
								download.download(Statics.COMP_BY_ID + c.getID(), XMLPath);
								// Download images file
								// download.download(c.getLogo(), LogoPath);
							}
						} else {
							new DBAdapter(LoginActivity.this).updateTime(System.currentTimeMillis());
							redirect();
						}
					} else {
						Toast.makeText(LoginActivity.this, login.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute();
	}

	private void redirect() {
		finish();
		startActivity(new Intent(this, MainActivity.class));
	}

}
