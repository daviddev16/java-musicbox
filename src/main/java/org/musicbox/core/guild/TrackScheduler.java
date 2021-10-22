package org.musicbox.core.guild;

import java.nio.ByteBuffer;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.core.player.RepeatMode;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public final class TrackScheduler extends AudioEventAdapter implements AudioSendHandler, GuildWrapperPart {

   private final GuildWrapper guildWrapper;

   private final AudioPlayer player;

   private RepeatMode repeatMode;
   private AudioFrame lastFrame;

   public TrackScheduler(GuildWrapper guildWrapper) {

      this.player = BotAudioManager.getBotAudioManager().getAudioPlayerManager().createPlayer();
      this.player.setVolume(DefaultConfig.VOLUME);
      this.player.addListener(this);
      this.repeatMode = RepeatMode.ALL;
      this.guildWrapper = guildWrapper;

      getGuild().getAudioManager().setSendingHandler(this);
   }

   @Override
   public void onTrackStart(AudioPlayer player, AudioTrack track) {
      super.onTrackStart(player, track);
   }

   @Override
   public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
   }

   /*public void load(final String url, IAudioLoadResult result) {
      BotAudioManager.getBotAudioManager().getAudioPlayerManager().loadItemOrdered(this, stripUrl(url),
            new AudioLoadResultHandler() {
         @Override
         public void trackLoaded(AudioTrack track) {
            try {
               queue(track);
               result.onQueuedSingle(track);
            } catch (Exception e) {
               result.onFailed(e);
            }
         }

         @Override
         public void playlistLoaded(AudioPlaylist playlist) {
            try {
               for (AudioTrack audioTrack : playlist.getTracks()) {
                  queue(audioTrack);
               }
               result.onQueuedPlaylist(playlist);
            } catch (Exception e) {
               result.onFailed(e);
            }
         }

         @Override
         public void noMatches() {
            result.noMatches();
         }

         @Override
         public void loadFailed(FriendlyException exception) {
            result.onFailed(exception);
         }
      });
   }

   private String stripUrl(String url) {
      if (url.startsWith("<") && url.endsWith(">")) {
         return url.substring(1, url.length() - 1);
      } else {
         return url;
      }
   }*/

   @Override
   public boolean canProvide() {
      if (lastFrame == null) {
         lastFrame = player.provide();
      }

      return lastFrame != null;
   }

   @Override
   public ByteBuffer provide20MsAudio() {
      if (lastFrame == null) {
         lastFrame = player.provide();
      }

      byte[] data = lastFrame != null ? lastFrame.getData() : null;
      lastFrame = null;

      return ByteBuffer.wrap(data);
   }

   @Override
   public boolean isOpus() {
      return true;
   }

   public void setRepeatMode(RepeatMode repeatMode) {
      this.repeatMode = repeatMode;
   }

   public RepeatMode getRepeatMode() {
      return repeatMode;
   }

   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
   }

}