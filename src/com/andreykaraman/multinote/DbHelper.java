package com.andreykaraman.multinote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DbHelper extends SQLiteOpenHelper {
    public final static String LOG_TAG = "myLogs";

    public static final String DB_NAME = "main_db";
    public static final int DB_VERSION = 1;

    // -----------------------

    private Context cont;

    // ---------------------------------------------------------------------------
    public DbHelper(Context Cont) {
	super(Cont, DB_NAME, null, DB_VERSION);
	cont = Cont;
    }

    // поля таблица профессий
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";

    public static final String NOTE_TABLE_NAME = "notes";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE IF NOT EXISTS "
	    + NOTE_TABLE_NAME
	    + " ("
	    + NOTE_ID
	    + " INTEGER PRIMARY KEY, "
	    + NOTE_TITLE
	    + " TEXT NOT NULL, "
	    + NOTE_CONTENT
	    + " TEXT NOT NULL " + ");";


    public void testfillTable(SQLiteDatabase db) {

	// Add default records to ProfCard
	ContentValues CV = new ContentValues();

	String[] fields_name = { "Зарплата", "Пассивный доход", "Общий доход",
		"Ежемесячные затраты", "Ежемесячный поток",
		"Ежеквартальный поток", "Месяц", "Профессия", "Наличность" };
	int id = 1;
	for (String fName : fields_name) {
	    CV.clear();
	    CV.put(NOTE_ID, id);
	    CV.put(NOTE_TITLE, fName);
	    CV.put(NOTE_CONTENT, fName + " content");
	    db.insert(NOTE_TABLE_NAME, null, CV);
	    id++;
	}
	
	printTableLog(getCursor(db, NOTE_TABLE_NAME, null, null));
    }

    public static void printTableLog(Cursor c) {
	if (c != null) {
	    if (c.moveToFirst()) {
		String str;
		do {
		    str = "";
		    for (String cn : c.getColumnNames()) {
			str = str.concat(cn + "="
				+ c.getString(c.getColumnIndex(cn)) + "; ");
		    }
		    Log.d(LOG_TAG, str);

		} while (c.moveToNext());
	    }
	} else
	    Log.d(LOG_TAG, "Cursor is null");
    }

    public static Cursor getCursor(SQLiteDatabase db, String TableName,
	    String[] columns, String selection) {
	Cursor c = db.query(TableName, columns, selection, null, null, null,
		null);
	return c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	Log.d(LOG_TAG, "----Creating tables:-----");
	db.execSQL(CREATE_TABLE_NOTE);

	testfillTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME + ";");

	onCreate(db);

    }

    public void deleteDB(SQLiteDatabase db, int status) {
	Log.d(LOG_TAG, "----Delete tables:-----");

	db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME + ";");

    }

}
