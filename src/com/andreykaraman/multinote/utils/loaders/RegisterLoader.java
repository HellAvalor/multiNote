package com.andreykaraman.multinote.utils.loaders;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.RegisterClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerHelper;
import android.content.Context;
import android.util.Log;

public class RegisterLoader extends AbsLoader<RegisterClass, ServerResponse> {


    public RegisterLoader(Context context, RegisterClass request) {
	super(context, request);
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("RegisterLoader", String.format("RegisterLoader.loadInBackground"));
	ServerHelper sHelper = ServerHelper.getInstance();
	mResponse = new ServerResponse();
	
	int sessionId = -1;
	try {
	    sessionId = sHelper.registrationNewUser(getmRequest().getLogin(), getmRequest().getPass(), getmRequest().getRepPassword());

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    //    e.printStackTrace();
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