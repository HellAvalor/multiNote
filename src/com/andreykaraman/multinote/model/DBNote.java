package com.andreykaraman.multinote.model;

import android.content.ContentValues;
import android.database.Cursor;

public class DBNote extends AbsDBObject {

    public static final String TABLE_NAME = "DBNote";

    public static final String NOTE_ID = "_id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";

    public static final String[] FIELDS = { NOTE_ID, NOTE_TITLE, NOTE_CONTENT };

    public static final String CREATE_TABLE =
	    "CREATE TABLE " + TABLE_NAME
	    + "(" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + NOTE_TITLE + " TEXT NOT NULL,"
	    + NOTE_CONTENT + " TEXT NOT NULL );";

    public long id = EMPTY_ID;
    public String title;
    public String content;

    @Override
    public String toString() {
	return title;
    }

    public DBNote(final Cursor cursor) {
	fillFromCursor(cursor);
    }


    @Override
    public void fillFromCursor(Cursor cursor) {
	this.id = cursor.getLong(0);
	this.title = cursor.getString(1);
	this.content = cursor.getString(2);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion into
     * the database.
     */
    public ContentValues getContent() {
	final ContentValues values = new ContentValues();
	if (id != EMPTY_ID) {
	    values.put(NOTE_ID, id);
	}

	values.put(NOTE_TITLE, title);
	values.put(NOTE_CONTENT, content);

	return values;
    }

    @Override
    public long getId() {
	return id;
    }

    @Override
    public String getTableName() {
	return TABLE_NAME;
    }

    @Override
    public String getCreateQuery() {
	return CREATE_TABLE;
    }

}
