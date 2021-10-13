package org.musicbox.core.models;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface IAudioLoadResult {

	public void noMatches();

	public void onFailed(Exception e);

	public void onQueuedSingle(AudioTrack track);

	public void onQueuedPlaylist(AudioPlaylist playlist);

}
