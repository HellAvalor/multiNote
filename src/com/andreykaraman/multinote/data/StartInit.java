package com.andreykaraman.multinote.data;

import android.app.Application;

import com.andreykaraman.multinote.data.Singleton;

public class StartInit extends Application {

    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	Singleton.getInstance().Init();
	Singleton.getInstance().InitUser();
    }

}
