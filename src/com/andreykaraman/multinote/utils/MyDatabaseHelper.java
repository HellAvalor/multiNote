package com.andreykaraman.multinote.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andreykaraman.multinote.model.DBNote;

public final class MyDatabaseHelper extends SQLiteOpenHelper {

	public final static String TAG = MyDatabaseHelper.class.getSimpleName();

	private final Context mContext;

	public Context getmContext() {
	    return mContext;
	}

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "com.andreykaraman.multinote.database";

	/*
	 * Table identifiers will be prefixed with _, since there is a collision
	 * with model class identifiers
	 */
	interface Tables {
		String TABLES_DBNOTE = DBNote.TABLE_NAME;
	//	String _DBServiceGroup = DBServiceGroup.TABLE_NAME;
	}

	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		//this.mContext = context.getApplicationContext();
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropAndRecreateDatabase(db);
	}

	private void createDatabase(SQLiteDatabase db) {
		db.execSQL(DBNote.CREATE_TABLE);
	//	db.execSQL(DBServiceGroup.CREATE_TABLE);
	}

	public void dropAndRecreateDatabase(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBNote.TABLE_NAME);
	//	db.execSQL("DROP TABLE IF EXISTS " + DBServiceGroup.TABLE_NAME);
		onCreate(db);
		notifyEveryone();
	}

	private void notifyEveryone() {
                // TODO
	}
}
