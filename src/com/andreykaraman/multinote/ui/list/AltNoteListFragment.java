/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andreykaraman.multinote.ui.list;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.model.MyContentProvider;
import com.andreykaraman.multinote.model.ServerDBHelper;
import com.andreykaraman.multinote.model.db.DBNote;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.login.MainActivity;

public class AltNoteListFragment extends ListFragment implements
	OnItemClickListener, LoaderCallbacks<Cursor> {

    OnHeadlineSelectedListener mHeadCallback;
    private final static String LOG_SECTION = MainActivity.class.getName();
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private NoteAdapter scAdapter;
    private ListView noteView;
    private int sessionId;
    protected Object mActionMode;

    // The container Activity must implement this interface so the frag can
    // deliver messages
    public interface OnHeadlineSelectedListener {
	/** Called by HeadlinesFragment when a list item is selected */
	public void onArticleSelected(int position, long id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// We need to use a different list item layout for devices older than
	// Honeycomb
//	int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
//		: android.R.layout.simple_list_item_1;
	//int layout = R.id.fragment_alt_notes_list;
	//noteView = (ListView) view.findViewById(R.id.listView1);
	// Create an array adapter for the list view, using the Ipsum headlines
	// array
//	setListAdapter(new ArrayAdapter<String>(getActivity(), layout,
//		Ipsum.Headlines));
	
    }

    @Override
    public void onStart() {
	super.onStart();

	// When in two-pane layout, set the listview to highlight the selected
	// list item
	// (We do this during onStart because at the point the listview is
	// available.)
	if (getFragmentManager().findFragmentById(R.id.fragment_new_note) != null) {
	    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	sessionId = getActivity().getIntent().getIntExtra(
		APIStringConstants.CONST_SESSOIN_ID, -1);
	

	noteView = (ListView) view.findViewById(R.id.fragment_alt_notes_list);
	scAdapter = new NoteAdapter(getActivity(), null);
	noteView.setAdapter(scAdapter);

	noteView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	// Capture ListView item click
	noteView.setMultiChoiceModeListener(new MultiSelectListener());

	fillData();
	noteView.setOnItemClickListener(this);
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

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);

	// This makes sure that the container activity has implemented
	// the callback interface. If not, it throws an exception.
	try {
	    mHeadCallback = (OnHeadlineSelectedListener) activity;
	} catch (ClassCastException e) {
	    throw new ClassCastException(activity.toString()
		    + " must implement OnHeadlineSelectedListener");
	}
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	// Notify the parent activity of selected item
	mHeadCallback.onArticleSelected(position, id);

	// Set the item as checked to be highlighted when in two-pane layout
	getListView().setItemChecked(position, true);
    }

    private class MultiSelectListener implements MultiChoiceModeListener {
	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
		long id, boolean checked) {
	    // Capture total checked items
	    final int checkedCount = noteView.getCheckedItemCount();
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
	    noteView.clearChoices();
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
			noteView.getCheckedItemIds())
		.putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);
	getActivity().startService(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {

	Intent intent = new Intent(view.getContext(), EditNoteActivity.class)
		.putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);
	intent.putExtra(APIStringConstants.CONST_NOTE_ID, id);
	startActivity(intent);
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
}