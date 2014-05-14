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
	if (id == R.id.action_settings) {
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

	    EditText titleText = (EditText) rootView
		    .findViewById(R.id.editTextNewTitle);
	    EditText contentText = (EditText) rootView
		    .findViewById(R.id.editTextNewNote);
	    if (noteId != null) {
		titleText.setVisibility(View.GONE);
	    } else {
		
	    }

	    return rootView;
	}
    }

}
