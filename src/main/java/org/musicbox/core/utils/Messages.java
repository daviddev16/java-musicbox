package org.musicbox.core.utils;

import java.util.List;
import java.util.function.Consumer;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.translation.Translations;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Messages {

   public static final class Embed {

      public static void send(TextChannel channel, MessageEmbed embed, Consumer<Message> message) {
         channel.sendMessageEmbeds(embed).queue(message);
      }

      public static void send(TextChannel channel, PlaceholderBuilder builder, TranslationKeys key,
            Consumer<Message> message) {
         send(channel, builder.build(), key, message);
      }

      public static void send(TextChannel channel, List<Placeholder> placeholders, TranslationKeys key,
            Consumer<Message> message) {
         send(channel, Translations.getEmbedTranslation(channel.getGuild(), key, placeholders), message);
      }

      public static void send(TextChannel channel, TranslationKeys key, Consumer<Message> message,
            Placeholder... placeholderArray) {
         send(channel, PlaceholderBuilder.createDefault(true).collect(placeholderArray).build(), key, message);
      }

      public static void send(MessageReceivedEvent event, TranslationKeys key, PlaceholderBuilder builder,
            Consumer<Message> message) {
         send(event.getTextChannel(), ((builder != null) ? builder : PlaceholderBuilder.createBy(event, true)).build(),
               key, message);
      }

   }

}
