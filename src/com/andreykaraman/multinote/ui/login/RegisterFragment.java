package com.andreykaraman.multinote.ui.login;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.ui.list.NoteListActivity;
import com.andreykaraman.multinote.ui.login.MainActivity.LoadingHandler;

import com.andreykaraman.multinote.utils.loaders.AddUserLoader;

public class RegisterFragment extends Fragment {
    static SharedPreferences savedData;
    static SharedPreferences sharedPrefs;
    static String login;
    static Button registerButton;

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
	View rootView = inflater.inflate(R.layout.fragment_register, container,
		false);

	final EditText loginText = (EditText) rootView
		.findViewById(R.id.editTextRegisterLogin);
	final EditText passwordText = (EditText) rootView
		.findViewById(R.id.editTextRegisterPass);
	final EditText newPasswordText = (EditText) rootView
		.findViewById(R.id.editTextRegisterRepPassword);

	registerButton = (Button) rootView.findViewById(R.id.buttonRegister);

	registerButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		executeLoginLoader(loginText.getText().toString(), passwordText
			.getText().toString(), newPasswordText.getText()
			.toString());
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
			savedData.getString(ARG_LOGIN, ""), Toast.LENGTH_SHORT)
			.show();

		startActivity(new Intent(getActivity(), NoteListActivity.class));
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
		return new AddUserLoader(getActivity(), login, pass, repPass);
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