package org.musicbox.core.guild.modules;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.core.models.IAudioLoadResult;
import org.musicbox.core.player.RepeatMode;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public final class ScheduleModule extends GuildModule implements AudioSendHandler {

   private AudioPlayer player;
   private List<AudioTrack> tracklist;

   private volatile int currentPosition = -1;

   private RepeatMode repeatMode;
   private AudioFrame lastFrame;

   @Override
   public void load() {
      this.player = BotAudioManager.getBotAudioManager().getAudioPlayerManager().createPlayer();
      getInstance().getGuild().getAudioManager().setSendingHandler(this);
      this.tracklist = Collections.synchronizedList(new LinkedList<>());
      this.player.addListener(createAudioEvent());
      this.player.setVolume(DefaultConfig.VOLUME);
      this.repeatMode = RepeatMode.ALL;
   }

   private AudioEventAdapter createAudioEvent() {

      AudioEventAdapter audioEventAdapter = new AudioEventAdapter() {
         @Override
         public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
            if (repeatMode == RepeatMode.NONE) {
               if (!isEnd()) {
                  next(true);
               } else {
                  currentPosition = -1;
               }
            }
            else if (repeatMode == RepeatMode.SINGLE) {
               play(currentPosition, true);
            }
            else if (repeatMode == RepeatMode.ALL) {
               if (!isEnd()) {
                  next(true);
               } else {
                  play(0, true);
               }
            }
         }
      };

      return audioEventAdapter;
   }

   public void load(final String url, IAudioLoadResult result) {
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
   }

   public synchronized void stop() {
      setPauseState(true);
      player.stopTrack();
      tracklist.clear();
      currentPosition = -1;
   }

   public boolean isEnd() {
      if (tracklist.isEmpty()) {
         return true;
      }
      return currentPosition >= tracklist.size() - 1;
   }

   public void shuffle() {
      Collections.shuffle(tracklist);
   }

   public synchronized boolean play(int position, boolean now) {
      if (position < 0 || position >= tracklist.size()) {
         return false;
      }
      currentPosition = position;
      AudioTrack track = tracklist.get(currentPosition);
      return player.startTrack(track.makeClone(), !now);
   }

   public synchronized void queue(AudioTrack track) {
      if (!contains(track)) {
         tracklist.add(track);
      }
      if (!isPlaying()) {
         play(0, true);
      }
   }

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

   public synchronized boolean isPlaying() {
      return player.getPlayingTrack() != null;
   }

   public synchronized boolean next(boolean now) {
      return play(currentPosition + 1, true);
   }

   public synchronized void setPauseState(boolean paused) {
      player.setPaused(paused);
   }

   public synchronized boolean select(int index) {
      return play(index, true);
   }

   public boolean isQueuePosition(int position) {
      return (position >= 0 && position < tracklist.size());
   }

   private boolean contains(AudioTrack track) {
      for (AudioTrack tracks : tracklist) {
         if (tracks.getInfo().uri.equals(track.getInfo().uri)) {
            return true;
         }
      }
      return false;
   }

   public synchronized List<AudioTrack> getTracklist() {
      return tracklist;
   }

   public void setRepeatMode(RepeatMode repeatMode) {
      this.repeatMode = repeatMode;
   }

   public RepeatMode getRepeatMode() {
      return repeatMode;
   }

}