package org.musicbox.core.config;

import org.musicbox.core.translation.TranslationKeys;

import com.google.gson.JsonObject;

public class LanguageConfiguration extends JsonConfiguration {

   private LanguageConfiguration(String name) {
      super("languages/" + name);
   }

   public static LanguageConfiguration createLanguage(String name) {
      return new LanguageConfiguration(name);
   }
   
   public JsonObject getEmbed(TranslationKeys key) {
      return getJsonObject("embeds").getAsJsonObject(key.getKey());
   }
   
   public String getLabel(TranslationKeys key) {
      return getJsonObject("labels").get(key.getKey()).getAsString();
   }
   
}
