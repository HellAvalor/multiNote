package com.andreykaraman.multinote.utils.loaders;

import android.content.Context;
import android.util.Log;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.model.User;
import com.andreykaraman.multinote.utils.ServerSimulation;

public class ChPassLoader extends AbsLoader<User, ServerResponse> {

    public ChPassLoader(Context context, User request) {
	super(context, request);
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("LoginLoader", String.format("LoginLoader.loadInBackground"));
	ServerSimulation ss = ServerSimulation.getInstance();
	mResponse = new ServerResponse();

	try {
	    ss.checkLogin(getmRequest());

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
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

}
