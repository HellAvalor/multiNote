package com.andreykaraman.multinote;

import java.util.Locale;

import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.AddUserLoader;
import com.andreykaraman.multinote.utils.LoginLoader;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {

    static final String LOG_SECTION = MainActivity.class.getName();
    static SharedPreferences savedData;
    static SharedPreferences sharedPrefs;
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
    static Button button;
    static String login;
    static Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Log.d(LOG_SECTION, "Activity.onCreate");
	// Set up the action bar.
	final ActionBar actionBar = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	savedData = getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

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
    protected void onDestroy() {
	Log.d(LOG_SECTION, "Activity.onDestroy");
	super.onDestroy();
    }

    @Override
    protected void onResume() {
	super.onResume();

	Log.d(LOG_SECTION, "Activity.onResume");
    }

    @Override
    protected void onPause() {
	super.onPause();
	Log.d(LOG_SECTION, "Activity.onPause");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static class LoginRequest {

	String login;
    }



    public interface LoadingHandler<T> {
	void onStartLoading();

	void onStopLoading();

	void onLoadingResult(T result);

    }

    public static class LoginFragment extends Fragment {

	private final static String ARG_LOGIN = "login";
	private final static String ARG_PASSWORD = "password";

	private void initLoginLoader() {
	    final Loader<?> loader = getLoaderManager().getLoader(
		    R.id.loader_login);
	    if (loader != null && loader.isStarted()) {
		mLoginLoadingHandler.onStartLoading();
		getLoaderManager().initLoader(R.id.loader_login, null,
			mLoginLoaderCallback);
	    } else {
		mLoginLoadingHandler.onStopLoading();
	    }
	}

	private void executeLoginLoader(String login, String password) {
	    mLoginLoadingHandler.onStartLoading();
	    final Bundle args = new Bundle();
	    args.putString(ARG_LOGIN, login);
	    args.putString(ARG_PASSWORD, password);
	    getLoaderManager().restartLoader(R.id.loader_login, args,
		    mLoginLoaderCallback);
	}

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

	    View rootView = inflater.inflate(R.layout.fragment_login, container,
		    false);

	    loginText = (EditText) rootView.findViewById(R.id.editTextLogin);
	    passwordText = (EditText) rootView
		    .findViewById(R.id.editTextPassword);
	    if (sharedPrefs.getBoolean("stay_login", false)) {
		loginText.setText(savedData.getString(ARG_LOGIN, ""));
	    }
	    button = (Button) rootView.findViewById(R.id.buttonLogin);

	    button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

		    executeLoginLoader(loginText.getText().toString(),
			    passwordText.getText().toString());
		}
	    });

	    return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // Log.d("test", "PlaceholderFragment.onViewCreated");
	    super.onViewCreated(view, savedInstanceState);

	    // init loaders
	    initLoginLoader();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    // Log.d("test", "PlaceholderFragment.onCreate");
	    super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
	    // Log.d("test", "PlaceholderFragment.onDestroy");
	    super.onDestroy();
	}

	@Override
	public void onResume() {
	    // Log.d("test", "PlaceholderFragment.onResume");
	    super.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    // Log.d("test", "PlaceholderFragment.onPause");
	}

	private final LoadingHandler<ServerResponse> mLoginLoadingHandler = new LoadingHandler<ServerResponse>() {
	    @Override
	    public void onStartLoading() {
		button.setEnabled(false);
	    }

	    @Override
	    public void onStopLoading() {
		button.setEnabled(true);
	    }

	    @Override
	    public void onLoadingResult(ServerResponse result) {
		Toast.makeText(getActivity(), result.getStatus().toString(),
			Toast.LENGTH_SHORT).show();

		if (result.getStatus() == Error.OK) {
		    passwordText.setText("");
		    
		    if (sharedPrefs.getBoolean("stay_login", false)) {
			savedData.edit().putString(ARG_LOGIN, login).commit();
		    }
		    Toast.makeText(getActivity(),
			    savedData.getString(ARG_LOGIN, ""),
			    Toast.LENGTH_SHORT).show();

		    if (sharedPrefs.getBoolean("alt_UI", false)) { 
			startActivity(new Intent(getActivity(),
				    AltNoteListActivity.class));
		    } else {
			startActivity(new Intent(getActivity(),
				    NoteListActivity.class));
		    }
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
		case R.id.loader_login: {
		    login = args.getString(ARG_LOGIN);
		    String pass = args.getString(ARG_PASSWORD);
		    return new LoginLoader(getActivity(), login, pass);
		}
		}
		throw new RuntimeException("logic mistake");
	    }

	    @Override
	    public void onLoadFinished(Loader<ServerResponse> loader,
		    ServerResponse data) {
		Log.d("test", String.format(
			"LoaderCallbacks.onLoadFinished %d, %s",
			loader.getId(), data));
		switch (loader.getId()) {
		case R.id.loader_login: {
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
		case R.id.loader_login: {
		    mLoginLoadingHandler.onStopLoading();
		    return;
		}
		}
		throw new RuntimeException("logic mistake");
	    }
	};

    }

    public static class RegisterFragment extends Fragment {

	private final static String ARG_LOGIN = "login";
	private final static String ARG_PASSWORD = "password";
	private final static String ARG_REPEAT_PASSWORD = "repeat_password";

	private void initLoginLoader() {
	    final Loader<?> loader = getLoaderManager().getLoader(
		    R.id.loader_new_user);
	    if (loader != null && loader.isStarted()) {
		mLoginLoadingHandler.onStartLoading();
		getLoaderManager().initLoader(R.id.loader_new_user, null,
			mLoginLoaderCallback);
	    } else {
		mLoginLoadingHandler.onStopLoading();
	    }
	}

	private void executeLoginLoader(String login, String password,
		String repeatPass) {
	    mLoginLoadingHandler.onStartLoading();
	    final Bundle args = new Bundle();
	    args.putString(ARG_LOGIN, login);
	    args.putString(ARG_PASSWORD, password);
	    args.putString(ARG_REPEAT_PASSWORD, repeatPass);
	    getLoaderManager().restartLoader(R.id.loader_new_user, args,
		    mLoginLoaderCallback);
	}

	public static RegisterFragment newInstance() {
	    RegisterFragment fragment = new RegisterFragment();
	    return fragment;
	}

	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_register,
		    container, false);

	    final EditText loginText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterLogin);
	    final EditText passwordText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterPass);
	    final EditText newPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextRegisterRepPassword);

	    registerButton = (Button) rootView
		    .findViewById(R.id.buttonRegister);

	    registerButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub

		    executeLoginLoader(loginText.getText().toString(),
			    passwordText.getText().toString(), newPasswordText
				    .getText().toString());
		}
	    });

	    return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // Log.d("test", "PlaceholderFragment.onViewCreated");
	    super.onViewCreated(view, savedInstanceState);

	    // init loaders
	    initLoginLoader();
	}

	private final LoadingHandler<ServerResponse> mLoginLoadingHandler = new LoadingHandler<ServerResponse>() {
	    @Override
	    public void onStartLoading() {
		registerButton.setEnabled(false);
	    }

	    @Override
	    public void onStopLoading() {
		registerButton.setEnabled(true);
	    }

	    @Override
	    public void onLoadingResult(ServerResponse result) {
		Toast.makeText(getActivity(), result.getStatus().toString(),
			Toast.LENGTH_SHORT).show();

		if (result.getStatus() == Error.OK) {
		    if (sharedPrefs.getBoolean("stay_login", false)) {
			savedData.edit().putString(ARG_LOGIN, login).commit();
		    }
		    Toast.makeText(getActivity(),
			    savedData.getString(ARG_LOGIN, ""),
			    Toast.LENGTH_SHORT).show();

		    startActivity(new Intent(getActivity(),
			    NoteListActivity.class));
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
		case R.id.loader_new_user: {
		    login = args.getString(ARG_LOGIN);
		    String pass = args.getString(ARG_PASSWORD);
		    String repPass = args.getString(ARG_REPEAT_PASSWORD);
		    return new AddUserLoader(getActivity(), login, pass,
			    repPass);
		}
		}
		throw new RuntimeException("logic mistake");
	    }

	    @Override
	    public void onLoadFinished(Loader<ServerResponse> loader,
		    ServerResponse data) {
		Log.d("test", String.format(
			"LoaderCallbacks.onLoadFinished %d, %s",
			loader.getId(), data));
		switch (loader.getId()) {
		case R.id.loader_new_user: {
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
		case R.id.loader_new_user: {
		    mLoginLoadingHandler.onStopLoading();
		    return;
		}
		}
		throw new RuntimeException("logic mistake");
	    }
	};
    }

}
