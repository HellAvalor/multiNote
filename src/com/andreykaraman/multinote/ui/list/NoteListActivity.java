package com.andreykaraman.multinote.ui.list;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.list.Events.*;
import com.andreykaraman.multinote.ui.list.NoteListFragment;
import com.andreykaraman.multinote.ui.list.menu.EditNoteActivity;
import com.andreykaraman.multinote.ui.list.menu.EditPassActivity;
import com.andreykaraman.multinote.ui.login.menu.AboutDialogFragment;

import de.greenrobot.event.EventBus;

public class NoteListActivity extends Activity {

    private EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_alt_note_list);
	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new NoteListFragment()).commit();
	}
	bus = EventBus.getDefault();
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
	    Toast.makeText(this,
		    event.getStatus().resource(this),
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
    public void onBackPressed() {
	moveTaskToBack(true);
    }
}
