package org.musicbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.musicbox.config.Configs;
import org.musicbox.utils.Constants;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class PlaceholderBuilder {

  private List<Placeholder> placeholders;

  public PlaceholderBuilder(boolean setupDefaults) {
	this.placeholders = new ArrayList<Placeholder>();
	
	if (setupDefaults) {
	  add(Constants.KEY_GLOBAL_PREFIX, Configs.PREFIX);
	  add(Constants.KEY_OWNER, Configs.OWNER);
	}
	
  }

  public PlaceholderBuilder add(Placeholder placeholder) {
	placeholders.add(placeholder);
	return this;
  }

  public PlaceholderBuilder event(MessageReceivedEvent event) {
	add(Constants.KEY_SENDER_NAME, event.getAuthor().getAsTag());
	add(Constants.KEY_SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl());
	return this;
  }

  public PlaceholderBuilder add(String suffix, String replacement) {
	placeholders.add(Placeholder.create(suffix, replacement));
	return this;
  }

  public List<Placeholder> build() {
	return placeholders;
  }

  public static PlaceholderBuilder createBy(MessageReceivedEvent event, boolean setupDefaults) {
	return new PlaceholderBuilder(setupDefaults).event(event);
  }
}
