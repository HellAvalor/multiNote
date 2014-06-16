package com.andreykaraman.multinote.ui.list.menu;

import android.app.Activity;
import android.os.Bundle;

import com.andreykaraman.multinote.R;

public class EditPassActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_pass);
	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new EditPassFragment()).commit();
	}
    }
}
