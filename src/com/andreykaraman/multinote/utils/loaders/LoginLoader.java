package com.andreykaraman.multinote.utils.loaders;


import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerSimulation;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class LoginLoader extends AsyncTaskLoader<ServerResponse> {

    public ServerResponse mResponse;

    private String mLogin;
    private String mPassword;


    public LoginLoader(Context context, String login, String pass) {
	super(context);
	mLogin = login;
	mPassword = pass;
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("LoginLoader", String.format("LoginLoader.loadInBackground"));
	ServerSimulation ss = ServerSimulation.getInstance();
	mResponse = new ServerResponse();

	try {
	    ss.checkLogin(mLogin, mPassword);

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