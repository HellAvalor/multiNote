package com.andreykaraman.multinote.model;

public class Note {

    private long noteId;
    private String noteTitle;
    private String noteContent;

    public Note(String noteTitle, String noteContent) {

	this.noteTitle = noteTitle;
	this.noteContent = noteContent;
    }

    public Note(long noteId, String noteTitle, String noteContent) {
	this.noteId = noteId;
	this.noteTitle = noteTitle;
	this.noteContent = noteContent;
    }

    public Note() {
    }

    public void setNoteId(long noteId) {
	this.noteId = noteId;
    }

    public String getNoteTitle() {
	return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
	this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
	return noteContent;
    }

    public void setNoteContent(String noteContent) {
	this.noteContent = noteContent;
    }

    public long getNoteId() {
	return noteId;
    }
}
