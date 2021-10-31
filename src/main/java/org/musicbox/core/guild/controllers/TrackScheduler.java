package org.musicbox.core.guild.controllers;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.builders.LRHBuilder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.AudioManager;
import org.musicbox.core.models.GuildWrapperPart;
import org.musicbox.core.module.Modules;
import org.musicbox.core.utils.Utilities;
import org.musicbox.models.QueuedTrackResult;
import org.musicbox.modules.youtube.YoutubeSearchModule;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public final class TrackScheduler extends AudioEventAdapter implements AudioSendHandler, GuildWrapperPart {

   private final GuildWrapper guildWrapper;
   private final AudioPlayer player;

   private final List<AudioTrack> tracklist;
   private int currentPosition = -1;

   private RepeatMode repeatMode;
   private AudioFrame lastFrame;

   public TrackScheduler(GuildWrapper guildWrapper) {
      this.player = AudioManager.getAudioManager().getPlayerManager().createPlayer();
      this.tracklist = Collections.synchronizedList(new LinkedList<>());
      this.player.setVolume(DefaultConfig.VOLUME);
      this.player.addListener(this);
      this.repeatMode = RepeatMode.NONE;
      this.guildWrapper = guildWrapper;
      getGuild().getAudioManager().setSendingHandler(this);
   }

   @Override
   public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
      if (repeatMode == RepeatMode.SINGLE) {
         player.startTrack(track.makeClone(), true);
         return;
      }
      else if (endReason.mayStartNext) {
         nextTrack();
      }
   }

   public void select(int selectedPosition, boolean now) {
      if (selectedPosition < 0 && selectedPosition >= getTracklist().size()) {
         return;
      }
      AudioTrack selectedTrack = getValidTrackState(selectedPosition);
      play(selectedTrack, now);
   }

   public AudioTrack getValidTrackState(int position) {
      AudioTrack audioTrack = getTracklist().get(position);
      if (audioTrack != null && (audioTrack.getState() != AudioTrackState.INACTIVE
            || audioTrack.getState() != AudioTrackState.STOPPING)) {

         audioTrack = audioTrack.makeClone();
         getTracklist().set(position, audioTrack);
      }
      return audioTrack;
   }

   private void load(final String url, QueuedTrackResult result) {
      AudioPlayerManager manager = AudioManager.getAudioManager().getPlayerManager();
      AudioLoadResultHandler loadResultHandler = LRHBuilder.create()
            .onFailed((exception) -> result.onFailed(exception))
            .onNoMatches((voId) -> result.noMatches())
            .onTrackLoaded(track -> {
               try {
                  queue(track);
                  result.onQueuedSingle(track);
               } catch (Exception e) {
                  result.onFailed(e);
               }
            }).onPlaylistLoaded((playlist) -> {
               try {
                  for (AudioTrack audioTrack : playlist.getTracks()) {
                     queue(audioTrack);
                  }
                  result.onQueuedPlaylist(playlist);
               } catch (Exception e) {
                  result.onFailed(e);
               }
            })
            .build();
      manager.loadItemOrdered(this, Utilities.stripUrl(url), loadResultHandler);
   }

   public void queue(String content, final QueuedTrackResult result) {
      if (!Utilities.isURL(content)) {
         content = Modules.getModule(YoutubeSearchModule.class)
               .getUrlBasedOnText(content);
      }
      load(content, result);
   }

   public void play(AudioTrack track, boolean now) {
      if (player.startTrack(track, !now))
         currentPosition = getTracklist().indexOf(track);
   }

   public String getCurrentName() {
      if(currentPosition < 0 || currentPosition >= tracklist.size())
         return "-";
      return currentPosition + " - " + tracklist.get(currentPosition).getInfo().title;
   }

   public void restart() {
      select(currentPosition, true);
   }

   public void queue(AudioTrack track) {
      if (getTracklist().contains(track)) {
         return;
      }
      getTracklist().add(track);
      if(player.getPlayingTrack() == null)
         play(track, true);
   }

   public void previousTrack() {
      currentPosition--;
      if (currentPosition < 0) {
         if (keepGoing())
            currentPosition = tracklist.size() - 1;
         else {
            stopSchedule();
            return;
         }
      }
      select(currentPosition, true);
   }

   public void nextTrack() {
      currentPosition++;
      if (currentPosition >= tracklist.size()) {
         if (keepGoing())
            currentPosition = 0;
         else {
            stopSchedule();
            return;
         }
      }
      select(currentPosition, true);
   }

   public void stopSchedule() {
      tracklist.clear();
      if(player.getPlayingTrack() != null) {
         player.stopTrack();
      }
      currentPosition = 0;
   }

   public void setRepeatMode(RepeatMode repeatMode) {
      this.repeatMode = repeatMode;
   }

   public RepeatMode getRepeatMode() {
      return repeatMode;
   }

   public int getCurrentPosition() {
      return currentPosition;
   }

   public void setPauseState(boolean paused) {
      player.setPaused(paused);
   }

   public boolean validPosition(int position) {
      return position >= 0 && position < tracklist.size();
   }

   public boolean keepGoing() {
      return getRepeatMode() == RepeatMode.ALL ? true : false;
   }

   public List<AudioTrack> getTracklist() {
      return tracklist;
   }

   public boolean isEmptyOrDone() {
      return getTracklist().isEmpty();
   }

   public boolean isPlaying() {
      return player.getPlayingTrack() != null;
   }

   public int getTrackPosition(AudioTrack track) {
      
      if(getTracklist().isEmpty() || track == null)
         return -1;
      
      return  getTracklist().indexOf(track);
   }

   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
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

   public enum RepeatMode {
      ALL,
      SINGLE,
      NONE;
   }


}