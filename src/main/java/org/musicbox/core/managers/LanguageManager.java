package org.musicbox.core.managers;

import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.core.config.LanguageConfiguration;
import org.musicbox.miscs.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public final class LanguageManager {

   private static Logger logger = LoggerFactory.getLogger(LanguageManager.class);
   private static LanguageManager languageManager;

   private Map<String, LanguageConfiguration> languagesMap;

   private LanguageManager() throws InstanceAlreadyExistsException {

      if (languageManager != null)
         throw new InstanceAlreadyExistsException("AudioManager instance already exists.");

      languagesMap = new HashMap<>();

      createLanguage(Constants.PT_BR,
            LanguageConfiguration.createLanguage(Constants.PT_BR));

      logger.info("LanguageManager loaded.");
      languageManager = this;
   }

   public static void setup() { 
      try {
         new LanguageManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.info(e.getLocalizedMessage());
      } 
   }

   public void createLanguage(String languageId, LanguageConfiguration languageConfiguration) {
      languagesMap.put(languageId, languageConfiguration);
   }

   public static LanguageManager getLanguageManager() {
      return languageManager;
   }

   public JsonObject getMessage(String languageId, int messageId) {
      return languagesMap.get(languageId).getMessage(messageId).getAsJsonObject();
   }

}
