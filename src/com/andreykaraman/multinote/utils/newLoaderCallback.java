package com.andreykaraman.multinote.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

public class newLoaderCallback<ResponseClass> implements LoaderManager.LoaderCallbacks<ResponseClass>{

    @Override
    public Loader<ResponseClass> onCreateLoader(int id, Bundle args) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void onLoadFinished(Loader<ResponseClass> arg0, ResponseClass arg1) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onLoaderReset(Loader<ResponseClass> arg0) {
	// TODO Auto-generated method stub
	
    }
    
    public void initLoader(){
	
    }
    
    public void executeLoader(){
	
    }

}
