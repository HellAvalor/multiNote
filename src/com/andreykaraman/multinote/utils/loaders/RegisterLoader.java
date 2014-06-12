package com.andreykaraman.multinote.utils.loaders;

import android.content.Context;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.RegisterClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerHelper;

public class RegisterLoader extends AbsLoader<RegisterClass, ServerResponse> {

    public RegisterLoader(Context context, RegisterClass request) {
	super(context, request);
    }

    @Override
    public ServerResponse creatEmtpyRespose() {
	return new ServerResponse();
    }

    @Override
    public void onLoading(ServerHelper sHelper, ServerResponse respClass) {

	try {
	    int sessionId = sHelper.registrationNewUser(getmRequest()
		    .getLogin(), getmRequest().getPass(), getmRequest()
		    .getRepPassword());
	    respClass.setStatus(UserExceptions.Error.OK);
	    respClass.setSessionId(sessionId);
	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    respClass.setStatus(e.getError());
	    respClass.setSessionId(-1);
	}
    }
}