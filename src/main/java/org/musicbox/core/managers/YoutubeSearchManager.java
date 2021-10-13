package org.musicbox.core.managers;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.google.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class YoutubeSearchManager {

	private static Logger logger = LoggerFactory.getLogger(YoutubeSearchManager.class);
	private static YoutubeSearchManager searchManager;
	private final YouTube youtube;

	private YoutubeSearchManager() {
		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName("musicbox-java-search").build();
		searchManager = this;
		logger.info("YoutubeSearchManager loaded.");
	}

	public String getUrlBasedOnText(String text) {
		try {
			YouTube.Search.List search = youtube.search().list("id,snippet");
			search.setKey(DefaultConfig.YOUTUBE_API_KEY);
			search.setQ(text);
			search.setType("video");

			search.setFields("items(id/videoId)");
			search.setMaxResults(2L);

			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();
			if (searchResultList != null) {
				SearchResult singleVideo = searchResultList.get(0);
				ResourceId rId = singleVideo.getId();
				String url = "youtube.com/watch?v=" + rId.getVideoId();
				return url;
			}
		} catch (Exception e) {
			return "invalid";
		}
		return "invalid";
	}

	public static YoutubeSearchManager getSearchManager() {
		return searchManager;
	}

	public static void setup() {
		new YoutubeSearchManager();
	}

}