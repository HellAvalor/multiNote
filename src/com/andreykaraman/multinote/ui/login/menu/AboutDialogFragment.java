package com.andreykaraman.multinote.ui.login.menu;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.andreykaraman.multinote.R;

public class AboutDialogFragment extends DialogFragment {

    public static AboutDialogFragment newInstance() {
	return new AboutDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.about, container, false);

	// Watch for button clicks.
	Button buttonOK = (Button) view.findViewById(R.id.buttonOK);
	buttonOK.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dismiss();
	    }
	});

	Button buttonRate = (Button) view.findViewById(R.id.buttonRateApp);
	buttonRate.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="
			+ getActivity().getPackageName()));
		startActivity(intent);
	    }
	});
	return view;
    }
}
