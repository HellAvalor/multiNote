package com.andreykaraman.multinote.model.req;

import java.io.Serializable;

public class LogoutReq implements Serializable {

    private static final long serialVersionUID = 1L;
    private int sessionId;

    public LogoutReq(int sessionId) {
	this.setSessionId(sessionId);
    }

    public int getSessionId() {
	return sessionId;
    }

    public void setSessionId(int sessionId) {
	this.sessionId = sessionId;
    }

}
