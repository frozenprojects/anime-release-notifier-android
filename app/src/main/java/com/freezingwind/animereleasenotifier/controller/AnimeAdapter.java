package com.freezingwind.animereleasenotifier.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import com.freezingwind.animereleasenotifier.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.freezingwind.animereleasenotifier.anime.AnimeListActivity;

import android.graphics.Bitmap;
import android.graphics.Color;

public class AnimeAdapter extends ArrayAdapter<Anime> {
	// View lookup cache
	private static class ViewHolder {
		View listItem;
		TextView title;
		TextView airingDate;
		ImageView image;
	}

	public AnimeAdapter(Context context, ArrayList<Anime> anime) {
		super(context, R.layout.row, anime);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final Anime anime = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		final ViewHolder viewHolder; // view lookup cache stored in tag

		if(convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.row, parent, false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.airingDate = (TextView) convertView.findViewById(R.id.airingDate);
			viewHolder.listItem = convertView.findViewById(R.id.listItem);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// Populate the data into the template view using the data object
		viewHolder.title.setText(anime.title);
		viewHolder.airingDate.setText(anime.airingTimeRemaining);

		// Mark as new
		if(anime.watched < anime.available - anime.offset) {
			int newTextColor = Color.rgb(16, 16, 16);

			viewHolder.title.setTextColor(newTextColor);
			viewHolder.airingDate.setTextColor(newTextColor);

			if(viewHolder.listItem != null) {
				viewHolder.listItem.setBackgroundColor(Color.argb(127, 80, 255, 80));
				viewHolder.listItem.setAlpha(1.0f);
			}
		} else {
			if(viewHolder.listItem != null)
				viewHolder.listItem.setAlpha(0.7f);
		}

		ImageRequest imageRequest = new ImageRequest(anime.imageURL,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap bitmap) {
						viewHolder.image.setImageBitmap(bitmap);
					}
				}, 0, 0, null,
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						// TODO: ...
						//viewHolder.image.setImageResource(R.drawable.image_load_error);
					}
				});

		// Execute request
		MyVolley.getRequestQueue().add(imageRequest);

		// Return the completed view to render on screen
		return convertView;
	}
}