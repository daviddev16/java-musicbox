package org.musicbox.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.musicbox.I18n;
import org.musicbox.MusicBox;
import org.musicbox.player.TrackChunk;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Placeholder {

  public String suffix;
  private String replacement;

  private Placeholder(String suffix, String replacement) {
	this.suffix = suffix;
	this.replacement = replacement;
  }

  public static List<Placeholder> of(Placeholder... phs) {
	List<Placeholder> placeholders = new ArrayList<>();
	if (phs != null) {
	  Arrays.stream(phs).forEach(placeholders::add);
	}
	return placeholders;
  }

  public static List<Placeholder> of(List<Placeholder> otherPlaceholders, Placeholder... phs) {
	List<Placeholder> placeholders = of(phs);
	if (otherPlaceholders != null) {
	  otherPlaceholders.forEach(placeholders::add);
	}
	return placeholders;
  }

  public static List<Placeholder> defaultPlaceholders() {
	return of(create(I18n.TRACK_ADDED_ICON, I18n.ADDED), create(I18n.TRACK_PAUSED_ICON, I18n.PAUSED),
		create(I18n.TRACK_REPEAT_MODE, I18n.REPEAT), create(I18n.TRACK_SKIPPED_ICON, I18n.SKIPPED),
		create(I18n.TRACK_RESTARTED_ICON, I18n.RESTARTED), create(I18n.TRACK_RESUMED_ICON, I18n.RESUMED),
		create(I18n.FAILED_ICON, I18n.FAILED), create(I18n.SLEEPING_ICON, I18n.SLEEPING));
  }

  public static void messageEventPlaceholders(MessageReceivedEvent event, List<Placeholder> placeholders) {
	placeholders.add(create(I18n.SENDER_TAG, event.getAuthor().getAsTag()));
	placeholders.add(create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()));
  }

  public static void trackPlaceholder(TrackChunk trackChunk, List<Placeholder> placeholders) {

	if (trackChunk.getPlayInfo() == null)
	  return;

	String timestamp = (trackChunk.getPlayInfo().isPlaylist()) ? Long.toString(trackChunk.getPlayInfo().getLength())
		: Utils.getTimestamp(trackChunk.getPlayInfo().getLength());

	placeholders.add(create(I18n.TRACK_TITLE, trackChunk.getPlayInfo().getTitle()));
	placeholders.add(create(I18n.TRACK_TIMESTAMP, timestamp));
  }

  public static void userInputPlaceholder(String url, List<Placeholder> placeholders) {
	placeholders.add(create(I18n.USER_INPUT_TAG, url.trim()));
  }

  public static void addReason(List<Placeholder> placeholders, String failedReason) {
	placeholders.add(create(I18n.FAIL_REASON, failedReason));
  }

  public static Placeholder owner() {
	return create(I18n.OWNER_TAG, MusicBox.getConfiguration().getOwnerTag());
  }

  public static Placeholder create(String suffix, String replacement) {
	return new Placeholder(suffix, replacement);
  }

  public String getSuffix() {
	return suffix;
  }

  public void setSuffix(String suffix) {
	this.suffix = suffix;
  }

  public String getReplacement() {
	return replacement;
  }

  public void setReplacement(String replacement) {
	this.replacement = replacement;
  }

  public String getCode() {
	return ("$[" + getSuffix() + "]").trim();
  }

}
