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

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.list.Events.LogoutRequest;
import com.andreykaraman.multinote.ui.list.Events.LogoutResponse;
import com.andreykaraman.multinote.ui.list.menu.AltEditNoteFragment;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.list.menu.EditPassActivity;
import com.andreykaraman.multinote.ui.login.menu.AboutDialogFragment;

import de.greenrobot.event.EventBus;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AltNoteActivity extends FragmentActivity implements
	AltNoteListFragment.OnHeadlineSelectedListener {
    private EventBus bus;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.fragment_alt_notes_list);
	bus = EventBus.getDefault();
	// Check whether the activity is using the layout version with
	// the fragment_container FrameLayout. If so, we must add the first
	// fragment
	if (findViewById(R.id.fragment_container) != null) {

	    // However, if we're being restored from a previous state,
	    // then we don't need to do anything and should return or else
	    // we could end up with overlapping fragments.
	    if (savedInstanceState != null) {
		return;
	    }

	    // Create an instance of ExampleFragment
	    AltNoteListFragment firstFragment = new AltNoteListFragment();

	    // In case this activity was started with special instructions from
	    // an Intent,
	    // pass the Intent's extras to the fragment as arguments
	    firstFragment.setArguments(getIntent().getExtras());

	    // Add the fragment to the 'fragment_container' FrameLayout
	    getFragmentManager().beginTransaction()
		    .add(R.id.fragment_container, firstFragment).commit();
	}
    }

    public void onArticleSelected(int position, long id) {
	// The user selected the headline of an article from the
	// HeadlinesFragment

	// Capture the article fragment from the activity layout
	AltEditNoteFragment articleFrag = (AltEditNoteFragment) getFragmentManager()
		.findFragmentById(R.id.fragment_new_note);

	if (articleFrag != null) {
	    // If article frag is available, we're in two-pane layout...

	    // Call a method in the ArticleFragment to update its content
	    articleFrag.updateArticleView(position);

	} else {
	    // If the frag is not available, we're in the one-pane layout and
	    // must swap frags...

	    // Create fragment and give it an argument for the selected article
	    AltEditNoteFragment newFragment = new AltEditNoteFragment();
	    Bundle args = new Bundle();
	    args.putLong(AltEditNoteFragment.ARG_ID, id);

	    newFragment.setArguments(args);
	    FragmentTransaction transaction = getFragmentManager()
		    .beginTransaction();

	    // Replace whatever is in the fragment_container view with this
	    // fragment,
	    // and add the transaction to the back stack so the user can
	    // navigate back
	    transaction.replace(R.id.fragment_container, newFragment);
	    transaction.addToBackStack(null);

	    // Commit the transaction
	    transaction.commit();
	}
    }

    @Override
    public void onBackPressed() {
	moveTaskToBack(true);
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
	    bus.postSticky(new LogoutRequest(getIntent().getIntExtra(
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

    public void onEventMainThread(LogoutResponse event) {
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {
	    finish();
	} else {
	    Toast.makeText(this, event.getStatus().resource(this),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(LogoutRequest logout) {
	bus.removeStickyEvent(logout);
	LogoutResponse event = new LogoutResponse();
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