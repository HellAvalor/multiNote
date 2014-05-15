package com.andreykaraman.multinote;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class EditNoteActivity extends Activity {
    static String noteId;
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
	noteId = intent.getStringExtra("id"); // if it's a string you stored.

	Toast.makeText(this, "" + noteId, Toast.LENGTH_SHORT).show();

	if (noteId != null) {
	    this.setTitle(Note.getNote(this, Integer.getInteger(noteId))
		    .getNoteTitle());
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
	int id = item.getItemId();
	if (id == R.id.action_save_note) {

	    if (noteId != null) {
		Note.addEditNote(this, new Note(Integer.getInteger(noteId), titleText.getText().toString(),
			contentText.getText().toString()), Note.EDIT);
	    } else {
		Note.addEditNote(this, new Note(0, titleText.getText().toString(),
			contentText.getText().toString()), Note.ADD);
	    }

	    Toast.makeText(this, "adding/saving note", Toast.LENGTH_LONG)
		    .show();

	    return true;
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
	    if (noteId != null) {
		titleText.setVisibility(View.GONE);
		int id = Integer.getInteger(noteId);
		Note note = Note.getNote(rootView.getContext(), id);
		titleText.setText(note.getNoteTitle());
		contentText.setText(note.getNoteContent());
	    } else {

	    }

	    return rootView;
	}
    }

}
