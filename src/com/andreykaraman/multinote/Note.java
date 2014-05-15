package com.andreykaraman.multinote;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Note {

    final static String LOG_TAG = "myLogs";
    final static int ADD = 1;
    final static int EDIT = 2;

    private long noteId;
    private String noteTitle;
    private String noteContent;

    public Note(long noteId, String noteTitle, String noteContent) {

	this.noteId = noteId;
	this.noteTitle = noteTitle;
	this.noteContent = noteContent;
    }

    public Note() {

	this.noteId = 0;
	this.noteTitle = "";
	this.noteContent = "";

    }

    public String getNoteTitle() {
	return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
	this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
	return noteContent;
    }

    public void setNoteContent(String noteContent) {
	this.noteContent = noteContent;
    }

    public long getNoteId() {
	return noteId;
    }

    public static void addEditNote(Context context, Note note, int addEditFlag) {

	Log.d(LOG_TAG, "add new note");

	int maxID = 0;

	DbHelper mDbHelper = new DbHelper(context);
	SQLiteDatabase db = mDbHelper.getWritableDatabase();

	ContentValues CV = new ContentValues();

	CV.put(DbHelper.NOTE_TITLE, note.getNoteTitle());
	CV.put(DbHelper.NOTE_CONTENT, note.getNoteContent());

	if (addEditFlag == ADD) {
	    Cursor cursor = DbHelper.getCursor(db, DbHelper.NOTE_TABLE_NAME,
		    new String[] { "MAX(" + DbHelper.NOTE_ID + ") as _id" },
		    "1");

	    if (cursor != null) {
		if (cursor.moveToFirst()) {
		    maxID = cursor.getInt(0) + 1;
		}
	    } else {
		Log.d(LOG_TAG, "Cursor is null");
	    }
	    cursor.close();
	    CV.put(DbHelper.NOTE_ID, maxID);

	  
	    db.insert(DbHelper.NOTE_TABLE_NAME, null, CV);
	} else if (addEditFlag == EDIT) {
	    CV.put(DbHelper.NOTE_ID, note.getNoteId());
	    db.update(DbHelper.NOTE_TABLE_NAME, CV,
		    DbHelper.NOTE_ID + " = " + note.getNoteId(),
		    null);
	}

	Log.d(LOG_TAG, "--------- Table after add/edit --------------");

	Cursor cursor = DbHelper.getCursor(db, DbHelper.NOTE_TABLE_NAME, null,
		    null);
	DbHelper.printTableLog(cursor);
	
	db.close();

    }

    public static void dellNote(Context context, Note note) {

	DbHelper mDbHelper = new DbHelper(context);
	SQLiteDatabase db = mDbHelper.getWritableDatabase();

	db.delete(DbHelper.NOTE_TABLE_NAME,
		DbHelper.NOTE_ID + " = " + note.getNoteId(), null);
	
	
	Log.d(LOG_TAG, "--------- Table after delete --------------");

	Cursor cursor = DbHelper.getCursor(db, DbHelper.NOTE_TABLE_NAME, null,
		    null);
	DbHelper.printTableLog(cursor);
	
	db.close();
	
	
    }

    public static Note getNote(Context context, long noteId) {

	DbHelper mDbHelper = new DbHelper(context);
	SQLiteDatabase db = mDbHelper.getReadableDatabase();

	Cursor cursor = DbHelper.getCursor(db, DbHelper.NOTE_TABLE_NAME, null,
		DbHelper.NOTE_ID + " = " + noteId);
	Note tempNote = new Note();
	if (cursor != null) {
	    if (cursor.moveToFirst()) {
		tempNote.noteId = cursor.getLong(cursor
			.getColumnIndex(DbHelper.NOTE_ID));
		tempNote.noteTitle = cursor.getString(cursor
			.getColumnIndex(DbHelper.NOTE_TITLE));
		tempNote.noteContent = cursor.getString(cursor
			.getColumnIndex(DbHelper.NOTE_CONTENT));

	    }
	} else
	    Log.d(LOG_TAG, "Cursor is null");

	DbHelper.printTableLog(cursor);

	cursor.close();
	db.close();

	return tempNote;
    }
/*
    public static ArrayList<Note> getEstateCardsArray(Context context,
	    int big_small) {

	ArrayList<Note> estateArray = new ArrayList<Note>();
	DbHelper mDbHelper = new DbHelper(context);
	SQLiteDatabase db = mDbHelper.getReadableDatabase();

	Cursor cursor = null;

	if (big_small == 0) {
	    cursor = DbHelper.getCursor(db, DbHelper.ESTATE_TABLE_NAME, null,
		    null);
	} else {
	    cursor = DbHelper.getCursor(db, DbHelper.ESTATE_TABLE_NAME, null,
		    DbHelper.BIG_SMALL + " = " + big_small);

	}

	if (cursor != null) {
	    if (cursor.moveToFirst()) {
		do {
		    Note tempEstate = new Note();
		    tempEstate.estate_id = cursor.getLong(cursor
			    .getColumnIndex(DbHelper.NOTE_ID));
		    tempEstate.estate_name = cursor.getString(cursor
			    .getColumnIndex(DbHelper.ESTATE_NAME));
		    tempEstate.estate_descr = cursor.getString(cursor
			    .getColumnIndex(DbHelper.ESTATE_DESCR));
		    tempEstate.estate_investments = cursor.getInt(cursor
			    .getColumnIndex(DbHelper.ESTATE_INVESTMENTS));
		    tempEstate.estate_passive_income = cursor.getInt(cursor
			    .getColumnIndex(DbHelper.ESTATE_PASSIVE_INCOME));
		    tempEstate.estate_payments = cursor.getInt(cursor
			    .getColumnIndex(DbHelper.ESTATE_PAYMENTS));
		    tempEstate.estate_credit_id = cursor.getInt(cursor
			    .getColumnIndex(DbHelper.ESTATE_CREDIT_ID));
		    tempEstate.big_small = cursor.getInt(cursor
			    .getColumnIndex(DbHelper.BIG_SMALL));
		    estateArray.add(tempEstate);
		} while (cursor.moveToNext());
	    }
	} else
	    Log.d(LOG_TAG, "Cursor is null");

	DbHelper.printTableLog(cursor);

	cursor.close();
	db.close();

	return estateArray;
    }
*/

    public static ArrayList<Note> getNotes(Context context  ) {

	ArrayList<Note> notes = new ArrayList<Note>();
	DbHelper mDbHelper = new DbHelper(context);
	SQLiteDatabase db = mDbHelper.getReadableDatabase();

	Cursor cursor = null;


	cursor = DbHelper.getCursor(db, DbHelper.NOTE_TABLE_NAME,
		    null, null);

	if (cursor != null) {
	    if (cursor.moveToFirst()) {
		do {
		    Note tempNote = new Note();
		    tempNote.noteId = cursor.getLong(cursor
			    .getColumnIndex(DbHelper.NOTE_ID));
		    tempNote.noteTitle = cursor.getString(cursor
			    .getColumnIndex(DbHelper.NOTE_TITLE));
		    tempNote.noteContent = cursor.getString(cursor
			    .getColumnIndex(DbHelper.NOTE_CONTENT));
		    notes.add(tempNote);
		} while (cursor.moveToNext());
	    }
	} else
	    Log.d(LOG_TAG, "Cursor is null");

	DbHelper.printTableLog(cursor);

	cursor.close();
	db.close();

	return notes;
    }

}
