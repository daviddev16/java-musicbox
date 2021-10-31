package org.musicbox.models;

import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.LoadResult;
import org.musicbox.core.translation.PlaceholderKeys;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Utilities;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueuedTrackResult implements LoadResult {

   private final MessageReceivedEvent event;
   private final GuildWrapper guildWrapper;
   private final List<Placeholder> placeholders;

   public QueuedTrackResult(MessageReceivedEvent event, GuildWrapper guildWrapper, 
         List<Placeholder> placeholders) {
      
      this.event = event;
      this.guildWrapper = guildWrapper;
      this.placeholders = placeholders;
   }

   @Override
   public void noMatches() {

      Messages.Embed.send(getEvent().getTextChannel(), placeholders, 
            TranslationKeys.NO_MATCHES, null);  

   }

   @Override
   public void onFailed(Exception e) {

      PlaceholderBuilder.add(placeholders, 

            Placeholder.create(PlaceholderKeys.GENERIC_ERROR_MESSAGE, 
                  e.getMessage()), 

            Placeholder.create(PlaceholderKeys.ERROR_LEVEL, 
                  (e instanceof FriendlyException) ? "LOW" : "HIGH"));
            
      Messages.Embed.send(getEvent().getTextChannel(), placeholders, 
            TranslationKeys.GENERIC_ERROR, null);  

      if(!(e instanceof FriendlyException)) {
         e.printStackTrace();
         System.exit(-1);
      }

   }

   @Override
   public void onQueuedSingle(AudioTrack track) {

      PlaceholderBuilder.add(placeholders, 

            Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
                  track.getInfo().title),
            
            Placeholder.create(PlaceholderKeys.TRACK_DURATION, 
                  Utilities.getTimestamp(track.getDuration())),
            
            Placeholder.create(PlaceholderKeys.TRACK_POSITION, 
                  ""+getWrapper().getScheduler().getTrackPosition(track)));

      Messages.Embed.send(getEvent().getTextChannel(), placeholders, 
            TranslationKeys.TRACK_QUEUED, null);
   
   }

   @Override
   public void onQueuedPlaylist(AudioPlaylist playlist) {

      PlaceholderBuilder.add(placeholders, 
            
            Placeholder.create(PlaceholderKeys.PLAYLIST_NAME, 
                  playlist.getName()),
            
            Placeholder.create(PlaceholderKeys.PLAYLIST_LENGTH, 
                  ""+playlist.getTracks().size()));      
      
      Messages.Embed.send(getEvent().getTextChannel(), placeholders, 
            TranslationKeys.PLAYLIST_QUEUED, null);
   
   }

   public MessageReceivedEvent getEvent() {
      return event;
   }

   public GuildWrapper getWrapper() {
      return guildWrapper;
   }
}
