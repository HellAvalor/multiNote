package com.andreykaraman.multinote.ui.list.menu;

import com.andreykaraman.multinote.events.ResponceSimpleEvent;
import com.andreykaraman.multinote.model.Note;

public class Events {

    public static class ChangePassRequest {
	private int sessionId;
	private String oldPassword;
	private String newPassword;

	public ChangePassRequest(int sessionId, String oldPassword,
		String newPassword) {
	    this.setSessionId(sessionId);
	    this.setOldPassword(oldPassword);
	    this.setNewPassword(newPassword);
	}

	public int getSessionId() {
	    return sessionId;
	}

	public void setSessionId(int sessionId) {
	    this.sessionId = sessionId;
	}

	public String getOldPassword() {
	    return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
	    this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
	    return newPassword;
	}

	public void setNewPassword(String newPassword) {
	    this.newPassword = newPassword;
	}
    }

    public static class ChangePassResponse extends ResponceSimpleEvent {
    }

    public static class GetNoteRequest {

	private int sessionId;
	private long noteId;

	public GetNoteRequest(int sessionId, long noteId) {
	    this.setSessionId(sessionId);
	    this.setNoteId(noteId);
	}

	public int getSessionId() {
	    return sessionId;
	}

	public void setSessionId(int sessionId) {
	    this.sessionId = sessionId;
	}

	public long getNoteId() {
	    return noteId;
	}

	public void setNoteId(long noteId) {
	    this.noteId = noteId;
	}

    }

    public static class GetNoteResponse extends ResponceSimpleEvent {
	private Note note;

	GetNoteResponse(Note note) {
	    this.setNote(note);
	}

	public Note getNote() {
	    return note;
	}

	public void setNote(Note note) {
	    this.note = note;
	}
    }

}
