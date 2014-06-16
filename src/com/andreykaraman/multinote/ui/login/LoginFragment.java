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
import com.andreykaraman.multinote.model.req.LoginReq;
import com.andreykaraman.multinote.model.resp.LoginResp;
import com.andreykaraman.multinote.ui.list.AltNoteListActivity;
import com.andreykaraman.multinote.utils.ServerHelper;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    }

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
	savedData = inflater.getContext().getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(container
		.getContext());
	loginText = (EditText) rootView.findViewById(R.id.editTextLogin);
	passwordText = (EditText) rootView.findViewById(R.id.editTextPassword);
	if (sharedPrefs.getBoolean("stay_login", false)) {
	    loginText.setText(savedData.getString(APIStringConstants.ARG_LOGIN,
		    ""));
	}
	button = (Button) rootView.findViewById(R.id.buttonLogin);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		button.setEnabled(false);
		bus.postSticky(new LoginReq(loginText.getText().toString(),
			passwordText.getText().toString()));
	    }
	});
	return rootView;
    }

    public void onEventMainThread(LoginResp event) {
	Log.d("onEventMainThread", "onEventMainThread start");
	button.setEnabled(true);
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {
	    passwordText.setText("");
	    if (sharedPrefs.getBoolean("stay_login", false)) {
		savedData.edit().putString(APIStringConstants.ARG_LOGIN, login)
			.commit();
	    }
	    startActivity(new Intent(getActivity(), AltNoteListActivity.class)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID,
			    event.getSessionId()));
	} else {
	    Toast.makeText(getActivity(), event.getStatus().resource(getActivity()),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(LoginReq req) {
	bus.removeStickyEvent(req);
	LoginResp event = new LoginResp();
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