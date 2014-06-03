package com.andreykaraman.multinote.ui.list.menu;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.R.id;
import com.andreykaraman.multinote.R.layout;
import com.andreykaraman.multinote.R.menu;
import com.andreykaraman.multinote.R.string;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.utils.ServerDBSimulation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends Activity {

    protected final String TAG = this.getClass().getSimpleName();

    final static int TASK1_CODE = 1;
    final static int TASK2_CODE = 2;
    final static int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_TASK = "task";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_STATUS = "status";

    Note note = new Note();
    static long noteId;
    static EditText titleText;
    static EditText contentText;
    public final static String BROADCAST_ACTION = "com.andreykaraman.multinote.getnotebroadcast";
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_note);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

	Intent intent = getIntent();
	noteId = intent.getLongExtra("id", -1);

	if (noteId != -1) {
	    // создаем фильтр для BroadcastReceiver
	    final ProgressDialog ringProgressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);
	    ringProgressDialog.setCancelable(false);
	    br = new BroadcastReceiver() {
		
		// действия при получении сообщений
		public void onReceive(Context context, Intent intent) {


		    // int task = intent.getIntExtra(PARAM_TASK, 0);
		    int status = intent.getIntExtra(PARAM_STATUS, 0);
		    Log.d(TAG, "onReceive: task = " + ", status = " + status);

		    // Ловим сообщения о старте задач
		    if (status == STATUS_START) {
			// TODO add here loader screen
					    }

		    // Ловим сообщения об окончании задач
		    if (status == STATUS_FINISH) {

			Log.d(TAG, "onReceive id= " + noteId + " title="
				+ intent.getStringExtra("title") + " content="
				+ intent.getStringExtra("content"));
			// int result = intent.getIntExtra(PARAM_RESULT, 0);
			note.setNoteId(noteId);
			note.setNoteTitle(intent.getStringExtra("title"));
			note.setNoteContent(intent.getStringExtra("content"));

			Log.d(TAG, "onReceive: task = " + ", status = "
				+ status);

			titleText.setText(note.getNoteTitle());
			contentText.setText(note.getNoteContent());
			setTitle(note.getNoteTitle());
			ringProgressDialog.dismiss();
		    }
		}
	    };

	} else {
	    this.setTitle(getString(R.string.new_note));
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_edit_note, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.

	switch (item.getItemId()) {
	case android.R.id.home:

	    if (isContentChanged()) {
		// return false;
		Toast.makeText(this, "back Changed true", Toast.LENGTH_SHORT)
			.show();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		showCancelChangesDialog(this);
	    } else {
		Toast.makeText(this, "back Changed false", Toast.LENGTH_SHORT)
			.show();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		finish();
		return true;
	    }

	    Toast.makeText(this, "back Changed out", Toast.LENGTH_SHORT).show();
	    break;

	case R.id.action_save_note:
	    saveNote();
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
	// дерегистрируем (выключаем) BroadcastReceiver
	super.onDestroy();

    }

    protected void onPause() {
	super.onPause();
	if (br != null) {
	    unregisterReceiver(br);
	}

    }

    protected void onResume() {
	super.onResume();
	IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
	intFilt.addCategory(Intent.CATEGORY_DEFAULT);
	// регистрируем (включаем) BroadcastReceiver
	registerReceiver(br, intFilt);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_new_note, container,
		    false);

	    titleText = (EditText) rootView.findViewById(R.id.editTextNewTitle);
	    contentText = (EditText) rootView
		    .findViewById(R.id.editTextNewNote);

	    if (noteId != -1) {

		titleText.setVisibility(View.GONE);

		Intent intent = new Intent(container.getContext(),
			ServerDBSimulation.class)
			.putExtra("update_notes_on_remote", R.id.load_note)
			.putExtra("getNote", noteId).putExtra(PARAM_TIME, 7)
			.putExtra(PARAM_TASK, TASK1_CODE);

		container.getContext().startService(intent);

	    }

	    return rootView;
	}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    if (isContentChanged()) {
		return false;
	    }
	    finish();
	    return true;
	}

	return super.onKeyDown(keyCode, event);
    }

    private boolean isContentChanged() {
	
	//TODO add for new note
	if (!contentText.getText().toString().equals(note.getNoteContent())) {
	    Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
	    showCancelChangesDialog(this);
	    return true;
	}
	Toast.makeText(this, "Not Changed", Toast.LENGTH_SHORT).show();
	return false;
    }

    private void showCancelChangesDialog(Context context) {

	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
	myAlertDialog.setMessage(getText(R.string.save_note));
	myAlertDialog.setPositiveButton(getText(R.string.ok),
		new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface arg0, int arg1) {
			saveNote();
		    }
		});
	myAlertDialog.setNegativeButton(getText(R.string.cancel),
		new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface arg0, int arg1) {
			finish();
		    }
		});
	myAlertDialog.show();

    }

    private void saveNote() {

	if (noteId != -1) {

	    Intent intent = new Intent(this, ServerDBSimulation.class)
		    .putExtra("update_notes_on_remote", R.id.edit_note)
		    .putExtra("id", noteId)
		    .putExtra("title", titleText.getText().toString())
		    .putExtra("content", contentText.getText().toString());

	    this.startService(intent);

	    Toast.makeText(getBaseContext(), R.string.note_updated,
		    Toast.LENGTH_SHORT).show();

	} else {

	    Intent intent = new Intent(this, ServerDBSimulation.class)
		    .putExtra("update_notes_on_remote", R.id.add_note)
		    .putExtra("title", titleText.getText().toString())
		    .putExtra("content", contentText.getText().toString());

	    this.startService(intent);

	    Toast.makeText(getBaseContext(), R.string.note_added,
		    Toast.LENGTH_SHORT).show();
	}

	finish();

    }
}
