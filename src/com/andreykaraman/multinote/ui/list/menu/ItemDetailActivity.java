package com.andreykaraman.multinote.ui.list.menu;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;

public class ItemDetailActivity extends FragmentActivity {
    ItemDetailFragment fragmentItemDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_item_detail);
	// Fetch the item to display from bundle
	// Item item = (Item) getIntent().getSerializableExtra("item");

	int sessionId = getIntent().getIntExtra(
		APIStringConstants.CONST_SESSOIN_ID, -1);
	long noteId = getIntent().getLongExtra(
		APIStringConstants.CONST_NOTE_ID, -1);

	if (savedInstanceState == null) {
	    // Insert detail fragment based on the item passed
	    fragmentItemDetail = ItemDetailFragment.newInstance(sessionId, noteId, true); // <-------
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.flDetailContainer, fragmentItemDetail);
	    ft.commit();
	}
    }

}