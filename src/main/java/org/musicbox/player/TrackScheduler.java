package org.musicbox.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.core.GuildInstance;
import org.musicbox.utils.Utils;

public class TrackScheduler extends AudioEventAdapter {

  private final AudioPlayer player;
  private final List<AudioTrack> tracklist;
  private final GuildInstance guildInstance;

  private int currentPosition = -1;

  private RepeatMode repeatMode;

  public TrackScheduler(AudioPlayer player, GuildInstance guildInstance) {
	this.player = player;
	this.guildInstance = guildInstance;
	this.repeatMode = RepeatMode.NONE;
	this.tracklist = new LinkedList<>();
  }

  public synchronized void queue(AudioTrack track) {
	if (!contains(track)) {
	  tracklist.add(track);
	}
	if (!isPlaying()) {
	  play(0, true);
	}
  }

  private boolean contains(AudioTrack track) {
	for (AudioTrack tracks : tracklist) {
	  if (tracks.getInfo().uri.equals(track.getInfo().uri)) {
		return true;
	  }
	}
	return false;
  }

  public synchronized boolean play(int position, boolean now) {
	if (position < 0 || position >= tracklist.size()) {
	  return false;
	}
	
	int localcurrentpos = currentPosition;
	currentPosition = position;
	AudioTrack track = tracklist.get(position);
	System.out.println(localcurrentpos + ": " + track.getInfo().title + "> " + currentPosition + " {" + isEnd() + "}");
	
	return player.startTrack(track.makeClone(), !now);
  }

  public synchronized boolean isPlaying() {
	return Utils.isPresentOnGuild(guildInstance.getGuild()) && player.getPlayingTrack() != null;
  }

  public synchronized boolean next(boolean now) {
	return play(currentPosition + 1, true);
  }

  public synchronized void setPauseState(boolean paused) {
	player.setPaused(paused);
  }
  
  public synchronized boolean select(int index) {
	AudioTrack track = tracklist.get(index);
	return play(index, true);
  }

  public synchronized void stop() {
	tracklist.clear();
	currentPosition = -1;
  }

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

  public boolean isEnd() {
	if (tracklist.isEmpty()) {
	  return true;
	}
	return currentPosition >= tracklist.size() - 1;
  }

  public List<AudioTrack> getTracklist() {
	return tracklist;
  }

  public void setRepeating(RepeatMode mode) {
	this.repeatMode = mode;
  }

  public void shuffle() {
	Collections.shuffle((List<?>) tracklist);
  }

  public RepeatMode getRepeatMode() {
	return repeatMode;
  }

  public void setRepeatMode(RepeatMode repeatMode) {
	this.repeatMode = repeatMode;
  }

  public GuildInstance getGuildInstance() {
	return guildInstance;
  }

}