package com.andreykaraman.multinote.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.andreykaraman.multinote.R;

public class NoteAdapter extends CursorAdapter {

    protected static class RowViewHolder {
	public TextView noteTitle;
	public TextView noteContent;
    }

    public NoteAdapter(Context context, Cursor cursor) {
	super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	// when the view will be created for first time,
	// we need to tell the adapters, how each item will look
	LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	View retView = inflater.inflate(R.layout.list_item_alt_note, parent,
		false);
	RowViewHolder holder = new RowViewHolder();

	holder.noteTitle = (TextView) retView.findViewById(R.id.textNoteTitle);
	holder.noteContent = (TextView) retView
		.findViewById(R.id.textNoteContent);

	retView.setTag(holder);
	return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
	// here we are setting our data
	// that means, take the data from the cursor and put it in views

	RowViewHolder holder = (RowViewHolder) view.getTag();
	holder.noteTitle.setText(cursor.getString(cursor.getColumnIndex(cursor
		.getColumnName(1))));
	holder.noteContent.setText(cursor.getString(cursor
		.getColumnIndex(cursor.getColumnName(2))));
    }
}
