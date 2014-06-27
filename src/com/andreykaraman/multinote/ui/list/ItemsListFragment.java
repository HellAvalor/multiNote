package com.andreykaraman.multinote.ui.list;

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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.model.MyContentProvider;
import com.andreykaraman.multinote.model.ServerDBHelper;
import com.andreykaraman.multinote.model.db.DBNote;
import com.andreykaraman.multinote.ui.login.MainActivity;

public class ItemsListFragment extends Fragment implements
	LoaderCallbacks<Cursor> {
    // private ArrayAdapter<Item> adapterItems;
    private ListView lvItems;
    private final static String LOG_SECTION = MainActivity.class.getName();
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private NoteAdapter scAdapter;
    private int sessionId;
    protected Object mActionMode;
    private OnListItemSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Create arraylist from item fixtures
	// ArrayList<Item> items = Item.getItems();
	// Create adapter based on items
	// adapterItems = new ArrayAdapter<Item>(getActivity(),
	// android.R.layout.simple_list_item_activated_1, items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// Inflate view
	View view = inflater.inflate(R.layout.fragment_item_list, container,
		false);
	// Return view
	return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onViewCreated(view, savedInstanceState);
	sessionId = getActivity().getIntent().getIntExtra(
		APIStringConstants.CONST_SESSOIN_ID, -1);
	scAdapter = new NoteAdapter(getActivity(), null);

	// Attach adapter to listview
	lvItems = (ListView) view.findViewById(R.id.lvItems);
	// lvItems.setAdapter(adapterItems);
	lvItems.setAdapter(scAdapter);
	lvItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	lvItems.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapterView, View item,
		    int position, long rowId) {
		// Item item1 = adapterItems.getItem(position);
		// Fire selected listener event with item
		listener.onItemSelected(rowId); // <--------------
	    }
	});

	lvItems.setMultiChoiceModeListener(new MultiSelectListener());

	fillData();

	mCallbacks = this;

	// создаем лоадер для чтения данных
	getLoaderManager().initLoader(0, null, mCallbacks);
	Log.d(LOG_SECTION, "Before service");

	Intent intent = new Intent(getActivity(), ServerDBHelper.class)
		.putExtra("update_notes_on_remote", R.id.update_notes)
		.putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);

	getActivity().startService(intent);
	Log.d(LOG_SECTION, "After service");
    }

    private class MultiSelectListener implements MultiChoiceModeListener {
	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
		long id, boolean checked) {
	    // Capture total checked items
	    final int checkedCount = lvItems.getCheckedItemCount();
	    // Set the CAB title according to total checked items
	    mode.setTitle(checkedCount + " Selected");
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.item_delete:
		delete();
		// Close CAB
		mode.finish();
		return true;
	    default:
		return false;
	    }
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	    mode.getMenuInflater().inflate(R.menu.menu_alt_note_list, menu);
	    return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
	    lvItems.clearChoices();
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    return false;
	}
    }

    private void delete() {

	Intent intent = new Intent(getActivity(), ServerDBHelper.class)
		.putExtra("update_notes_on_remote", R.id.delete_notes)
		.putExtra(APIStringConstants.CONST_NOTE_ID,
			lvItems.getCheckedItemIds())
		.putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);
	getActivity().startService(intent);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	String[] projection = { DBNote.NOTE_ID, DBNote.NOTE_TITLE,
		DBNote.NOTE_CONTENT };

	CursorLoader cursorLoader = new CursorLoader(getActivity(),
		MyContentProvider.URI_NOTE_TABLE, projection, null, null, null);
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

    private void fillData() {
	getLoaderManager().initLoader(0, null, this);
    }

    public interface OnListItemSelectedListener {
	public void onItemSelected(long id);
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	if (activity instanceof OnListItemSelectedListener) {
	    listener = (OnListItemSelectedListener) activity;
	} else {
	    throw new ClassCastException(
		    activity.toString()
			    + " must implement ItemsListFragment.OnListItemSelectedListener");
	}
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
	// When setting CHOICE_MODE_SINGLE, ListView will automatically
	// give items the 'activated' state when touched.
//	lvItems.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
//		: ListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    //
    // // TODO change for multi
    // Intent intent = new Intent(view.getContext(), EditNoteActivity.class)
    // .putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);
    // intent.putExtra(APIStringConstants.CONST_NOTE_ID, id);
    // startActivity(intent);
    // }
}