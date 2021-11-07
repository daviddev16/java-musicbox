package org.musicbox.modules.paginator.templates;

import java.util.ArrayList;
import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.LanguageManager;
import org.musicbox.core.translation.PlaceholderKeys;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Utilities;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;

public class TracklistTemplate implements PageTemplate<JsonObject> {

   private  JsonObject messageTemplate;
   private final List<MessageEmbed> pages;

   public TracklistTemplate(GuildWrapper wrapper) {
      pages = new ArrayList<>();
      messageTemplate = LanguageManager.getLanguage(wrapper.getGuild()).getEmbed(TranslationKeys.QUEUE_PAGE);
   }

   public void createPages(List<String> contents, GenericEvent event) {
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, false)
            .add(PlaceholderKeys.MAX_PAGE, Integer.toString(contents.size()))
            .build();
      int position = 0;
      for(String content : contents) {
         PlaceholderBuilder.putOrReplace(placeholders, 
               Placeholder.create(PlaceholderKeys.CURRENT_PAGE, Integer.toString(position+1))); 

         PlaceholderBuilder.putOrReplace(placeholders, 
               Placeholder.create(PlaceholderKeys.MAX_PAGE, content)); 

         EmbedBuilder builder = Utilities.translate(messageTemplate, placeholders);         
         pages.add(builder.build());
         position++;
      }
   }

   @Override
   public MessageEmbed[] getAllPages() {
      return pages.toArray(new MessageEmbed[pages.size()]);
   }

   @Override
   public JsonObject getTemplateSource() {
      return messageTemplate;
   }
}
