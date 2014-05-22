package com.andreykaraman.multinote;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NoteListActivity extends Activity {

    static Intent intent;
    static DbHelperNew db;
    static NoteAdapter scAdapter;
    static ListView noteView;
    private static final int ACTIVITY_CREATE = 0;
    
    
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
	getMenuInflater().inflate(R.menu.note_list, menu);
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
	    // Intent intent = new Intent(this, NoteList.class);
	    // startActivity(intent);
	    return true;
	}

	if (id == R.id.actionAddNote) {

	    // Toast.makeText(this, "Adding new note",
	    // Toast.LENGTH_SHORT).show();

	    intent = new Intent(this, EditNoteActivity.class);
	 //   startActivityForResult(intent, ACTIVITY_CREATE);
	    startActivity(intent);

	    return true;
	}
	return super.onOptionsItemSelected(item);
    }//

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

	    db = new DbHelperNew(container.getContext());
	    db.open();

	    scAdapter = new NoteAdapter(container.getContext(), null);
	    View rootView = inflater.inflate(R.layout.notes_list, container,
		    false);

	    noteView = (ListView) rootView.findViewById(R.id.listView1);

	    noteView.setAdapter(scAdapter);
	    noteView.setOnItemClickListener(this);
	    mCallbacks = this;
	    // создаем лоадер для чтения данных
	    getLoaderManager().initLoader(0, null, mCallbacks);

	    return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	    
	
	    // TODO Auto-generated method stub
	    Toast.makeText(view.getContext(),
		    "tap on " + position + " id " + id, Toast.LENGTH_SHORT)
		    .show();

	    intent = new Intent(view.getContext(), EditNoteActivity.class);
	    intent.putExtra("id", id);
	    startActivity(intent);
	    // scAdapter.notifyDataSetChanged();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
	    return new MyCursorLoader(getActivity(), db);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    scAdapter.swapCursor(cursor);
	    // scAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	    // scAdapter.notifyDataSetChanged();
	}

	// public void onResume() {
	// super.onResume();
	// getLoaderManager().initLoader(0, null, this);
	// }


	
    }

    protected void onDestroy() {
	super.onDestroy();
	// закрываем подключение при выходе
	db.close();
    }

    // protected void onResume() {
    // super.onResume();
    // getLoaderManager().initLoader(0, null, this);
    // }

}
