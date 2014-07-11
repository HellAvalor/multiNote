package com.andreykaraman.multinote.events;

public class ResponceComplexEvent extends ResponceSimpleEvent {

    int sessionId;

    public int getSessionId() {
	return sessionId;
    }

    public void setSessionId(int sessionId) {
	this.sessionId = sessionId;
    }
}