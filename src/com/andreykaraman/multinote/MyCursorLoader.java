package com.andreykaraman.multinote;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

class MyCursorLoader extends CursorLoader {

    DB db;
    
    public MyCursorLoader(Context context, DB db) {
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