package com.andreykaraman.multinote.ui.login;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.model.User;
import com.andreykaraman.multinote.ui.list.AltNoteListActivity;
import com.andreykaraman.multinote.ui.list.NoteListActivity;
import com.andreykaraman.multinote.ui.login.MainActivity.LoadingHandler;
import com.andreykaraman.multinote.utils.loaders.LogLoader;

public class LoginFragment extends Fragment {

    static SharedPreferences sharedPrefs;
    static EditText loginText;
    static EditText passwordText;
    static SharedPreferences savedData;
    static Button button;
    static String login;

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

	savedData = inflater.getContext().getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(container
		.getContext());

	loginText = (EditText) rootView.findViewById(R.id.editTextLogin);
	passwordText = (EditText) rootView.findViewById(R.id.editTextPassword);
	if (sharedPrefs.getBoolean("stay_login", false)) {
	    loginText.setText(savedData.getString(ARG_LOGIN, ""));
	}
	button = (Button) rootView.findViewById(R.id.buttonLogin);

	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		executeLoginLoader(new User(loginText.getText().toString(),
			passwordText.getText().toString()));

	    }
	});

	return rootView;
    }

    private final static String ARG_LOGIN = "login";
    private final static String ARG_PASSWORD = "password";
    private final static String ARG_USER = "user";

    private void initLoginLoader() {
	final Loader<?> loader = getLoaderManager()
		.getLoader(R.id.loader_login);
	if (loader != null && loader.isStarted()) {
	    mLoginLoadingHandler.onStartLoading();
	    getLoaderManager().initLoader(R.id.loader_login, null,
		    mLoginLoaderCallback);
	} else {
	    mLoginLoadingHandler.onStopLoading();
	}
    }

    private void executeLoginLoader(User user) {
	mLoginLoadingHandler.onStartLoading();
	final Bundle args = new Bundle();
	login = user.getLogin();
	args.putSerializable(ARG_USER, user);

	getLoaderManager().restartLoader(R.id.loader_login, args,
		mLoginLoaderCallback);
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
	    int sessionId = -1;
	    if (result.getStatus() == Error.OK) {

		passwordText.setText("");
		sessionId = result.getSessionId();
		if (sharedPrefs.getBoolean("stay_login", false)) {
		    savedData.edit().putString(ARG_LOGIN, login).commit();
		}
		Toast.makeText(getActivity(),
			savedData.getString(ARG_LOGIN, ""), Toast.LENGTH_SHORT)
			.show();

		if (sharedPrefs.getBoolean("alt_UI", false)) {
		    startActivity(new Intent(getActivity(),
			    AltNoteListActivity.class).putExtra(
			    APIStringConstants.CONST_SESSOIN_ID, sessionId));
		} else {
		    startActivity(new Intent(getActivity(),
			    NoteListActivity.class).putExtra(
			    APIStringConstants.CONST_SESSOIN_ID, sessionId));
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

		User user = (User) args.getSerializable(ARG_USER);
		// return new LoginLoader(getActivity(), login, pass);
		return new LogLoader(getActivity(), user);
	    }
	    }
	    throw new RuntimeException("logic mistake");
	}

	@Override
	public void onLoadFinished(Loader<ServerResponse> loader,
		ServerResponse data) {
	    Log.d("test", String.format(
		    "LoaderCallbacks.onLoadFinished %d, %s", loader.getId(),
		    data));
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