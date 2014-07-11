package com.andreykaraman.multinote.events;

import com.andreykaraman.multinote.data.UserExceptions.Error;

public class ResponceSimpleEvent extends AbsEvent {

    Error status;

    public Error getStatus() {
	return status;
    }

    public void setStatus(Error errorStatus) {
	this.status = errorStatus;
    }

    @Override
    public String getEventType() {
	return ResponceSimpleEvent.class.getSimpleName();
    }
}