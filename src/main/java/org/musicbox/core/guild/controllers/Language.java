package org.musicbox.core.guild.controllers;

import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.GuildWrapperPart;
import org.musicbox.core.translation.LanguageManager;

public final class Language implements GuildWrapperPart {

   private final GuildWrapper guildWrapper;

   public Language(GuildWrapper guildWrapper) {
      this.guildWrapper = guildWrapper;
   }

   public String getUsedLanguage() {
      return LanguageManager.PT_BR;
   }

   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
   }

}
