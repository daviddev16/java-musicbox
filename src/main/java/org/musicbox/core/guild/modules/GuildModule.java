package org.musicbox.core.guild.modules;

import org.musicbox.core.guild.GuildInstance;

import net.dv8tion.jda.api.entities.Guild;

public abstract class GuildModule {

   private GuildInstance guildInstance;

   public GuildInstance getInstance() {
      return guildInstance;
   }

   public abstract void load();

   public Guild getGuild() {
      return getInstance().getGuild();
   }

   public <M extends GuildModule> M getModule(Class<M> moduleClass) {
      return getInstance().getModule(moduleClass);
   }

}
