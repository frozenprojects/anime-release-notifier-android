package com.freezingwind.animereleasenotifier.ui.animelist;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBar;

import com.freezingwind.animereleasenotifier.R;
import com.freezingwind.animereleasenotifier.controller.AppController;
import com.freezingwind.animereleasenotifier.receiver.BootReceiver;
import com.freezingwind.animereleasenotifier.ui.adapter.TabsAdapter;
import com.freezingwind.animereleasenotifier.ui.settings.SettingsActivity;

public class AnimeListActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener, ActionBar.TabListener {
	private ViewPager viewPager;
	private ActionBar actionBar;
	private TabsAdapter tabsAdapter;

	// Tab titles
	private String[] tabs = {
		"Watching",
		"Completed"
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_main);

	    // Tabs
	    viewPager = (ViewPager) findViewById(R.id.pager);
	    actionBar = getSupportActionBar();
	    tabsAdapter = new TabsAdapter(getSupportFragmentManager());

	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		viewPager.setAdapter(tabsAdapter);

	    /**
	     * on swiping the viewpager make respective tab selected
	     * */
	    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

		    @Override
		    public void onPageSelected(int position) {
			    // on changing the page
			    // make respected tab selected
			    actionBar.setSelectedNavigationItem(position);
		    }

		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }

		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
	    });

		// Adding Tabs
		for(String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

	    // Listen to settings changes
	    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

	    // Update title
	    updateTitle();

	    // Enable boot receiver
	    enableBootReceiver();

	    // Schedule alarm
	    AppController.scheduleAlarm(this);
    }

	// Enable boot receiver
	private void enableBootReceiver() {
		ComponentName receiver = new ComponentName(this, BootReceiver.class);
		PackageManager packageManager = this.getPackageManager();

		packageManager.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch(key) {
			case "userName":
				updateTitle();
				break;

			case "updateInterval":
				AppController.scheduleAlarm(this);
				break;
		}
	}

	// Update title
	protected void updateTitle() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String title = sharedPrefs.getString("userName", getString(R.string.app_name));

		if(title.length() == 0)
			title = getString(R.string.app_name);

		setTitle(title);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anime_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

	}
}
