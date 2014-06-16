package com.andreykaraman.multinote.model.resp;


public class ResponceComplexEvent extends ResponceSimpleEvent {

    int sessionId;

    public int getSessionId() {
	return sessionId;
    }

    public void setSessionId(int sessionId) {
	this.sessionId = sessionId;
    }
}