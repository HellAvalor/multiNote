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
package com.andreykaraman.multinote.ui.list.menu;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.model.ServerDBHelper;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.list.menu.Events.GetNoteRequest;
import com.andreykaraman.multinote.ui.list.menu.Events.GetNoteResponse;

import de.greenrobot.event.EventBus;

import android.text.TextUtils;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AltEditNoteFragment extends Fragment {
 //   final static String ARG_POSITION = "position";
    public final static String ARG_ID = "id";
   // int mCurrentPosition = -1;
    long mCurrentId = -1;
    protected final String TAG = this.getClass().getSimpleName();

    private EditText titleText;
    private EditText contentText;
    private Note note = new Note();
    private int sessionId;
    private long noteId;
    private EventBus bus;
    private ProgressDialog ringProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	bus = EventBus.getDefault();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	// If activity recreated (such as from screen rotate), restore
	// the previous article selection set by onSaveInstanceState().
	// This is primarily necessary when in the two-pane layout.
	if (savedInstanceState != null) {
	    mCurrentId = savedInstanceState.getLong(ARG_ID);
	}

	// Inflate the layout for this fragment
	return inflater.inflate(R.layout.fragment_new_note, container, false);
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

    @Override
    public void onStart() {
	super.onStart();

	// During startup, check if there are arguments passed to the fragment.
	// onStart is a good place to do this because the layout has already
	// been
	// applied to the fragment at this point so we can safely call the
	// method
	// below that sets the article text.
	Bundle args = getArguments();
	if (args != null) {
	    // Set article based on argument passed in
	    updateArticleView(args.getLong(ARG_ID));
	} else if (mCurrentId != -1) {
	    // Set article based on saved instance state defined during
	    // onCreateView
	    updateArticleView(mCurrentId);
	}
    }

    public void updateArticleView(long id) {
	
	if (id != -1) {

	    ringProgressDialog = ProgressDialog.show(getActivity(), "",
		    getResources().getString(R.string.loading), true);
	    ringProgressDialog.setCancelable(false);
	    bus.postSticky(new GetNoteRequest(sessionId, id));

	} else {
	    getActivity().setTitle(getString(R.string.new_note));
	}
	
//	TextView article = (TextView) getActivity().findViewById(R.id.article);
//	article.setText(Ipsum.Articles[position]);
	mCurrentId = id;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

	// Save the current article selection in case we need to recreate the
	// fragment
	outState.putLong(ARG_ID, mCurrentId);
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