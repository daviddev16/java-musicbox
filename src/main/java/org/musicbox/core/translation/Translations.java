package org.musicbox.core.translation;

import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.config.LanguageConfiguration;
import org.musicbox.core.utils.Utilities;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Translations {

   public static EmbedBuilder getEmbedTranslation(JsonObject embedMap, List<Placeholder> placeholders) {
      return Utilities.translate(embedMap, placeholders);
   }

   public static MessageEmbed getEmbedTranslation(LanguageConfiguration languageConfiguration, TranslationKeys key, 
         List<Placeholder> placeholders) {

      return getEmbedTranslation(languageConfiguration.getEmbed(key), placeholders).build();
   }
   
   public static MessageEmbed getEmbedTranslation(Guild guild, TranslationKeys key, 
         List<Placeholder> placeholders) {

      return getEmbedTranslation(LanguageManager.getLanguage(guild).getEmbed(key), placeholders).build();
   }
   
   public static MessageEmbed getEmbed(MessageReceivedEvent event, TranslationKeys key, 
         Placeholder... placeholderArray) {
      
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .collect(placeholderArray).build();

      return getEmbedTranslation(event.getGuild(), key, placeholders);
   }
   
   public static MessageEmbed getEmbed(MessageReceivedEvent event, TranslationKeys key) {
      return getEmbed(event, key, null);
   }

}
