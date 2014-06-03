package com.andreykaraman.multinote.model;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


public abstract class AbsLoader<RequestClass, ResponseClass> extends
	AsyncTaskLoader<ResponseClass> {

    private RequestClass mRequest;
    public ResponseClass mResponse;

    public AbsLoader(Context context, RequestClass request) {
	super(context);
	mRequest = request;
    }
    
    public RequestClass getmRequest() {
	return mRequest;
    }

    @Override
    protected void onStartLoading() {
	Log.d("test", String.format("LoginLoader.onStartLoading"));
	super.onStartLoading();
	if (mResponse == null) {
	    forceLoad();
	} else {
	    deliverResult(mResponse);
	}
    }

    @Override
    protected void onStopLoading() {
	Log.d("test", String.format("LoginLoader.onStopLoading"));
	super.onStopLoading();
    }

    @Override
    protected void onReset() {
	Log.d("test", String.format("LoginLoader.onReset"));
	super.onReset();
	mResponse = null;
    }
}