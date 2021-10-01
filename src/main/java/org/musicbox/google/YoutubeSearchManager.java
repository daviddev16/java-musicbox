package org.musicbox.google;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.util.List;

import org.musicbox.MusicBox;

public class YoutubeSearchManager {

  private final YouTube youtube;

  public YoutubeSearchManager() {
	youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
	  public void initialize(HttpRequest request) throws IOException {
	  }
	}).setApplicationName("musicbox-java-search").build();
  }

  public String getUrlBaseOnText(String text) {
	try {
	  YouTube.Search.List search = youtube.search().list("id,snippet");
	  search.setKey(MusicBox.getConfiguration().getYoutubeAPIKey());
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

}