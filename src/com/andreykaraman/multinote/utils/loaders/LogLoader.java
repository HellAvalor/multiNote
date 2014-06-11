package com.andreykaraman.multinote.utils.loaders;

import android.content.Context;
import android.util.Log;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.model.User;
import com.andreykaraman.multinote.utils.ServerHelper;


public class LogLoader extends AbsLoader<User, ServerResponse> {

    public LogLoader(Context context, User request) {
	super(context, request);
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("LoginLoader", String.format("LoginLoader.loadInBackground"));
	ServerHelper sHelper = ServerHelper.getInstance();
	mResponse = new ServerResponse();
	int sessionId = -1;

	try {
	    sessionId = sHelper.checkLogin(getmRequest().getLogin(), getmRequest().getPass());

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    mResponse.setStatus(e.getError());
	    mResponse.setSessionId(-1);
	    return mResponse;
	}

	if (isReset()) {
	    return null;
	} else {

	    mResponse.setStatus(UserExceptions.Error.OK);
	    mResponse.setSessionId(sessionId);
	    return mResponse;
	}
    }
}
