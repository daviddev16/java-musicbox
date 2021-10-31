package org.musicbox.core.translation;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.config.LanguageConfiguration;
import org.musicbox.core.managers.GuildManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.dv8tion.jda.api.entities.Guild;

public final class LanguageManager {

   public static final String PT_BR = "PT_BR2";
   
   private static Logger logger = LoggerFactory.getLogger(LanguageManager.class);
   private static LanguageManager languageManager;

   private Map<String, LanguageConfiguration> languagesMap;

   private LanguageManager() throws InstanceAlreadyExistsException {

      if (languageManager != null)
         throw new InstanceAlreadyExistsException("AudioManager instance already exists.");

      languagesMap = new ConcurrentHashMap<>();

      createLanguage(PT_BR, LanguageConfiguration.createLanguage(DefaultConfig.LANGUAGE));

      logger.info("LanguageManager loaded.");
      languageManager = this;
   }

   public static void setup() { 
      try {
         new LanguageManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      } 
   }

   public void createLanguage(String languageId, LanguageConfiguration languageConfiguration) {
      languagesMap.put(languageId, languageConfiguration);
   }
  
   public static LanguageConfiguration getLanguage(String languageId) {
      return getLanguageManager().languagesMap.get(languageId);
   }
   
   public static LanguageConfiguration getLanguage(Guild guild) {
      return getLanguage(GuildManager.getGuildManager().getWrapper(guild)
            .getLanguage().getUsedLanguage());
   }

   public static LanguageManager getLanguageManager() {
      return languageManager;
   }



}
