package com.andreykaraman.multinote.utils;


import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.model.User;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class ChangePassLoader extends AsyncTaskLoader<ServerResponse> {

    public ServerResponse mResponse;

    private User mUser;
    private String mPassword;
    private String mNewPassword;

    public ChangePassLoader(Context context, User user, String pass, String newPass) {
	super(context);
	mUser = user;
	mPassword = pass;
	mNewPassword = newPass;
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("ChangePassLoader", String.format("ChangePassLoader.loadInBackground"));
	ServerSimulation ss = ServerSimulation.getInstance();
	mResponse = new ServerResponse();

	try {
	    ss.setPassword(mUser, mPassword, mNewPassword);

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
	Log.d("ChangePassLoader", String.format("ChangePassLoader.onStartLoading"));
	super.onStartLoading();
	if (mResponse == null) {
	    forceLoad();
	} else {
	    deliverResult(mResponse);
	}
    }

    @Override
    protected void onStopLoading() {
	Log.d("ChangePassLoader", String.format("ChangePassLoader.onStopLoading"));
	super.onStopLoading();
    }

    @Override
    protected void onReset() {
	Log.d("ChangePassLoader", String.format("ChangePassLoader.onReset"));
	super.onReset();
	mResponse = null;
    }

}