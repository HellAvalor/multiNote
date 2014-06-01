package com.andreykaraman.multinote.utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.andreykaraman.multinote.EditNoteActivity;
import com.andreykaraman.multinote.MainActivity;
import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.model.DBItem;
import com.andreykaraman.multinote.model.DBnote;
import com.andreykaraman.multinote.model.Note;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ServerDBSimulation extends IntentService {

    static final String LOG_SECTION = MainActivity.class.getName();

    public ServerDBSimulation() {
	super("ServerDBSimulation");
    }

    public void onCreate() {
	super.onCreate();
	Log.d(LOG_SECTION, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
	Log.d(LOG_SECTION, "onHandleIntent");
	int query = intent.getIntExtra("update_notes_on_remote",
		R.id.update_notes);

	switch (query) {
	case R.id.update_notes:
	    getNotes();
	    break;

	case R.id.add_note:
	    Log.d(LOG_SECTION, "Add element");
	    addNote(intent.getStringExtra("title"),
		    intent.getStringExtra("content"));
	    break;

	case R.id.load_note:
	    Log.d(LOG_SECTION, "Get element");

	    Intent broadIntent = new Intent();
	    broadIntent.setAction(EditNoteActivity.BROADCAST_ACTION);
	    broadIntent.addCategory(Intent.CATEGORY_DEFAULT);
	    // intent.putExtra(EditNoteActivity.PARAM_TASK, task);
	    broadIntent.putExtra(EditNoteActivity.PARAM_STATUS,
		    EditNoteActivity.STATUS_START);
	    sendBroadcast(broadIntent);

	    // начинаем выполнение задачи
	    Note note = getNote(intent.getLongExtra("getNote", -1));

	    // сообщаем об окончании задачи
	    broadIntent.setAction(EditNoteActivity.BROADCAST_ACTION);
	    broadIntent.addCategory(Intent.CATEGORY_DEFAULT);

	    broadIntent.putExtra(EditNoteActivity.PARAM_STATUS,
		    EditNoteActivity.STATUS_FINISH);
	    broadIntent.putExtra("title", note.getNoteTitle());
	    broadIntent.putExtra("content", note.getNoteContent());

	    // intent.putExtra(EditNoteActivity.PARAM_RESULT, time * 100);
	    sendBroadcast(broadIntent);

	    break;

	case R.id.edit_note:
	    Log.d(LOG_SECTION, "Edit element");

	    saveNote(intent.getLongExtra("id", -1),
		    intent.getStringExtra("title"),
		    intent.getStringExtra("content"));
	    break;

	case R.id.delete_note:
	    Log.d(LOG_SECTION, "Delete element");
	    delNote(intent.getIntExtra("delId", -1));

	    break;

	default:
	    Log.d(LOG_SECTION, "Wrong id");
	    break;
	}

    }

    public void onDestroy() {
	super.onDestroy();
	Log.d(LOG_SECTION, "onDestroy");
    }

    private void getNotes() {
	Log.d(LOG_SECTION, "ThreadInit");
	try {
	    
	    //TODO update after server API connection
	    Cursor cursor;

	    TimeUnit.SECONDS.sleep(3);

	    cursor = getContentResolver().query(
		    MyContentProvider.URI_NOTE_TABLE, null, "1", null, null);
	    if (cursor.getCount() == 0) {
		ServerSimulation ss = ServerSimulation.getInstance();
		ArrayList<Note> notes = ss.getNotes();
		for (Note note : notes) {
		    ContentValues cv = new ContentValues();

		    cv.put(DBnote.NOTE_TITLE, note.getNoteTitle());
		    cv.put(DBnote.NOTE_CONTENT, note.getNoteContent());

		    Uri result = getContentResolver().insert(
			    MyContentProvider.URI_NOTE_TABLE, cv);
		    Log.d(LOG_SECTION, result.toString());
		}
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private void addNote(String title, String content) {
	Log.d(LOG_SECTION, "ThreadInit");
	try {
	    ServerSimulation ss = ServerSimulation.getInstance();

	    ss.addNote(title, content);
	    TimeUnit.SECONDS.sleep(3);
	    ContentValues cv = new ContentValues();
	    // cv.put(DBnote.NOTE_ID, note.getNoteTitle());
	    cv.put(DBnote.NOTE_TITLE, title);
	    cv.put(DBnote.NOTE_CONTENT, content);

	    Uri result = getContentResolver().insert(
		    MyContentProvider.URI_NOTE_TABLE, cv);
	    Log.d(LOG_SECTION, result.toString());

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private void delNote(int id) {
	Log.d(LOG_SECTION, "ThreadInit");
	try {
	    TimeUnit.SECONDS.sleep(3);
	    int result = getContentResolver().delete(
		    MyContentProvider.URI_NOTE_TABLE,
		    DBnote.NOTE_ID + "=" + id, null);
	    Log.d(LOG_SECTION, "" + result);

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private Note getNote(long id) {

	Note note = new Note();
	Log.d(LOG_SECTION, "getNoteThreadInit");
	try {

	    Cursor result;

	    TimeUnit.SECONDS.sleep(3);

	    result = getContentResolver().query(
		    MyContentProvider.URI_NOTE_TABLE, null,
		    DBnote.NOTE_ID + "=" + id, null, null);
	    Log.d(LOG_SECTION, "id = " + id + " result " + result.toString());

	    if (result.moveToFirst()) {
		note.setNoteId(result.getInt(result
			.getColumnIndex(DBnote.NOTE_ID)));
		note.setNoteTitle(result.getString(result
			.getColumnIndex(DBnote.NOTE_TITLE)));
		note.setNoteContent(result.getString(result
			.getColumnIndex(DBnote.NOTE_CONTENT)));
		Log.d(LOG_SECTION,
			note.getNoteId() + " / " + note.getNoteTitle() + " / "
				+ note.getNoteContent());
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return note;
    }

    private void saveNote(long id, String title, String content) {
	try {

	    TimeUnit.SECONDS.sleep(3);

	    ContentValues cv = new ContentValues();

	    // Defines a variable to contain the number of updated rows
	    int rowsUpdated;

	    cv.put(DBnote.NOTE_ID, id);
	    cv.put(DBnote.NOTE_TITLE, title);
	    cv.put(DBnote.NOTE_CONTENT, content);

	    rowsUpdated = getContentResolver().update(
		    MyContentProvider.URI_NOTE_TABLE, // the user dictionary
						      // content URI
		    cv, // the columns to update
		    DBnote.NOTE_ID + "=" + id, // the column to select on
		    null // the value to compare to
		    );

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
