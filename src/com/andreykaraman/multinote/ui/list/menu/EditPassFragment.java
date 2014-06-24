package com.andreykaraman.multinote.ui.list.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.andreykaraman.multinote.ui.list.menu.Events.*;

import de.greenrobot.event.EventBus;

public class EditPassFragment extends Fragment {

    private EditText oldPasswordText;
    private EditText newPasswordText;
    private EditText repPasswordText;
    private Button button;
    private int sessionId;
    private EventBus bus;

    public EditPassFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	bus = EventBus.getDefault();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	sessionId = getActivity().getIntent().getIntExtra(
		APIStringConstants.CONST_SESSOIN_ID, -1);
	View rootView = inflater.inflate(R.layout.fragment_change_pass,
		container, false);

	return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

	super.onViewCreated(view, savedInstanceState);

	oldPasswordText = (EditText) view
		.findViewById(R.id.editTextOldPassword);
	newPasswordText = (EditText) view
		.findViewById(R.id.editTextNewPassword);
	repPasswordText = (EditText) view
		.findViewById(R.id.editTextNewRepPassword);
	button = (Button) view.findViewById(R.id.buttonChangePassword);
	button.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if (TextUtils.equals(newPasswordText.getText(),
			repPasswordText.getText())) {
		    button.setEnabled(false);
		    bus.postSticky(new ChangePassRequest(sessionId, oldPasswordText
			    .getText().toString(), newPasswordText.getText()
			    .toString()));
		} else {
		    Toast.makeText(v.getContext(),
			    R.string.passwords_not_match, Toast.LENGTH_SHORT)
			    .show();
		}
	    }
	});
    }

    public void onEventMainThread(ChangePassResponse event) {
	button.setEnabled(true);
	bus.removeStickyEvent(event);
	if (event.getStatus() == Error.OK) {
	    Toast.makeText(getActivity(), R.string.passwords_changed,
		    Toast.LENGTH_SHORT).show();
	    getActivity().finish();
	} else {
	    Toast.makeText(getActivity(),
		    event.getStatus().resource(getActivity()),
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void onEventBackgroundThread(ChangePassRequest changePass) {
	bus.removeStickyEvent(changePass);
	ChangePassResponse event = new ChangePassResponse();
	try {
	    ServerHelper sHelper = ServerHelper.getInstance();
	    sHelper.changePass(changePass.getSessionId(),
		    changePass.getOldPassword(), changePass.getNewPassword());
	    event.setStatus(Error.OK);
	    EventBus.getDefault().postSticky(event);
	} catch (UserExceptions e) {
	    event.setStatus(e.getError());
	    EventBus.getDefault().postSticky(event);
	}
    }
}