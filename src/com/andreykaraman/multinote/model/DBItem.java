package com.andreykaraman.multinote.model;


import android.content.ContentValues;
import android.database.Cursor;


public interface DBItem {

	String getTableName();

	String getCreateQuery();

	long getId();

	Long getIdAsObject();

	void fillFromCursor(Cursor cursor);

	ContentValues getContent();

}
