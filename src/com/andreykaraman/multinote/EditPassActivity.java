package com.andreykaraman.multinote;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class EditPassActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_note_list);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.note_list, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.change_pass) {
	    // TODO Auto-generated method stub

	    return true;
	}

	return super.onOptionsItemSelected(item);
    }

 

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.change_pass,
		    container, false);
	    
	    final EditText oldPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextOldPassword);
	    final EditText newPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextNewPassword);
	    final EditText repPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextNewRepPassword);
	    
	    Button button = (Button) rootView.findViewById(R.id.buttonChangePassword);

	    button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		   
		    Toast.makeText(v.getContext(),
			    oldPasswordText.getText() + "/" + newPasswordText.getText()+ "/" + repPasswordText.getText(),
			    Toast.LENGTH_SHORT).show();
		    
		 // TODO Auto-generated method stub
		 // check passChange   
		    
		  //  Intent intent = new Intent(v.getContext(), NoteListActivity.class);
		  //  startActivity(intent);

		}
	    });
	    return rootView;
	}
    }

}
