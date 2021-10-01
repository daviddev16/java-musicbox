package org.musicbox.models;

import org.musicbox.utils.Utils;

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

  public String getTitle() {
	if (getPlayInfo() != null) {
	  return getPlayInfo().getTitle();
	}
	return "none";
  }

  public long getLength() {
	if (getPlayInfo() != null) {
	  return getPlayInfo().getLength();
	}
	return -1;
  }

  public String getTiming() {
	if (getPlayInfo() != null) {
	  if (getPlayInfo().isPlaylist()) {
		return Long.toString(getPlayInfo().getLength());
	  } else {
		return Utils.getTimestamp(getPlayInfo().getLength());
	  }
	}
	return "none";
  }

}
