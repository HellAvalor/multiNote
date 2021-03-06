package com.andreykaraman.multinote.model;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.db.DBNote;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.login.MainActivity;

public class ServerDBHelper extends IntentService {

    static final String LOG_SECTION = MainActivity.class.getName();

    public ServerDBHelper() {
	super("ServerDBHelper");
    }

    @Override
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

//	case R.id.load_note:
//	    Log.d(LOG_SECTION, "Get element");
//	    // TODO ���������� �� eventBus
//
//
//	    // �������� ���������� ������
//	    Note note = getNote(
//		    intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID, -1),
//		    intent.getLongExtra(APIStringConstants.CONST_NOTE_ID, -1));
//
//	    break;

	case R.id.edit_note:
	    Log.d(LOG_SECTION, "Edit element");
	    saveNote(
		    intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID, -1),
		    intent.getLongExtra(APIStringConstants.CONST_NOTE_ID, -1),
		    intent.getStringExtra(APIStringConstants.CONST_NOTE_CONTENT));
	    break;

	case R.id.delete_notes:
	    Log.d(LOG_SECTION, "Delete elements");
	    delNotes(sessionId,
		    intent.getLongArrayExtra(APIStringConstants.CONST_NOTE_ID));
	    break;
	case R.id.delete_note:
	    Log.d(LOG_SECTION, "Delete element");
	    delNote(sessionId, (int) intent.getLongExtra(
		    APIStringConstants.CONST_NOTE_ID, -1));
	    break;

	default:
	    Log.d(LOG_SECTION, "Wrong id");
	    break;
	}

    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	Log.d(LOG_SECTION, "onDestroy");
    }

    private Note getNote(int sessionId, long noteId) {
	Note note = new Note();
	ServerHelper sHelper = ServerHelper.getInstance();

	try {
	    note = sHelper.getNote(sessionId, noteId);
	} catch (UserExceptions e) {
	    e.printStackTrace();
	}
	return note;

    }

    private void getNotes(int sessionID) {
	Cursor cursor;

	getContentResolver()
		.delete(MyContentProvider.URI_NOTE_TABLE, "1", null);
	cursor = getContentResolver().query(MyContentProvider.URI_NOTE_TABLE,
		null, "1", null, null);

	if (cursor.getCount() == 0) {
	    ServerHelper sHelper = ServerHelper.getInstance();
	    ArrayList<Note> notes = sHelper.getNotes(sessionID);
	    for (Note note : notes) {
		Log.d(LOG_SECTION, note.getNoteId() + "/" + note.getNoteTitle()
			+ "/" + note.getNoteContent());
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

	ServerHelper sHelper = ServerHelper.getInstance();

	int noteId;
	try {
	    noteId = sHelper.addNote(sessionID, title, content);
	    ContentValues cv = new ContentValues();
	    cv.put(DBNote.NOTE_ID, noteId);
	    cv.put(DBNote.NOTE_TITLE, title);
	    cv.put(DBNote.NOTE_CONTENT, content);

	    Uri result = getContentResolver().insert(
		    MyContentProvider.URI_NOTE_TABLE, cv);
	    Log.d(LOG_SECTION, result.toString());
	} catch (UserExceptions e) {
	    e.printStackTrace();
	}

    }

    private void delNote(int sessionId, int noteId) {

	ServerHelper sHelper = ServerHelper.getInstance();
	try {
	    sHelper.delNote(sessionId, noteId);
	    getContentResolver().delete(MyContentProvider.URI_NOTE_TABLE,
		    DBNote.NOTE_ID + "=" + noteId, null);
	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private void delNotes(int sessionId, long[] ids) {

	for (long noteId : ids) {
	    delNote(sessionId, (int) noteId);
	}

    }

    private void saveNote(int sessionId, long id, String content) {
	ServerHelper sHelper = ServerHelper.getInstance();

	try {

	    sHelper.editNote(sessionId, id, content);
	    ContentValues cv = new ContentValues();
	    // cv.put(DBnote.NOTE_ID, note.getNoteTitle());

	    cv.put(DBNote.NOTE_CONTENT, content);

	    getContentResolver().update(MyContentProvider.URI_NOTE_TABLE, cv,
		    DBNote.NOTE_ID + "=" + id, null);

	} catch (UserExceptions e) {

	    e.printStackTrace();
	}

    }

}
