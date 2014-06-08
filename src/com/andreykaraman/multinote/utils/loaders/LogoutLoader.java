package com.andreykaraman.multinote.utils.loaders;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.LogoutClass;
import com.andreykaraman.multinote.model.RegisterClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerHelper;
import android.content.Context;
import android.util.Log;

public class LogoutLoader extends AbsLoader<LogoutClass, ServerResponse> {

    public LogoutLoader(Context context, LogoutClass request) {
	super(context, request);
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("RegisterLoader",
		String.format("RegisterLoader.loadInBackground"));
	ServerHelper ss = ServerHelper.getInstance();
	mResponse = new ServerResponse();

	try {
	    ss.logout(getmRequest().getSessionId());

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    mResponse.setStatus(e.getError());
	  //  mResponse.setSessionId(-1);
	    return mResponse;
	}

	if (isReset()) {
	    return null;
	} else {

	    mResponse.setStatus(UserExceptions.Error.OK);
	    //mResponse.setSessionId(sessionId);
	    return mResponse;
	}
    }

}