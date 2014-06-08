package com.andreykaraman.multinote.utils.loaders;

import android.content.Context;
import android.util.Log;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.ChangePassClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerHelper;

public class ChPassLoader extends AbsLoader<ChangePassClass, ServerResponse> {

    public ChPassLoader(Context context, ChangePassClass request) {
	super(context, request);
    }

    @Override
    public ServerResponse loadInBackground() {
	Log.d("LoginLoader", String.format("LoginLoader.loadInBackground"));
	ServerHelper ss = ServerHelper.getInstance();
	mResponse = new ServerResponse();

	try {
	    ss.changePass(getmRequest().getSessionId(), getmRequest()
		    .getOldPassword(), getmRequest().getNewPassword());

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
