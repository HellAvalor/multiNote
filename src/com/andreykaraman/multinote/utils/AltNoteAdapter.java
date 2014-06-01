package com.andreykaraman.multinote.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.model.DBNote;
import com.andreykaraman.multinote.model.Note;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AltNoteAdapter extends CursorAdapter {

    final static String LOG_TAG = "myLogs";
    Note note;
    Context context;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    protected static class RowViewHolder {
	public TextView noteTitle;
	public TextView noteContent;
	public ImageButton deleteButton;
    }

    @SuppressWarnings("deprecation")
    public AltNoteAdapter(Context context, Cursor c) {
	super(context, c);
	this.context = context;
	// TODO Auto-generated constructor stub
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	// when the view will be created for first time,
	// we need to tell the adapters, how each item will look
	LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	View retView = inflater.inflate(R.layout.list_item_alt_note, parent,
		false);

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

	view.setTag(holder);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View v = super.getView(position, convertView, parent);// let the adapter
							      // handle setting
							      // up the row
							      // views
	v.setBackgroundColor(parent.getResources().getColor(
		android.R.color.background_light)); // default color

	if (mSelection.get(position) != null) {
	    v.setBackgroundColor(parent.getResources().getColor(
		    android.R.color.holo_blue_light));// this is a selected
						      // position so make it red
	}
	return v;
    }

    public void setNewSelection(int position, boolean value) {
	mSelection.put(position, value);
	notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
	Boolean result = mSelection.get(position);
	return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
	return mSelection.keySet();
    }

    public void removeSelection(int position) {
	mSelection.remove(position);
	notifyDataSetChanged();
    }

    public void clearSelection() {

	mSelection = new HashMap<Integer, Boolean>();
	notifyDataSetChanged();
    }

    public void deleteItems(Context context, long[] ids) {

	String[] string_list = new String[ids.length];

	for (int i = 0; i < ids.length; i++) {
	    string_list[i] = String.valueOf(ids[i]);
	}

	Log.d(LOG_TAG, "delete ids " + string_list);

	Intent intent = new Intent(context, ServerDBSimulation.class).putExtra(
		"update_notes_on_remote", R.id.delete_notes).putExtra("delId",
		string_list);

	// notifyDataSetChanged();
	context.startService(intent);
    }

}
