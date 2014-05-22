package com.andreykaraman.multinote;

import java.util.concurrent.TimeUnit;

import com.andreykaraman.multinote.DbHelperNew;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class MyCursorLoader extends CursorLoader {

    DbHelperNew db;

    public MyCursorLoader(Context context, DbHelperNew db) {
	super(context);
	this.db = db;
    }

    @Override
    public Cursor loadInBackground() {
	Cursor cursor = db.getAllData();
	try {
	    TimeUnit.SECONDS.sleep(3);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return cursor;
    }

}
