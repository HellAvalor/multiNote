package com.andreykaraman.multinote.model.resp;


public abstract class AbsEvent {

	public abstract String getEventType();

	@Override
	public String toString() {
		return String.format("Event type: %s", getEventType());
	}
}
