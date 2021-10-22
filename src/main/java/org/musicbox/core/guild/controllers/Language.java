package org.musicbox.core.guild.controllers;

import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.GuildWrapperPart;
import org.musicbox.miscs.Constants;

import net.dv8tion.jda.api.entities.Guild;

public final class Language implements GuildWrapperPart {

   private final GuildWrapper guildWrapper;

   public Language(GuildWrapper guildWrapper) {
      this.guildWrapper = guildWrapper;
   }

   public String getUsedLanguage() {
      return Constants.PT_BR;
   }

   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
   }

}
