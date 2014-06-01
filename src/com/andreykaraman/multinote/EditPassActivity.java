package com.andreykaraman.multinote;

import com.andreykaraman.multinote.MainActivity.LoadingHandler;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ChangePassLoader;
import com.andreykaraman.multinote.utils.ServerSimulation;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPassActivity extends Activity {

    static EditText oldPasswordText;
    static EditText newPasswordText;
    static EditText repPasswordText;
    static Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_pass);

	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.edit_pass, menu);
    // return true;
    // }

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

	private final static String ARG_LOGIN = "login";
	private final static String ARG_PASSWORD = "password";
	private final static String ARG_NEW_PASSWORD = "new_password";

	private void initLoginLoader() {
	    final Loader<?> loader = getLoaderManager().getLoader(
		    R.id.loader_change_pass);
	    if (loader != null && loader.isStarted()) {
		mLoginLoadingHandler.onStartLoading();
		getLoaderManager().initLoader(R.id.loader_change_pass, null,
			mLoginLoaderCallback);
	    } else {
		mLoginLoadingHandler.onStopLoading();
	    }
	}

	private void executeLoginLoader(String login, String password,
		String newPassword) {
	    mLoginLoadingHandler.onStartLoading();
	    final Bundle args = new Bundle();
	    args.putString(ARG_LOGIN, login);
	    args.putString(ARG_PASSWORD, password);
	    args.putString(ARG_NEW_PASSWORD, newPassword);
	    getLoaderManager().restartLoader(R.id.loader_change_pass, args,
		    mLoginLoaderCallback);
	}

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	    View rootView = inflater.inflate(R.layout.fragment_change_pass, container,
		    false);

	    oldPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextOldPassword);
	    newPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextNewPassword);
	    repPasswordText = (EditText) rootView
		    .findViewById(R.id.editTextNewRepPassword);

	    button = (Button) rootView.findViewById(R.id.buttonChangePassword);

	    button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

		    Toast.makeText(
			    v.getContext(),
			    oldPasswordText.getText() + "/"
				    + newPasswordText.getText() + "/"
				    + repPasswordText.getText(),
			    Toast.LENGTH_SHORT).show();

		    ServerSimulation ss = ServerSimulation.getInstance();
		    if (newPasswordText.getText().toString().equals(
			    repPasswordText.getText().toString())) {

			// TODO
			executeLoginLoader(ss.getUserInSystem().getLogin()
				.toString(), oldPasswordText.getText()
				.toString(), newPasswordText.getText()
				.toString());

		    } else {
			Toast.makeText(v.getContext(),
				R.string.passwords_not_match,
				Toast.LENGTH_SHORT).show();
		    }
		    // TODO Auto-generated method stub
		    // check passChange

		    // Intent intent = new Intent(v.getContext(),
		    // NoteListActivity.class);
		    // startActivity(intent);

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
		button.setEnabled(false);
	    }

	    @Override
	    public void onStopLoading() {
		button.setEnabled(true);
	    }

	    @Override
	    public void onLoadingResult(ServerResponse result) {

		if (result.getStatus() == Error.OK) {
		    Toast.makeText(getActivity(), R.string.passwords_changed,
			    Toast.LENGTH_SHORT).show();
		    getActivity().finish();
		} else {
		    Toast.makeText(getActivity(),
			    result.getStatus().toString(), Toast.LENGTH_SHORT)
			    .show();
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
		case R.id.loader_change_pass: {
		    ServerSimulation ss = ServerSimulation.getInstance();

		    String pass = args.getString(ARG_PASSWORD);
		    String newPass = args.getString(ARG_NEW_PASSWORD);
		    return new ChangePassLoader(getActivity(),
			    ss.getUserInSystem(), pass, newPass);
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
		case R.id.loader_change_pass: {
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
		case R.id.loader_change_pass: {
		    mLoginLoadingHandler.onStopLoading();
		    return;
		}
		}
		throw new RuntimeException("logic mistake");
	    }
	};
    }

}
