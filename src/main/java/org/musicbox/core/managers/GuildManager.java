package org.musicbox.core.managers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.musicbox.core.guild.GuildWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;

public final class GuildManager {

   private static Logger logger = LoggerFactory.getLogger(GuildManager.class);

   private static GuildManager guildManager;
   private final Map<Long, GuildWrapper> guildWrappers;

   private GuildManager() {
      guildWrappers = new ConcurrentHashMap<>();
      guildManager = this;
      logger.info("GuildManager loaded.");
   }

   public synchronized GuildWrapper getWrapper(Guild guild) {

      GuildWrapper guildWrapper = guildWrappers.get(guild.getIdLong());
      if (guildWrapper != null) {
         return guildWrapper;
      }
      guildWrapper = new GuildWrapper(guild);
      guildWrappers.put(guild.getIdLong(), guildWrapper);
      return guildWrapper;
   }

   public Map<Long, GuildWrapper> getWrappers() {
      return guildWrappers;
   }

   public static GuildManager getGuildManager() {
      return guildManager;
   }

   public static void setup() {
      new GuildManager();
   }

}
