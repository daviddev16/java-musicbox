package org.musicbox.player;

import org.musicbox.managing.LoadingResult;

public final class TrackChunk {

	private final PlayInfo playInfo;
	private final LoadingResult loadingResult;
	
	public TrackChunk(PlayInfo playInfo, LoadingResult loadingResult) {
		this.playInfo = playInfo;
		this.loadingResult = loadingResult;
	}

	public LoadingResult getLoadingResult() {
		return loadingResult;
	}

	public PlayInfo getPlayInfo() {
		return playInfo;
	}
}
