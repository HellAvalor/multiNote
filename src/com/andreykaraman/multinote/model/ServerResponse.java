package com.andreykaraman.multinote.model;

import com.andreykaraman.multinote.data.UserExceptions.Error;

public class ServerResponse {
    
	Error status;
	int sessionId;

	public int getSessionId() {
	    return sessionId;
	}

	public void setSessionId(int sessionId) {
	    this.sessionId = sessionId;
	}

	public Error getStatus() {
	    return status;
	}

	public void setStatus(Error errorStatus) {
	    this.status = errorStatus;
	}
}