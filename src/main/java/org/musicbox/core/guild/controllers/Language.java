package org.musicbox.core.guild.controllers;

import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.GuildWrapperPart;
import org.musicbox.core.translation.LanguageManager;
import org.musicbox.core.translation.TranslationKeys;

public final class Language implements GuildWrapperPart {

   private final GuildWrapper guildWrapper;

   public Language(GuildWrapper guildWrapper) {
      this.guildWrapper = guildWrapper;
   }

   public String getUsedLanguage() {
      return LanguageManager.PT_BR;
   }

   public String getLabel(TranslationKeys key) {
      return LanguageManager.getLanguage(getUsedLanguage()).getLabel(key);
   }
   
   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
   }

}
