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
import com.andreykaraman.multinote.model.req.RegisterReq;
import com.andreykaraman.multinote.model.resp.RegisterResp;
import com.andreykaraman.multinote.ui.list.AltNoteListActivity;
import com.andreykaraman.multinote.utils.ServerHelper;

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

	savedData = inflater.getContext().getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(container
		.getContext());

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
		registerButton.setEnabled(false);
		bus.postSticky(new RegisterReq(loginText.getText().toString(),
			passwordText.getText().toString(), newPasswordText
				.getText().toString()));
	    }
	});
	return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    }

    public void onEventMainThread(RegisterResp event) {

	registerButton.setEnabled(true);
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {

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

    public void onEventBackgroundThread(RegisterReq registerClass) {
	bus.removeStickyEvent(registerClass);
	RegisterResp event = new RegisterResp();
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