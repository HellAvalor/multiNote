package com.andreykaraman.multinote.ui.login;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.remote.ServerHelper;
import com.andreykaraman.multinote.ui.list.ItemsListActivity;
import com.andreykaraman.multinote.ui.login.Events.LoginRequest;
import com.andreykaraman.multinote.ui.login.Events.LoginResponse;

import de.greenrobot.event.EventBus;

public class LoginFragment extends Fragment {

    private SharedPreferences sharedPrefs;
    private EditText loginText;
    private EditText passwordText;
    private SharedPreferences savedData;
    private Button button;
    private String login;
    private EventBus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	bus = EventBus.getDefault();

    }

    public static LoginFragment newInstance() {
	return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View rootView = inflater.inflate(R.layout.fragment_login, container,
		false);

	return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);

	savedData = getActivity().getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(getActivity());
	loginText = (EditText) view.findViewById(R.id.editTextLogin);
	passwordText = (EditText) view.findViewById(R.id.editTextPassword);
	if (sharedPrefs.getBoolean("stay_login", false)) {
	    loginText.setText(savedData.getString(APIStringConstants.ARG_LOGIN,
		    ""));
	}
	button = (Button) view.findViewById(R.id.buttonLogin);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		button.setEnabled(false);
		bus.postSticky(new LoginRequest(loginText.getText().toString(),
			passwordText.getText().toString()));
	    }
	});
    }

    public void onEventMainThread(LoginResponse event) {
	Log.d("onEventMainThread", "onEventMainThread start");
	button.setEnabled(true);
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {
	    passwordText.setText("");
	    if (sharedPrefs.getBoolean("stay_login", false)) {
		savedData.edit().putString(APIStringConstants.ARG_LOGIN, login)
			.commit();
	    }
	    // startActivity(new Intent(getActivity(), AltNoteActivity.class)
	    // .putExtra(APIStringConstants.CONST_SESSOIN_ID,
	    // event.getSessionId()));
	    startActivity(new Intent(getActivity(), ItemsListActivity.class)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID,
			    event.getSessionId()));
	} else {
	    Toast.makeText(getActivity(),
		    event.getStatus().resource(getActivity()),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(LoginRequest req) {
	bus.removeStickyEvent(req);
	LoginResponse event = new LoginResponse();
	try {
	    ServerHelper sHelper = ServerHelper.getInstance();
	    event.setSessionId(sHelper.checkLogin(req.getLogin(), req.getPass()));
	    event.setStatus(Error.OK);
	    EventBus.getDefault().postSticky(event);
	} catch (UserExceptions e) {
	    event.setSessionId(-1);
	    event.setStatus(e.getError());
	    EventBus.getDefault().postSticky(event);
	}
    }
}