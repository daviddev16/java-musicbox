package org.musicbox.core.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LanguageConfiguration extends JsonConfiguration {

   private LanguageConfiguration(String name) {
      super("languages/" + name);
   }

   public static LanguageConfiguration createLanguage(String name) {
      return new LanguageConfiguration(name);
   }

   public JsonObject getMessage(int messageId) {
      JsonArray messagesArray = getJsonArray("messages");
      for(JsonElement element : messagesArray) {
         if(element.isJsonObject() && element.getAsJsonObject().get("id").getAsInt() == messageId) {
            return element.getAsJsonObject();
         }
      }
      return null;
   }

}
