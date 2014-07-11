package com.andreykaraman.multinote.ui.list;

import com.andreykaraman.multinote.events.ResponceSimpleEvent;

public class Events {
    
    public static class LogoutRequest {
	private int sessionId;

	public LogoutRequest(int sessionId) {
	    this.setSessionId(sessionId);
	}

	public int getSessionId() {
	    return sessionId;
	}

	public void setSessionId(int sessionId) {
	    this.sessionId = sessionId;
	}
    }

    public static class LogoutResponse extends ResponceSimpleEvent {
    }

}
