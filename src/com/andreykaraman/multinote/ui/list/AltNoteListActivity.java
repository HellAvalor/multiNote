package com.andreykaraman.multinote.ui.list;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.db.DBNote;
import com.andreykaraman.multinote.model.req.LogoutReq;
import com.andreykaraman.multinote.model.resp.LogoutResp;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.list.menu.EditPassActivity;
import com.andreykaraman.multinote.ui.login.MainActivity;
import com.andreykaraman.multinote.ui.login.menu.AboutDialogFragment;
import com.andreykaraman.multinote.utils.MyContentProvider;
import com.andreykaraman.multinote.utils.ServerDBHelper;
import com.andreykaraman.multinote.utils.ServerHelper;
import com.andreykaraman.multinote.utils.adapters.AltNoteAdapter;

import de.greenrobot.event.EventBus;

public class AltNoteListActivity extends Activity {

    private final static String LOG_SECTION = MainActivity.class.getName();
    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_alt_note_list);
	bus = EventBus.getDefault();
	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}
    }

    @Override
    public void onResume() {
	super.onResume();
	bus.registerSticky(this);
    }

    @Override
    public void onPause() {
	bus.unregister(this);
	super.onPause();
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
	    startActivity(new Intent(this, EditPassActivity.class).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID,
		    getIntent().getIntExtra(
			    APIStringConstants.CONST_SESSOIN_ID, -1)));
	    return true;
	}

	if (id == R.id.logout) {
	    bus.postSticky(new LogoutReq(getIntent().getIntExtra(
		    APIStringConstants.CONST_SESSOIN_ID, -1)));
	    return true;
	}

	if (id == R.id.about) {
	    showAboutDialog();
	    return true;
	}

	if (id == R.id.actionAddNote) {
	    startActivity(new Intent(this, EditNoteActivity.class).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID,
		    getIntent().getIntExtra(
			    APIStringConstants.CONST_SESSOIN_ID, -1)));
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    void showAboutDialog() {

	FragmentTransaction ft = getFragmentManager().beginTransaction();
	Fragment prev = getFragmentManager().findFragmentByTag("aboutDialog");
	if (prev != null) {
	    ft.remove(prev);
	}
	ft.addToBackStack(null);
	// Create and show the dialog.
	DialogFragment newFragment = AboutDialogFragment.newInstance();
	newFragment.show(ft, "aboutDialog");
    }

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
	private AltNoteAdapter scAdapter;
	private ListView noteView;
	private int sessionId;

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    sessionId = getActivity().getIntent().getIntExtra(
		    APIStringConstants.CONST_SESSOIN_ID, -1);
	    scAdapter = new AltNoteAdapter(container.getContext(), null,
		    sessionId);
	    View rootView = inflater.inflate(R.layout.fragment_alt_notes_list,
		    container, false);

	    noteView = (ListView) rootView.findViewById(R.id.listView1);
	    noteView.setAdapter(scAdapter);
	    noteView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
	    noteView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
		private int nr = 0;

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		    return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		    scAdapter.clearSelection();
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		    nr = 0;
		    MenuInflater inflater = getActivity().getMenuInflater();
		    inflater.inflate(R.menu.menu_alt_note_list, menu);
		    return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode,
			MenuItem item) {
		    switch (item.getItemId()) {

		    case R.id.item_delete:
			nr = 0;
			scAdapter.deleteItems(noteView.getContext(),
				noteView.getCheckedItemIds(), sessionId);
			scAdapter.clearSelection();
			mode.finish();
		    }
		    return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode,
			int position, long id, boolean checked) {
		    if (checked) {
			nr++;
			scAdapter.setNewSelection(position, checked);
		    } else {
			nr--;
			scAdapter.removeSelection(position);
		    }
		    mode.setTitle(nr + " selected");
		}
	    });

	    noteView.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {

		    noteView.setItemChecked(position,
			    !scAdapter.isPositionChecked(position));
		    return false;
		}
	    });

	    fillData();
	    noteView.setOnItemClickListener(this);
	    mCallbacks = this;

	    // создаем лоадер для чтения данных
	    getLoaderManager().initLoader(0, null, mCallbacks);
	    Log.d(LOG_SECTION, "Before service");

	    Intent intent = new Intent(getActivity().getBaseContext(),
		    ServerDBHelper.class).putExtra("update_notes_on_remote",
		    R.id.update_notes).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID, sessionId);

	    getActivity().getBaseContext().startService(intent);

	    Log.d(LOG_SECTION, "After service");
	    return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {

	    Intent intent = new Intent(view.getContext(),
		    EditNoteActivity.class).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID, sessionId);
	    intent.putExtra(APIStringConstants.CONST_NOTE_ID, id);
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

	private void fillData() {
	    getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    super.onViewCreated(view, savedInstanceState);
	}
    }

    public void onEventMainThread(LogoutResp event) {
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {
	    finish();
	} else {
	    Toast.makeText(this, event.getStatus().resource(getApplication()),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(LogoutReq logout) {
	bus.removeStickyEvent(logout);
	LogoutResp event = new LogoutResp();
	try {
	    ServerHelper sHelper = ServerHelper.getInstance();
	    sHelper.logout(logout.getSessionId());
	    event.setStatus(Error.OK);
	    EventBus.getDefault().postSticky(event);
	} catch (UserExceptions e) {
	    event.setStatus(e.getError());
	    EventBus.getDefault().postSticky(event);
	}
    }
}
