package org.musicbox.core.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.miscs.Constants;

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

   public PlaceholderBuilder set(String suffix, String newReplacement) {
      Placeholder placeholder = placeholders.stream()
            .filter(ph -> ph.getSuffix().equals(suffix))
            .findFirst().orElse(null);

      if(placeholder != null)
         placeholder.setReplacement(newReplacement);

      return this;
   }

   public static List<Placeholder> replace(List<Placeholder> placeholders, String suffix, String newReplacement) {
      Placeholder placeholder = placeholders.stream()
            .filter(ph -> ph.getSuffix().equals(suffix))
            .findFirst().orElse(null);

      if(placeholder != null)
         placeholder.setReplacement(newReplacement);

      return placeholders;
   }

   public static List<Placeholder> put(List<Placeholder> placeholders, Placeholder placeholder) {

      if(!placeholders.contains(placeholder))
         placeholders.add(placeholder);      

      return placeholders;
   }

   public List<Placeholder> build() {
      return placeholders;
   }

   public static PlaceholderBuilder createBy(GenericEvent event, boolean setupDefaults) {
      return new PlaceholderBuilder(setupDefaults).event(event);
   }

   public static final class Placeholder {

      public String suffix;
      private String replacement;

      private Placeholder(String suffix, String replacement) {
         this.suffix = suffix;
         this.replacement = replacement;
      }

      @Deprecated
      public static List<Placeholder> of(Placeholder... phs) {
         List<Placeholder> placeholders = new ArrayList<>();
         if (phs != null) {
            Arrays.stream(phs).forEach(placeholders::add);
         }
         return placeholders;
      }

      @Deprecated
      public static List<Placeholder> of(List<Placeholder> otherPlaceholders, Placeholder... phs) {
         List<Placeholder> placeholders = of(phs);
         if (otherPlaceholders != null) {
            otherPlaceholders.forEach(placeholders::add);
         }
         return placeholders;
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
}