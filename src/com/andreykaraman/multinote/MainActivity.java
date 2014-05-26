package com.andreykaraman.multinote;

import java.util.Locale;

import com.andreykaraman.multinote.data.LoginLoader;
import com.andreykaraman.multinote.data.ResponseLogin;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {
    

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this
     * becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    static EditText loginText;
    static EditText passwordText;
    static Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	// Set up the action bar.
	final ActionBar actionBar = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	// Create the adapter that will return a fragment for each of the three
	// primary sections of the activity.
	mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

	// Set up the ViewPager with the sections adapter.
	mViewPager = (ViewPager) findViewById(R.id.pager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	// When swiping between different sections, select the corresponding
	// tab. We can also use ActionBar.Tab#select() to do this if we have
	// a reference to the Tab.
	mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		    @Override
		    public void onPageSelected(int position) {
			actionBar.setSelectedNavigationItem(position);
		    }
		});

	// For each of the sections in the app, add a tab to the action bar.
	for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	    // Create a tab with text corresponding to the page title defined by
	    // the adapter. Also specify this Activity object, which implements
	    // the TabListener interface, as the callback (listener) for when
	    // this tab is selected.
	    actionBar.addTab(actionBar.newTab()
		    .setText(mSectionsPagerAdapter.getPageTitle(i))
		    .setTabListener(this));
	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    Toast.makeText(this, "go to settings", Toast.LENGTH_SHORT).show();
	    Intent intent = new Intent(this, SettingsActivity.class);
	    
	    startActivity(intent);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
	// When the given tab is selected, switch to the corresponding page in
	// the ViewPager.
	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	    // getItem is called to instantiate the fragment for the given page.
	    // Return a PlaceholderFragment (defined as a static inner class
	    // below).
	    // return PlaceholderFragment.newInstance(position);

	    switch (position) {
	    case 0:
		return LoginFragment.newInstance();
	    case 1:
		return RegisterFragment.newInstance();
	    }
	    return null;
	}

	@Override
	public int getCount() {
	    // Show 2 total pages.
	    return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    Locale l = Locale.getDefault();
	    switch (position) {
	    case 0:
		return getString(R.string.login).toUpperCase(l);
	    case 1:
		return getString(R.string.registration_tab).toUpperCase(l);
	    }
	    return null;
	}
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    // public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    // private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    /*
     * public static PlaceholderFragment newInstance(int sectionNumber) {
     * PlaceholderFragment fragment = new PlaceholderFragment(); Bundle args =
     * new Bundle(); args.putInt(ARG_SECTION_NUMBER, sectionNumber);
     * fragment.setArguments(args); return fragment; }
     * 
     * public PlaceholderFragment() { }
     * 
     * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
     * container, Bundle savedInstanceState) { View rootView =
     * inflater.inflate(R.layout.login_frame, container, false); /* TextView
     * textView = (TextView) rootView .findViewById(R.id.section_label);
     * textView.setText(Integer.toString(getArguments().getInt(
     * ARG_SECTION_NUMBER)));
     */
    /*
     * return rootView; } }
     */

    public static class LoginFragment extends Fragment {

	final LoaderManager.LoaderCallbacks<ResponseLogin> mLoaderCallback = new LoaderManager.LoaderCallbacks<ResponseLogin>() {

	    @Override
	    public Loader<ResponseLogin> onCreateLoader(int id, Bundle args) {
		LoginLoader loader = new LoginLoader(getActivity());
		if (args != null) {
		    String login = args.getString("LOGIN");
		    String pass = args.getString("PASSWORD");
		    loader.setLoginAndPasswrod(login, pass);
		}
		return loader;
	    }

	    @Override
	    public void onLoadFinished(Loader<ResponseLogin> loader,
		    ResponseLogin data) {
		if (data.isSuccess()) {
		    Toast.makeText(getActivity(),
			    ((LoginLoader) loader).getmLogin(),
			    Toast.LENGTH_SHORT).show();
		    // startActivity(new Intent(getActivity(),
		    // ActivityRoom.class));
		} else {
		    Toast.makeText(getActivity(), data.getError().toString(),
			    Toast.LENGTH_SHORT).show();
		}
	    }

	    @Override
	    public void onLoaderReset(Loader<ResponseLogin> loader) {
	    }
	};

	public static LoginFragment newInstance() {
	    LoginFragment fragment = new LoginFragment();
	    return fragment;
	}

	public LoginFragment() {
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	    View rootView = inflater.inflate(R.layout.login_frame, container,
		    false);

	    loader = getLoaderManager().getLoader(R.id.loader_login);
	    if (loader == null || (loader != null && loader.isStarted())) {
		getLoaderManager().initLoader(R.id.loader_login, null,
			mLoaderCallback);
	    }
	    loginText = (EditText) rootView.findViewById(R.id.editTextLogin);
	    passwordText = (EditText) rootView
		    .findViewById(R.id.editTextPassword);

	    Button button = (Button) rootView.findViewById(R.id.buttonLogin);

	    button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    // Toast.makeText(v.getContext(),
		    // loginText.getText() + "/" + passwordText.getText(),
		    // Toast.LENGTH_SHORT).show();
		    
		    // Intent intent = new Intent(v.getContext(),
		    // NoteListActivity.class);
		    // startActivity(intent);
		    Bundle params = new Bundle();
		    params.putString("LOGIN", loginText.getText().toString());
		    params.putString("PASSWORD", passwordText.getText()
			    .toString());
		    Toast.makeText(getActivity(), "restart loader",
			    Toast.LENGTH_SHORT).show();
		    getLoaderManager().restartLoader(R.id.loader_login, params,
			    mLoaderCallback);
		}
	    });

	    return rootView;
	}
    }

    public static class RegisterFragment extends Fragment {

	public static RegisterFragment newInstance() {
	    RegisterFragment fragment = new RegisterFragment();
	    return fragment;
	}

	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.register_frame,
		    container, false);

	    final EditText loginText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterLogin);
	    final EditText passwordText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterPass);
	    final EditText newPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterRepPassword);

	    Button button = (Button) rootView.findViewById(R.id.buttonRegister);

	    button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    Toast.makeText(
			    v.getContext(),
			    loginText.getText() + "/" + passwordText.getText()
				    + "/" + newPasswordText.getText(),
			    Toast.LENGTH_SHORT).show();
		    Intent intent = new Intent(v.getContext(),
			    NoteListActivity.class);
		    startActivity(intent);
		}
	    });

	    return rootView;
	}
    }

}
