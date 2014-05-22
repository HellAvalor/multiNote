package com.andreykaraman.multinote;

import com.andreykaraman.multinote.model.Note;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends Activity {
   
    static Note note;
    static long noteId;
    static EditText titleText;
    static EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_note);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

	Intent intent = getIntent();
	noteId = intent.getLongExtra("id", -1); // if it's a string you stored.

	// Toast.makeText(this, "create for id " + noteId, Toast.LENGTH_SHORT)
	// .show();

	if (noteId != -1) {
	    note = DbHelperNew.getNote(this, noteId);
	    this.setTitle(note.getNoteTitle());
	} else {
	    this.setTitle(getString(R.string.new_note));
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.edit_note, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.new_note, container,
		    false);

	    titleText = (EditText) rootView.findViewById(R.id.editTextNewTitle);
	    contentText = (EditText) rootView
		    .findViewById(R.id.editTextNewNote);
	    if (noteId != -1) {
		titleText.setVisibility(View.GONE);

		titleText.setText(note.getNoteTitle());
		contentText.setText(note.getNoteContent());
	    }

	    // Toast.makeText(container.getContext(),
	    // "edit/add note id " + noteId, Toast.LENGTH_LONG).show();

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
	if (!contentText.getText().toString()
		.equals(note.getNoteContent())) {
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
	    DbHelperNew.saveRec(new Note(noteId, titleText.getText().toString(), contentText
		    .getText().toString()));
	    Toast.makeText(getBaseContext(), R.string.note_added, Toast.LENGTH_SHORT).show();
	    
	} else {
	    DbHelperNew.addRec(titleText.getText().toString(), contentText
		    .getText().toString());
	    Toast.makeText(getBaseContext(), R.string.note_added, Toast.LENGTH_SHORT).show();
	    // Note.addEditNote(this, new Note(0,
	    // titleText.getText().toString(),
	    // contentText.getText().toString()), Note.ADD);
	}
	
	  //  Intent intentMessage=new Intent();
	    
	        // put the message in Intent
	    //    intentMessage.putExtra("MESSAGE",message);
	        // Set The Result in Intent
	    //    setResult(2,intentMessage);
	        // finish The activity 
	    //    finish();
	
//	setResult(RESULT_OK, this.getIntent());
	finish();
	
    }
}
