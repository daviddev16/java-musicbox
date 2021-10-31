package org.musicbox.core.builders;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.translation.PlaceholderKeys;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class PlaceholderBuilder {

   private List<Placeholder> placeholders;

   public PlaceholderBuilder(boolean setupDefaults) {
      this.placeholders = new ArrayList<>();
      if (setupDefaults) {
         defaults(this);
      }
   }

   public PlaceholderBuilder add(Placeholder placeholder) {
      placeholders.add(placeholder);
      return this;
   }

   public PlaceholderBuilder command(GenericCommand command) {
      if (command != null) {
         add(PlaceholderKeys.COMMAND_USAGE, command.toUsageString());
         add(PlaceholderKeys.COMMAND_NAME, command.getName());
      }
      return this;
   }

   public PlaceholderBuilder collect(Placeholder... placeholders) {
      if(placeholders != null)
         Stream.of(placeholders).forEachOrdered(ph -> add(ph));
      return this;
   }

   public PlaceholderBuilder user(User user) {
      if(user != null) {
         add(PlaceholderKeys.SENDER_NAME, user.getAsTag());
         add(PlaceholderKeys.SENDER_AVATAR, user.getEffectiveAvatarUrl());
      }
      return this;
   }

   public PlaceholderBuilder event(GenericEvent event) {
      if (event instanceof MessageReceivedEvent) {
         user(((MessageReceivedEvent)event).getAuthor());
      }
      return this;
   }

   public PlaceholderBuilder add(PlaceholderKeys suffix, String replacement) {
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

   public static PlaceholderBuilder of(boolean defaults, Placeholder... placeholders) {
      return PlaceholderBuilder.createDefault(defaults).collect(placeholders);
   }

   public static PlaceholderBuilder createBy(GenericEvent event, boolean setupDefaults) {
      return new PlaceholderBuilder(setupDefaults).event(event);
   }

   public static PlaceholderBuilder createDefault(boolean setupDefaults) {
      return new PlaceholderBuilder(setupDefaults);
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

   public static void add(List<Placeholder> placeholders, Placeholder... phs) {
      if(phs != null && placeholders != null) {
         Stream.of(phs).forEach(placeholders::add);
      }
   }
   
   public static void putOrReplace(List<Placeholder> placeholders, Placeholder placeholder) {

      Placeholder fPlaceholder = placeholders.stream()
            .filter(ph -> ph.getSuffix().equals(placeholder.getSuffix()))
            .findAny().orElse(null);

      if(fPlaceholder != null)
         fPlaceholder.setReplacement(placeholder.getReplacement());
      else
         placeholders.add(placeholder);
   }

   public static List<Placeholder> toList(boolean defaults, Placeholder... placeholderArray) {
      if(placeholderArray != null) {
         PlaceholderBuilder builder = new PlaceholderBuilder(defaults);
         Stream.of(placeholderArray).forEachOrdered(ph -> builder.add(ph));
         return builder.build();
      }
      return Arrays.asList();
   }

   public static PlaceholderBuilder defaults(PlaceholderBuilder phBuilder) {
      if(phBuilder != null) {
         phBuilder.add(PlaceholderKeys.GLOBAL_PREFIX, DefaultConfig.PREFIX);
         phBuilder.add(PlaceholderKeys.OWNER, DefaultConfig.OWNER);
      }
      return phBuilder;
   }

   public List<Placeholder> build() {
      return placeholders;
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

      public static Placeholder create(PlaceholderKeys suffix, String replacement) {
         return new Placeholder(suffix.getTag(), replacement);
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