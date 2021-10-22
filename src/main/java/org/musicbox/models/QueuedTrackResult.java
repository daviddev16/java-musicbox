package org.musicbox.models;

import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.LoadResult;
import org.musicbox.miscs.Constants;
import org.musicbox.miscs.Messages;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueuedTrackResult implements LoadResult {

   private final MessageReceivedEvent event;
   private final GuildWrapper guildWrapper;
   private final List<Placeholder> placeholders;

   public QueuedTrackResult(MessageReceivedEvent event, GuildWrapper guildWrapper, List<Placeholder> placeholders) {
      this.event = event;
      this.guildWrapper = guildWrapper;
      this.placeholders = placeholders;
   }

   @Override
   public void noMatches() {
      Messages.translatedMessage(getEvent(), Messages.NO_MATCHES, placeholders);
   }

   @Override
   public void onFailed(Exception e) {
      placeholders.add(Placeholder.create(Constants.KEY_EXCEPTION_MESSAGE, e.getMessage()));
      Messages.translatedMessage(getEvent(), Messages.COMMAND_FAILED, placeholders);

      if(!(e instanceof FriendlyException)) {
         e.printStackTrace();
         System.exit(-1);
      }
   }

   @Override
   public void onQueuedSingle(AudioTrack track) {
      placeholders.add(Placeholder.create(Constants.KEY_TRACK_TITLE, track.getInfo().title));
      Messages.translatedMessage(getEvent(), Messages.TRACK_ADDED, placeholders);
   }

   @Override
   public void onQueuedPlaylist(AudioPlaylist playlist) {
      placeholders.add(Placeholder.create(Constants.KEY_TRACK_TITLE, playlist.getName()));
      Messages.translatedMessage(getEvent(), Messages.PLAYLIST_ADDED, placeholders);
   }
   
   public MessageReceivedEvent getEvent() {
      return event;
   }

   public GuildWrapper getWrapper() {
      return guildWrapper;
   }
}
