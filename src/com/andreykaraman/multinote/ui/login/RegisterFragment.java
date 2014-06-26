package com.andreykaraman.multinote.ui.login;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.andreykaraman.multinote.ui.login.Events.RegisterRequest;
import com.andreykaraman.multinote.ui.login.Events.RegisterResponse;

import de.greenrobot.event.EventBus;

public class RegisterFragment extends Fragment {
    private SharedPreferences savedData;
    private SharedPreferences sharedPrefs;
    private String login;
    private Button registerButton;
    private EventBus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	bus = EventBus.getDefault();
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

	return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);

	savedData = getActivity().getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(getActivity());

	final EditText loginText = (EditText) view
		.findViewById(R.id.editTextRegisterLogin);
	final EditText passwordText = (EditText) view
		.findViewById(R.id.editTextRegisterPass);
	final EditText newPasswordText = (EditText) view
		.findViewById(R.id.editTextRegisterRepPassword);

	registerButton = (Button) view.findViewById(R.id.buttonRegister);
	registerButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		registerButton.setEnabled(false);
		bus.postSticky(new RegisterRequest(loginText.getText().toString(),
			passwordText.getText().toString(), newPasswordText
				.getText().toString()));
	    }
	});
    }

    public void onEventMainThread(RegisterResponse event) {

	registerButton.setEnabled(true);
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {

	    if (sharedPrefs.getBoolean("stay_login", false)) {
		savedData.edit().putString(APIStringConstants.ARG_LOGIN, login)
			.commit();
	    }

	    startActivity(new Intent(getActivity(), ItemsListActivity.class)
		    .putExtra(APIStringConstants.CONST_SESSOIN_ID,
			    event.getSessionId()));
	} else {
	    Toast.makeText(getActivity(),
		    event.getStatus().resource(getActivity()),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(RegisterRequest registerClass) {
	bus.removeStickyEvent(registerClass);
	RegisterResponse event = new RegisterResponse();
	try {
	    ServerHelper sHelper = ServerHelper.getInstance();

	    event.setSessionId(sHelper.registrationNewUser(
		    registerClass.getLogin(), registerClass.getPass(),
		    registerClass.getRepPassword()));
	    event.setStatus(Error.OK);
	    EventBus.getDefault().postSticky(event);

	} catch (UserExceptions e) {

	    event.setSessionId(-1);
	    event.setStatus(e.getError());
	    EventBus.getDefault().postSticky(event);
	}
    }
}