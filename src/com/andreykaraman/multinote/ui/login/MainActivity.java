package com.andreykaraman.multinote.ui.login;

import java.util.Locale;

import com.andreykaraman.multinote.R;
import com.andreykaraman.multinote.ui.login.menu.AboutDialogFragment;
import com.andreykaraman.multinote.ui.login.menu.SettingsActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements ActionBar.TabListener {

    static final String LOG_SECTION = MainActivity.class.getName();
    static SharedPreferences savedData;
    static SharedPreferences sharedPrefs;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this
     * becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Log.d(LOG_SECTION, "Activity.onCreate");
	// Set up the action bar.
	final ActionBar actionBar = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	savedData = getSharedPreferences("settings", 0);
	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	// Create the adapter that will return a fragment for each of the three
	// primary sections of the activity.
	mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

	// Set up the ViewPager with the sections adapter.
	mViewPager = (ViewPager) findViewById(R.id.pager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	// When swiping between different sections, select the corresponding
	// tab. We can also use ActionBar.Tab#select() to do this if we have
	// a reference to the Tab.
	mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		    @Override
		    public void onPageSelected(int position) {
			actionBar.setSelectedNavigationItem(position);
		    }
		});

	// For each of the sections in the app, add a tab to the action bar.
	for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	    // Create a tab with text corresponding to the page title defined by
	    // the adapter. Also specify this Activity object, which implements
	    // the TabListener interface, as the callback (listener) for when
	    // this tab is selected.
	    actionBar.addTab(actionBar.newTab()
		    .setText(mSectionsPagerAdapter.getPageTitle(i))
		    .setTabListener(this));
	}

    }

    @Override
    protected void onDestroy() {
	Log.d(LOG_SECTION, "Activity.onDestroy");
	super.onDestroy();
    }

    @Override
    protected void onResume() {
	super.onResume();

	Log.d(LOG_SECTION, "Activity.onResume");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	    // getItem is called to instantiate the fragment for the given page.
	    // Return a PlaceholderFragment (defined as a static inner class
	    // below).
	    // return PlaceholderFragment.newInstance(position);

	    switch (position) {
	    case 0:
		return LoginFragment.newInstance();
	    case 1:
		return RegisterFragment.newInstance();
	    }
	    return null;
	}

	@Override
	public int getCount() {
	    // Show 2 total pages.
	    return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    Locale local = Locale.getDefault();
	    switch (position) {
	    case 0:
		return getString(R.string.login).toUpperCase(local);
	    case 1:
		return getString(R.string.registration_tab).toUpperCase(local);
	    }
	    return null;
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    Intent intent = new Intent(this, SettingsActivity.class);
	    startActivity(intent);
	    return true;
	} else if (id == R.id.about) {
	    showAboutDialog();
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    void showAboutDialog() {

	FragmentTransaction ft = getFragmentManager().beginTransaction();
	Fragment prev = getFragmentManager().findFragmentByTag("aboutDialog");
	if (prev != null) {
	    ft.remove(prev);
	}
	ft.addToBackStack(null);

	// Create and show the dialog.
	DialogFragment newFragment = AboutDialogFragment.newInstance();
	newFragment.show(ft, "aboutDialog");
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
	// When the given tab is selected, switch to the corresponding page in
	// the ViewPager.
	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

//    public static class LoginRequest {
//	String login;
//    }

    public interface LoadingHandler<T> {
	void onStartLoading();

	void onStopLoading();

	void onLoadingResult(T result);

    }

}
