package org.musicbox.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.musicbox.config.DefaultConfig;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class PlaceholderBuilder {

   private List<Placeholder> placeholders;

   public PlaceholderBuilder(boolean setupDefaults) {
      this.placeholders = new ArrayList<>();
      if (setupDefaults) {
         add(Constants.KEY_GLOBAL_PREFIX, DefaultConfig.PREFIX);
         add(Constants.KEY_OWNER, DefaultConfig.OWNER);
      }
   }

   public PlaceholderBuilder add(Placeholder placeholder) {
      placeholders.add(placeholder);
      return this;
   }

   /**
    * add all placeholders from event
    * */
   public PlaceholderBuilder event(GenericEvent event) {
      if (event instanceof MessageReceivedEvent) {
         add(Constants.KEY_SENDER_NAME, ((MessageReceivedEvent) event).getAuthor().getAsTag());
         add(Constants.KEY_SENDER_AVATAR, ((MessageReceivedEvent) event).getAuthor().getEffectiveAvatarUrl());
      }
      return this;
   }

   public PlaceholderBuilder add(String suffix, String replacement) {
      placeholders.add(Placeholder.create(suffix, replacement));
      return this;
   }

   public List<Placeholder> build() {
      return placeholders;
   }

   public static PlaceholderBuilder createBy(GenericEvent event, boolean setupDefaults) {
      return new PlaceholderBuilder(setupDefaults).event(event);
   }
}
