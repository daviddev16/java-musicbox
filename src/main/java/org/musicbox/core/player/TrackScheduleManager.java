package org.musicbox.core.player;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.core.GuildInstance;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class TrackScheduleManager extends AudioEventAdapter implements AudioSendHandler {

   private final AudioPlayer player;
   private final List<AudioTrack> tracklist;
   private final GuildInstance guildInstance;

   private volatile int currentPosition = -1;
   private RepeatMode repeatMode;
   private AudioFrame lastFrame;

   public TrackScheduleManager(AudioPlayer player, GuildInstance guildInstance) {
      this.player = player;
      this.guildInstance = guildInstance;
      this.repeatMode = RepeatMode.ALL;
      this.tracklist = Collections.synchronizedList(new LinkedList<>());
   }

   @Override
   public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
      if (repeatMode == RepeatMode.NONE) {
         if (!isEnd()) {
            next(true);
         } else {
            currentPosition = -1;
         }
      } else if (repeatMode == RepeatMode.SINGLE) {
         play(currentPosition, true);
      } else if (repeatMode == RepeatMode.ALL) {
         if (!isEnd()) {
            next(true);
         } else {
            play(0, true);
         }
      }

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

   public boolean isValidPosition(int position) {
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

   @Override
   public boolean canProvide() {
      if (lastFrame == null) {
         lastFrame = player.provide();
      }

      return lastFrame != null;
   }

   public GuildInstance getGuildInstance() {
      return guildInstance;
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

}