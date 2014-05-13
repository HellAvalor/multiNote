package com.andreykaraman.multinote;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
    final static String LOG_TAG = "myLogs";

    ArrayList<Note> notes = new ArrayList<Note>();
    Context context;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
	if (notes != null) {
	    this.notes = notes;
	}
	this.context = context;
    }

    public void setNotesList(ArrayList<Note> notes) {
	this.notes = notes;
    }

    public int getCount() {
	return notes.size();
    }

    public Object getItem(int num) {
	return notes.get(num);
    }

    public long getItemId(int itemID) {

	return notes.get(itemID).getNoteId();
    }

    public View getView(int position, View view, ViewGroup arg2) {

	LayoutInflater inflater = LayoutInflater.from(context);
	
	if (view == null) {
	    view = inflater.inflate(R.layout.note_item, arg2, false);
	 
	}
	TextView noteTitle = (TextView) view
		.findViewById(R.id.textNoteTitle);
	TextView noteContent = (TextView) view
		.findViewById(R.id.textNoteContent);

	noteTitle.setText(notes.get(position).getNoteTitle());

	noteContent
		.setText(notes.get(position).getNoteContent());

	return view;
    }

}