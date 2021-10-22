package org.musicbox.core.utils;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public final class LRHBuilder {

   private Consumer<AudioTrack> trackLoadedConsumer;
   private Consumer<AudioPlaylist> trackPlaylistConsumer;
   private Consumer<Class<Void>> noMatchesConsumer;
   private Consumer<FriendlyException> loadFailedConsumer;
   
   private LRHBuilder() {}
   
   public static LRHBuilder create() {
      return new LRHBuilder();
   }
   
   public LRHBuilder onTrackLoaded(Consumer<AudioTrack> trackLoadedConsumer) {
      this.trackLoadedConsumer = trackLoadedConsumer;
      return this;
   }
   
   public LRHBuilder onNoMatches(Consumer<Class<Void>> noMatchesConsumer) {
      this.noMatchesConsumer = noMatchesConsumer;
      return this;
   }
   
   public LRHBuilder onPlaylistLoaded(Consumer<AudioPlaylist> trackPlaylistConsumer) {
      this.trackPlaylistConsumer = trackPlaylistConsumer;
      return this;
   }
   
   public LRHBuilder onFailed(Consumer<FriendlyException> loadFailedConsumer) {
      this.loadFailedConsumer = loadFailedConsumer;
      return this;
   }
   
   public AudioLoadResultHandler build() {
      
      return new AudioLoadResultHandler() {
         
         @Override
         public void trackLoaded(AudioTrack track) {
            if(trackLoadedConsumer != null)
               trackLoadedConsumer.accept(track);
         }

         @Override
         public void playlistLoaded(AudioPlaylist playlist) {
            if(trackPlaylistConsumer != null)
               trackPlaylistConsumer.accept(playlist);
         }

         @Override
         public void noMatches() {
            if(noMatchesConsumer != null)
               noMatchesConsumer.accept(Void.TYPE);
         }

         @Override
         public void loadFailed(FriendlyException exception) {
            if(loadFailedConsumer != null)
               loadFailedConsumer.accept(exception);
         }
         
      };
      
   }
   
}
