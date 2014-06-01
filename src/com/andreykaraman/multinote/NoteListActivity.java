package com.andreykaraman.multinote;

import com.andreykaraman.multinote.model.DBNote;
import com.andreykaraman.multinote.utils.MyContentProvider;
import com.andreykaraman.multinote.utils.NoteAdapter;
import com.andreykaraman.multinote.utils.ServerDBSimulation;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NoteListActivity extends Activity {

    static Intent intent;
    static NoteAdapter scAdapter;
    static ListView noteView;
    static final String LOG_SECTION = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_note_list);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_note_list, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.change_pass) {
	    Toast.makeText(this, "going to change pass", Toast.LENGTH_SHORT)
		    .show();
	    Intent intent = new Intent(this, EditPassActivity.class);
	    startActivity(intent);

	    return true;
	}

	if (id == R.id.logout) {
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
	    finish();

	    return true;
	}

	if (id == R.id.actionAddNote) {

	    intent = new Intent(this, EditNoteActivity.class);
	    startActivity(intent);

	    return true;
	}
	return super.onOptionsItemSelected(item);
    }//

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    moveTaskToBack(true);
	}

	return super.onKeyDown(keyCode, event);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements
	    OnItemClickListener, LoaderCallbacks<Cursor> {

	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	    scAdapter = new NoteAdapter(container.getContext(), null);
	    View rootView = inflater.inflate(R.layout.fragment_notes_list, container,
		    false);

	    noteView = (ListView) rootView.findViewById(R.id.listView1);

	    noteView.setAdapter(scAdapter);
	    fillData();

	    noteView.setOnItemClickListener(this);

	    mCallbacks = this;

	    // создаем лоадер для чтения данных
	    getLoaderManager().initLoader(0, null, mCallbacks);
	    Log.d(LOG_SECTION, "Before service");

	    intent = new Intent(getActivity().getBaseContext(),
		    ServerDBSimulation.class).putExtra(
		    "update_notes_on_remote", R.id.update_notes);

	    getActivity().getBaseContext().startService(intent);

	    Log.d(LOG_SECTION, "After service");
	    return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {

	    Toast.makeText(view.getContext(),
		    "tap on " + position + " id " + id, Toast.LENGTH_SHORT)
		    .show();

	    intent = new Intent(view.getContext(), EditNoteActivity.class);
	    intent.putExtra("id", id);
	    startActivity(intent);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    String[] projection = { DBNote.NOTE_ID, DBNote.NOTE_TITLE,
		    DBNote.NOTE_CONTENT };

	    CursorLoader cursorLoader = new CursorLoader(this.getActivity()
		    .getBaseContext(), MyContentProvider.URI_NOTE_TABLE,
		    projection, null, null, null);
	    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    scAdapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	    scAdapter.swapCursor(null);
	}

	// TODO -------------- added part ---------------
	private void fillData() {

	    getLoaderManager().initLoader(0, null, this);

	}

    }

    protected void onDestroy() {
	super.onDestroy();
    }

}
