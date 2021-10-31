package org.musicbox.miscs;

import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.translation.Translations;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Messages {

   public static final class Embed {

      public static void send(TextChannel channel, MessageEmbed embed) {
         channel.sendMessageEmbeds(embed).queue(); 
      }

      public static void send(TextChannel channel, PlaceholderBuilder builder, TranslationKeys key) {
         send(channel, builder.build(), key);
      }

      public static void send(TextChannel channel, List<Placeholder> placeholders, TranslationKeys key) {
         send(channel, Translations.getEmbedTranslation(channel.getGuild(), key, placeholders));
      }

      public static void send(TextChannel channel, TranslationKeys key, Placeholder... placeholderArray) {
         send(channel, PlaceholderBuilder.createDefault(true).collect(placeholderArray).build(), key);
      }

      public static void send(MessageReceivedEvent event, TranslationKeys key, PlaceholderBuilder builder) {
         send(event.getTextChannel(), ( (builder != null) ? builder : PlaceholderBuilder.createBy(event, true) )
               .build(), key);
      }

   }
}
