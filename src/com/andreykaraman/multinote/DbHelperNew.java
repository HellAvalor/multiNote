package com.andreykaraman.multinote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelperNew {

    public final static String LOG_TAG = "myLogs";
    public static final String DB_NAME = "main_db";
    public static final int DB_VERSION = 3;

    public static final String NOTE_ID = "_id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";

    public static final String NOTE_TABLE_NAME = "notes";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE IF NOT EXISTS "
	    + NOTE_TABLE_NAME
	    + " ("
	    + NOTE_ID
	    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	    + NOTE_TITLE
	    + " TEXT NOT NULL, " + NOTE_CONTENT + " TEXT NOT NULL " + ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private static SQLiteDatabase mDB;

    public DbHelperNew(Context ctx) {
	mCtx = ctx;
    }

    // открыть подключение
    public void open() {
	mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
	mDB = mDBHelper.getWritableDatabase();

    }

    // закрыть подключение
    public void close() {
	if (mDBHelper != null)
	    mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
	return mDB.query(NOTE_TABLE_NAME, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(SQLiteDatabase db, String title, String content) {
	ContentValues cv = new ContentValues();
	cv.put(NOTE_TITLE, title);
	cv.put(NOTE_CONTENT, content);
	db.insert(NOTE_TABLE_NAME, null, cv);
    }

    // добавить запись в DB_TABLE
    public static void addRec(String title, String content) {
	ContentValues cv = new ContentValues();
	cv.put(NOTE_TITLE, title);
	cv.put(NOTE_CONTENT, content);
	mDB.insert(NOTE_TABLE_NAME, null, cv);
	printTableLog(getCursor(mDB, NOTE_TABLE_NAME, null, null));
    }

    // обновить запись в DB_TABLE
    public static void saveRec(int id, String title, String content) {
	ContentValues cv = new ContentValues();
	cv.put(NOTE_ID, id);
	cv.put(NOTE_TITLE, title);
	cv.put(NOTE_CONTENT, content);

	mDB.update(NOTE_TABLE_NAME, cv, NOTE_ID + " = " + id, null);
	printTableLog(getCursor(mDB, NOTE_TABLE_NAME, null, null));
    }

    // обновить запись в DB_TABLE
    public static void saveRec(Note note) {
	ContentValues cv = new ContentValues();
	cv.put(NOTE_ID, note.getNoteId());
	cv.put(NOTE_TITLE, note.getNoteTitle());
	cv.put(NOTE_CONTENT, note.getNoteContent());

	mDB.update(NOTE_TABLE_NAME, cv, NOTE_ID + " = " + note.getNoteId(), null);
	printTableLog(getCursor(mDB, NOTE_TABLE_NAME, null, null));
    }
    // удалить запись из DB_TABLE
    public void delRec(SQLiteDatabase db, long id) {
	db.delete(NOTE_TABLE_NAME, NOTE_ID + " = " + id, null);
    }

    // удалить запись из DB_TABLE
    public static void delRec(long id) {
	mDB.delete(NOTE_TABLE_NAME, NOTE_ID + " = " + id, null);
	printTableLog(getCursor(mDB, NOTE_TABLE_NAME, null, null));
    }

    public static Note getNote(Context context, long noteId) {

	Cursor cursor = getCursor(mDB, NOTE_TABLE_NAME, null,
		NOTE_ID + " = " + noteId);
	Note tempNote = new Note();
	if (cursor != null) {
	    if (cursor.moveToFirst()) {
		tempNote.setNoteId(cursor.getLong(cursor
			.getColumnIndex(NOTE_ID)));
		tempNote.setNoteTitle(cursor.getString(cursor
			.getColumnIndex(NOTE_TITLE)));
		tempNote.setNoteContent(cursor.getString(cursor
			.getColumnIndex(NOTE_CONTENT)));

	    }
	} else
	    Log.d(LOG_TAG, "Cursor is null");

	printTableLog(cursor);

	return tempNote;
    }
    
    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
		int version) {
	    super(context, name, factory, version);
	}

	// создаем и заполняем БД
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(CREATE_TABLE_NOTE);
	    testfillTable(db);
	    printTableLog(getCursor(db, NOTE_TABLE_NAME, null, null));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    deleteDB(db, 1);
	    onCreate(db);
	}

	public void deleteDB(SQLiteDatabase db, int status) {
	    Log.d(LOG_TAG, "----Delete tables:-----");

	    db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME + ";");

	}
    }

    public void testfillTable(SQLiteDatabase db) {

	// Add default records to ProfCard
	ContentValues CV = new ContentValues();

	String[] fields_name = { "Зарплата", "Пассивный доход", "Общий доход",
		"Ежемесячные затраты", "Ежемесячный поток",
		"Ежеквартальный поток", "Месяц", "Профессия", "Наличность",
		"Ежемесячный поток", "Ежеквартальный поток", "Месяц",
		"Профессия", "Наличность", "Ежемесячный поток",
		"Ежеквартальный поток", "Месяц", "Профессия", "Наличность" };

	for (String fName : fields_name) {
	    CV.clear();

	    CV.put(NOTE_TITLE, fName);
	    CV.put(NOTE_CONTENT, fName + " content");
	    db.insert(NOTE_TABLE_NAME, null, CV);

	}

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
}