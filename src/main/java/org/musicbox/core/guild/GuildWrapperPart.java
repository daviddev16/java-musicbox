package org.musicbox.core.guild;

import net.dv8tion.jda.api.entities.Guild;

public interface GuildWrapperPart {

   public GuildWrapper getWrapper();

   public default Guild getGuild() {
      if(getWrapper() == null)
         throw new NullPointerException("Guild wrapper is null.");
      
      return getWrapper().getGuild();
   }

}
