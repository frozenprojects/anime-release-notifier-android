package com.freezingwind.animereleasenotifier.anime;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.freezingwind.animereleasenotifier.R;
import com.freezingwind.animereleasenotifier.controller.AnimeListUpdateCallBack;
import com.freezingwind.animereleasenotifier.controller.AnimeUpdater;
import com.freezingwind.animereleasenotifier.controller.Anime;
import com.freezingwind.animereleasenotifier.controller.AnimeAdapter;
import com.freezingwind.animereleasenotifier.controller.MyVolley;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimeListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	protected Activity activity;
	protected ListView animeListView;

	protected AnimeAdapter adapter;
	protected AnimeUpdater animeUpdater;

	protected SharedPreferences sharedPrefs;

	public AnimeListFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		activity = getActivity();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);

		animeListView = new ListView(activity);
		animeUpdater = new AnimeUpdater();

		adapter = new AnimeAdapter(activity, animeUpdater.getAnimeList());
		animeListView.setAdapter(adapter);
		animeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
				Anime anime = (Anime) parent.getItemAtPosition(position);

				Intent intent;

				// Video URL
				if(anime.videoURL != null && !anime.videoURL.isEmpty()) {
					Uri videoURI = Uri.parse(anime.videoURL);
					intent = new Intent(Intent.ACTION_VIEW, videoURI);
					intent.setDataAndType(videoURI, "video/*");

					try {
						startActivity(intent);
						return;
					} catch (ActivityNotFoundException e) {
						Log.d("AnimeListFragment", e.toString());
					}
				}

				// Next episode URL
				if(anime.nextEpisodeURL != null && !anime.nextEpisodeURL.isEmpty()) {
					Uri uri = Uri.parse(anime.nextEpisodeURL);
					intent = new Intent(Intent.ACTION_VIEW, uri);

					try {
						startActivity(intent);
						return;
					} catch (ActivityNotFoundException e) {
						Log.d("AnimeListFragment", e.toString());
					}
				}

				// Anime provider URL
				if(anime.animeProviderURL != null && !anime.animeProviderURL.isEmpty()) {
					Uri uri = Uri.parse(anime.animeProviderURL);
					intent = new Intent(Intent.ACTION_VIEW, uri);

					try {
						startActivity(intent);
						return;
					} catch (ActivityNotFoundException e) {
						Log.d("AnimeListFragment", e.toString());
					}
				}
			}
		});

		sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		update();

		return animeListView;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch(key) {
			case "userName":
				update();
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	void update() {
		String userName = sharedPrefs.getString("userName", "");

		animeUpdater.update(userName, activity, new AnimeListUpdateCallBack() {
			@Override
			public void execute() {
				adapter.notifyDataSetChanged();
			}
		});
	}
}
