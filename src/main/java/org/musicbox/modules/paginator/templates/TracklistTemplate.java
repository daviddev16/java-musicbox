package org.musicbox.modules.paginator.templates;

import java.util.ArrayList;
import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.LanguageManager;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Utilities;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;

public class TracklistTemplate implements PageTemplate<JsonObject> {

   private  JsonObject messageTemplate;
   private final List<EmbedBuilder> pages;
   
   public TracklistTemplate(GuildWrapper wrapper) {
      pages = new ArrayList<>();
      /*messageTemplate = LanguageManager.getLanguageManager().getMessage(wrapper.getLanguage()
            .getUsedLanguage(), Messages.QUEUE_PAGE);*/
   }
   
   public void createPages(List<String> contents, GenericEvent event) {
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, false)
            //.add(Constants.KEY_MAX_PAGES, Integer.toString(contents.size()))
            .build();
      
      int position = 0;
      
      for(String content : contents) {
         /* replace the current page value */
      //   PlaceholderBuilder.putOrReplace(placeholders, 
      //         Placeholder.create(Constants.KEY_CURRENT_PAGE, Integer.toString(position+1))); 

         /* replace to a new content */
      //   PlaceholderBuilder.putOrReplace(placeholders, 
      //         Placeholder.create(Constants.KEY_INFORMATIVE_QUEUE, content)); 
         
         EmbedBuilder builder = Utilities.translate(messageTemplate, placeholders);         
         pages.add(builder);
         position++;
      }
   }
   
   @Override
   public EmbedBuilder[] getAllPages() {
      return pages.toArray(new EmbedBuilder[pages.size()]);
   }
   
   @Override
   public JsonObject getTemplateSource() {
      return messageTemplate;
   }
}
