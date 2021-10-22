package org.musicbox.core.guild;

import org.musicbox.core.utils.Constants;

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
