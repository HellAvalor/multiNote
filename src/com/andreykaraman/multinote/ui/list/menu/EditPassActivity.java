package com.andreykaraman.multinote.ui.list.menu;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ChangePassClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.ui.login.MainActivity.LoadingHandler;
import com.andreykaraman.multinote.utils.loaders.ChPassLoader;

public class EditPassActivity extends Activity {

    private final static String ARG_CHANGE_PASS = "change_pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_pass);
	if (savedInstanceState == null) {
	    getFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}
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

	private EditText oldPasswordText;
	private EditText newPasswordText;
	private EditText repPasswordText;
	private Button button;
	private int sessionId;

	public PlaceholderFragment() {

	}

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

	private void executeLoginLoader(int sessionId, String oldPassword,
		String newPassword) {
	    mLoginLoadingHandler.onStartLoading();
	    final Bundle args = new Bundle();

	    args.putSerializable(ARG_CHANGE_PASS, new ChangePassClass(
		    sessionId, oldPassword, newPassword));
	    getLoaderManager().restartLoader(R.id.loader_change_pass, args,
		    mLoginLoaderCallback);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    sessionId = getActivity().getIntent().getIntExtra(
		    APIStringConstants.CONST_SESSOIN_ID, -1);
	    View rootView = inflater.inflate(R.layout.fragment_change_pass,
		    container, false);
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

		    if (newPasswordText.getText().toString()
			    .equals(repPasswordText.getText().toString())) {

			executeLoginLoader(sessionId, oldPasswordText.getText()
				.toString(), newPasswordText.getText()
				.toString());

		    } else {
			Toast.makeText(v.getContext(),
				R.string.passwords_not_match,
				Toast.LENGTH_SHORT).show();
		    }
		}
	    });
	    return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
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

		    ChangePassClass chPass = (ChangePassClass) args
			    .getSerializable(ARG_CHANGE_PASS);
		    return new ChPassLoader(getActivity(), chPass);
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
