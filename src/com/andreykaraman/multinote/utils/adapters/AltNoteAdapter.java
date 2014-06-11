package com.andreykaraman.multinote.utils.adapters;

import java.util.HashMap;
import java.util.Set;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.utils.ServerDBHelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AltNoteAdapter extends CursorAdapter {

    final static String LOG_TAG = "myLogs";
    Note note;
    Context context;
    int sessionId;
    
    
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    protected static class RowViewHolder {
	public TextView noteTitle;
	public TextView noteContent;
	public ImageButton deleteButton;
    }

    @SuppressWarnings("deprecation")
    public AltNoteAdapter(Context context, Cursor cursor, int sessionId) {
	super(context, cursor);
	this.context = context;
	this.sessionId = sessionId;
	// TODO Auto-generated constructor stub
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
	// note = new Note();

	RowViewHolder holder = (RowViewHolder) view.getTag();

	holder.noteTitle.setText(cursor.getString(cursor.getColumnIndex(cursor
		.getColumnName(1))));

	holder.noteContent.setText(cursor.getString(cursor
		.getColumnIndex(cursor.getColumnName(2))));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View view = super.getView(position, convertView, parent);// let the adapter
							      // handle setting
							      // up the row
							      // views
	view.setBackgroundColor(parent.getResources().getColor(
		android.R.color.background_light)); // default color

	if (mSelection.get(position) != null) {
	    view.setBackgroundColor(parent.getResources().getColor(
		    android.R.color.holo_blue_light));// this is a selected
						      // position so make it red
	}
	return view;
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

    public void deleteItems(Context context, long[] ids, int sessionId) {

	Intent intent = new Intent(context, ServerDBHelper.class)
		.putExtra("update_notes_on_remote", R.id.delete_notes)
		.putExtra(APIStringConstants.CONST_NOTE_ID, ids)
		.putExtra(APIStringConstants.CONST_SESSOIN_ID, sessionId);

	context.startService(intent);
    }

}
