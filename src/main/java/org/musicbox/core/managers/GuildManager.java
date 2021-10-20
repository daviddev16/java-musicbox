package org.musicbox.core.managers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.musicbox.core.guild.GuildInstance;
import org.musicbox.core.guild.modules.InspectorModule;
import org.musicbox.core.guild.modules.LanguageModule;
import org.musicbox.core.guild.modules.ScheduleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;

public final class GuildManager {

   private static Logger logger = LoggerFactory.getLogger(GuildManager.class);

   private static GuildManager guildManager;
   private final Map<Long, GuildInstance> guildInstances;

   private GuildManager() {
      guildInstances = new ConcurrentHashMap<>();
      guildManager = this;
      logger.info("GuildManager loaded.");
   }

   public synchronized GuildInstance getGuildInstance(Guild guild) {

      GuildInstance guildInstance = guildInstances.get(guild.getIdLong());
      if (guildInstance != null) {
         return guildInstance;
      }

      guildInstance = new GuildInstance(guild);

      guildInstance.addModule(new ScheduleModule(), new LanguageModule(), new InspectorModule());

      guildInstances.put(guild.getIdLong(), guildInstance);

      return guildInstance;
   }

   public Map<Long, GuildInstance> getGuildInstances() {
      return guildInstances;
   }

   public static GuildManager getGuildManager() {
      return guildManager;
   }

   public static void setup() {
      new GuildManager();
   }

}
