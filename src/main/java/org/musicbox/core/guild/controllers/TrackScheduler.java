package org.musicbox.core.guild.controllers;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.builders.LRHBuilder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.core.models.GuildWrapperPart;
import org.musicbox.core.utils.Utilities;
import org.musicbox.models.QueuedTrackResult;

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

   private final List<AudioTrack> tracklist;

   private int currentPosition = -1;

   private final AudioPlayer player;

   private AudioTrack lastTrack;

   private RepeatMode repeatMode;
   private AudioFrame lastFrame;

   public TrackScheduler(GuildWrapper guildWrapper) {

      this.player = BotAudioManager.getBotAudioManager().getAudioPlayerManager().createPlayer();
      this.tracklist = Collections.synchronizedList(new LinkedList<>());
      this.player.setVolume(DefaultConfig.VOLUME);
      this.player.addListener(this);
      this.repeatMode = RepeatMode.ALL;
      this.guildWrapper = guildWrapper;

      getGuild().getAudioManager().setSendingHandler(this);
   }

   @Override
   public void onTrackStart(AudioPlayer player, AudioTrack track) {
   }

   @Override
   public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
      lastTrack = track;
      if (repeatMode == RepeatMode.SINGLE) {
         play(track.makeClone(), true);
         return;
      }
      if (endReason.mayStartNext) {
         skip();
      }
   }

   public void skip() {
      nextTrack(getRepeatMode() == RepeatMode.ALL ? true : false);
   }

   public void nextTrack(boolean keepGoing) {
      currentPosition++;
      if (currentPosition >= tracklist.size()) {
         if (keepGoing)
            currentPosition = 0;
         else
            stopSchedule();
      }
      select(currentPosition, true);
   }

   public void select(int selectedPosition, boolean now) {

      if (selectedPosition < 0 && selectedPosition >= getTracklist().size())
         return;

      AudioTrack selectedTrack = getValidTrackState(selectedPosition);
      play(selectedTrack, now);
   }

   public void setPauseState(boolean paused) {
      player.setPaused(paused);
   }

   public void stopSchedule() {
      tracklist.clear();
      player.stopTrack();
      lastTrack = null;
      currentPosition = 0;
   }

   public boolean validPosition(int position) {
      return tracklist.get(position) != null;
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

   public void play(AudioTrack track, boolean now) {
      if (player.startTrack(track, !now))
         currentPosition = getTracklist().indexOf(track);
   }

   public void queue(String content, final QueuedTrackResult result) {
      if (!Utilities.isURL(content)) {
         content = YoutubeSearchManager.getSearchManager().getUrlBasedOnText(content);
      }
      load(content, result);
   }
   
   public void queue(AudioTrack track) {
      if (getTracklist().contains(track))
         return;
      getTracklist().add(track);
      if(player.getPlayingTrack() == null)
         play(track, true);
   }

   public void load(final String url, QueuedTrackResult result) {
      AudioPlayerManager manager = BotAudioManager.getBotAudioManager().getAudioPlayerManager();
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
      manager.loadItemOrdered(this, stripUrl(url), loadResultHandler);
   }

   private String stripUrl(String url) {
      if (url.startsWith("<") && url.endsWith(">")) {
         return url.substring(1, url.length() - 1);
      } else {
         return url;
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

   public List<AudioTrack> getTracklist() {
      return tracklist;
   }

   public AudioTrack getLastTrack() {
      return lastTrack;
   }

   public enum RepeatMode {
      ALL,
      SINGLE,
      NONE;
   }

}