package com.andreykaraman.multinote.utils.loaders;

import android.content.Context;

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
    public ServerResponse creatEmtpyRespose() {
	return new ServerResponse();
    }

    @Override
    public void onLoading(ServerHelper sHelper, ServerResponse respClass) {

	try {
	    sHelper.changePass(getmRequest().getSessionId(), getmRequest()
		    .getOldPassword(), getmRequest().getNewPassword());
	    respClass.setStatus(UserExceptions.Error.OK);
	} catch (UserExceptions e) {
	    respClass.setStatus(e.getError());
	}
    }
}
