package com.andreykaraman.multinote.model;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.andreykaraman.multinote.utils.ServerHelper;

public abstract class AbsLoader<RequestClass, ResponseClass> extends
	AsyncTaskLoader<ResponseClass> {

    private RequestClass mRequest;
    private ResponseClass mResponse;

    public abstract ResponseClass creatEmtpyRespose();

    public abstract void onLoading(ServerHelper sHelper, ResponseClass respClass);

    public AbsLoader(Context context, RequestClass request) {
	super(context);
	mRequest = request;
    }

    public RequestClass getmRequest() {
	return mRequest;
    }

    @Override
    protected void onStartLoading() {

	super.onStartLoading();
	if (mResponse == null) {
	    mResponse = creatEmtpyRespose();
	    forceLoad();
	} else {
	    deliverResult(mResponse);
	}
    }

    @Override
    protected void onStopLoading() {
	super.onStopLoading();
    }

    @Override
    protected void onReset() {
	super.onReset();
	mResponse = null;
    }

    @Override
    public ResponseClass loadInBackground() {

	onLoading(ServerHelper.getInstance(), mResponse);

	if (isReset()) {
	    return null;
	} else {
	    return mResponse;
	}
    }
}