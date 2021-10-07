package org.musicbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.musicbox.core.models.Placeholder;
import org.musicbox.utils.Constants;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class PlaceholderBuilder {

  private List<Placeholder> placeholders;
  
  public PlaceholderBuilder() {
	this.placeholders = new ArrayList<Placeholder>();
  }
  
  public PlaceholderBuilder add(Placeholder placeholder) {
	placeholders.add(placeholder);
	return this;
  }
  
  public PlaceholderBuilder event(MessageReceivedEvent event) {
	add(Constants.KEY_SENDER_TAG, event.getAuthor().getAsTag());
	add(Constants.KEY_SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl());
	return this;
  }
  
  public PlaceholderBuilder add(String suffix, String replacement) {
	placeholders.add(Placeholder.create(suffix, replacement));
	return this;
  }

  public List<Placeholder> build(){
	return placeholders;
  }
  
}
