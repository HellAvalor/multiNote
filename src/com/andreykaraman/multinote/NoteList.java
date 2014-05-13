package com.andreykaraman.multinote;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Build;

public class NoteList extends Activity {

    static ArrayList<Note> notes = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_note_list);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

	DbHelper mDbHelper = new DbHelper(this);
	SQLiteDatabase db = mDbHelper.getWritableDatabase();
	mDbHelper.deleteDB(db, 1);
	mDbHelper.onCreate(db);
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
	    View rootView = inflater.inflate(R.layout.notes_list, container,
		    false);
	    notes = Note.getNotes(rootView.getContext());
	    ListView noteView = (ListView) rootView
		    .findViewById(R.id.listView1);
	    NoteAdapter noteAdapter = new NoteAdapter(rootView.getContext(),
		    notes);
	    // footerLayout = getLayoutInflater()
	    // .inflate(R.layout.estate_footer, null);

	    // ImageView imageView2 = (ImageView) footerLayout
	    // .findViewById(R.id.imageViewFooter);

	    // SVG svg2 = SVGParser.getSVGFromResource(getResources(),
	    // / R.raw.noun_project_1439);

	    // imageView2.setImageDrawable(svg2.createPictureDrawable());

	    // EstateView.addFooterView(footerLayout, null, false);

	    // LinearLayout linAddEstate = (LinearLayout) EstateView
	    // .findViewById(R.id.estate_footer_add);
	    // linAddEstate.setOnClickListener(this);
	    noteView.setAdapter(noteAdapter);
	    // registerForContextMenu(EstateView);

	    return rootView;
	}
    }

}
