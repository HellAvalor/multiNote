package com.andreykaraman.multinote.model;

import com.andreykaraman.multinote.data.UserExceptions.Error;

public class ServerResponse {
    
	Error status;

	public Error getStatus() {
	    return status;
	}

	public void setStatus(Error errorStatus) {
	    this.status = errorStatus;
	}
}