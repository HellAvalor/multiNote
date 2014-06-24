package com.andreykaraman.multinote.ui.list.menu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.model.ServerDBHelper;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.list.menu.Events.*;

import de.greenrobot.event.EventBus;

public class EditNoteFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();

    private EditText titleText;
    private EditText contentText;
    private Note note = new Note();
    private int sessionId;
    private long noteId;
    private EventBus bus;
    private ProgressDialog ringProgressDialog;

    public EditNoteFragment() {
	super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setHasOptionsMenu(true);
	bus = EventBus.getDefault();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.menu_edit_note, menu);
	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.

	switch (item.getItemId()) {
	case android.R.id.home:

	    if (isContentChanged()) {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		showCancelChangesDialog(getActivity());
	    } else {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().finish();
		return true;
	    }
	    break;
	case R.id.action_save_note:
	    saveNoteCheck();
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.fragment_new_note, container,
		false);

	return rootView;
    }

    @Override
    public void onPause() {
	bus.unregister(this);
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
	bus.registerSticky(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);

	noteId = getActivity().getIntent().getLongExtra(
		APIStringConstants.CONST_NOTE_ID, -1);
	sessionId = getActivity().getIntent().getIntExtra(
		APIStringConstants.CONST_SESSOIN_ID, -1);

	if (noteId != -1) {
	    
	    ringProgressDialog = ProgressDialog.show(getActivity(), "",
		    getResources().getString(R.string.loading), true);
	    ringProgressDialog.setCancelable(false);
	    bus.postSticky(new GetNoteRequest(sessionId, noteId));

	} else {
	    getActivity().setTitle(getString(R.string.new_note));
	}

	titleText = (EditText) view.findViewById(R.id.editTextNewTitle);
	contentText = (EditText) view.findViewById(R.id.editTextNewNote);

    }

    public boolean isContentChanged() {

	// TODO add for new note
	if (!TextUtils.equals(contentText.getText(), note.getNoteContent())) {
	    return true;
	}
	return false;
    }

    public void showCancelChangesDialog(Context context) {

	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
	myAlertDialog.setMessage(getText(R.string.save_note));
	myAlertDialog.setPositiveButton(getText(R.string.ok),
		new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface arg0, int arg1) {

			saveNoteCheck();
		    }
		});
	myAlertDialog.setNegativeButton(getText(R.string.cancel),
		new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface arg0, int arg1) {

		    }
		});
	myAlertDialog.show();
    }

    public void saveNoteCheck() {

	if (noteId != -1) {

	    Intent intent = new Intent(getActivity(), ServerDBHelper.class)
		    .putExtra("update_notes_on_remote", R.id.edit_note)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId)
		    .putExtra(APIStringConstants.CONST_NOTE_ID, noteId)
		    .putExtra(APIStringConstants.CONST_NOTE_CONTENT,
			    contentText.getText().toString());

	    getActivity().startService(intent);

	    Toast.makeText(getActivity(), R.string.note_updated,
		    Toast.LENGTH_SHORT).show();

	} else {

	    Log.d(TAG, "sessionId = " + sessionId);

	    Intent intent = new Intent(getActivity(), ServerDBHelper.class)
		    .putExtra("update_notes_on_remote", R.id.add_note)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId)
		    .putExtra("title", titleText.getText().toString())
		    .putExtra("content", contentText.getText().toString());

	    getActivity().startService(intent);

	    Toast.makeText(getActivity(), R.string.note_added,
		    Toast.LENGTH_SHORT).show();
	}

	getActivity().finish();
    }

    public void onEventMainThread(GetNoteResponse event) {
	Log.d("onEventMainThread", "onEventMainThread start");

	titleText.setVisibility(View.GONE);
	ringProgressDialog.dismiss();
	bus.removeStickyEvent(event);

	if (event.getStatus() == Error.OK) {
	    titleText.setText(event.getNote().getNoteTitle());
	    contentText.setText(event.getNote().getNoteContent());
	    getActivity().setTitle(event.getNote().getNoteTitle());

	} else {
	    Toast.makeText(getActivity(),
		    event.getStatus().resource(getActivity()),
		    Toast.LENGTH_SHORT).show();
	    getActivity().finish();
	}
    }

    public void onEventBackgroundThread(GetNoteRequest req) {
	bus.removeStickyEvent(req);
	GetNoteResponse event = new GetNoteResponse(new Note());
	try {
	    ServerHelper sHelper = ServerHelper.getInstance();
	    event.setNote(sHelper.getNote(req.getSessionId(), req.getNoteId()));
	    event.setStatus(Error.OK);
	    EventBus.getDefault().postSticky(event);
	} catch (UserExceptions e) {
	    event.setStatus(e.getError());
	    EventBus.getDefault().postSticky(event);
	}
    }
}