package com.andreykaraman.multinote.utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.DBNote;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.login.MainActivity;

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
	int sessionId = intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID,
		-1);
	switch (query) {
	case R.id.update_notes:
	    getNotes(sessionId);
	    break;

	case R.id.add_note:
	    Log.d(LOG_SECTION, "Add element");
	    addNote(sessionId,
		    intent.getStringExtra(APIStringConstants.CONST_NOTE_TITLE),
		    intent.getStringExtra(APIStringConstants.CONST_NOTE_CONTENT));
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
	    Note note = getNote(
		    intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID, -1),
		    intent.getLongExtra(APIStringConstants.CONST_NOTE_ID, -1));

	    // сообщаем об окончании задачи
	    broadIntent.setAction(EditNoteActivity.BROADCAST_ACTION);
	    broadIntent.addCategory(Intent.CATEGORY_DEFAULT);

	    broadIntent.putExtra(EditNoteActivity.PARAM_STATUS,
		    EditNoteActivity.STATUS_FINISH);
	    broadIntent.putExtra(APIStringConstants.CONST_NOTE_TITLE,
		    note.getNoteTitle());
	    broadIntent.putExtra(APIStringConstants.CONST_NOTE_CONTENT,
		    note.getNoteContent());

	    // intent.putExtra(EditNoteActivity.PARAM_RESULT, time * 100);
	    sendBroadcast(broadIntent);

	    break;

	case R.id.edit_note:
	    Log.d(LOG_SECTION, "Edit element");

	    saveNote(
		    intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID, -1),
		    intent.getLongExtra(APIStringConstants.CONST_NOTE_ID, -1),
		    intent.getStringExtra(APIStringConstants.CONST_NOTE_CONTENT));
	    break;

	case R.id.delete_note:
	    Log.d(LOG_SECTION, "Delete element");
	    delNote(intent.getIntExtra("delId", -1));

	    break;

	case R.id.delete_notes:
	    Log.d(LOG_SECTION, "Delete elements");
	    delNotes(sessionId,
		    intent.getLongArrayExtra(APIStringConstants.CONST_NOTE_ID));

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

    private Note getNote(int sessionId, long noteId) {
	Note note = new Note();
	ServerHelper ss = ServerHelper.getInstance();

	try {
	    note = ss.getNote(sessionId, noteId);
	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return note;

    }

    private Note getNote(long id) {

	Note note = new Note();
	Log.d(LOG_SECTION, "getNoteThreadInit");
	try {

	    Cursor result;

	    TimeUnit.SECONDS.sleep(3);

	    result = getContentResolver().query(
		    MyContentProvider.URI_NOTE_TABLE, null,
		    DBNote.NOTE_ID + "=" + id, null, null);
	    Log.d(LOG_SECTION, "id = " + id + " result " + result.toString());

	    if (result.moveToFirst()) {
		note.setNoteId(result.getInt(result
			.getColumnIndex(DBNote.NOTE_ID)));
		note.setNoteTitle(result.getString(result
			.getColumnIndex(DBNote.NOTE_TITLE)));
		note.setNoteContent(result.getString(result
			.getColumnIndex(DBNote.NOTE_CONTENT)));
		Log.d(LOG_SECTION,
			note.getNoteId() + " / " + note.getNoteTitle() + " / "
				+ note.getNoteContent());
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return note;
    }

    private void getNotes(int sessionID) {
	Log.d(LOG_SECTION, "ThreadInit");
	Cursor cursor;

	getContentResolver()
		.delete(MyContentProvider.URI_NOTE_TABLE, "1", null);
	cursor = getContentResolver().query(MyContentProvider.URI_NOTE_TABLE,
		null, "1", null, null);

	if (cursor.getCount() == 0) {
	    ServerHelper ss = ServerHelper.getInstance();
	    ArrayList<Note> notes = ss.getNotes(sessionID);
	    for (Note note : notes) {
		ContentValues cv = new ContentValues();
		cv.put(DBNote.NOTE_ID, note.getNoteId());
		cv.put(DBNote.NOTE_TITLE, note.getNoteTitle());
		cv.put(DBNote.NOTE_CONTENT, note.getNoteContent());

		Uri result = getContentResolver().insert(
			MyContentProvider.URI_NOTE_TABLE, cv);
		Log.d(LOG_SECTION, result.toString());
	    }
	}
    }

    private void addNote(int sessionID, String title, String content) {
	Log.d(LOG_SECTION, "ThreadInit");

	ServerHelper ss = ServerHelper.getInstance();

	int noteId;
	try {

	    noteId = ss.addNote(sessionID, title, content);
	    ContentValues cv = new ContentValues();
	    // cv.put(DBnote.NOTE_ID, note.getNoteTitle());
	    cv.put(DBNote.NOTE_ID, noteId);
	    cv.put(DBNote.NOTE_TITLE, title);
	    cv.put(DBNote.NOTE_CONTENT, content);

	    Uri result = getContentResolver().insert(
		    MyContentProvider.URI_NOTE_TABLE, cv);
	    Log.d(LOG_SECTION, result.toString());

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void delNote(int id) {
	Log.d(LOG_SECTION, "ThreadInit");
	try {
	    TimeUnit.SECONDS.sleep(3);
	    int result = getContentResolver().delete(
		    MyContentProvider.URI_NOTE_TABLE,
		    DBNote.NOTE_ID + "=" + id, null);
	    Log.d(LOG_SECTION, "" + result);

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private void delNote(int sessionId, int noteId) {

	ServerHelper ss = ServerHelper.getInstance();
	try {
	    ss.delNote(sessionId, noteId);
	    getContentResolver().delete(MyContentProvider.URI_NOTE_TABLE,
		    DBNote.NOTE_ID + "=" + noteId, null);
	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void delNotes(String[] ids) {
	Log.d(LOG_SECTION, "ThreadInit");

	String selectionArgs[] = ids;
	String selection = DBNote.NOTE_ID + " in (";
	for (int i = 0; i < selectionArgs.length; i++)
	    selection += "?, ";
	selection = selection.substring(0, selection.length() - 2) + ")";
	// selection is 'DBColumns.History._ID + " in (?, ?, ?)"'

	try {
	    TimeUnit.SECONDS.sleep(3);
	    int result = getContentResolver().delete(
		    MyContentProvider.URI_NOTE_TABLE, selection, selectionArgs);
	    Log.d(LOG_SECTION, "" + result);

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    private void delNotes(int sessionId, long[] ids) {

	for (long noteId : ids) {
	    delNote(sessionId, (int) noteId);
	}

	// String selectionArgs[] = ids;
	// String selection = DBNote.NOTE_ID + " in (";
	// for (int i = 0; i < selectionArgs.length; i++)
	// selection += "?, ";
	// selection = selection.substring(0, selection.length() - 2) + ")";
	// // selection is 'DBColumns.History._ID + " in (?, ?, ?)"'
	//
	// int result = getContentResolver().delete(
	// MyContentProvider.URI_NOTE_TABLE, selection, selectionArgs);
	// Log.d(LOG_SECTION, "" + result);
    }

    private void saveNote(int sessionId, long id, String content) {
	Log.d(LOG_SECTION, "ThreadInit");

	ServerHelper ss = ServerHelper.getInstance();

	try {

	    ss.editNote(sessionId, id, content);
	    ContentValues cv = new ContentValues();
	    // cv.put(DBnote.NOTE_ID, note.getNoteTitle());

	    cv.put(DBNote.NOTE_CONTENT, content);

	    getContentResolver().update(MyContentProvider.URI_NOTE_TABLE, cv,
		    DBNote.NOTE_ID + "=" + id, null);

	} catch (UserExceptions e) {

	    e.printStackTrace();
	}

    }

    private void saveNote(long id, String title, String content) {
	try {

	    TimeUnit.SECONDS.sleep(3);

	    ContentValues cv = new ContentValues();

	    // Defines a variable to contain the number of updated rows

	    cv.put(DBNote.NOTE_ID, id);
	    cv.put(DBNote.NOTE_TITLE, title);
	    cv.put(DBNote.NOTE_CONTENT, content);

	    getContentResolver().update(MyContentProvider.URI_NOTE_TABLE, // the
									  // user
									  // dictionary
									  // content
									  // URI
		    cv, // the columns to update
		    DBNote.NOTE_ID + "=" + id, // the column to select on
		    null // the value to compare to
		    );

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
