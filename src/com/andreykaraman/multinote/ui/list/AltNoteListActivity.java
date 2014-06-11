package com.andreykaraman.multinote.ui.list;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.DBNote;
import com.andreykaraman.multinote.model.LogoutClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.list.menu.EditPassActivity;
import com.andreykaraman.multinote.ui.login.MainActivity;
import com.andreykaraman.multinote.ui.login.MainActivity.LoadingHandler;
import com.andreykaraman.multinote.ui.login.menu.AboutDialogFragment;
import com.andreykaraman.multinote.utils.MyContentProvider;
import com.andreykaraman.multinote.utils.ServerDBHelper;
import com.andreykaraman.multinote.utils.adapters.AltNoteAdapter;
import com.andreykaraman.multinote.utils.loaders.LogoutLoader;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.ProgressDialog;
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

public class AltNoteListActivity extends Activity {

    static Intent intent;
    static AltNoteAdapter scAdapter;
    static ListView noteView;
    static int sessionId;
    static final String LOG_SECTION = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_alt_note_list);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

	Intent intent = getIntent();

	sessionId = intent.getIntExtra(APIStringConstants.CONST_SESSOIN_ID, -1);
	;

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
	initLoginLoader();

	int id = item.getItemId();
	if (id == R.id.change_pass) {
	    Toast.makeText(this, "going to change pass", Toast.LENGTH_SHORT)
		    .show();
	    Intent intent = new Intent(this, EditPassActivity.class).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID, sessionId);
	    startActivity(intent);

	    return true;
	}

	if (id == R.id.logout) {
	    // TODO add asynktask
	    Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
	    executeLoader(sessionId);

	    return true;
	}

	if (id == R.id.about) {
	    showAboutDialog();

	    return true;
	}

	if (id == R.id.actionAddNote) {

	    intent = new Intent(this, EditNoteActivity.class).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID, sessionId);
	    startActivity(intent);

	    return true;
	}
	return super.onOptionsItemSelected(item);
    }//

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

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

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
		    // TODO Auto-generated method stub
		    return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		    // TODO Auto-generated method stub
		    scAdapter.clearSelection();
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		    // TODO Auto-generated method stub

		    nr = 0;

		    MenuInflater inflater = getActivity().getMenuInflater();
		    inflater.inflate(R.menu.menu_alt_note_list, menu);
		    return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode,
			MenuItem item) {
		    // TODO Auto-generated method stub
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
		    // TODO Auto-generated method stub
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
		    // TODO Auto-generated method stub

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

	    intent = new Intent(getActivity().getBaseContext(),
		    ServerDBHelper.class).putExtra(
		    "update_notes_on_remote", R.id.update_notes).putExtra(
		    APIStringConstants.CONST_SESSOIN_ID, sessionId);

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

	    intent = new Intent(view.getContext(), EditNoteActivity.class)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);
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

	// TODO -------------- added part ---------------
	private void fillData() {
	    getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // Log.d("test", "PlaceholderFragment.onViewCreated");
	    super.onViewCreated(view, savedInstanceState);

	}
    }

    protected void onDestroy() {
	super.onDestroy();
    }

    private void initLoginLoader() {
	final Loader<?> loader = getLoaderManager().getLoader(
		R.id.loader_logout);
	if (loader != null && loader.isStarted()) {
	    mLoginLoadingHandler.onStartLoading();
	    getLoaderManager().initLoader(R.id.loader_logout, null,
		    mLoginLoaderCallback);
	} else {
	    mLoginLoadingHandler.onStopLoading();
	}
    }

    private void executeLoader(int sessionId) {
	mLoginLoadingHandler.onStartLoading();
	final Bundle args = new Bundle();

	args.putInt(APIStringConstants.CONST_SESSOIN_ID, sessionId);

	getLoaderManager().restartLoader(R.id.loader_logout, args,
		mLoginLoaderCallback);
    }

    private final LoadingHandler<ServerResponse> mLoginLoadingHandler = new LoadingHandler<ServerResponse>() {

	ProgressDialog ringProgressDialog;

	@Override
	public void onStartLoading() {
	    ringProgressDialog = ProgressDialog.show(AltNoteListActivity.this,
		    "", getResources().getString(R.string.loading), true);
	    ringProgressDialog.setCancelable(false);
	}

	@Override
	public void onStopLoading() {
	    if (ringProgressDialog != null) {
		ringProgressDialog.dismiss();
	    }
	}

	@Override
	public void onLoadingResult(ServerResponse result) {
	    Toast.makeText(AltNoteListActivity.this,
		    result.getStatus().toString(), Toast.LENGTH_SHORT).show();

	    if (result.getStatus() == Error.OK) {
		finish();
	    }
	}
    };

    // loader callback

    private final LoaderManager.LoaderCallbacks<ServerResponse> mLoginLoaderCallback = new LoaderManager.LoaderCallbacks<ServerResponse>() {
	@Override
	public Loader<ServerResponse> onCreateLoader(int id, Bundle args) {
	    // Log.d("test", String.format(
	    // "LoaderCallbacks.onCreateLoader %d, %s", id, args));
	    switch (id) {
	    case R.id.loader_logout: {
		int sessionId = args
			.getInt(APIStringConstants.CONST_SESSOIN_ID);

		return new LogoutLoader(AltNoteListActivity.this,
			new LogoutClass(sessionId));
	    }
	    }
	    throw new RuntimeException("logic mistake");
	}

	@Override
	public void onLoadFinished(Loader<ServerResponse> loader,
		ServerResponse data) {
	    Log.d("loader_logout", String.format(
		    "LoaderCallbacks.onLoadFinished %d, %s", loader.getId(),
		    data));
	    switch (loader.getId()) {
	    case R.id.loader_logout: {
		mLoginLoadingHandler.onStopLoading();
		mLoginLoadingHandler.onLoadingResult(data);
		getLoaderManager().destroyLoader(loader.getId());
		return;
	    }
	    }
	    throw new RuntimeException("logic mistake");
	}

	@Override
	public void onLoaderReset(Loader<ServerResponse> loader) {
	    Log.d("test", String.format("LoaderCallbacks.onLoaderReset"));
	    switch (loader.getId()) {
	    case R.id.loader_logout: {
		mLoginLoadingHandler.onStopLoading();
		return;
	    }
	    }
	    throw new RuntimeException("logic mistake");
	}
    };

}
