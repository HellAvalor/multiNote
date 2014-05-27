package com.andreykaraman.multinote.data;

import com.andreykaraman.multinote.MainActivity.LoginResponse;
import com.andreykaraman.multinote.data.ServerSimulation;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class AddUserLoader extends AsyncTaskLoader<LoginResponse> {

    public LoginResponse mResponse;

    private String mLogin;
    private String mPassword;
    private String mRepeatPassword;

    public AddUserLoader(Context context, String login, String pass, String repeatPass) {
	super(context);
	mLogin = login;
	mPassword = pass;
	mRepeatPassword = repeatPass;
    }

    @Override
    public LoginResponse loadInBackground() {
	Log.d("LoginLoader", String.format("LoginLoader.loadInBackground"));
	ServerSimulation ss = ServerSimulation.getInstance();
	mResponse = new LoginResponse();

	try {
	    ss.registrationNewUser(mLogin, mPassword, mRepeatPassword);

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    //    e.printStackTrace();
	    mResponse.setStatus(e.getError());
	    return mResponse;
	}

	if (isReset()) {
	    return null;
	} else {

	    mResponse.setStatus(UserExceptions.Error.OK);
	    return mResponse;
	}
    }

    @Override
    protected void onStartLoading() {
	Log.d("test", String.format("LoginLoader.onStartLoading"));
	super.onStartLoading();
	if (mResponse == null) {
	    forceLoad();
	} else {
	    deliverResult(mResponse);
	}
    }

    @Override
    protected void onStopLoading() {
	Log.d("test", String.format("LoginLoader.onStopLoading"));
	super.onStopLoading();
    }

    @Override
    protected void onReset() {
	Log.d("test", String.format("LoginLoader.onReset"));
	super.onReset();
	mResponse = null;
    }

}