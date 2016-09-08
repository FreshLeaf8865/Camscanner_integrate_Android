package com.android.lgm.gmails;

import java.io.File;
import java.util.ArrayList;

import javax.mail.event.TransportEvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.lgm.MainActivity;
import com.android.lgm.Objects.LGMMail;
import com.android.lgm.Objects.LGMPairValue;

public class DBAdapter {
	private static final String DATABASE_NAME = "Database_LGM_Solution";
	private static final String prefix = "c_";
	private static final String KEY_ID = prefix + "id";
	private static final String KEY_DISPLAY = prefix + "display";
	private static final String KEY_TO = prefix + "to";
	private static final String KEY_CC = prefix + "cc";
	private static final String KEY_SUBJECT = prefix + "subject";
	private static final String KEY_BODY = prefix + "body";
	private static final String KEY_TIME = prefix + "time";

	private static final String KEY_NAME = prefix + "name";
	private static final String KEY_PATH = prefix + "path";
	private static final String KEY_MAIL_ID = prefix + "mail_id";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;
	private static final int DATABASE_VERSION = 1;
	private Context mContext;
	public static int count = 0;
	/*
	 * tables
	 */

	private static final String TABLE_MAIL = prefix + "mails";
	private static final String TABLE_ATTACH = prefix + "attaches";
	private static final String TABLE_LAST_UPDATE = prefix + "last_update";
	// create string
	private static final String CREATE_TABLE_MAIL = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MAIL
			+ " ("
			+ KEY_ID
			+ " integer primary key AUTOINCREMENT not null, "
			+ KEY_DISPLAY
			+ " text not null, "
			+ KEY_TO
			+ " text not null, "
			+ KEY_CC
			+ " text not null, "
			+ KEY_SUBJECT
			+ " text not null, "
			+ KEY_BODY
			+ " text not null); ";
	private static final String CREATE_TABLE_ATTACH = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_ATTACH
			+ " ("
			+ KEY_ID
			+ " integer primary key AUTOINCREMENT not null, "
			+ KEY_NAME
			+ " text not null, "
			+ KEY_PATH
			+ " text not null, "
			+ KEY_MAIL_ID
			+ " integer not null); ";
	private static final String CREATE_TABLE_LAST_UPDATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_LAST_UPDATE
			+ " ("
			+ KEY_ID
			+ " integer primary key AUTOINCREMENT not null, "
			+ KEY_TIME
			+ " NUMERIC not null DEFAULT 0); ";

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE_MAIL);
				db.execSQL(CREATE_TABLE_ATTACH);
				db.execSQL(CREATE_TABLE_LAST_UPDATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		}

	}

	public DBAdapter(Context ctx) {
		this.mContext = ctx;
	}

	public DBAdapter open() {
		count++;
		Log.i("COUNT", count + "");
		if (mDB != null && mDB.isOpen()) {
			return this;
		} else {
			try {
				mDbHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,
						DATABASE_VERSION);
				mDB = mDbHelper.getWritableDatabase();
			} catch (SQLException e) {

			}
		}
		return this;
	}

	public void close() {
		count--;
		Log.i("COUNT", count + "");
		if (count == 0)
			mDbHelper.close();
	}

	private boolean createTable(String tableName, String sqlCommand) {
		open();
		try {
			mDB.execSQL("DROP TABLE IF EXISTS " + tableName);
			mDB.execSQL(sqlCommand);
			close();
			return true;
		} catch (Exception e) {
			close();
			return false;
		}
	}

	public boolean updateTime(long t) {
		open();
		long id = -1l;

		Cursor cursor = mDB.query(true, TABLE_LAST_UPDATE, null, null, null,
				null, null, null, null);
		ContentValues inititalValues = new ContentValues();
		inititalValues.put(KEY_TIME, t);
		if (cursor.moveToFirst()) {
			id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
			mDB.update(TABLE_LAST_UPDATE, inititalValues, KEY_ID + "=" + id,
					null);
		} else {
			mDB.insert(TABLE_LAST_UPDATE, null, inititalValues);
		}
		close();
		return true;
	}

	public long getUpdateTime() {
		long t = 0l;
		open();
		Cursor cursor = mDB.query(true, TABLE_LAST_UPDATE, null, null, null,
				null, null, null, null);

		if (cursor.moveToFirst()) {
			t = cursor.getLong(cursor.getColumnIndex(KEY_TIME));
		}
		cursor.close();
		close();
		return t;
	}

	public long addMail(LGMMail mail) {
		open();
		long idAdded = 0;

		ContentValues inititalValues = new ContentValues();
		inititalValues.put(KEY_DISPLAY, mail.getDisplay());
		inititalValues.put(KEY_TO, mail.getTo());
		inititalValues.put(KEY_CC, mail.getCc());
		inititalValues.put(KEY_SUBJECT, mail.getSubject());
		inititalValues.put(KEY_BODY, mail.getBody());
		try {
			idAdded = mDB.insert(TABLE_MAIL, null, inititalValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return idAdded;
	}

	public long addAttach(long mail_id, LGMPairValue p) {
		open();
		long idAdded = 0;

		ContentValues inititalValues = new ContentValues();
		inititalValues.put(KEY_NAME, p.getName());
		inititalValues.put(KEY_PATH, p.getValue());
		inititalValues.put(KEY_MAIL_ID, mail_id);
		try {
			idAdded = mDB.insert(TABLE_ATTACH, null, inititalValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return idAdded;
	}

	public ArrayList<LGMMail> getAllMail() {
		ArrayList<LGMMail> mails = new ArrayList<LGMMail>();
		open();
		Cursor cursor = mDB.query(true, TABLE_MAIL, null, null, null, null,
				null, KEY_ID, null);

		if (cursor.moveToFirst()) {
			do {
				LGMMail m = new LGMMail(cursor.getInt(cursor
						.getColumnIndex(KEY_ID)), cursor.getString(cursor
						.getColumnIndex(KEY_DISPLAY)), cursor.getString(cursor
						.getColumnIndex(KEY_TO)), cursor.getString(cursor
						.getColumnIndex(KEY_CC)), cursor.getString(cursor
						.getColumnIndex(KEY_SUBJECT)), cursor.getString(cursor
						.getColumnIndex(KEY_BODY))) {
					@Override
					public void messageDelivered(TransportEvent arg0) {

						deleteMail(getID());
						deleteAttachs(getID());
						if (this.getAttaches() != null) {
							for (LGMPairValue value : getAttaches()) {
								File f = new File(value.getValue());
								f.delete();
							}
						}

						Log.d("ANH YEU EM", "aaaaa");
						super.messageDelivered(arg0);

					}

					@Override
					public void messageNotDelivered(TransportEvent arg0) {
						super.messageNotDelivered(arg0);

					}

					@Override
					public void messagePartiallyDelivered(TransportEvent arg0) {
						super.messageNotDelivered(arg0);
					}

				};
				m.setAttaches(getAttaches(m.getID()));
				mails.add(m);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return mails;
	}

	public ArrayList<LGMPairValue> getAttaches(int mail_id) {
		ArrayList<LGMPairValue> attaches = new ArrayList<LGMPairValue>();
		open();
		Cursor cursor = mDB.query(true, TABLE_ATTACH, null, KEY_MAIL_ID + "="
				+ mail_id, null, null, null, KEY_ID, null);

		if (cursor.moveToFirst()) {
			LGMPairValue p = new LGMPairValue(cursor.getString(cursor
					.getColumnIndex(KEY_NAME)), cursor.getString(cursor
					.getColumnIndex(KEY_PATH)));
			attaches.add(p);
		}
		cursor.close();
		close();
		return attaches;
	}

	public boolean deleteMail(long id) {
		boolean value;
		open();
		value = mDB.delete(TABLE_MAIL, KEY_ID + "=" + id, null) > 0;
		close();
		return value;
	}

	public boolean deleteAttachs(long mail_id) {
		boolean value;
		open();
		value = mDB.delete(TABLE_ATTACH, KEY_MAIL_ID + "=" + mail_id, null) > 0;
		close();
		return value;
	}

}