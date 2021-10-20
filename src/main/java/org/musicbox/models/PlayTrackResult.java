package org.musicbox.models;

import java.util.List;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.models.IAudioLoadResult;
import org.musicbox.core.utils.Constants;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Placeholder;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayTrackResult implements IAudioLoadResult {

   private final MessageReceivedEvent event;
   private final GuildInstance guildInstance;
   private final List<Placeholder> placeholders;

   public PlayTrackResult(MessageReceivedEvent event, GuildInstance guildInstance, List<Placeholder> placeholders) {
      this.event = event;
      this.guildInstance = guildInstance;
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
         /* report to owner */
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

   public GuildInstance getGuildInstance() {
      return guildInstance;
   }

   public MessageReceivedEvent getEvent() {
      return event;
   }

}
