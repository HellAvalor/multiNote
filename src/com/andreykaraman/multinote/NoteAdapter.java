package com.andreykaraman.multinote;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NoteAdapter extends BaseAdapter {
    final static String LOG_TAG = "myLogs";

    ArrayList<Note> notes = new ArrayList<Note>();


    public NoteAdapter(Context context, ArrayList<Note> notes) {
	if (notes != null) {
	    this.notes = notes;
	}

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

	LayoutInflater inflater = LayoutInflater.from(arg2.getContext());

	if (view == null) {
	    view = inflater.inflate(R.layout.note_item, arg2, false);

	}
	TextView noteTitle = (TextView) view.findViewById(R.id.textNoteTitle);
	TextView noteContent = (TextView) view
		.findViewById(R.id.textNoteContent);

	noteTitle.setText(notes.get(position).getNoteTitle());

	// view.setTag(tag) holder
	
	noteContent.setText(notes.get(position).getNoteContent());
	ImageButton deleteButton = (ImageButton) view
		.findViewById(R.id.imageButtonDelete);
	deleteButton.setOnClickListener(new NoteOnClickListener(notes.get(position)));
	
	
	return view;
    }
    
    public class NoteOnClickListener implements OnClickListener
    {

        Note note;
        public NoteOnClickListener(Note note) {
             this.note = note;
        }

        public void onClick(View v) {
         
            
            Toast.makeText(v.getContext(), note.getNoteTitle(), Toast.LENGTH_LONG).show();
            Note.dellNote(v.getContext(), note);
            
        }

     }
}
