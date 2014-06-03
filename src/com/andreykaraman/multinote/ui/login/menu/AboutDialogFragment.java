package com.andreykaraman.multinote.ui.login.menu;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.R.id;
import com.andreykaraman.multinote.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class AboutDialogFragment extends DialogFragment {

    public static AboutDialogFragment newInstance() {
	AboutDialogFragment f = new AboutDialogFragment();

	return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setStyle(AboutDialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.about, container, false);

	// Watch for button clicks.
	Button buttonOK = (Button) v.findViewById(R.id.buttonOK);

	buttonOK.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dismiss();
	    }
	});

	Button buttonRate = (Button) v.findViewById(R.id.buttonRateApp);

	buttonRate.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("market://details?id="
			+ getActivity().getPackageName()));
		startActivity(i);
	    }
	});

	return v;
    }

}
