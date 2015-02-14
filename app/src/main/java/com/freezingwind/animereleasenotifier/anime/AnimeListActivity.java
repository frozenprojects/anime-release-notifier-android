package com.freezingwind.animereleasenotifier.anime;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.freezingwind.animereleasenotifier.R;
import com.freezingwind.animereleasenotifier.alarm.AlarmHelper;
import com.freezingwind.animereleasenotifier.receiver.AlarmReceiver;
import com.freezingwind.animereleasenotifier.receiver.BootReceiver;
import com.freezingwind.animereleasenotifier.settings.SettingsActivity;


public class AnimeListActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_list);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		// Enable boot receiver
		ComponentName receiver = new ComponentName(this, BootReceiver.class);
		PackageManager pm = this.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);

		// Schedule alarm
		BootReceiver.scheduleAlarm(this);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch(key) {
			case "updateInterval":
				BootReceiver.scheduleAlarm(this);
				break;
		}
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
}
