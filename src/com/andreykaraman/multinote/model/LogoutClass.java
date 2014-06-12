package com.andreykaraman.multinote.model;

import java.io.Serializable;

public class LogoutClass implements Serializable {

    private static final long serialVersionUID = 1L;
    private int sessionId;

    public LogoutClass(int sessionId) {
	this.setSessionId(sessionId);
    }

    public int getSessionId() {
	return sessionId;
    }

    public void setSessionId(int sessionId) {
	this.sessionId = sessionId;
    }

}
