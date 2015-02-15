package com.freezingwind.animereleasenotifier.controller;

import android.app.Application;

import com.freezingwind.animereleasenotifier.helpers.NetworkManager;

/**
 * Created by Eduard on 2/1/2015.
 */
public class AppController extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		init();
	}

	private void init() {
		NetworkManager.init(this);
	}
}
