package com.andreykaraman.multinote.data;

import android.app.Application;

import com.andreykaraman.multinote.utils.ServerSimulation;

public class StartInit extends Application {

    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	ServerSimulation.getInstance().Init();
	ServerSimulation.getInstance().InitUser();
    }

}
