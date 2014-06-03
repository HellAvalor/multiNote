package com.andreykaraman.multinote.utils.adapters;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.model.DBNote;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.utils.ServerDBSimulation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NoteAdapter extends CursorAdapter {

    final static String LOG_TAG = "myLogs";
    Note note;
    Context context;

    protected static class RowViewHolder {
	public TextView noteTitle;
	public TextView noteContent;
	public ImageButton deleteButton;
    }

    @SuppressWarnings("deprecation")
    public NoteAdapter(Context context, Cursor c) {
	super(context, c);
	this.context = context;
	// TODO Auto-generated constructor stub
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	// when the view will be created for first time,
	// we need to tell the adapters, how each item will look
	LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	View retView = inflater.inflate(R.layout.list_item_note, parent, false);

	return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
	// here we are setting our data
	// that means, take the data from the cursor and put it in views
	// note = new Note();
	RowViewHolder holder = new RowViewHolder();

	holder.noteTitle = (TextView) view.findViewById(R.id.textNoteTitle);
	holder.noteContent = (TextView) view.findViewById(R.id.textNoteContent);

	// view.setTag(tag) holder

	holder.noteTitle.setText(cursor.getString(cursor.getColumnIndex(cursor
		.getColumnName(1))));

	holder.noteContent.setText(cursor.getString(cursor
		.getColumnIndex(cursor.getColumnName(2))));
	holder.deleteButton = (ImageButton) view
		.findViewById(R.id.imageButtonDelete);
	holder.deleteButton.setOnClickListener(new NoteOnClickListener(cursor
		.getInt(cursor.getColumnIndex(DBNote.NOTE_ID))));
	view.setTag(holder);

    }

    public class NoteOnClickListener implements OnClickListener {

	int id;

	public NoteOnClickListener(int id) {
	    this.id = id;
	    // this.cur = cur;
	}

	public void onClick(View v) {
	    v.getRootView().getRootView().setEnabled(false);

	    Toast.makeText(v.getContext(), "id " + id, Toast.LENGTH_SHORT)
		    .show();

	    Intent intent = new Intent(v.getContext(), ServerDBSimulation.class)
		    .putExtra("update_notes_on_remote", R.id.delete_note)
		    .putExtra("delId", id);

	    v.getContext().startService(intent);

	}
    }

}
