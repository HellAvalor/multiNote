package com.andreykaraman.multinote.events;

public abstract class AbsEvent {

    public abstract String getEventType();

    @Override
    public String toString() {
	return String.format("Event type: %s", getEventType());
    }
}
