package org.musicbox.models;

public final class TrackChunk {

  private final PlayInfo playInfo;

  public TrackChunk(PlayInfo playInfo) {
	this.playInfo = playInfo;
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

 

}
