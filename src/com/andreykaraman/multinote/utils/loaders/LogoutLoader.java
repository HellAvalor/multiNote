package com.andreykaraman.multinote.utils.loaders;

import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.model.AbsLoader;
import com.andreykaraman.multinote.model.LogoutClass;
import com.andreykaraman.multinote.model.ServerResponse;
import com.andreykaraman.multinote.utils.ServerHelper;
import android.content.Context;

public class LogoutLoader extends AbsLoader<LogoutClass, ServerResponse> {

    public LogoutLoader(Context context, LogoutClass request) {
	super(context, request);
    }

    @Override
    public ServerResponse creatEmtpyRespose() {
	return new ServerResponse();
    }

    @Override
    public void onLoading(ServerHelper sHelper, ServerResponse respClass) {

	try {
	    sHelper.logout(getmRequest().getSessionId());
	    respClass.setStatus(UserExceptions.Error.OK);

	} catch (UserExceptions e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    respClass.setStatus(e.getError());
	}
    }
}