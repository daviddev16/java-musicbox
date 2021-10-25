package org.musicbox.modules.youtube;

import java.io.IOException;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.google.Auth;
import org.musicbox.core.module.CoreModule;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class YoutubeSearchModule extends CoreModule {

   private YouTube youtube;
   
   @Override
   public void onRegistered() {
      youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
         public void initialize(HttpRequest request) throws IOException {}
      }).setApplicationName("musicbox-java-search").build();
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
         List<SearchResult> searchResults = searchResponse.getItems();
         if (searchResults != null && !searchResults.isEmpty()) {
            return "youtube.com/watch?v=" +  searchResults.get(0).getId().getVideoId();
         }
      } catch (Exception e) {}
      return "-";
   }
   
   @Override
   public void onDisabling() {}
   
   public YouTube getYoutube() {
      return this.youtube;
   }
   
}
