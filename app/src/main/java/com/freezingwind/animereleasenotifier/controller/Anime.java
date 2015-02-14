package com.freezingwind.animereleasenotifier.controller;

public class Anime {
	public String title;
	public String imageURL;
	public String url;

	public String animeProviderURL;
	public String nextEpisodeURL;
	public String videoURL;

	public int watched;
	public int available;
	public int max;
	public int offset;

	public String airingTimeRemaining;

	public boolean notify;

	public Anime(
			String title,
			String imageURL,
			String url,
			String animeProviderURL,
			String nextEpisodeURL,
			String videoURL,
			int watched,
			int available,
			int max,
			int offset,
			String airingTimeRemaining
	) {
		this.title = title;
		this.imageURL = imageURL;
		this.url = url;

		this.videoURL = videoURL;
		this.animeProviderURL = animeProviderURL;
		this.nextEpisodeURL = nextEpisodeURL;

		this.watched = watched;
		this.available = available;
		this.max = max;
		this.offset = offset;

		this.airingTimeRemaining = airingTimeRemaining;
	}
}